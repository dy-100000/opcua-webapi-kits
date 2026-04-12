import { RequestHeader } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";
import { UaBrowseDescription } from "opcua-webapi-ts";

export class BrowseContext extends ServiceContext
{
    private _nodesToBrowse: Array<UaBrowseDescription>;
    private _requestedMaxReferencesPerNode: number;   
    
    constructor(
        browseDescription: Array<UaBrowseDescription>, 
        requestedMaxReferencesPerNode? : number,
        serverUri?: string, 
        requestHeader? : RequestHeader)
    {
        super(serverUri, requestHeader);
        this._nodesToBrowse = browseDescription;
        this._requestedMaxReferencesPerNode = (requestedMaxReferencesPerNode && requestedMaxReferencesPerNode > 0) ? requestedMaxReferencesPerNode : 0;
    }

    get nodesToBrowse() : Array<UaBrowseDescription>
    {
        return this._nodesToBrowse;
    }

    get requestedMaxReferencesPerNode() : number
    {
        return this._requestedMaxReferencesPerNode;
    }
}