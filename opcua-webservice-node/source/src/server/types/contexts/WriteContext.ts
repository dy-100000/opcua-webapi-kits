import { RequestHeader, WriteRequest, WriteValue } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";
import { UaWriteValue } from "opcua-webapi-ts";

export class WriteContext extends ServiceContext
{
    private _nodesToWrite: Array<UaWriteValue>;
    
    constructor(
        nodesToWrite: Array<UaWriteValue>,
        serverUri?: string,
        requestHeader?: RequestHeader)
    {
        super(serverUri, requestHeader);
        this._nodesToWrite = (nodesToWrite) ? nodesToWrite : [];
    }

    get nodesToWrite() : Array<UaWriteValue>
    {
        return this._nodesToWrite;
    }
}