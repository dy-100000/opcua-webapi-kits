import { UaObjectId } from "../../../types";
import { UaObjectType,UaReferenceType } from "../../../addressspace";

export class DeleteReferenceRequest {
    private readonly _sourceId: UaObjectId;
    private readonly _targetId: UaObjectId;
    private readonly _targetType: UaObjectType;
    private readonly _referenceType: UaReferenceType;

    constructor(
        sourceId: UaObjectId,
        targetId: UaObjectId,
        targetType: UaObjectType,
        referenceType: UaReferenceType) {
        this._sourceId = sourceId;
        this._targetId = targetId;
        this._targetType = targetType;
        this._referenceType = referenceType;
    }

    get sourceId(): UaObjectId {
        return this._sourceId;
    }

    get targetId(): UaObjectId {
        return this._targetId;
    }

    get targetType(): UaObjectType {
        return this._targetType;
    }

    get referenceType(): UaReferenceType {
        return this._referenceType;
    }
}