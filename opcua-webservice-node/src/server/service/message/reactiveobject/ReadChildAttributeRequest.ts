import { UaVariableType } from "../../../addressspace/nodes/UaVariableType";
import { UaChildId, UaObjectId } from "../../../types";

export class ReadChildAttributeRequest {
    private readonly _objectId: UaObjectId;
    private readonly _childId: UaChildId;
    private readonly _childVariableType: UaVariableType | null;

    constructor(
        objectId: UaObjectId,
        childId: UaChildId,
        childVariableType: UaVariableType | null,
    ) {
        this._objectId = objectId;
        this._childId = childId;
        this._childVariableType = childVariableType;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }

    get childId(): UaChildId {
        return this._childId;
    }

    get childVariableType(): UaVariableType | null {
        return this._childVariableType;
    }
}