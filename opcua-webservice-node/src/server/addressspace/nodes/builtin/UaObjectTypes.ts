import { ObjectTypeIds, UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaObjectType } from "../UaObjectType";

export class UaObjectTypes {
    static readonly BaseObjectType = new UaObjectType(UaNodeId.from(ObjectTypeIds.BaseObjectType), "BaseObjectType", new UaLocalizedText("BaseObjectType"), false);
    static readonly FolderType = new UaObjectType(UaNodeId.from(ObjectTypeIds.FolderType), "FolderType", new UaLocalizedText("FolderType"), false);
    static readonly ServerType = new UaObjectType(UaNodeId.from(ObjectTypeIds.ServerType), "ServerType", new UaLocalizedText("ServerType"), false);
    static readonly ModellingRuleType = new UaObjectType(UaNodeId.from(ObjectTypeIds.ModellingRuleType), "ModellingRuleType", new UaLocalizedText("ModellingRuleType"), false);
    static readonly BaseEventType = new UaObjectType(UaNodeId.from(ObjectTypeIds.BaseEventType), "BaseEventType", new UaLocalizedText("BaseEventType"), false);

    static readonly DigitalTwinType = new UaObjectType(new UaNodeId(1, 10), "DigitalTwinType", new UaLocalizedText("DigitalTwinType"), true);
    static readonly DigitalTwinRepositoryType = new UaObjectType(new UaNodeId(1, 11), "DigitalTwinRepositoryType", new UaLocalizedText("DigitalTwinRepositoryType"), true);
    static readonly SubmodelType = new UaObjectType(new UaNodeId(1, 12), "SubmodelType", new UaLocalizedText("SubmodelType"), true);
    static readonly ElementType = new UaObjectType(new UaNodeId(1, 13), "ElementType", new UaLocalizedText("ElementType"), true);
    static readonly ReferenceElementType = new UaObjectType(new UaNodeId(1, 14), "ReferenceElementType", new UaLocalizedText("ReferenceElementType"), true);
    static readonly ElementCollectionType = new UaObjectType(new UaNodeId(1, 15), "ElementCollectionType", new UaLocalizedText("ElementCollectionType"), true);
    static readonly ElementListType = new UaObjectType(new UaNodeId(1, 16), "ElementListType", new UaLocalizedText("ElementListType"), true);
    static readonly EventElementType = new UaObjectType(new UaNodeId(1, 17), "EventElementType", new UaLocalizedText("EventElementType"), true);
}