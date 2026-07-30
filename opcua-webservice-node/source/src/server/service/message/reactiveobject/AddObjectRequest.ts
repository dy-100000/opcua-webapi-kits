import { UaLocalizedText } from "opcua-webapi-ts";
import { UaObjectId } from "../../../types";

export class AddObjectRequest {
    private readonly _parentId: UaObjectId;
    private readonly _browseName: string | null;
    private readonly _displayName: UaLocalizedText | null;

    constructor(
        parentId: UaObjectId, 
        browseName: string | null, 
        displayName: UaLocalizedText | null) {
        this._parentId = parentId;
        this._browseName = browseName;
        this._displayName = displayName;
    }

    get parentId(): UaObjectId {
        return this._parentId;
    }

    get browseName(): string | null {
        return this._browseName;
    }

    get displayName(): UaLocalizedText | null {
        return this._displayName;
    }
}