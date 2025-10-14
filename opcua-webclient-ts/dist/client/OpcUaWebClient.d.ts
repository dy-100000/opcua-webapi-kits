import { DefaultApi, ViewDescription, BrowseDescription, BrowseResult, ReadValueId, WriteValue, StatusCode, CallMethodRequest, CallMethodResult, BrowsePath, BrowsePathResult, ApplicationDescription, EndpointDescription, DataValue as DataValueNative } from "opcua-webapi";
import { DataValue } from "node-opcua-data-value";
import { NodeId } from "node-opcua-nodeid";
import { UaBrowseResult, UaNodeAttributes, UaVariableAttributes } from "../common";
import { UaClientConfiguration, UaClientParameters } from "..";
export declare class OpcUaWebClient {
    private api;
    private clientConfig;
    private requestHandle;
    protected authenticationToken: string | undefined;
    constructor(clientConfig: UaClientConfiguration);
    getNativeApi(): DefaultApi;
    browseChild(nodeId: NodeId, nodeClass?: number, maxReferences?: number): Promise<UaBrowseResult>;
    browseNextChild(continuationPoint: string): Promise<UaBrowseResult>;
    readValues(nodeIds: Array<NodeId>, timestampsToReturn?: number): Promise<Array<DataValue>>;
    readNodeAttributes(nodeId: NodeId, returnDescription?: boolean): Promise<UaNodeAttributes>;
    readVariableAttributes(nodeId: NodeId): Promise<UaVariableAttributes>;
    browse(nodesToBrowse: Array<BrowseDescription>, maxReferencesPerNode?: number, view?: ViewDescription, additionalParameters?: UaClientParameters): Promise<Array<BrowseResult>>;
    browseNext(continuationPoints: Array<string>, releaseContinuationPoints: boolean, additionalParameters?: UaClientParameters): Promise<Array<BrowseResult>>;
    translate(browsePaths: Array<BrowsePath>, additionalParameters?: UaClientParameters): Promise<Array<BrowsePathResult>>;
    read(nodesToRead: Array<ReadValueId>, timestampsToReturn?: number, maxAge?: number, additionalParameters?: UaClientParameters): Promise<Array<DataValueNative>>;
    write(nodesToWrite: Array<WriteValue>, additionalParameters?: UaClientParameters): Promise<Array<StatusCode>>;
    call(methodsToCall: Array<CallMethodRequest>, additionalParameters?: UaClientParameters): Promise<Array<CallMethodResult>>;
    findServer(serverUris: Array<string>, endpointUrl: string, localeIds?: Array<string>, additionalParameters?: UaClientParameters): Promise<Array<ApplicationDescription>>;
    getEndpoints(endpointUrl?: string, localeIds?: Array<string>, profileUris?: Array<string>, additionalParameters?: UaClientParameters): Promise<Array<EndpointDescription>>;
    private requestHeader;
}
