import { RequestHeader } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";
import { UaCallMethodRequest } from "opcua-webapi-ts";

export class CallContext extends ServiceContext
{
    private _methodsToCall: Array<UaCallMethodRequest>;
    
    constructor(
        methodsToCall: Array<UaCallMethodRequest>,
        serverUri?: string, 
        requestHeader? : RequestHeader)
    {
        super(serverUri, requestHeader);
        this._methodsToCall = (methodsToCall) ? methodsToCall : [];
    }

    get methodsToCall() : Array<UaCallMethodRequest>
    {
        return this._methodsToCall;
    }
}