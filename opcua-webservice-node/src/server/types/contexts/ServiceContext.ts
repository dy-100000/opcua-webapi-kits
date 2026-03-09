import { RequestHeader } from "opcua-webapi";

export abstract class ServiceContext
{
    private _serverUri : string | null;
    private _requestHeader : RequestHeader | null;

    constructor(
        serverUri : string | undefined,
        requestHeader: RequestHeader | undefined)
    {
        this._serverUri = (serverUri) ? serverUri : null;
        this._requestHeader = (requestHeader) ? requestHeader : null;
    }

    get serverUri() : string | null
    {
        return this._serverUri;
    }

    get requestHeader() : RequestHeader | null
    {
        return this._requestHeader;
    }
}