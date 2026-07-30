import { UaObjectId } from "../../../types";

export class ReadObjectAttributeRequest {
    private readonly _objectId: UaObjectId;
    private readonly _attributeIds: Set<number>;

    constructor(objectId: UaObjectId, attributeIds: Set<number>) {
        this._objectId = objectId;
        this._attributeIds = attributeIds;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }

    get attributeIds(): Set<number> {
        return this._attributeIds;
    }
}