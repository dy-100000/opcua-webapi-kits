import { RequestHeader } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";
import { UaReadValueId } from "opcua-webapi-ts";

export class ReadContext extends ServiceContext
{
    private _nodesToRead: Array<UaReadValueId>;
    private _timestampsToReturn: number;
    private _maxAge: number | null;
        
    constructor(
        nodesToRead: Array<UaReadValueId>,
        timestampsToReturn?: number,
        maxAge?: number,
        serverUri?: string,
        requestHeader?: RequestHeader)
    {
        super(serverUri, requestHeader);

        this._nodesToRead = (nodesToRead) ? nodesToRead : [];
        this._maxAge = (maxAge !== undefined) ? maxAge : null;
        this._timestampsToReturn = (timestampsToReturn  && timestampsToReturn > 0) ? timestampsToReturn : 0;
    }

    get nodesToRead() : Array<UaReadValueId>
    {
        return this._nodesToRead;
    }

    get timestampsToReturn() : number
    {
        return this._timestampsToReturn;
    }

    get maxAge() : number | null
    {
        return this._maxAge;
    }
}
