import { BrowsePath, TranslateBrowsePathsToNodeIdsRequest } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";

export class TranslateContext extends ServiceContext
{
    private _browsePaths: Array<BrowsePath>;
    
    constructor(request: TranslateBrowsePathsToNodeIdsRequest, serverUri?: string)
    {
        super(serverUri, request.RequestHeader);
        this._browsePaths = (request.BrowsePaths) ? request.BrowsePaths : [];
    }

    get browsePaths() : Array<BrowsePath>
    {
        return this._browsePaths;
    }
}