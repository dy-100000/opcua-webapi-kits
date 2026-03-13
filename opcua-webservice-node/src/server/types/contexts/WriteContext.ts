import { WriteRequest, WriteValue } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";

export class WriteContext extends ServiceContext
{
    private _nodesToWrite: Array<WriteValue>;
    
    constructor(request: WriteRequest, serverUri?: string)
    {
        super(serverUri, request.RequestHeader);
        this._nodesToWrite = (request.NodesToWrite) ? request.NodesToWrite : [];
    }

    get nodesToWrite() : Array<WriteValue>
    {
        return this._nodesToWrite;
    }
}