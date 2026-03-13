import { CallMethodRequest, CallRequest } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";

export class CallContext extends ServiceContext
{
    private _methodsToCall: Array<CallMethodRequest>;
    
    constructor(request: CallRequest, serverUri?: string)
    {
        super(serverUri, request.RequestHeader);
        this._methodsToCall = (request.MethodsToCall) ? request.MethodsToCall : [];
    }

    get methodsToCall() : Array<CallMethodRequest>
    {
        return this._methodsToCall;
    }
}