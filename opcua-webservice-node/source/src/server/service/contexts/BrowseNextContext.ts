import { RequestHeader } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";

export class BrowseNextContext extends ServiceContext
{
    private _continuationPoints: Array<string>;
    private _releaseContinuationPoints: boolean;  
    
    constructor(
        releaseContinuationPoints: boolean,
        continuationPoints?: Array<string>,        
        serverUri?: string,
        requestHeader? : RequestHeader)
    {
        super(serverUri, requestHeader);
        this._releaseContinuationPoints = releaseContinuationPoints;
        this._continuationPoints = (continuationPoints) ? continuationPoints : [];        
    }

    get continuationPoints() : Array<string>
    {
        return this._continuationPoints;
    }

    get releaseContinuationPoints() : boolean
    {
        return this._releaseContinuationPoints;
    }
}