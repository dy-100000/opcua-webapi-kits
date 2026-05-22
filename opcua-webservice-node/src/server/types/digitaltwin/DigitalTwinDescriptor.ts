import { UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaObjectType } from "../../addressspace/nodes/UaObjectType";

export class DigitalTwinDescriptor {
    private readonly _id: string;
    private readonly _displayName: UaLocalizedText;
    private readonly _typeId: UaNodeId;

    constructor(
        id: string, 
        displayName: UaLocalizedText, 
        digitalTwinType: UaObjectType) {
        this._id = id;
        this._displayName = displayName;
        this._typeId = digitalTwinType.nodeId;
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
