import { CallMethodResult, StatusCode, Variant } from "opcua-webapi";
import { DataTypeIds } from "../nodes";
import { UaNodeId, UaStatusCode, UaVariant } from "../types";
import { UaPayloadMapper } from ".";

export class UaCallMethodResult
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.CallMethodResult);

    private _statusCode: UaStatusCode;
    private _outputArguments: Array<UaVariant>;
    private _inputArgumentResults?: Array<UaStatusCode>;

    constructor(
        statusCode: UaStatusCode,
        outputArguments: Array<UaVariant>,
        inputArgumentResults?: Array<UaStatusCode>)
    {
        this._statusCode = statusCode;
        this._outputArguments = outputArguments;
        this._inputArgumentResults = inputArgumentResults;
    }

    get statusCode() : UaStatusCode
    {
        return this._statusCode;
    }

    get outputArguments() : Array<UaVariant>
    {
        return this._outputArguments;
    }

    get inputArgumentResults() : Array<UaStatusCode> | undefined
    {
        return this._inputArgumentResults;
    }

    toStruct() : CallMethodResult
    {
        let statusCode = UaPayloadMapper.statusCodeToWebApi(this._statusCode);

        let outputArguments: Array<Variant> = [];
        for (let item of this._outputArguments)
        {
            outputArguments.push(UaPayloadMapper.variantToWebApi(item));
        }

        let inputArgumentResults: Array<StatusCode> | undefined = undefined;
        if (this._inputArgumentResults) {
            inputArgumentResults = [];
            for (let item of this._inputArgumentResults)            {
                inputArgumentResults.push(UaPayloadMapper.statusCodeToWebApi(item));
            }
        }
               

        let callMethodResult : CallMethodResult = {
            StatusCode: statusCode,
            OutputArguments: outputArguments,
            InputArgumentResults: inputArgumentResults
        };

        return callMethodResult;
    }

    static fromStruct(callMethodResult : CallMethodResult) : UaCallMethodResult | null
    {
        let statusCode = UaPayloadMapper.statusCodeFromWebApi(callMethodResult.StatusCode);
        
        let outputArguments: Array<UaVariant> = [];

        if (callMethodResult.OutputArguments) {
            for (let item of callMethodResult.OutputArguments) {
                outputArguments.push(UaPayloadMapper.variantFromWebApi(item));
            }
        }

        let inputArgumentResults: Array<UaStatusCode> | undefined = undefined;
        if (callMethodResult.InputArgumentResults) {
            inputArgumentResults = [];
            for (let item of callMethodResult.InputArgumentResults) {
                inputArgumentResults.push(UaPayloadMapper.statusCodeFromWebApi(item));
            }
        }

        return new UaCallMethodResult(
            statusCode,
            outputArguments,
            inputArgumentResults
        );
    }
}