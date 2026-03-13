import { ExtensionObject, HistoryReadRequest, HistoryReadValueId } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";

export class HistoryReadContext extends ServiceContext
{
    private _nodesToRead: Array<HistoryReadValueId>;
    private _historyReadDetails: ExtensionObject | null;
    private _timestampsToReturn: number;
    private _releaseContinuationPoints: boolean;   
    
    constructor(request: HistoryReadRequest, serverUri?: string)
    {
        super(serverUri, request.RequestHeader);
        this._nodesToRead = request.NodesToRead || [];
        this._historyReadDetails = (request.HistoryReadDetails) ? request.HistoryReadDetails : null;
        this._timestampsToReturn = request.TimestampsToReturn || 0;
        this._releaseContinuationPoints = request.ReleaseContinuationPoints || false;
    }

    get nodesToRead() : Array<HistoryReadValueId>
    {
        return this._nodesToRead;
    }

    get historyReadDetails() : ExtensionObject | null
    {
        return this._historyReadDetails;
    }

    get timestampsToReturn() : number
    {
        return this._timestampsToReturn;
    }

    get releaseContinuationPoints() : boolean
    {
        return this._releaseContinuationPoints;
    }
}