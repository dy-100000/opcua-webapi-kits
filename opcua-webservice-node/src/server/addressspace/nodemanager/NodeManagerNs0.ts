import { NodeClass } from "opcua-webapi";
import {
    DataTypeIds,
    UaAccessLevel,
    UaBrowseDescription,
    UaLocalizedText,
    UaNodeId,
    UaValueRank,
    UaVariant,
    VariableIds,
} from "opcua-webapi-ts";
import { UaBrowseAdditionalInfo, ReadContext, ServiceContext } from "../../types";
import { UaBrowseNodeTransaction, UaBrowseTransaction, UaReadNodeTransaction, UaReadTransaction } from "../../service/transactions";
import { UaVariable } from "../nodes";
import { UaDataTypes, UaObjectTypes, UaObjects, UaReferenceTypes, UaVariableTypes } from "../nodes/builtin";
import { NodeManager } from "./NodeManager";

export class NodeManagerNs0 extends NodeManager {
    constructor() {
        super("http://opcfoundation.org/UA/");
    }

    async onStartUp(): Promise<void> {
        this.buildReferenceTypes();
        this.buildObjectTypes();
        this.buildVariableTypes();
        this.buildDataTypes();
        this.buildObjects();
        this.buildVariables();
    }

    updateServerArray(serverUris: string[]): void {
        const node = this.getNode(UaNodeId.from(VariableIds.Server_ServerArray));
        if (node === null || node.nodeClass !== NodeClass.Variable) {
            return;
        }

        (node as UaVariable).value = UaVariant.strings(serverUris);
    }

    updateNamespaceArray(namespaceUris: string[]): void {
        const node = this.getNode(UaNodeId.from(VariableIds.Server_NamespaceArray));
        if (node === null || node.nodeClass !== NodeClass.Variable) {
            return;
        }

        (node as UaVariable).value = UaVariant.strings(namespaceUris);
    }

    private buildReferenceTypes(): void {
        this.addNode(UaReferenceTypes.References);

        this.addNode(UaReferenceTypes.HierarchicalReferences);
        UaReferenceTypes.HierarchicalReferences.setParentType(UaReferenceTypes.References);

        this.addNode(UaReferenceTypes.Organizes);
        UaReferenceTypes.Organizes.setParentType(UaReferenceTypes.HierarchicalReferences);

        this.addNode(UaReferenceTypes.HasChild);
        UaReferenceTypes.HasChild.setParentType(UaReferenceTypes.HierarchicalReferences);

        this.addNode(UaReferenceTypes.HasSubtype);
        UaReferenceTypes.HasSubtype.setParentType(UaReferenceTypes.HasChild);

        this.addNode(UaReferenceTypes.Aggregates);
        UaReferenceTypes.Aggregates.setParentType(UaReferenceTypes.HasChild);

        this.addNode(UaReferenceTypes.HasComponent);
        UaReferenceTypes.HasComponent.setParentType(UaReferenceTypes.Aggregates);

        this.addNode(UaReferenceTypes.HasProperty);
        UaReferenceTypes.HasProperty.setParentType(UaReferenceTypes.Aggregates);

        this.addNode(UaReferenceTypes.NonHierarchicalReferences);
        UaReferenceTypes.NonHierarchicalReferences.setParentType(UaReferenceTypes.References);

        this.addNode(UaReferenceTypes.HasTypeDefinition);
        UaReferenceTypes.HasTypeDefinition.setParentType(UaReferenceTypes.NonHierarchicalReferences);

        this.addNode(UaReferenceTypes.HasModellingRule);
        UaReferenceTypes.HasModellingRule.setParentType(UaReferenceTypes.NonHierarchicalReferences);

        this.addNode(UaReferenceTypes.GeneratesEvent);
        UaReferenceTypes.GeneratesEvent.setParentType(UaReferenceTypes.NonHierarchicalReferences);
    }

    private buildObjectTypes(): void {
        this.addNode(UaObjectTypes.BaseObjectType);        

        this.addNode(UaObjectTypes.FolderType);
        UaObjectTypes.FolderType.setParentType(UaObjectTypes.BaseObjectType);

        this.addNode(UaObjectTypes.ServerType);
        UaObjectTypes.ServerType.setParentType(UaObjectTypes.BaseObjectType);
        
        this.addNode(UaObjectTypes.ModellingRuleType);
        UaObjectTypes.ModellingRuleType.setParentType(UaObjectTypes.BaseObjectType);

        this.addNode(UaObjectTypes.BaseEventType);
        UaObjectTypes.BaseEventType.setParentType(UaObjectTypes.BaseObjectType);
    }

    private buildVariableTypes(): void {
        this.addNode(UaVariableTypes.BaseVariableType);

        this.addNode(UaVariableTypes.BaseDataVariableType);
        UaVariableTypes.BaseDataVariableType.setParentType(UaVariableTypes.BaseVariableType);

        this.addNode(UaVariableTypes.PropertyType);
        UaVariableTypes.PropertyType.setParentType(UaVariableTypes.BaseVariableType);

        this.addNode(UaVariableTypes.DataItemType);
        UaVariableTypes.DataItemType.setParentType(UaVariableTypes.BaseDataVariableType);

        this.addNode(UaVariableTypes.BaseAnalogItemType);
        UaVariableTypes.BaseAnalogItemType.setParentType(UaVariableTypes.DataItemType);
    }

    private buildDataTypes(): void {
        this.addNode(UaDataTypes.BaseDataType);        

        this.addNode(UaDataTypes.Boolean);
        UaDataTypes.Boolean.setParentType(UaDataTypes.BaseDataType);

        this.addNode(UaDataTypes.Number);
        UaDataTypes.Number.setParentType(UaDataTypes.BaseDataType);

        this.addNode(UaDataTypes.Integer);
        UaDataTypes.Integer.setParentType(UaDataTypes.Number);

        this.addNode(UaDataTypes.SByte);
        UaDataTypes.SByte.setParentType(UaDataTypes.Integer);

        this.addNode(UaDataTypes.Int16);
        UaDataTypes.Int16.setParentType(UaDataTypes.Integer);

        this.addNode(UaDataTypes.Int32);
        UaDataTypes.Int32.setParentType(UaDataTypes.Integer);

        this.addNode(UaDataTypes.Int64);
        UaDataTypes.Int64.setParentType(UaDataTypes.Integer);

        this.addNode(UaDataTypes.UInteger);
        UaDataTypes.UInteger.setParentType(UaDataTypes.Number);

        this.addNode(UaDataTypes.Byte);
        UaDataTypes.Byte.setParentType(UaDataTypes.UInteger);

        this.addNode(UaDataTypes.UInt16);
        UaDataTypes.UInt16.setParentType(UaDataTypes.UInteger);

        this.addNode(UaDataTypes.UInt32);
        UaDataTypes.UInt32.setParentType(UaDataTypes.UInteger);

        this.addNode(UaDataTypes.UInt64);
        UaDataTypes.UInt64.setParentType(UaDataTypes.UInteger);

        this.addNode(UaDataTypes.Float);
        UaDataTypes.Float.setParentType(UaDataTypes.Number);

        this.addNode(UaDataTypes.Double);
        UaDataTypes.Double.setParentType(UaDataTypes.Number);

        this.addNode(UaDataTypes.Duration);
        UaDataTypes.Duration.setParentType(UaDataTypes.Double);

        this.addNode(UaDataTypes.Decimal);
        UaDataTypes.Decimal.setParentType(UaDataTypes.Number);

        this.addNode(UaDataTypes.String);
        UaDataTypes.String.setParentType(UaDataTypes.BaseDataType);

        this.addNode(UaDataTypes.DateString);
        UaDataTypes.DateString.setParentType(UaDataTypes.String);

        this.addNode(UaDataTypes.NormalizedString);
        UaDataTypes.NormalizedString.setParentType(UaDataTypes.String);

        this.addNode(UaDataTypes.NumericRange);
        UaDataTypes.NumericRange.setParentType(UaDataTypes.String);

        this.addNode(UaDataTypes.UriString);
        UaDataTypes.UriString.setParentType(UaDataTypes.String);

        this.addNode(UaDataTypes.ByteString);
        UaDataTypes.ByteString.setParentType(UaDataTypes.BaseDataType);

        this.addNode(UaDataTypes.Image);
        UaDataTypes.Image.setParentType(UaDataTypes.ByteString);

        this.addNode(UaDataTypes.ImageBMP);
        UaDataTypes.ImageBMP.setParentType(UaDataTypes.Image);

        this.addNode(UaDataTypes.ImageGIF);
        UaDataTypes.ImageGIF.setParentType(UaDataTypes.Image);

        this.addNode(UaDataTypes.ImageJPG);
        UaDataTypes.ImageJPG.setParentType(UaDataTypes.Image);

        this.addNode(UaDataTypes.ImagePNG);
        UaDataTypes.ImagePNG.setParentType(UaDataTypes.Image);

        this.addNode(UaDataTypes.AudioDataType);
        UaDataTypes.AudioDataType.setParentType(UaDataTypes.ByteString);

        this.addNode(UaDataTypes.Enumeration);
        UaDataTypes.Enumeration.setParentType(UaDataTypes.BaseDataType);

        this.addNode(UaDataTypes.DateTime);
        UaDataTypes.DateTime.setParentType(UaDataTypes.BaseDataType);

        this.addNode(UaDataTypes.UtcTime);
        UaDataTypes.UtcTime.setParentType(UaDataTypes.DateTime);

        this.addNode(UaDataTypes.NodeId);
        UaDataTypes.NodeId.setParentType(UaDataTypes.BaseDataType);

        this.addNode(UaDataTypes.ExpandedNodeId);
        UaDataTypes.ExpandedNodeId.setParentType(UaDataTypes.BaseDataType);

        this.addNode(UaDataTypes.Guid);
        UaDataTypes.Guid.setParentType(UaDataTypes.BaseDataType);

        this.addNode(UaDataTypes.LocalizedText);
        UaDataTypes.LocalizedText.setParentType(UaDataTypes.BaseDataType);

        this.addNode(UaDataTypes.QualifiedName);
        UaDataTypes.QualifiedName.setParentType(UaDataTypes.BaseDataType);

        this.addNode(UaDataTypes.StatusCode);
        UaDataTypes.StatusCode.setParentType(UaDataTypes.BaseDataType);

        this.addNode(UaDataTypes.Structure);
        UaDataTypes.Structure.setParentType(UaDataTypes.BaseDataType);

        this.addNode(UaDataTypes.Range);
        UaDataTypes.Range.setParentType(UaDataTypes.Structure);

        this.addNode(UaDataTypes.EUInformation);
        UaDataTypes.EUInformation.setParentType(UaDataTypes.Structure);

        this.addNode(UaDataTypes.EnumValueType);
        UaDataTypes.EnumValueType.setParentType(UaDataTypes.Structure);
    }

    private buildObjects(): void {
        this.addNode(UaObjects.RootFolder);
        this.addNode(UaObjects.ObjectsFolder);
        this.addNode(UaObjects.TypesFolder);
        this.addNode(UaObjects.ViewsFolder);
        this.addNode(UaObjects.Server);
        this.addNode(UaObjects.ObjectTypesFolder);
        this.addNode(UaObjects.VariableTypesFolder);
        this.addNode(UaObjects.EventTypesFolder);
        this.addNode(UaObjects.DataTypesFolder);
        this.addNode(UaObjects.ReferenceTypesFolder);

        UaObjects.RootFolder.organizes(UaObjects.ObjectsFolder);
        UaObjects.RootFolder.organizes(UaObjects.TypesFolder);
        UaObjects.RootFolder.organizes(UaObjects.ViewsFolder);

        UaObjects.ObjectsFolder.organizes(UaObjects.Server);

        UaObjects.TypesFolder.organizes(UaObjects.ObjectTypesFolder);
        UaObjects.TypesFolder.organizes(UaObjects.VariableTypesFolder);
        UaObjects.TypesFolder.organizes(UaObjects.EventTypesFolder);
        UaObjects.TypesFolder.organizes(UaObjects.DataTypesFolder);
        UaObjects.TypesFolder.organizes(UaObjects.ReferenceTypesFolder);

        UaObjects.EventTypesFolder.organizes(UaObjectTypes.BaseEventType);

        UaObjects.ObjectTypesFolder.organizes(UaObjectTypes.BaseObjectType);
        UaObjects.VariableTypesFolder.organizes(UaVariableTypes.BaseVariableType);
        UaObjects.DataTypesFolder.organizes(UaDataTypes.BaseDataType);
        UaObjects.ReferenceTypesFolder.organizes(UaReferenceTypes.References);
    }

    private buildVariables(): void {
        // ServerType
        const serverType_ServerArray = new UaVariable(
            UaNodeId.from(VariableIds.ServerType_ServerArray),
            "ServerArray",
            new UaLocalizedText("ServerArray"),
            UaNodeId.from(DataTypeIds.String),
            UaValueRank.OneDimension,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType,
        );       

        UaObjectTypes.ServerType.addMember(serverType_ServerArray);
        this.addNode(serverType_ServerArray);

        const serverType_NamespaceArray = new UaVariable(
            UaNodeId.from(VariableIds.ServerType_NamespaceArray),
            "NamespaceArray",
            new UaLocalizedText("NamespaceArray"),
            UaNodeId.from(DataTypeIds.String),
            UaValueRank.OneDimension,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType,
        );

        UaObjectTypes.ServerType.addMember(serverType_NamespaceArray);
        this.addNode(serverType_NamespaceArray);       

        // Server
        const server_ServerArray = new UaVariable(
            UaNodeId.from(VariableIds.Server_ServerArray),
            "ServerArray",
            new UaLocalizedText("ServerArray"),
            UaNodeId.from(DataTypeIds.String),
            UaValueRank.OneDimension,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType,
        );
        UaObjects.Server.addMember(server_ServerArray);
        this.addNode(server_ServerArray);

        const server_NamespaceArray = new UaVariable(
            UaNodeId.from(VariableIds.Server_NamespaceArray),
            "NamespaceArray",
            new UaLocalizedText("NamespaceArray"),
            UaNodeId.from(DataTypeIds.String),
            UaValueRank.OneDimension,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType,
        );
        UaObjects.Server.addMember(server_NamespaceArray);
        this.addNode(server_NamespaceArray);

        // BaseEventType
        const baseEventType_EventId = new UaVariable(
            UaNodeId.from(VariableIds.BaseEventType_EventId),
            "EventId",
            new UaLocalizedText("EventId"),
            UaNodeId.from(DataTypeIds.ByteString),
            UaValueRank.Scalar,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType);   

        UaObjectTypes.BaseEventType.addMember(baseEventType_EventId);
        this.addNode(baseEventType_EventId);

        const baseEventType_EventType = new UaVariable(
            UaNodeId.from(VariableIds.BaseEventType_EventType),
            "EventType",
            new UaLocalizedText("EventType"),
            UaNodeId.from(DataTypeIds.NodeId),
            UaValueRank.Scalar,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType
        );
        UaObjectTypes.BaseEventType.addMember(baseEventType_EventType);
        this.addNode(baseEventType_EventType);

        const baseEventType_Time = new UaVariable(
            UaNodeId.from(VariableIds.BaseEventType_Time),
            "Time",
            new UaLocalizedText("Time"),            
            UaNodeId.from(DataTypeIds.UtcTime),
            UaValueRank.Scalar,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType
        );
        UaObjectTypes.BaseEventType.addMember(baseEventType_Time);
        this.addNode(baseEventType_Time);

        const baseEventType_Message = new UaVariable(
            UaNodeId.from(VariableIds.BaseEventType_Message),
            "Message",
            new UaLocalizedText("Message"),
            UaNodeId.from(DataTypeIds.LocalizedText),
            UaValueRank.Scalar,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType
        );
        UaObjectTypes.BaseEventType.addMember(baseEventType_Message);
        this.addNode(baseEventType_Message);

        // DataItemType
        const dataItemType_Definition = new UaVariable(
            UaNodeId.from(VariableIds.DataItemType_Definition),
            "Definition",
            new UaLocalizedText("Definition"),            
            UaNodeId.from(DataTypeIds.String),
            UaValueRank.Scalar,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType);

        this.addNode(dataItemType_Definition);
        UaVariableTypes.DataItemType.addMember(dataItemType_Definition);

        const dataItemType_ValuePrecision = new UaVariable(
            UaNodeId.from(VariableIds.DataItemType_ValuePrecision),
            "ValuePrecision",
            new UaLocalizedText("ValuePrecision"),            
            UaNodeId.from(DataTypeIds.Double),
            UaValueRank.Scalar,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType);
       
        this.addNode(dataItemType_ValuePrecision);
        UaVariableTypes.DataItemType.addMember(dataItemType_ValuePrecision);

        // BaseAnalogItemType
        const baseAnalogItemType_EuRange = new UaVariable(
            UaNodeId.from(VariableIds.BaseAnalogType_EURange),
            "EURange",
            new UaLocalizedText("EURange"),            
            UaNodeId.from(DataTypeIds.Range),
            UaValueRank.Scalar,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType);       

        this.addNode(baseAnalogItemType_EuRange);
        UaVariableTypes.BaseAnalogItemType.addMember(baseAnalogItemType_EuRange);

        const baseAnalogItemType_EngineeringUnits = new UaVariable(
            UaNodeId.from(VariableIds.BaseAnalogType_EngineeringUnits),
            "EngineeringUnits",
            new UaLocalizedText("EngineeringUnits"),            
            UaNodeId.from(DataTypeIds.EUInformation),
            UaValueRank.Scalar,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType);
       
        this.addNode(baseAnalogItemType_EngineeringUnits);
        UaVariableTypes.BaseAnalogItemType.addMember(baseAnalogItemType_EngineeringUnits);
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

    getReadTransactions(context: ReadContext, handleIds: Array<number>): Array<UaReadTransaction> {
        return [new UaReadNodeTransaction(context, handleIds, this)];
    }
}