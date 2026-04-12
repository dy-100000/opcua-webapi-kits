import { UaBrowseDescription } from "opcua-webapi-ts";
import { ReadContext, ServiceContext, UaBrowseAdditionalInfo } from "../../types";
import {
    UaBrowseNodeTransaction,
    UaBrowseTransaction,
    UaReadNodeTransaction,
    UaReadTransaction,
} from "../../service/transactions";
import { UaObjectTypes, UaReferenceTypes } from "../nodes/builtin";
import { NodeManager } from "./NodeManager";

export class NodeManagerNs1 extends NodeManager {
    constructor() {
        super("http://opcfoundation.org/UA/DigitalTwin");
    }

    async onStartUp(): Promise<void> {
        this.buildReferenceTypes();
        this.buildObjectTypes();
    }

    private buildObjectTypes(): void {
        this.addNode(UaObjectTypes.DigitalTwinRepositoryType);
        UaObjectTypes.DigitalTwinRepositoryType.setParentType(UaObjectTypes.BaseObjectType);

        this.addNode(UaObjectTypes.DigitalTwinType);
        UaObjectTypes.DigitalTwinType.setParentType(UaObjectTypes.BaseObjectType);

        this.addNode(UaObjectTypes.SubmodelType);
        UaObjectTypes.SubmodelType.setParentType(UaObjectTypes.BaseObjectType);

        this.addNode(UaObjectTypes.ElementType);
        UaObjectTypes.ElementType.setParentType(UaObjectTypes.BaseObjectType);

        this.addNode(UaObjectTypes.ReferenceElementType);
        UaObjectTypes.ReferenceElementType.setParentType(UaObjectTypes.ElementType);

        this.addNode(UaObjectTypes.ElementCollectionType);
        UaObjectTypes.ElementCollectionType.setParentType(UaObjectTypes.ElementType);

        this.addNode(UaObjectTypes.ElementListType);
        UaObjectTypes.ElementListType.setParentType(UaObjectTypes.ElementType);

        this.addNode(UaObjectTypes.EventElementType);
        UaObjectTypes.EventElementType.setParentType(UaObjectTypes.ElementType);
    }

    private buildReferenceTypes(): void {
        this.addNode(UaReferenceTypes.HasLink);
        UaReferenceTypes.HasLink.setParentType(UaReferenceTypes.NonHierarchicalReferences);
    }

    getBrowseTransaction(
        context: ServiceContext,
        nodeToBrowse: UaBrowseDescription,
        additionalInfo: UaBrowseAdditionalInfo,
        handleId: number,
    ): UaBrowseTransaction {
        return new UaBrowseNodeTransaction(
            context,
            nodeToBrowse,
            additionalInfo,
            handleId,
            this,
        );
    }

    getReadTransactions(
        context: ReadContext,
        handleIds: Array<number>,
    ): Array<UaReadTransaction> {
        return [new UaReadNodeTransaction(context, handleIds, this)];
    }
}
