import { UaObjectId } from "../../../types";

export class ReadObjectAttributeRequest {
    private readonly _objectId: UaObjectId;

    constructor(objectId: UaObjectId) {
        this._objectId = objectId;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }
}