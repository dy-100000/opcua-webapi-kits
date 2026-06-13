import { RequestHeader } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";
import { UaDeleteNodesItem } from "opcua-webapi-ts";

export class DeleteNodesContext extends ServiceContext
{
    private _nodesToDelete: Array<UaDeleteNodesItem>;
    
    constructor(
        nodesToDelete: Array<UaDeleteNodesItem>,
        serverUri?: string, 
        requestHeader? : RequestHeader)
    {
        super(serverUri, requestHeader);
        this._nodesToDelete = (nodesToDelete) ? nodesToDelete : [];
    }

    get nodesToDelete() : Array<UaDeleteNodesItem>
    {
        return this._nodesToDelete;
    }
}