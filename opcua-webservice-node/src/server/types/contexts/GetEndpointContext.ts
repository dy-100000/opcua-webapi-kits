import { GetEndpointsRequest } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";

export class GetEndpointContext extends ServiceContext
{
    private _endpointUrl : string;
    private _localeIds : Array<string>;
    private _profileUris : Array<string>;

    constructor(request: GetEndpointsRequest)
    {
        super("", request.RequestHeader);

        this._endpointUrl = (request.EndpointUrl) ? request.EndpointUrl : "";
        this._localeIds = (request.LocaleIds) ? request.LocaleIds : [];
        this._profileUris = (request.ProfileUris) ? request.ProfileUris : [];
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