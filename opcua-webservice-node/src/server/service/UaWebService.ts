import { DataValue, EndpointDescription } from "opcua-webapi";
import { GetEndpointContext, ReadContext, UaServerConfigure } from "../types";

export interface UaWebService {
    getServerConfigure() : UaServerConfigure;
    
    getEndpoints(context : GetEndpointContext) : Promise<Array<EndpointDescription>>;

    read(context : ReadContext) : Promise<Array<DataValue>>;
}