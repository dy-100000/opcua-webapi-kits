import { ApplicationDescription, EndpointDescription } from "opcua-webapi";
import { BrowseContext, BrowseNextContext, CallContext, FindServerContext, GetEndpointContext, HistoryReadContext, ReadContext, UaServerConfigure, WriteContext } from "../types";
import { UaBrowseResult, UaCallMethodResult, UaDataValue, UaHistoryReadResult, UaStatusCode } from "opcua-webapi-ts";

export interface UaWebService {
    getServerConfigure() : UaServerConfigure;
    
    getEndpoints(context : GetEndpointContext) : Promise<Array<EndpointDescription>>;

    findServers(context : FindServerContext) : Promise<Array<ApplicationDescription>>;

    browse(context : BrowseContext) : Promise<Array<UaBrowseResult>>;

    browseNext(context : BrowseNextContext) : Promise<Array<UaBrowseResult>>;

    read(context : ReadContext) : Promise<Array<UaDataValue>>;

    write(context : WriteContext) : Promise<Array<UaStatusCode>>;

    call(context : CallContext) : Promise<Array<UaCallMethodResult>>;

    historyRead(context : HistoryReadContext) : Promise<Array<UaHistoryReadResult>>;
}