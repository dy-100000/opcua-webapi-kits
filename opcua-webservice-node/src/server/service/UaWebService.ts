import { ApplicationDescription, BrowseResult, CallMethodResult, DataValue, EndpointDescription, StatusCode, BrowsePathResult, HistoryReadResult, HistoryUpdateResult } from "opcua-webapi";
import { BrowseContext, BrowseNextContext, CallContext, FindServerContext, GetEndpointContext, HistoryReadContext, HistoryUpdateContext, ReadContext, TranslateContext, UaServerConfigure, WriteContext } from "../types";

export interface UaWebService {
    getServerConfigure() : UaServerConfigure;
    
    getEndpoints(context : GetEndpointContext) : Promise<Array<EndpointDescription>>;

    findServers(context : FindServerContext) : Promise<Array<ApplicationDescription>>;

    browse(context : BrowseContext) : Promise<Array<BrowseResult>>;

    browseNext(context : BrowseNextContext) : Promise<Array<BrowseResult>>;

    translate(context : TranslateContext) : Promise<Array<BrowsePathResult>>;

    read(context : ReadContext) : Promise<Array<DataValue>>;

    write(context : WriteContext) : Promise<Array<StatusCode>>;

    call(context : CallContext) : Promise<Array<CallMethodResult>>;

    historyRead(context : HistoryReadContext) : Promise<Array<HistoryReadResult>>;

    historyUpdate(context : HistoryUpdateContext) : Promise<Array<HistoryUpdateResult>>;
}