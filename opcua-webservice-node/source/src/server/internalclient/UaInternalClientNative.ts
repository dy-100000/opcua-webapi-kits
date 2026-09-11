import { AddNodesRequest, AddNodesResponse, AddReferencesRequest, AddReferencesResponse, Configuration, DefaultApi, DeleteNodesRequest, DeleteNodesResponse, DeleteReferencesRequest, DeleteReferencesResponse } from "opcua-webapi";
import { BrowseNextRequest, BrowseNextResponse, BrowseRequest, BrowseResponse, CallRequest, CallResponse, FindServersRequest, FindServersResponse, GetEndpointsRequest, GetEndpointsResponse, HistoryReadRequest, HistoryReadResponse, ReadRequest, ReadResponse, WriteRequest, WriteResponse } from "opcua-webapi";
import { UaWebClientApi } from "opcua-webapi-ts";
import { uaServerApi } from "../..";

export class UaInternalClientNative implements UaWebClientApi
{
    private _path: string | undefined;
    
    constructor(path: string | undefined)
    {        
        this._path = path;
    }

    async browse(request: BrowseRequest, initOverrides?: RequestInit): Promise<BrowseResponse>
    {
        return await uaServerApi.browse(request, this._path);
    }

    async browseNext(request: BrowseNextRequest, initOverrides?: RequestInit): Promise<BrowseNextResponse>
    {
        return await uaServerApi.browseNext(request, this._path);
    }

    async read(request: ReadRequest, initOverrides?: RequestInit): Promise<ReadResponse>
    {
        return await uaServerApi.read(request, this._path);
    }

    async write(request: WriteRequest, initOverrides?: RequestInit): Promise<WriteResponse>
    {
        return await uaServerApi.write(request, this._path);
    }

    async call(request: CallRequest, initOverrides?: RequestInit): Promise<CallResponse>
    {
        return await uaServerApi.call(request, this._path);
    }

    async historyRead(request: HistoryReadRequest, initOverrides?: RequestInit): Promise<HistoryReadResponse>
    {
        return await uaServerApi.historyRead(request, this._path);
    }

    async findServers(request: FindServersRequest, initOverrides?: RequestInit): Promise<FindServersResponse>
    {
        return await uaServerApi.findServers(request);
    }

    async getEndpoints(request: GetEndpointsRequest, initOverrides?: RequestInit): Promise<GetEndpointsResponse>
    {
        return await uaServerApi.getEndpoints(request, this._path);
    }

    async addNodes(request: AddNodesRequest, initOverrides?: RequestInit): Promise<AddNodesResponse>
    {
        return await uaServerApi.addNodes(request, this._path);
    }

    async deleteNodes(request: DeleteNodesRequest, initOverrides?: RequestInit): Promise<DeleteNodesResponse>
    {
        return await uaServerApi.deleteNodes(request, this._path);
    }

    async addReferences(request: AddReferencesRequest, initOverrides?: RequestInit): Promise<AddReferencesResponse>
    {        
        return await uaServerApi.addReferences(request, this._path);
    }

    async deleteReferences(request: DeleteReferencesRequest, initOverrides?: RequestInit): Promise<DeleteReferencesResponse>
    {
        return await uaServerApi.deleteReferences(request, this._path);
    }
}