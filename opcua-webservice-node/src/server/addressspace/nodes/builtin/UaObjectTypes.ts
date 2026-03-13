import { ObjectTypeIds, UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaObjectType } from "../UaObjectType";

export class UaObjectTypes {
    static readonly BaseObjectType = new UaObjectType(UaNodeId.from(ObjectTypeIds.BaseObjectType), "BaseObjectType", new UaLocalizedText("BaseObjectType"), false, null);
    static readonly FolderType = new UaObjectType(UaNodeId.from(ObjectTypeIds.FolderType), "FolderType", new UaLocalizedText("FolderType"), false, UaObjectTypes.BaseObjectType);
    static readonly ServerType = new UaObjectType(UaNodeId.from(ObjectTypeIds.ServerType), "ServerType", new UaLocalizedText("ServerType"), false, UaObjectTypes.BaseObjectType);

    static readonly ModellingRuleType = new UaObjectType(UaNodeId.from(ObjectTypeIds.ModellingRuleType), "ModellingRuleType", new UaLocalizedText("ModellingRuleType"), false, UaObjectTypes.BaseObjectType);

    static readonly BaseEventType = new UaObjectType(UaNodeId.from(ObjectTypeIds.BaseEventType), "BaseEventType", new UaLocalizedText("BaseEventType"), false, UaObjectTypes.BaseObjectType);

    static readonly DigitalTwinType = new UaObjectType(new UaNodeId(1, 10), "DigitalTwinType", new UaLocalizedText("DigitalTwinType"), true, UaObjectTypes.BaseObjectType);
    static readonly DigitalTwinRepositoryType = new UaObjectType(new UaNodeId(1, 11), "DigitalTwinRepositoryType", new UaLocalizedText("DigitalTwinRepositoryType"), true, UaObjectTypes.BaseObjectType);
    static readonly SubmodelType = new UaObjectType(new UaNodeId(1, 12), "SubmodelType", new UaLocalizedText("SubmodelType"), true, UaObjectTypes.BaseObjectType);
    static readonly ElementType = new UaObjectType(new UaNodeId(1, 13), "ElementType", new UaLocalizedText("ElementType"), true, UaObjectTypes.BaseObjectType);
    static readonly ReferenceElementType = new UaObjectType(new UaNodeId(1, 14), "ReferenceElementType", new UaLocalizedText("ReferenceElementType"), true, UaObjectTypes.ElementType);
    static readonly ElementCollectionType = new UaObjectType(new UaNodeId(1, 15), "ElementCollectionType", new UaLocalizedText("ElementCollectionType"), true, UaObjectTypes.ElementType);
    static readonly ElementListType = new UaObjectType(new UaNodeId(1, 16), "ElementListType", new UaLocalizedText("ElementListType"), true, UaObjectTypes.ElementType);
    static readonly EventElementType = new UaObjectType(new UaNodeId(1, 17), "EventElementType", new UaLocalizedText("EventElementType"), true, UaObjectTypes.ElementType);
}