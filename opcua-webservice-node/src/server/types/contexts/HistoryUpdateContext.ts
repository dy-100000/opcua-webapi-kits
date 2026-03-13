import { ExtensionObject, HistoryUpdateRequest } from "opcua-webapi";
import { ServiceContext } from "./ServiceContext";

export class HistoryUpdateContext extends ServiceContext
{
    private _historyUpdateDetails?: Array<ExtensionObject>;
    
    constructor(request: HistoryUpdateRequest, serverUri?: string)
    {
        super(serverUri, request.RequestHeader);
        this._historyUpdateDetails = request.HistoryUpdateDetails || [];
    }

    get historyUpdateDetails() : Array<ExtensionObject> | undefined
    {
        return this._historyUpdateDetails;
    }
}