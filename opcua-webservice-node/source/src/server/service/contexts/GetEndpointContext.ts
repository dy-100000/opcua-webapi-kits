import { RequestHeader } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";

export class GetEndpointContext extends ServiceContext
{
    private _endpointUrl : string;
    private _localeIds : Array<string>;
    private _profileUris : Array<string>;

    constructor(
        endpointUrl?: string, 
        localeIds?: Array<string>, 
        profileUris?: Array<string>,
        serverUri?: string, 
        requestHeader? : RequestHeader)
    {
        super(serverUri, requestHeader);

        this._endpointUrl = (endpointUrl) ? endpointUrl : "";
        this._localeIds = (localeIds) ? localeIds : [];
        this._profileUris = (profileUris) ? profileUris : [];
    }

    get endpointUrl() : string
    {
        return this._endpointUrl;
    }

    get localeIds() : Array<string>
    {
        return this._localeIds;
    }

    get profileUris() : Array<string>
    {
        return this._profileUris;
    }
}