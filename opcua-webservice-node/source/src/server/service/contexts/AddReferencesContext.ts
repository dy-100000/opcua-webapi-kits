import { RequestHeader } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";
import { UaAddReferencesItem } from "opcua-webapi-ts";

export class AddReferencesContext extends ServiceContext
{
    private _referencesToAdd: Array<UaAddReferencesItem>;
    
    constructor(
        referencesToAdd: Array<UaAddReferencesItem>,
        serverUri?: string, 
        requestHeader? : RequestHeader)
    {
        super(serverUri, requestHeader);
        this._referencesToAdd = (referencesToAdd) ? referencesToAdd : [];
    }

    get referencesToAdd() : Array<UaAddReferencesItem>
    {
        return this._referencesToAdd;
    }
}