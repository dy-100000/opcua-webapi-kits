import { UaChildId, UaObjectId } from "../../../types";

export class ReadMemberAttributeRequest {
    private readonly _objectId: UaObjectId;
    private readonly _childId: UaChildId;
    private readonly _isMethod: boolean;

    constructor(
        objectId: UaObjectId,
        childId: UaChildId,
        isMethod: boolean) {
        this._objectId = objectId;
        this._childId = childId;
        this._isMethod = isMethod;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }

    get childId(): UaChildId {
        return this._childId;
    }

    get isMethod(): boolean {
        return this._isMethod;
    }
}