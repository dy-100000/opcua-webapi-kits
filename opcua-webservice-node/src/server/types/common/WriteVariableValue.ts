import { DataValue } from "opcua-webapi";
import { UaChildId } from "./UaChildId";
import { UaDataValue } from "opcua-webapi-ts";

export class WriteVariableValue {
    private readonly _variableId: UaChildId;
    private readonly _value: UaDataValue;

    constructor(variableId: UaChildId, value: UaDataValue) {
        this._variableId = variableId;
        this._value = value;
    }

    get variableId(): UaChildId {
        return this._variableId;
    }

    get value(): UaDataValue {
        return this._value;
    }
}