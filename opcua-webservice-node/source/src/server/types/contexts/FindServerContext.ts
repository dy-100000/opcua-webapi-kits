import { FindServersRequest, RequestHeader } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";

export class FindServerContext extends ServiceContext
{
    private _endpointUrl: string;
    private _localeIds: Array<string>;
    private _serverUris: Array<string>;
    
    constructor(
        endpointUrl?: string, 
        localeIds?: Array<string>, 
        serverUris?: Array<string>,
        requestHeader? : RequestHeader)
    {
        super(undefined, requestHeader);
        this._endpointUrl = (endpointUrl) ? endpointUrl : "";
        this._localeIds = (localeIds) ? localeIds : [];
        this._serverUris = (serverUris) ? serverUris : [];
    }

    get endpointUrl() : string
    {
        return this._endpointUrl;
    }

    get localeIds() : Array<string>
    {
        return this._localeIds;
    }

    get serverUris() : Array<string>
    {
        return this._serverUris;
    }
}