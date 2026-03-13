import { FindServersRequest } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";

export class FindServerContext extends ServiceContext
{
    private _endpointUrl: string;
    private _localeIds: Array<string>;
    private _serverUris: Array<string>;
    
    constructor(request: FindServersRequest)
    {
        super(undefined, request.RequestHeader);
        this._endpointUrl = (request.EndpointUrl) ? request.EndpointUrl : "";
        this._localeIds = (request.LocaleIds) ? request.LocaleIds : [];
        this._serverUris = (request.ServerUris) ? request.ServerUris : [];
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