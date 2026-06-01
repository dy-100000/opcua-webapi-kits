import { NodeClass } from "opcua-webapi";
import { parseUaNodeIdOrNull, UaBrowseDescription, UaNodeId } from "opcua-webapi-ts";
import {
    CallContext,
    HistoryReadContext,
    ReadContext,
    ServiceContext,
    UaBrowseAdditionalInfo,
    UaObjectIdentifier,
    WriteContext,
} from "../../types";
import {
    UaBrowseTransaction,
    UaHistoryReadTransaction,
    UaMethodCallTransaction,
    UaReadTransaction,
    UaWriteTransaction,
} from "../../service/transactions";
import { UaReactiveObjectTransactionManager } from "../reactiveobject/UaReactiveObjectTransactionManager";
import { UaObject } from "../nodes";
import { UaReactiveObjectType } from "../reactiveobject/UaReactiveObjectType";
import { NodeManager } from "./NodeManager";

export class NodeManagerReactiveObject extends NodeManager {
    constructor(namespaceUri: string) {
        super(namespaceUri);
    }

    findObjectType(objectId: UaObjectIdentifier): UaReactiveObjectType | null {
        let typeId : UaNodeId | null = null;

        if (objectId.typeId !== null) {
            typeId = parseUaNodeIdOrNull(objectId.typeId);
        } else if (objectId.instanceDeclId !== null) {
            let instanceDeclNodeId = parseUaNodeIdOrNull(objectId.instanceDeclId);
            if (instanceDeclNodeId === null) return null;

            const instanceDeclNode = this.getNode(instanceDeclNodeId);
            if (instanceDeclNode === null || instanceDeclNode.nodeClass !== NodeClass.Object) return null;

            const typeDefinitionId = (instanceDeclNode as UaObject).typeDefinition.nodeId;
            typeId = typeDefinitionId;            
        }

        if (typeId === null) return null;
                
        let typeNode = this.getNode(typeId);
        if (typeNode === null || typeNode.nodeClass !== NodeClass.ObjectType) return null;

        return typeNode as unknown as UaReactiveObjectType;
    }

    findInstanceDeclaration(objectId: UaObjectIdentifier): UaObject | null {
        if (objectId.instanceDeclId === null) return null;
        
        let nodeId = parseUaNodeIdOrNull(objectId.instanceDeclId);
        if (nodeId === null) return null;
        
        const instanceDeclaration = this.getNode(nodeId);
        if (instanceDeclaration === null || instanceDeclaration.nodeClass !== NodeClass.Object) {
            return null;
        }

        return instanceDeclaration as UaObject;
    }

    getBrowseTransaction(
        context: ServiceContext,
        nodeToBrowse: UaBrowseDescription,
        additionalInfo: UaBrowseAdditionalInfo,
        handleId: number,
    ): UaBrowseTransaction {
        return new UaReactiveObjectTransactionManager(this).getBrowseTransaction(
            context,
            nodeToBrowse,
            additionalInfo,
            handleId,
        );
    }

    getReadTransactions(
        context: ReadContext,
        handleIds: Array<number>,
    ): Array<UaReadTransaction> {
        return new UaReactiveObjectTransactionManager(this).getReadTransactions(context, handleIds);
    }

    getWriteTransactions(
        context: WriteContext,
        handleIds: Array<number>,
    ): Array<UaWriteTransaction> {
        return new UaReactiveObjectTransactionManager(this).getWriteTransactions(context, handleIds);
    }

    getMethodCallTransaction(
        context: CallContext,
        handleId: number,
    ): UaMethodCallTransaction {
        return new UaReactiveObjectTransactionManager(this).getMethodCallTransaction(context, handleId);
    }

    getHistoryReadTransaction(
        context: HistoryReadContext,
        handleId: number,
    ): UaHistoryReadTransaction {
        return new UaReactiveObjectTransactionManager(this).getHistoryReadTransaction(context, handleId);
    }
}
