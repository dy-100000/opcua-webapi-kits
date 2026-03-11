import { makeUaStatusCode, UaStatusCode } from "./UaStatusCode";
import { UaVariant } from "./UaVariant";

export class UaDataValue
{
    private _value : UaVariant;
    private _statusCode : UaStatusCode;
    private _sourceTimestamp : Date | null;
    private _serverTimestamp : Date | null;

    constructor(
        value: UaVariant, 
        statusCode? : UaStatusCode,
        sourceTimestamp? : Date,
        serverTimestamp? : Date)
    {
        this._value = value;
        this._statusCode = (statusCode) ? statusCode : makeUaStatusCode();
        this._sourceTimestamp = (sourceTimestamp) ? sourceTimestamp : null;
        this._serverTimestamp = (serverTimestamp) ? serverTimestamp : null;
    }

    get value() : UaVariant
    {
        return this._value;
    }

    get statusCode() : UaStatusCode
    {
        return this._statusCode;
    }

    get sourceTimestamp() : Date | null
    { 
        return this._sourceTimestamp;
    }

    get serverTimestamp() : Date | null
    {
        return this._serverTimestamp;
    }

    static dataValue(value: UaVariant, timeStamp?: Date) : UaDataValue
    {
        return new UaDataValue(value,undefined,timeStamp,timeStamp);
    }
}