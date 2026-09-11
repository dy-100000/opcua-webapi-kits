import { Attributes, NodeClass, StatusCodes } from "opcua-webapi";
import { DataTypeIds, makeUaStatusCode, UaBrowseDescription, UaDataValue, UaNodeId, UaNodeIdType, UaObjectAttributes, UaStatusCode } from "opcua-webapi-ts";
import {
    UaBrowseAdditionalInfo,
    UaChildId,
    UaChildIdentifier,
    UaInstanceIdentifier,
    UaObjectId,
    UaObjectIdentifier,
    WriteVariableValue,
} from "../../types";
import {     
    AddNodesContext,
    AddReferencesContext,
    CallContext,
    DeleteNodesContext,
    DeleteReferencesContext,
    HistoryReadContext,
    ReadContext,
    ServiceContext,    
    UaAddReferenceTransaction,    
    UaDeleteNodeTransaction,    
    UaDeleteReferenceTransaction,    
    WriteContext } from "../../service"
import {
    UaBrowseNodeTransaction,
    UaBrowseTransaction,
    UaHistoryReadTransaction,
    UaMethodCallTransaction,
    UaReadNodeTransaction,
    UaReadTransaction,
    UaWriteTransaction,
} from "../../service/transactions";
import { 
    UaBrowseMemberTransaction ,
    UaBrowseObjectTransaction,
    UaCallMethodTransaction,
    UaReadDataHistoryTransaction,
    UaReadEventHistoryTransaction,
    UaReadMemberAttributeTransaction,
    UaReadObjectAttributeTransaction,
    UaReadVariableValueTransaction,
    UaWriteNodeTransaction,
    UaWriteObjectAttributeTransaction,
    UaWriteVariableValueTransaction,
    UaAddNodeTransaction } from "../../service/transactions";
import { NodeManagerReactiveObject } from "../nodemanager/NodeManagerReactiveObject";
import { UaAddObjectTransaction } from "../../service/transactions/reactiveobject/UaAddObjectTransaction";
import { UaAddObjectReferenceTransaction } from "../../service/transactions/reactiveobject/UaAddObjectReferenceTransaction";
import { UaDeleteObjectTransaction } from "../../service/transactions/reactiveobject/UaDeleteObjectTransaction";
import { UaDeleteObjectReferenceTransaction } from "../../service/transactions/reactiveobject/UaDeleteObjectReferenceTransaction";

type GroupedObjectHandles = {
    objectId: UaObjectIdentifier;
    handleIds: Array<number>;
};

export class UaReactiveObjectTransactionManager {
    private readonly nodeManager: NodeManagerReactiveObject;

    constructor(nodeManager: NodeManagerReactiveObject) {
        this.nodeManager = nodeManager;
    }

    getBrowseTransaction(
        context: ServiceContext,
        nodeToBrowse: UaBrowseDescription,
        additionalInfo: UaBrowseAdditionalInfo,
        handleId: number,
    ): UaBrowseTransaction {
        const transactionNothingToDo = new UaBrowseTransaction(
            context,
            nodeToBrowse,
            additionalInfo,
            handleId,
        );

        if (!this.isReactiveObjectNodeId(nodeToBrowse.nodeId)) {
            return new UaBrowseNodeTransaction(
                context,
                nodeToBrowse,
                additionalInfo,
                handleId,
                this.nodeManager,
            );
        }

        const identifier = this.getInstanceIdentifier(nodeToBrowse.nodeId);
        if (identifier === null) {
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
 
            if (!this.isReactiveObjectNodeId(nodeToRead.nodeId)) {
                handleIdsForNodes.push(handleId);
                continue;
            }

            const identifier = this.getInstanceIdentifier(nodeToRead.nodeId);
            const objectType = (identifier === null) ? null : this.nodeManager.findObjectType(identifier.objectId);

            if (identifier === null || objectType === null) {
                handleIdsForNodes.push(handleId);
                continue;
            }

            if (nodeToRead.attributeId === Attributes.Value) {
                if (identifier.childId === null) {
                    handleIdsForNodes.push(handleId);
                } else {
                    handleIdsForObjectVariableValues.set(handleId, identifier);
                }

                continue;
            }

            if (identifier.childId !== null) {
                handleIdsForObjectMemberAttributes.set(handleId, identifier);
            } else {
                handleIdsForObjectAttributes.set(handleId, identifier);
            }
        }

        const transactions: Array<UaReadTransaction> = [];

        if (handleIdsForNodes.length > 0) {
            transactions.push(new UaReadNodeTransaction(context, handleIdsForNodes, this.nodeManager));
        }

        let groupsForObjectAttributes = this.groupByObject(handleIdsForObjectAttributes);
        for (const group of groupsForObjectAttributes) {
            transactions.push(new UaReadObjectAttributeTransaction(
                context,
                group.handleIds,
                group.objectId,
                this.nodeManager,
            ));
        }

        let groupsForObjectMemberAttributes = this.groupByObject(handleIdsForObjectMemberAttributes);
        for (const group of groupsForObjectMemberAttributes) {
            const handleIdsByChildId = new Map<string, { childId: UaChildIdentifier; handleIds: Array<number> }>();

            for (const handleId of group.handleIds) {
                const memberId = handleIdsForObjectMemberAttributes.get(handleId)?.childId;
                if (memberId === null || memberId === undefined) {
                    continue;
                }

                const key = memberId.toString();
                const currentGroup = handleIdsByChildId.get(key);
                if (currentGroup === undefined) {
                    handleIdsByChildId.set(key, { childId: memberId, handleIds: [handleId] });
                    continue;
                }

                currentGroup.handleIds.push(handleId);
            }

            for (const childGroup of handleIdsByChildId.values()) {
                transactions.push(new UaReadMemberAttributeTransaction(
                    context,
                    group.objectId,
                    childGroup.childId,
                    childGroup.handleIds,
                    this.nodeManager,
                ));
            }
        }

        let groupsForObjectVariableValues = this.groupByObject(handleIdsForObjectVariableValues);
        for (const group of groupsForObjectVariableValues) {
            const handleIdsAndVariableIds = new Map<number, UaChildId>();

            for (const handleId of group.handleIds) {
                const memberId = handleIdsForObjectVariableValues.get(handleId)?.childId;
                if (memberId === null || memberId === undefined) {
                    continue;
                }

                handleIdsAndVariableIds.set(handleId, new UaChildId(memberId.path, memberId.pathL2));
            }

            transactions.push(new UaReadVariableValueTransaction(
                context,
                group.objectId,
                handleIdsAndVariableIds,
                this.nodeManager,
            ));
        }

        return transactions;
    }

    getWriteTransactions(context: WriteContext, handleIds: Array<number>): Array<UaWriteTransaction> {
        const handleIdsForNodes: Array<number> = [];
        const handleIdsForObjectAttributes = new Map<number, UaInstanceIdentifier>();
        const handleIdsForWriteVariableValues = new Map<number, UaInstanceIdentifier>();

        for (const handleId of handleIds) {
            const nodeToWrite = context.nodesToWrite[handleId];
 
            if (!this.isReactiveObjectNodeId(nodeToWrite.nodeId)) {
                handleIdsForNodes.push(handleId);
                continue;
            }

            const identifier = this.getInstanceIdentifier(nodeToWrite.nodeId);
            const objectType = identifier === null ? null : this.nodeManager.findObjectType(identifier.objectId);

            if (identifier === null || objectType === null) {
                handleIdsForNodes.push(handleId);
                continue;
            }

            if (nodeToWrite.attributeId === Attributes.Value) {
                if (identifier.childId !== null) {
                    handleIdsForWriteVariableValues.set(handleId, identifier); 
                } else {
                    handleIdsForNodes.push(handleId);
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

        let groupsForObjectAttributes = this.groupByObject(handleIdsForObjectAttributes);
        for (const group of groupsForObjectAttributes) {
            transactions.push(new UaWriteObjectAttributeTransaction(
                context,
                group.handleIds,
                group.objectId,
                this.nodeManager,
            ));
        }

        let groupsForWriteVariableValues = this.groupByObject(handleIdsForWriteVariableValues);
        for (const group of groupsForWriteVariableValues) {
            const handleIdsAndVariableValues = new Map<number, WriteVariableValue>();

            for (const handleId of group.handleIds) {
                const memberId = handleIdsForWriteVariableValues.get(handleId)?.childId;
                const value = context.nodesToWrite[handleId]?.value;
                if (memberId === null || memberId === undefined || value === undefined) {
                    continue;
                }

                handleIdsAndVariableValues.set(
                    handleId,
                    new WriteVariableValue(
                        new UaChildId(memberId.path, memberId.pathL2),
                        new UaDataValue(value),
                    ),
                );
            }

            transactions.push(new UaWriteVariableValueTransaction(
                context,
                group.objectId,
                handleIdsAndVariableValues,
                this.nodeManager,
            ));
        }

        return transactions;
    }

    getMethodCallTransaction(context: CallContext, handleId: number): UaMethodCallTransaction {
        const callRequest = context.methodsToCall[handleId];
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
        if (nodeToRead === undefined || !this.isReactiveObjectNodeId(nodeToRead.nodeId)) {
            transactionNothingToDo.setStatusCode(makeUaStatusCode(StatusCodes.BadNodeIdInvalid));
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
            if (context.historyReadDetails.typeId.equal(UaNodeId.from(DataTypeIds.ReadEventDetails))) {
                return new UaReadEventHistoryTransaction(context, handleId, identifier, this.nodeManager);
            }
        } else if (
            context.historyReadDetails.typeId.equal(UaNodeId.from(DataTypeIds.ReadRawModifiedDetails)) ||
            context.historyReadDetails.typeId.equal(UaNodeId.from(DataTypeIds.ReadAtTimeDetails)) ||
            context.historyReadDetails.typeId.equal(UaNodeId.from(DataTypeIds.ReadProcessedDetails))) {
            return new UaReadDataHistoryTransaction(context, handleId, identifier, this.nodeManager);
        }

        transactionNothingToDo.setStatusCode(makeUaStatusCode(StatusCodes.BadHistoryOperationUnsupported));
        return transactionNothingToDo;
    }

    getAddNodeTransaction(
            context: AddNodesContext,
            handleId: number): UaAddNodeTransaction
    {
        const transactionNothingToDo = new UaAddNodeTransaction(context, handleId);
        const nodeToAdd = context.nodesToAdd[handleId];
        const parentNodeId = nodeToAdd.parentNodeId.getNodeId();
        let typeDefinitionId = null;
        
        if (nodeToAdd.typeDefinition)
        {
            typeDefinitionId = nodeToAdd.typeDefinition.getNodeId();
        }

        let objectAttributes = UaObjectAttributes.fromExtensionObject(nodeToAdd.nodeAttributes);

        if (nodeToAdd.nodeClass !== NodeClass.Object ||
            null == objectAttributes ||
            null == parentNodeId ||
            typeDefinitionId == null)
        {
            transactionNothingToDo.setStatusCode(UaStatusCode.from(StatusCodes.BadInvalidArgument));
            return transactionNothingToDo;
        }

        if (!this.isReactiveObjectNodeId(parentNodeId)) {
            transactionNothingToDo.setStatusCode(UaStatusCode.from(StatusCodes.BadNodeIdInvalid));
            return transactionNothingToDo;
        }
        
        const parentIdentifier = this.getInstanceIdentifier(parentNodeId);
        if (parentIdentifier === null) {
            transactionNothingToDo.setStatusCode(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            return transactionNothingToDo;
        }
      
        let addObjectTransaction = new UaAddObjectTransaction(
            context,
            handleId,
            parentIdentifier,
            typeDefinitionId,
            nodeToAdd.browseName,
            objectAttributes.displayName,
            this.nodeManager);
        
        return addObjectTransaction;
    }

    getAddReferenceTransaction(
        context: AddReferencesContext,
        handleId: number): UaAddReferenceTransaction
    {
        const transactionNothingToDo = new UaAddReferenceTransaction(context, handleId);
        const referenceToAdd = context.referencesToAdd[handleId];
        const sourceNodeId = referenceToAdd.sourceNodeId;
        const targetNodeId = referenceToAdd.targetNodeId.getNodeId();

        if (null == targetNodeId ||
            referenceToAdd.targetNodeClass !== NodeClass.Object ||
            !referenceToAdd.isForward)
        {
            transactionNothingToDo.setStatusCode(UaStatusCode.from(StatusCodes.BadInvalidArgument));
            return transactionNothingToDo;
        }

        if (!this.isReactiveObjectNodeId(sourceNodeId) || 
            !this.isReactiveObjectNodeId(targetNodeId)) {
            transactionNothingToDo.setStatusCode(UaStatusCode.from(StatusCodes.BadNodeIdInvalid));
            return transactionNothingToDo;
        }

        const sourceIdentifier = this.getInstanceIdentifier(sourceNodeId);
        const targetIdentifier = this.getInstanceIdentifier(targetNodeId);

        if (sourceIdentifier === null || targetIdentifier === null) {
            transactionNothingToDo.setStatusCode(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            return transactionNothingToDo;
        }

        return new UaAddObjectReferenceTransaction(
            context,
            handleId,
            sourceIdentifier,
            targetIdentifier,
            referenceToAdd.referenceTypeId,
            this.nodeManager,
        );
    }

    getDeleteNodeTransaction(context: DeleteNodesContext, handleId: number): UaDeleteNodeTransaction {
        const transactionNothingToDo = new UaDeleteNodeTransaction(context, handleId);
        const nodeToDelete = context.nodesToDelete[handleId];
        const nodeId = nodeToDelete.nodeId;

        if (!this.isReactiveObjectNodeId(nodeId)) {
            transactionNothingToDo.setStatusCode(UaStatusCode.from(StatusCodes.BadNodeIdInvalid));
            return transactionNothingToDo;
        }

        const identifier = this.getInstanceIdentifier(nodeId);
        if (identifier === null) {
            transactionNothingToDo.setStatusCode(UaStatusCode.from(StatusCodes.BadNodeIdUnknown));
            return transactionNothingToDo;
        }
        
        return new UaDeleteObjectTransaction(context, handleId, identifier, this.nodeManager);
    }

    getDeleteReferenceTransaction(context: DeleteReferencesContext, handleId: number): UaDeleteReferenceTransaction {
        const transactionNothingToDo = new UaDeleteReferenceTransaction(context, handleId);
        const referenceToDelete = context.referencesToDelete[handleId];
        const sourceNodeId = referenceToDelete.sourceNodeId;
        const targetNodeId = referenceToDelete.targetNodeId.getNodeId();

        if (null == targetNodeId ||
            !referenceToDelete.isForward)
        {
            transactionNothingToDo.setStatusCode(UaStatusCode.from(StatusCodes.BadInvalidArgument));
            return transactionNothingToDo;
        }

        if (!this.isReactiveObjectNodeId(sourceNodeId) || 
            !this.isReactiveObjectNodeId(targetNodeId)) {
            transactionNothingToDo.setStatusCode(UaStatusCode.from(StatusCodes.BadNodeIdInvalid));
            return transactionNothingToDo;
        }

        const sourceIdentifier = this.getInstanceIdentifier(sourceNodeId);
        const targetIdentifier = this.getInstanceIdentifier(targetNodeId);

        if (sourceIdentifier === null || targetIdentifier === null) {
            transactionNothingToDo.setStatusCode(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            return transactionNothingToDo;
        }

        return new UaDeleteObjectReferenceTransaction(
            context,
            handleId,
            sourceIdentifier,
            targetIdentifier,
            referenceToDelete.referenceTypeId,
            this.nodeManager,
        );
    }

    private isReactiveObjectNodeId(nodeId: UaNodeId): boolean {
        return nodeId.identifierType === UaNodeIdType.BYTESTRING;
    }

    private getInstanceIdentifier(nodeId: UaNodeId): UaInstanceIdentifier | null {
        if (!this.isReactiveObjectNodeId(nodeId)) return null;
        const value = nodeId.value;
        return typeof value === "string" ? UaInstanceIdentifier.fromByteString(value) : null;
    }

    private groupByObject(handleIdsAndIdentifiers: Map<number, UaInstanceIdentifier>): Array<GroupedObjectHandles> {
        const groups = new Map<string, GroupedObjectHandles>();

        for (const [handleId, identifier] of handleIdsAndIdentifiers.entries()) {
            const key = identifier.objectId.toString();
            const group = groups.get(key);

            if (group === undefined) {
                groups.set(key, {
                    objectId: identifier.objectId,
                    handleIds: [handleId],
                });
                continue;
            }

            group.handleIds.push(handleId);
        }

        return [...groups.values()];
    }
}