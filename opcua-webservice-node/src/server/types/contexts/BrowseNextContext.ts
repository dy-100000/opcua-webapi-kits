import { BrowseNextRequest } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";

export class BrowseNextContext extends ServiceContext
{
    private _continuationPoints?: Array<string>;
    private _releaseContinuationPoints?: boolean;  
    
    constructor(request: BrowseNextRequest, serverUri?: string)
    {
        super(serverUri, request.RequestHeader);
        this._continuationPoints = (request.ContinuationPoints) ? request.ContinuationPoints : [];
        this._releaseContinuationPoints = (request.ReleaseContinuationPoints) ? request.ReleaseContinuationPoints : false;
    }

    get continuationPoints() : Array<string> | undefined
    {
        return this._continuationPoints;
    }

    get releaseContinuationPoints() : boolean | undefined
    {
        return this._releaseContinuationPoints;
    }
}