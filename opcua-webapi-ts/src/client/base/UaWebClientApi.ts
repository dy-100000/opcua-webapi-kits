import { BrowseNextRequest, BrowseNextResponse, BrowseRequest, BrowseResponse, CallRequest, CallResponse, FindServersRequest, FindServersResponse, GetEndpointsRequest, GetEndpointsResponse, HistoryReadRequest, HistoryReadResponse, ReadRequest, ReadResponse, WriteRequest, WriteResponse } from "opcua-webapi";

export interface UaWebClientApi
{
    browse(request: BrowseRequest, initOverrides?: RequestInit): Promise<BrowseResponse>;

    browseNext(request: BrowseNextRequest, initOverrides?: RequestInit): Promise<BrowseNextResponse>;

    call(request: CallRequest, initOverrides?: RequestInit): Promise<CallResponse>;

    findServers(request: FindServersRequest, initOverrides?: RequestInit): Promise<FindServersResponse>;

    getEndpoints(request: GetEndpointsRequest, initOverrides?: RequestInit): Promise<GetEndpointsResponse>;

    historyRead(request: HistoryReadRequest, initOverrides?: RequestInit): Promise<HistoryReadResponse>;
 
    read(request: ReadRequest, initOverrides?: RequestInit): Promise<ReadResponse>;
        
    write(request: WriteRequest, initOverrides?: RequestInit): Promise<WriteResponse>;
}