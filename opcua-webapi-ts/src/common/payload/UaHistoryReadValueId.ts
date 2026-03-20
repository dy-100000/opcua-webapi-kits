import { HistoryReadValueId } from "opcua-webapi";
import { DataTypeIds } from "../nodes";
import { parseUaNodeIdOrNull, UaNodeId } from "../types";

export class UaHistoryReadValueId
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.HistoryReadValueId);

    private _nodeId: UaNodeId;
    private _continuationPoint?: string;
    private _indexRange?: string;

    constructor(
        nodeId: UaNodeId,
        continuationPoint?: string | null,
        indexRange?: string)
    {
        this._nodeId = nodeId;
        this._continuationPoint = (continuationPoint) ? continuationPoint : undefined;
        this._indexRange = (indexRange) ? indexRange : undefined;
    }

    get nodeId() : UaNodeId
    {
        return this._nodeId;
    }

    get continuationPoint() : string | undefined
    {
        return this._continuationPoint;
    }

    get indexRange() : string | undefined
    {
        return this._indexRange;
    }

    toStruct() : HistoryReadValueId
    {
        let historyReadValueId : HistoryReadValueId = {
            NodeId: this._nodeId.toString(),
            ContinuationPoint: this._continuationPoint,
            IndexRange: this._indexRange
        };

        return historyReadValueId;
    }

    static fromStruct(historyReadValueId : HistoryReadValueId) : UaHistoryReadValueId | null
    {
        let nodeId = parseUaNodeIdOrNull(historyReadValueId.NodeId); 
        let continuationPoint = historyReadValueId.ContinuationPoint;
        let indexRange = historyReadValueId.IndexRange;
        
        if (null == nodeId) return null;

        return new UaHistoryReadValueId(
            nodeId, 
            continuationPoint, 
            indexRange);
    }
}