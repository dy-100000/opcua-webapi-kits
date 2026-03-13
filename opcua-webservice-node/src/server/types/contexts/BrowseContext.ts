import { BrowseDescription, BrowseRequest } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";

export class BrowseContext extends ServiceContext
{
    private _nodesToBrowse: Array<BrowseDescription>;
    private _requestedMaxReferencesPerNode: number;   
    
    constructor(request: BrowseRequest, serverUri?: string)
    {
        super(serverUri, request.RequestHeader);
        this._nodesToBrowse = (request.NodesToBrowse) ? request.NodesToBrowse : [];
        this._requestedMaxReferencesPerNode = (request.RequestedMaxReferencesPerNode && request.RequestedMaxReferencesPerNode > 0) ? request.RequestedMaxReferencesPerNode : 0;
    }

    get nodesToBrowse() : Array<BrowseDescription>
    {
        return this._nodesToBrowse;
    }

    get requestedMaxReferencesPerNode() : number
    {
        return this._requestedMaxReferencesPerNode;
    }
}