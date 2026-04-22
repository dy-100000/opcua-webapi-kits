import { UaVariant } from "opcua-webapi-ts";
import { UaObjectId } from "../../../types";

export class WriteVariableValueRequest {
    private readonly _objectId: UaObjectId;
    private readonly _variableValues: Map<string, UaVariant>;

    constructor(
        objectId: UaObjectId,
        variableValues: Map<string, UaVariant>,
    ) {
        this._objectId = objectId;
        this._variableValues = variableValues;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }

    get variableValues(): Map<string, UaVariant> {
        return this._variableValues;
    }
}