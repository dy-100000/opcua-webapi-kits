import { ReadRequest, ReadValueId } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";

export class ReadContext extends ServiceContext
{
    private _nodesToRead: Array<ReadValueId>;
    private _maxAge: number | null;
    private _timestampsToReturn: number | null;
    
    constructor(request: ReadRequest, serverUri?: string)
    {
        super(serverUri, request.RequestHeader);

        this._nodesToRead = (request.NodesToRead) ? request.NodesToRead : [];
        this._maxAge = (request.MaxAge !== undefined) ? request.MaxAge : null;
        this._timestampsToReturn = (request.TimestampsToReturn !== undefined) ? request.TimestampsToReturn : null;
    }

    get nodesToRead() : Array<ReadValueId>
    {
        return this._nodesToRead;
    }

    get maxAge() : number | null
    {
        return this._maxAge;
    }

    get timestampsToReturn() : number | null
    {
        return this._timestampsToReturn;
    }
}
