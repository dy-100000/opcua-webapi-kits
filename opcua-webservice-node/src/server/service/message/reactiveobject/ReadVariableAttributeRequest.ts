import { UaObjectId } from "../../../types";

export class ReadVariableAttributeRequest {
    private readonly _objectId: UaObjectId;
    private readonly _variableId: string;

    constructor(
        objectId: UaObjectId,
        variableId: string,
    ) {
        this._objectId = objectId;
        this._variableId = variableId;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }

    get variableId(): string {
        return this._variableId;
    }
}