import { AddNodesResult } from "opcua-webapi";
import { parseUaNodeIdOrNull, UaNodeId, UaStatusCode } from "../types";
import { DataTypeIds } from "../nodes";
import { UaPayloadMapper } from "../..";

export class UaAddNodesResult
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.AddNodesResult);
    
    private _statusCode : UaStatusCode;    
    private _addedNodeId : UaNodeId;

    constructor(
        statusCode : UaStatusCode,
        addedNodeId : UaNodeId)
    {
        this._statusCode = statusCode;
        this._addedNodeId = addedNodeId;
    }

    get statusCode() : UaStatusCode
    {
        return this._statusCode;
    }

    get addedNodeId() : UaNodeId
    {
        return this._addedNodeId;
    }

    toStruct() : AddNodesResult
    {        
        let addNodesResult : AddNodesResult = {
            StatusCode: UaPayloadMapper.statusCodeToWebApi(this._statusCode),
            AddedNodeId: this._addedNodeId.toString()
        };
        return addNodesResult;
    }

    static fromStruct(addNodesResult : AddNodesResult) : UaAddNodesResult | null
    {
        if (addNodesResult.StatusCode === undefined || addNodesResult.AddedNodeId === undefined) return null;

        let statusCode = UaPayloadMapper.statusCodeFromWebApi(addNodesResult.StatusCode);
        let addedNodeId = parseUaNodeIdOrNull(addNodesResult.AddedNodeId);

        if (addedNodeId === null) return null;
        return new UaAddNodesResult(statusCode, addedNodeId);
    }
}