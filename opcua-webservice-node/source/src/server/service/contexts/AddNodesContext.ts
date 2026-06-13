import { RequestHeader } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";
import { UaAddNodesItem } from "opcua-webapi-ts";

export class AddNodesContext extends ServiceContext
{
    private _nodesToAdd: Array<UaAddNodesItem>;
    
    constructor(
        nodesToAdd: Array<UaAddNodesItem>,
        serverUri?: string, 
        requestHeader? : RequestHeader)
    {
        super(serverUri, requestHeader);
        this._nodesToAdd = (nodesToAdd) ? nodesToAdd : [];
    }

    get nodesToAdd() : Array<UaAddNodesItem>
    {
        return this._nodesToAdd;
    }
}