import { UaChildId, UaObjectId } from "../../../types";

export class ReadMemberAttributeRequest {
    private readonly _objectId: UaObjectId;
    private readonly _childId: UaChildId;

    constructor(
        objectId: UaObjectId,
        childId: UaChildId) {
        this._objectId = objectId;
        this._childId = childId;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }

    get childId(): UaChildId {
        return this._childId;
    }
}