import { ApplicationDescription, EndpointDescription } from "opcua-webapi";
import { AddReferencesContext, BrowseContext, BrowseNextContext, CallContext, DeleteReferencesContext, FindServerContext, GetEndpointContext, HistoryReadContext, ReadContext, WriteContext } from "./contexts";
import { UaAddNodesResult, UaBrowseResult, UaCallMethodResult, UaDataValue, UaHistoryReadResult, UaStatusCode } from "opcua-webapi-ts";
import { AddNodesContext,DeleteNodesContext } from "./contexts";
import { UaServerConfigure } from "../..";

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
    
    addNodes(context : AddNodesContext) : Promise<Array<UaAddNodesResult>>;

    deleteNodes(context : DeleteNodesContext) : Promise<Array<UaStatusCode>>;

    addReferences(context : AddReferencesContext) : Promise<Array<UaStatusCode>>;

    deleteReferences(context : DeleteReferencesContext) : Promise<Array<UaStatusCode>>;
}