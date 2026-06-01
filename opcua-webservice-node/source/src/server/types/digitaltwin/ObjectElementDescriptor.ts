import { UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaObjectType } from "../../addressspace/nodes/UaObjectType";

export class ObjectElementDescriptor {
    private readonly _id: string;
    private readonly _displayName: UaLocalizedText;
    private readonly _typeId: UaNodeId;

    constructor(elementId: string, displayName: UaLocalizedText, elementType: UaObjectType) {
        this._id = elementId;
        this._displayName = displayName;
        this._typeId = elementType.nodeId;
    }

    get id(): string {
        return this._id;
    }

    get displayName(): UaLocalizedText {
        return this._displayName;
    }

    get typeId(): UaNodeId {
        return this._typeId;
    }
}