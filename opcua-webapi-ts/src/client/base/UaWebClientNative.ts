import { AddNodesRequest, AddNodesResponse, AddReferencesRequest, AddReferencesResponse, Configuration, DefaultApi, DeleteNodesRequest, DeleteNodesResponse, DeleteReferencesRequest, DeleteReferencesResponse } from "opcua-webapi";
import { UaWebClientApi } from "./UaWebClientApi";
import { BrowseNextRequest, BrowseNextResponse, BrowseRequest, BrowseResponse, CallRequest, CallResponse, FindServersRequest, FindServersResponse, GetEndpointsRequest, GetEndpointsResponse, HistoryReadRequest, HistoryReadResponse, ReadRequest, ReadResponse, WriteRequest, WriteResponse } from "opcua-webapi";

export class UaWebClientNative implements UaWebClientApi
{
    private api : DefaultApi;

    constructor(configure: Configuration)
    {
        this.api = new DefaultApi(configure);
    }

    browse(request: BrowseRequest, initOverrides?: RequestInit): Promise<BrowseResponse>
    {
        return this.api.browse({ browseRequest:request}, initOverrides);
    }

    browseNext(request: BrowseNextRequest, initOverrides?: RequestInit): Promise<BrowseNextResponse>
    {
        return this.api.browseNext({ browseNextRequest:request}, initOverrides);
    }

    read(request: ReadRequest, initOverrides?: RequestInit): Promise<ReadResponse>
    {
        return this.api.read({ readRequest:request}, initOverrides);
    }

    write(request: WriteRequest, initOverrides?: RequestInit): Promise<WriteResponse>
    {
        return this.api.write({ writeRequest:request}, initOverrides);
    }

    call(request: CallRequest, initOverrides?: RequestInit): Promise<CallResponse>
    {
        return this.api.call({ callRequest:request}, initOverrides);
    }

    historyRead(request: HistoryReadRequest, initOverrides?: RequestInit): Promise<HistoryReadResponse>
    {
        return this.api.historyRead({ historyReadRequest:request}, initOverrides);
    }

    findServers(request: FindServersRequest, initOverrides?: RequestInit): Promise<FindServersResponse>
    {
        return this.api.findServers({ findServersRequest:request}, initOverrides);
    }

    getEndpoints(request: GetEndpointsRequest, initOverrides?: RequestInit): Promise<GetEndpointsResponse>
    {
        return this.api.getEndpoints({ getEndpointsRequest:request}, initOverrides);
    }

    addNodes(request: AddNodesRequest, initOverrides?: RequestInit): Promise<AddNodesResponse>
    {
        return this.api.addNodes({ addNodesRequest:request}, initOverrides);
    }

    deleteNodes(request: DeleteNodesRequest, initOverrides?: RequestInit): Promise<DeleteNodesResponse>
    {
        return this.api.deleteNodes({ deleteNodesRequest:request}, initOverrides);
    }

    addReferences(request: AddReferencesRequest, initOverrides?: RequestInit): Promise<AddReferencesResponse>
    {        
        return this.api.addReferences({ addReferencesRequest:request}, initOverrides);
    }

    deleteReferences(request: DeleteReferencesRequest, initOverrides?: RequestInit): Promise<DeleteReferencesResponse>
    {
        return this.api.deleteReferences({ deleteReferencesRequest:request}, initOverrides);
    }
}