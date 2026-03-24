import { HistoryReadResult, HistoryReadValueId } from "opcua-webapi";
import { DataTypeIds } from "../nodes";
import { UaExtensionObject, UaNodeId, UaStatusCode } from "../types";
import { UaPayloadMapper } from ".";

export class UaHistoryReadResult
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.HistoryReadResult);

    private _statusCode: UaStatusCode;
    private _historyData?: UaExtensionObject;
    private _continuationPoint?: string;    

    constructor(
        statusCode: UaStatusCode, 
        historyData?: UaExtensionObject,
        continuationPoint?: string)
    {
        this._statusCode = statusCode;
        this._historyData = historyData;
        this._continuationPoint = (continuationPoint) ? continuationPoint : undefined;
    }

    get statusCode() : UaStatusCode
    {
        return this._statusCode;
    }

    get historyData() : UaExtensionObject | undefined
    {
        return this._historyData;
    }

    get continuationPoint() : string | undefined
    {
        return this._continuationPoint;
    }


    toStruct() : HistoryReadResult
    {
        let historyReadResult : HistoryReadResult = {
            StatusCode: UaPayloadMapper.statusCodeToWebApi(this._statusCode),
            HistoryData: UaPayloadMapper.extensionObjectToWebApi(this._historyData),
            ContinuationPoint: this._continuationPoint
        };

        return historyReadResult;
    }

    static fromStruct(historyReadResult : HistoryReadResult) : UaHistoryReadResult | null
    {
        let statusCode = UaPayloadMapper.statusCodeFromWebApi(historyReadResult.StatusCode);
        let historyData = undefined;
        let continuationPoint = historyReadResult.ContinuationPoint;
        
        if (historyReadResult.HistoryData) {
            historyData = UaPayloadMapper.extensionObjectFromWebApi(historyReadResult.HistoryData);
        }
        
        return new UaHistoryReadResult(
            statusCode,
            historyData,
            continuationPoint
        );
    }
}