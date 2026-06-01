import { UaExtensionObject } from "opcua-webapi-ts";
import { UaChildId, UaObjectId } from "../../../types";

export class ReadHistoryDataRequest {
    private readonly _objectId: UaObjectId;
    private readonly _childId: UaChildId;
    private readonly _details: UaExtensionObject;
    private readonly _offset: number;

    constructor(
        objectId: UaObjectId,
        childId: UaChildId,
        details: UaExtensionObject,
        offset: number,
    ) {
        this._objectId = objectId;
        this._childId = childId;
        this._details = details;
        this._offset = offset;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }

    get childId(): UaChildId {
        return this._childId;
    }

    get details(): UaExtensionObject {
        return this._details;
    }

    get offset(): number {
        return this._offset;
    }
}