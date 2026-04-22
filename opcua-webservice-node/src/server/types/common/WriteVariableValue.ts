import { DataValue } from "opcua-webapi";
import { UaChildId } from "./UaChildId";

export class WriteVariableValue {
    private readonly _variableId: UaChildId;
    private readonly _value: DataValue;

    constructor(variableId: UaChildId, value: DataValue) {
        this._variableId = variableId;
        this._value = value;
    }

    get variableId(): UaChildId {
        return this._variableId;
    }

    get value(): DataValue {
        return this._value;
    }
}