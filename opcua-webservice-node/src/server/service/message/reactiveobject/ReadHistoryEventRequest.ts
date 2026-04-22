import { UaExtensionObject } from "opcua-webapi-ts";
import { UaObjectId } from "../../../types";

export class ReadHistoryEventRequest {
    private readonly _objectId: UaObjectId;
    private readonly _details: UaExtensionObject;
    private readonly _offset: number;

    constructor(
        objectId: UaObjectId,
        details: UaExtensionObject,
        offset: number,
    ) {
        this._objectId = objectId;
        this._details = details;
        this._offset = offset;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }

    get details(): UaExtensionObject {
        return this._details;
    }

    get offset(): number {
        return this._offset;
    }
}