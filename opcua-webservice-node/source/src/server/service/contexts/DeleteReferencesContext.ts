import { RequestHeader } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";
import { UaDeleteReferencesItem } from "opcua-webapi-ts";

export class DeleteReferencesContext extends ServiceContext
{
    private _referencesToDelete: Array<UaDeleteReferencesItem>;
    
    constructor(
        referencesToDelete: Array<UaDeleteReferencesItem>,
        serverUri?: string, 
        requestHeader? : RequestHeader)
    {
        super(serverUri, requestHeader);
        this._referencesToDelete = (referencesToDelete) ? referencesToDelete : [];
    }

    get referencesToDelete() : Array<UaDeleteReferencesItem>
    {
        return this._referencesToDelete;
    }
}