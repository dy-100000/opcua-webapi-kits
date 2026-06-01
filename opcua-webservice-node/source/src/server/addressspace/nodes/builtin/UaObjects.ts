import { ObjectIds, UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaObject } from "../UaObject";
import { UaObjectTypes } from "./UaObjectTypes";

export class UaObjects {
    static RootFolder = new UaObject(UaNodeId.from(ObjectIds.RootFolder), "Root", new UaLocalizedText("Root"), UaObjectTypes.FolderType);
    static ObjectsFolder = new UaObject(UaNodeId.from(ObjectIds.ObjectsFolder), "Objects", new UaLocalizedText("Objects"), UaObjectTypes.FolderType);
    static Server = new UaObject(UaNodeId.from(ObjectIds.Server), "Server", new UaLocalizedText("Server"), UaObjectTypes.ServerType);

    static TypesFolder = new UaObject(UaNodeId.from(ObjectIds.TypesFolder), "Types", new UaLocalizedText("Types"), UaObjectTypes.FolderType);
    static ObjectTypesFolder = new UaObject(UaNodeId.from(ObjectIds.ObjectTypesFolder), "ObjectTypes", new UaLocalizedText("ObjectTypes"), UaObjectTypes.FolderType);
    static VariableTypesFolder = new UaObject(UaNodeId.from(ObjectIds.VariableTypesFolder), "VariableTypes", new UaLocalizedText("VariableTypes"), UaObjectTypes.FolderType);
    static EventTypesFolder = new UaObject(UaNodeId.from(ObjectIds.EventTypesFolder), "EventTypes", new UaLocalizedText("EventTypes"), UaObjectTypes.FolderType);
    static DataTypesFolder = new UaObject(UaNodeId.from(ObjectIds.DataTypesFolder), "DataTypes", new UaLocalizedText("DataTypes"), UaObjectTypes.FolderType);
    static ReferenceTypesFolder = new UaObject(UaNodeId.from(ObjectIds.ReferenceTypesFolder), "ReferenceTypes", new UaLocalizedText("ReferenceTypes"), UaObjectTypes.FolderType);
    static ViewsFolder = new UaObject(UaNodeId.from(ObjectIds.ViewsFolder), "Views", new UaLocalizedText("Views"), UaObjectTypes.FolderType);

    static ModellingRule_Mandatory = new UaObject(UaNodeId.from(ObjectIds.ModellingRule_Mandatory), "Mandatory", new UaLocalizedText("Mandatory"), UaObjectTypes.ModellingRuleType);
    static ModellingRule_Optional = new UaObject(UaNodeId.from(ObjectIds.ModellingRule_Optional), "Optional", new UaLocalizedText("Optional"), UaObjectTypes.ModellingRuleType);
    static ModellingRule_MandatoryPlaceHolder = new UaObject(UaNodeId.from(ObjectIds.ModellingRule_MandatoryPlaceholder), "MandatoryPlaceholder", new UaLocalizedText("MandatoryPlaceholder"), UaObjectTypes.ModellingRuleType);
    static ModellingRule_OptionalPlaceHolder = new UaObject(UaNodeId.from(ObjectIds.ModellingRule_OptionalPlaceholder), "OptionalPlaceholder", new UaLocalizedText("OptionalPlaceholder"), UaObjectTypes.ModellingRuleType);
}