import { UaObjectId } from "../common/UaObjectId";

export class ObjectServiceContext {
    private readonly _objectId: UaObjectId;

    constructor(objectId: UaObjectId) {
        this._objectId = objectId;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }

    getCurrentObjectName(): string {
        return this._objectId.instance?.browseName ?? this._objectId.id;
    }
}