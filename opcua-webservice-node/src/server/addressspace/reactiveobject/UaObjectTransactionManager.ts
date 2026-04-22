import { Attributes, DataTypeIds, DataValue, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaBrowseDescription, UaNodeId, UaNodeIdType } from "opcua-webapi-ts";
import { NodeManagerReactiveObject } from "../nodemanager/NodeManagerReactiveObject";
import { UaObject } from "../nodes/UaObject";
import {
    UaBrowseAdditionalInfo,
    UaChildId,
    UaChildIdentifier,
    UaInstanceIdentifier,
    UaObjectId,
    UaObjectIdentifier,
    WriteVariableValue,
} from "../../types";
import { CallContext, HistoryReadContext, ReadContext, ServiceContext, WriteContext } from "../../types/contexts";
import {
    UaBrowseNodeTransaction,
    UaBrowseTransaction,
    UaHistoryReadTransaction,
    UaMethodCallTransaction,
    UaReadNodeTransaction,
    UaReadTransaction,
    UaWriteTransaction,
} from "../../service/transactions/base";
import { UaBrowseMemberTransaction } from "../../service/transactions/reactiveobject/UaBrowseMemberTransaction";
import { UaBrowseObjectTransaction } from "../../service/transactions/reactiveobject/UaBrowseObjectTransaction";
import { UaReactiveObjectType } from "./UaReactiveObjectType";

type ObjectGroup = {
    objectId: UaObjectIdentifier;
    handleIds: Array<number>;
};

type ChildGroup = {
    childId: UaChildIdentifier;
    handleIds: Array<number>;
};

export class UaObjectTransactionManager {
    private readonly nodeManager: NodeManagerReactiveObject;

    constructor(nodeManager: NodeManagerReactiveObject) {
        this.nodeManager = nodeManager;
    }

    getBrowseTransaction(
        context: ServiceContext,
        nodeToBrowse: UaBrowseDescription,
        additionalInfo: UaBrowseAdditionalInfo,
        handleId: number): UaBrowseTransaction {
        const transactionNothingToDo = new UaBrowseTransaction(
            context,
            nodeToBrowse,
            additionalInfo,
            handleId);

        if (nodeToBrowse.nodeId.identifierType !== UaNodeIdType.BYTESTRING) {
            return new UaBrowseNodeTransaction(
                    context,
                    nodeToBrowse,
                    additionalInfo,
                    handleId,
                    this.nodeManager);
        }
        
        let identifier = UaInstanceIdentifier.fromByteString(nodeToBrowse.nodeId.stringId());
        if (null == identifier)
        {
            transactionNothingToDo.setStatusCode(makeUaStatusCode(StatusCodes.BadNodeIdInvalid));
            return transactionNothingToDo;
        }       

        const objectType = this.nodeManager.findObjectType(identifier.objectId);
        if (objectType === null) {
            transactionNothingToDo.setStatusCode(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            return transactionNothingToDo;
        }

        const instanceDeclaration = this.nodeManager.findInstanceDeclaration(identifier.objectId);
        const objectId = new UaObjectId(identifier.objectId.id, instanceDeclaration);

        if (identifier.childId === null) {
            return new UaBrowseObjectTransaction(
                context,
                nodeToBrowse,
                additionalInfo,
                handleId,
                objectType,
                objectId,
                this.nodeManager,
            );
        }

        return new UaBrowseMemberTransaction(
            context,
            nodeToBrowse,
            additionalInfo,
            handleId,
            objectType,
            objectId,
            identifier.childId,
            this.nodeManager,
        );
    }

    getReadTransactions(context: ReadContext, handleIds: Array<number>): Array<UaReadTransaction> {
        const handleIdsForNodes: Array<number> = [];
        const handleIdsForObjectAttributes = new Map<number, UaInstanceIdentifier>();
        const handleIdsForObjectMemberAttributes = new Map<number, UaInstanceIdentifier>();
        const handleIdsForObjectVariableValues = new Map<number, UaInstanceIdentifier>();       

        for (const handleId of handleIds) {
            const nodeToRead = context.nodesToRead[handleId];
            let nodeId = nodeToRead.nodeId;

            if (nodeId.identifierType === UaNodeIdType.BYTESTRING)
            {
                handleIdsForNodes.push(handleId);
                continue;
            }

            const identifier = this.getInstanceIdentifier(nodeId);
            const objectType = identifier === null ? null : this.nodeManager.findObjectType(identifier.objectId);

            if (identifier === null || objectType === null) {
                handleIdsForNodes.push(handleId);
                continue;
            }

            if (nodeToRead.attributeId === Attributes.Value) {
                if (identifier.childId === null) {
                    handleIdsForNodes.push(handleId);
                } else if (!identifier.childId.methodNode) {
                    handleIdsForObjectVariableValues.set(handleId, identifier);
                } else {
                    handleIdsForObjectMemberAttributes.set(handleId, identifier);
                }
                continue;
            }

            if (identifier.childId === null) {
                handleIdsForObjectAttributes.set(handleId, identifier);
            } else {
                handleIdsForObjectMemberAttributes.set(handleId, identifier);
            }
        }

        const transactions: Array<UaReadTransaction> = [];

        if (handleIdsForNodes.length > 0) {
            transactions.push(new UaReadNodeTransaction(context, handleIdsForNodes, this.nodeManager));
        }

        for (const group of this.groupByObject(handleIdsForObjectAttributes)) {
            transactions.push(
                new UaReadObjectAttributeTransaction(
                    context,
                    group.handleIds,
                    group.objectId,
                    this.nodeManager,
                ),
            );
        }

        for (const group of this.groupByObject(handleIdsForObjectMemberAttributes)) {
            for (const childGroup of this.groupByChild(group.handleIds, handleIdsForObjectMemberAttributes)) {
                transactions.push(
                    new UaReadMemberAttributeTransaction(
                        context,
                        group.objectId,
                        childGroup.childId,
                        childGroup.handleIds,
                        this.nodeManager,
                    ),
                );
            }
        }

        for (const group of this.groupByObject(handleIdsForObjectVariableValues)) {
            const handleIdsAndVariableIds = new Map<number, UaChildId>();

            for (const handleId of group.handleIds) {
                const childId = handleIdsForObjectVariableValues.get(handleId)?.childId;
                if (childId === null || childId === undefined) {
                    continue;
                }

                handleIdsAndVariableIds.set(handleId, new UaChildId(childId.path, childId.pathL2));
            }

            transactions.push(
                new UaReadVariableValueTransaction(
                    context,
                    group.objectId,
                    handleIdsAndVariableIds,
                    this.nodeManager,
                ),
            );
        }

        return transactions;
    }

    getWriteTransactions(context: WriteContext, handleIds: Array<number>): Array<UaWriteTransaction> {
        const handleIdsForNodes: Array<number> = [];
        const handleIdsForObjectAttributes = new Map<number, UaInstanceIdentifier>();
        const handleIdsForWriteVariableValues = new Map<number, UaInstanceIdentifier>();

        for (const handleId of handleIds) {
            const nodeToWrite = context.nodesToWrite[handleId];
            if (nodeToWrite === undefined) {
                continue;
            }

            const identifier = this.getInstanceIdentifier(nodeToWrite.nodeId);
            const objectType = identifier === null ? null : this.nodeManager.findObjectType(identifier.objectId);

            if (identifier === null || objectType === null) {
                handleIdsForNodes.push(handleId);
                continue;
            }

            if (nodeToWrite.attributeId === Attributes.Value) {
                if (identifier.childId === null) {
                    handleIdsForNodes.push(handleId);
                } else {
                    handleIdsForWriteVariableValues.set(handleId, identifier);
                }
                continue;
            }

            if (identifier.childId === null && identifier.objectId.instanceDeclId === null) {
                handleIdsForObjectAttributes.set(handleId, identifier);
            } else {
                handleIdsForNodes.push(handleId);
            }
        }

        const transactions: Array<UaWriteTransaction> = [];

        if (handleIdsForNodes.length > 0) {
            transactions.push(new UaWriteNodeTransaction(context, handleIdsForNodes));
        }

        for (const group of this.groupByObject(handleIdsForObjectAttributes)) {
            transactions.push(
                new UaWriteObjectAttributeTransaction(
                    context,
                    group.handleIds,
                    group.objectId,
                    this.nodeManager,
                ),
            );
        }

        for (const group of this.groupByObject(handleIdsForWriteVariableValues)) {
            const handleIdsAndVariableValues = new Map<number, WriteVariableValue>();

            for (const handleId of group.handleIds) {
                const childId = handleIdsForWriteVariableValues.get(handleId)?.childId;
                const value = context.nodesToWrite[handleId]?.value;
                if (childId === null || childId === undefined || value === undefined) {
                    continue;
                }

                handleIdsAndVariableValues.set(
                    handleId,
                    new WriteVariableValue(new UaChildId(childId.path, childId.pathL2), value as DataValue),
                );
            }

            transactions.push(
                new UaWriteVariableValueTransaction(
                    context,
                    group.objectId,
                    handleIdsAndVariableValues,
                    this.nodeManager,
                ),
            );
        }

        return transactions;
    }

    getMethodCallTransaction(context: CallContext, handleId: number): UaMethodCallTransaction {
        const callRequest = context.methodsToCall[handleId];
        if (callRequest === undefined) {
            return new UaMethodCallTransaction(context, handleId);
        }

        const objectIdentifier = this.getInstanceIdentifier(callRequest.objectId);
        const methodIdentifier = this.getInstanceIdentifier(callRequest.methodId);

        if (objectIdentifier === null || methodIdentifier === null) {
            return new UaMethodCallTransaction(context, handleId);
        }

        return new UaCallMethodTransaction(
            context,
            handleId,
            objectIdentifier,
            methodIdentifier,
            this.nodeManager,
        );
    }

    getHistoryReadTransaction(context: HistoryReadContext, handleId: number): UaHistoryReadTransaction {
        const transactionNothingToDo = new UaHistoryReadTransaction(context, handleId);
        const nodeToRead = context.nodesToRead[handleId];
        if (nodeToRead === undefined) {
            return transactionNothingToDo;
        }

        const identifier = this.getInstanceIdentifier(nodeToRead.nodeId);
        if (identifier === null) {
            transactionNothingToDo.setStatusCode(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            return transactionNothingToDo;
        }

        const objectType = this.nodeManager.findObjectType(identifier.objectId);
        if (objectType === null) {
            transactionNothingToDo.setStatusCode(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            return transactionNothingToDo;
        }

        if (identifier.childId === null) {
            if (context.historyReadDetails.typeId.value === DataTypeIds.ReadEventDetails) {
                return new UaReadEventHistoryTransaction(context, handleId, identifier, this.nodeManager);
            }
        } else if (
            context.historyReadDetails.typeId.value === DataTypeIds.ReadRawModifiedDetails
            || context.historyReadDetails.typeId.value === DataTypeIds.ReadAtTimeDetails
            || context.historyReadDetails.typeId.value === DataTypeIds.ReadProcessedDetails
        ) {
            return new UaReadDataHistoryTransaction(context, handleId, identifier, this.nodeManager);
        }

        return transactionNothingToDo;
    }

    private getInstanceIdentifier(nodeId: UaNodeId | undefined): UaInstanceIdentifier | null {
        if (nodeId === undefined || nodeId.identifierType !== UaNodeIdType.BYTESTRING) {
            return null;
        }

        return UaInstanceIdentifier.fromByteString(nodeId.stringId());
    }

    private groupByObject(source: Map<number, UaInstanceIdentifier>): Array<ObjectGroup> {
        const groups = new Map<string, ObjectGroup>();

        for (const [handleId, identifier] of source.entries()) {
            const key = identifier.objectId.toString();
            const group = groups.get(key);

            if (group === undefined) {
                groups.set(key, { objectId: identifier.objectId, handleIds: [handleId] });
            } else {
                group.handleIds.push(handleId);
            }
        }

        return [...groups.values()];
    }

    private groupByChild(handleIds: Array<number>, source: Map<number, UaInstanceIdentifier>): Array<ChildGroup> {
        const groups = new Map<string, ChildGroup>();

        for (const handleId of handleIds) {
            const childId = source.get(handleId)?.childId;
            if (childId === null || childId === undefined) {
                continue;
            }

            const key = childId.toString();
            const group = groups.get(key);
            if (group === undefined) {
                groups.set(key, { childId, handleIds: [handleId] });
            } else {
                group.handleIds.push(handleId);
            }
        }

        return [...groups.values()];
    }
}

class UaWriteNodeTransaction extends UaWriteTransaction {
    constructor(context: WriteContext, handleIds: Array<number>) {
        super(context, handleIds);
    }
}

class UaReadObjectAttributeTransaction extends UaReadTransaction {
    constructor(
        context: ReadContext,
        handleIds: Array<number>,
        _objectId: UaObjectIdentifier,
        _nodeManager: NodeManagerReactiveObject,
    ) {
        super(context, handleIds);
    }
}

class UaReadMemberAttributeTransaction extends UaReadTransaction {
    constructor(
        context: ReadContext,
        _objectId: UaObjectIdentifier,
        _childId: UaChildIdentifier,
        handleIds: Array<number>,
        _nodeManager: NodeManagerReactiveObject,
    ) {
        super(context, handleIds);
    }
}

class UaReadVariableValueTransaction extends UaReadTransaction {
    constructor(
        context: ReadContext,
        _objectId: UaObjectIdentifier,
        handleIdsAndVariableIds: Map<number, UaChildId>,
        _nodeManager: NodeManagerReactiveObject,
    ) {
        super(context, [...handleIdsAndVariableIds.keys()]);
    }
}

class UaWriteObjectAttributeTransaction extends UaWriteTransaction {
    constructor(
        context: WriteContext,
        handleIds: Array<number>,
        _objectId: UaObjectIdentifier,
        _nodeManager: NodeManagerReactiveObject,
    ) {
        super(context, handleIds);
    }
}

class UaWriteVariableValueTransaction extends UaWriteTransaction {
    constructor(
        context: WriteContext,
        _objectId: UaObjectIdentifier,
        handleIdsAndVariableValues: Map<number, WriteVariableValue>,
        _nodeManager: NodeManagerReactiveObject,
    ) {
        super(context, [...handleIdsAndVariableValues.keys()]);
    }
}

class UaCallMethodTransaction extends UaMethodCallTransaction {
    constructor(
        context: CallContext,
        handleId: number,
        _objectIdentifier: UaInstanceIdentifier,
        _methodIdentifier: UaInstanceIdentifier,
        _nodeManager: NodeManagerReactiveObject,
    ) {
        super(context, handleId);
    }
}

class UaReadEventHistoryTransaction extends UaHistoryReadTransaction {
    constructor(
        context: HistoryReadContext,
        handleId: number,
        _identifier: UaInstanceIdentifier,
        _nodeManager: NodeManagerReactiveObject,
    ) {
        super(context, handleId);
    }
}

class UaReadDataHistoryTransaction extends UaHistoryReadTransaction {
    constructor(
        context: HistoryReadContext,
        handleId: number,
        _identifier: UaInstanceIdentifier,
        _nodeManager: NodeManagerReactiveObject,
    ) {
        super(context, handleId);
    }
}
