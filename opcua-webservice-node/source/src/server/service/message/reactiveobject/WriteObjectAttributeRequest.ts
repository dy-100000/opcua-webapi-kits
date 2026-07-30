import { UaObjectId } from "../../../types";
import { UaLocalizedText } from "opcua-webapi-ts";

export class WriteObjectAttributeRequest {
    private readonly _objectId: UaObjectId;
    private readonly _displayName: UaLocalizedText | null;
    private readonly _description: UaLocalizedText | null;

    constructor(
        objectId: UaObjectId, 
        displayName: UaLocalizedText | null, 
        description: UaLocalizedText | null) {
        this._objectId = objectId;
        this._displayName = displayName;
        this._description = description;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }

    get displayName(): UaLocalizedText | null {
        return this._displayName;
    }

    get description(): UaLocalizedText | null {
        return this._description;
    }
}