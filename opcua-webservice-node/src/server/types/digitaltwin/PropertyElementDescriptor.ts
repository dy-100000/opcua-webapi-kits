import { UaLocalizedText, UaNodeId, VariableTypeIds } from "opcua-webapi-ts";
import { UaVariableType } from "../../addressspace";

export class PropertyElementDescriptor {
    private readonly _id: string;
    private readonly _displayName: UaLocalizedText;
    private readonly _typeId: UaNodeId;

    constructor(propertyId: string, displayName: UaLocalizedText, variableType?: UaVariableType) {
        this._id = propertyId;
        this._displayName = displayName;
        this._typeId = (variableType) ? variableType.nodeId
            : UaNodeId.from(VariableTypeIds.BaseDataVariableType);
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