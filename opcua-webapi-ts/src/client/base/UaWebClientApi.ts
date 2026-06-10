import { AddNodesRequest, AddNodesResponse, AddReferencesRequest, AddReferencesResponse, BrowseNextRequest, BrowseNextResponse, BrowseRequest, BrowseResponse, CallRequest, CallResponse, DeleteNodesRequest, DeleteNodesResponse, DeleteReferencesRequest, DeleteReferencesResponse, FindServersRequest, FindServersResponse, GetEndpointsRequest, GetEndpointsResponse, HistoryReadRequest, HistoryReadResponse, ReadRequest, ReadResponse, WriteRequest, WriteResponse } from "opcua-webapi";

export interface UaWebClientApi
{
    browse(request: BrowseRequest, initOverrides?: RequestInit): Promise<BrowseResponse>;

    browseNext(request: BrowseNextRequest, initOverrides?: RequestInit): Promise<BrowseNextResponse>;    
 
    read(request: ReadRequest, initOverrides?: RequestInit): Promise<ReadResponse>;
        
    write(request: WriteRequest, initOverrides?: RequestInit): Promise<WriteResponse>;

    call(request: CallRequest, initOverrides?: RequestInit): Promise<CallResponse>;

    historyRead(request: HistoryReadRequest, initOverrides?: RequestInit): Promise<HistoryReadResponse>;

    findServers(request: FindServersRequest, initOverrides?: RequestInit): Promise<FindServersResponse>;

    getEndpoints(request: GetEndpointsRequest, initOverrides?: RequestInit): Promise<GetEndpointsResponse>;

    addNodes(request: AddNodesRequest, initOverrides?: RequestInit): Promise<AddNodesResponse>;

    deleteNodes(request: DeleteNodesRequest, initOverrides?: RequestInit): Promise<DeleteNodesResponse>;

    addReferences(request: AddReferencesRequest, initOverrides?: RequestInit): Promise<AddReferencesResponse>;

    deleteReferences(request: DeleteReferencesRequest, initOverrides?: RequestInit): Promise<DeleteReferencesResponse>;
}