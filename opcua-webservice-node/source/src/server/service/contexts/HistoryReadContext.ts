import { RequestHeader } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";
import { UaExtensionObject, UaHistoryReadValueId } from "opcua-webapi-ts";

export class HistoryReadContext extends ServiceContext
{
    private _nodesToRead: Array<UaHistoryReadValueId>;
    private _historyReadDetails: UaExtensionObject;
    private _timestampsToReturn: number;
    private _releaseContinuationPoints: boolean;
    
    constructor(
        nodesToRead: Array<UaHistoryReadValueId>,
        historyReadDetails: UaExtensionObject,
        timestampsToReturn?: number,
        releaseContinuationPoints?: boolean,
        serverUri?: string,
        requestHeader?: RequestHeader)
    {
        super(serverUri, requestHeader);
        this._nodesToRead = nodesToRead || [];
        this._historyReadDetails = historyReadDetails;
        this._timestampsToReturn = (timestampsToReturn && timestampsToReturn > 0) ? timestampsToReturn : 0;
        this._releaseContinuationPoints = releaseContinuationPoints || false;
    }

    get nodesToRead() : Array<UaHistoryReadValueId>
    {
        return this._nodesToRead;
    }

    get historyReadDetails() : UaExtensionObject
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