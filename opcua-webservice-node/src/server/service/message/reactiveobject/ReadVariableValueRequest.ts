import { UaChildId, UaObjectId } from "../../../types";

export class ReadVariableValueRequest {
    private readonly _objectId: UaObjectId;
    private readonly _variableIds: Set<string>;

    constructor(
        objectId: UaObjectId,
        variableIds: Set<string>,
    ) {
        this._objectId = objectId;
        this._variableIds = variableIds;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }

    get variableIds(): Set<string> {
        return this._variableIds;
    }
}