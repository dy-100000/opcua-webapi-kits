import { ApplicationDescription, BrowseNextRequestFromJSON, BrowsePathResult, BrowseRequestFromJSON, BrowseResponse, BrowseResult, CallMethodResult, CallRequestFromJSON, CallResponse, DataValue, EndpointDescription, FindServersRequestFromJSON, FindServersResponse, GetEndpointsRequestFromJSON, GetEndpointsResponse, GetEndpointsResponseToJSONTyped, HistoryReadRequestFromJSON, HistoryReadResponse, HistoryReadResult, ReadRequestFromJSON, ReadResponse, ResponseHeader, StatusCode, StatusCodeFromJSON, StatusCodes, TranslateBrowsePathsToNodeIdsRequestFromJSON, TranslateBrowsePathsToNodeIdsResponse, WriteRequestFromJSON, WriteResponse } from "opcua-webapi";
import { GetEndpointContext } from "../types/contexts/GetEndpointContext";
import { UaWebService } from "./UaWebService";
import { makeUaStatusCode, UaError } from "opcua-webapi-ts";
import { BrowseContext, CallContext, FindServerContext, HistoryReadContext, ReadContext, TranslateContext, WriteContext } from "../types";

export class UaWebServerApi {
    private _service : UaWebService | null;

    constructor()
    {
        this._service = null;
    }

    registrateWebService(service : UaWebService)
    {
        this._service = service;
    }

    async getEndpoints(request: any) : Promise<any>
    {
        let statusCode = StatusCodes.Good;
        let endpoints : Array<EndpointDescription> | undefined = undefined;

        try
        {            
            if (null == this._service) throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
            let getEndpointsRequest = GetEndpointsRequestFromJSON(request); 
            let context = new GetEndpointContext (getEndpointsRequest);            
            endpoints = await this._service.getEndpoints(context);
        } catch (err) {
            if (err instanceof UaError) 
            {
                statusCode = (err as UaError).statusCode.value;
            } else {
                statusCode = StatusCodes.BadUnexpectedError;
            }
        }

        let responseHeader = this._getResponseHeader(statusCode);
        let response : GetEndpointsResponse = {
            ResponseHeader: responseHeader,
            Endpoints: endpoints
        };

        return response;
    }

    async findServers(request: any) : Promise<any>
    {
        let statusCode = StatusCodes.Good;
        let applicationDescriptions : Array<ApplicationDescription> | undefined = undefined;

        try
        {            
            if (null == this._service) throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
            let findServersRequest = FindServersRequestFromJSON(request); 
            let context = new FindServerContext(findServersRequest);            
            applicationDescriptions = await this._service.findServers(context);
        } catch (err) {
            if (err instanceof UaError) 
            {
                statusCode = (err as UaError).statusCode.value;
            } else {
                statusCode = StatusCodes.BadUnexpectedError;
            }
        }

        let responseHeader = this._getResponseHeader(statusCode);
        let response : FindServersResponse = {
            ResponseHeader: responseHeader,
            Servers: applicationDescriptions
        };

        return response;
    }

    async browse(request: any, path?: string) : Promise<any>
    {
        let statusCode = StatusCodes.Good;
        let results : Array<BrowseResult> | undefined = undefined;

        try
        {            
            if (null == this._service) throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
            let browseRequest = BrowseRequestFromJSON(request);  
            let context = new BrowseContext(browseRequest,path);
            results = await this._service.browse(context);
        } catch (err) {            
            if (err instanceof UaError) 
            {
                statusCode = (err as UaError).statusCode.value;
            } else {
                statusCode = StatusCodes.BadUnexpectedError;
            }
        }

        let responseHeader = this._getResponseHeader(statusCode);
        let response : BrowseResponse = {
            ResponseHeader: responseHeader,
            Results: results
        };

        return response;
    }

    async browseNext(request: any, path?: string) : Promise<any>
    {
        let statusCode = StatusCodes.Good;
        let results : Array<BrowseResult> | undefined = undefined;

        try
        {            
            if (null == this._service) throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
            let browseRequest = BrowseNextRequestFromJSON(request);  
            let context = new BrowseContext(browseRequest,path);
            results = await this._service.browse(context);
        } catch (err) {            
            if (err instanceof UaError) 
            {
                statusCode = (err as UaError).statusCode.value;
            } else {
                statusCode = StatusCodes.BadUnexpectedError;
            }
        }

        let responseHeader = this._getResponseHeader(statusCode);
        let response : BrowseResponse = {
            ResponseHeader: responseHeader,
            Results: results
        };

        return response;
    }

    async translate(request: any, path?: string) : Promise<any>
    {
        let statusCode = StatusCodes.Good;
        let results : Array<BrowsePathResult> | undefined = undefined;

        try
        {            
            if (null == this._service) throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
            let translateRequest = TranslateBrowsePathsToNodeIdsRequestFromJSON(request);  
            let context = new TranslateContext(translateRequest,path);
            results = await this._service.translate(context);
        } catch (err) {            
            if (err instanceof UaError) 
            {
                statusCode = (err as UaError).statusCode.value;
            } else {
                statusCode = StatusCodes.BadUnexpectedError;
            }
        }

        let responseHeader = this._getResponseHeader(statusCode);
        let response : TranslateBrowsePathsToNodeIdsResponse = {
            ResponseHeader: responseHeader,
            Results: results
        };

        return response;
    }

    async read(request: any, path?: string) : Promise<any>
    {
        let statusCode = StatusCodes.Good;
        let results : Array<DataValue> | undefined = undefined;

        try
        {            
            if (null == this._service) throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
            let readRequest = ReadRequestFromJSON(request);  
            let context = new ReadContext (readRequest,path);
            results = await this._service.read(context);
        } catch (err) {            
            if (err instanceof UaError) 
            {
                statusCode = (err as UaError).statusCode.value;
            } else {
                statusCode = StatusCodes.BadUnexpectedError;
            }
        }

        let responseHeader = this._getResponseHeader(statusCode);
        let response : ReadResponse = {
            ResponseHeader: responseHeader,
            Results: results
        };

        return response;
    }

    async write(request: any, path?: string) : Promise<any>
    {
        let statusCode = StatusCodes.Good;
        let results : Array<StatusCode> | undefined = undefined;

        try
        {            
            if (null == this._service) throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
            let writeRequest = WriteRequestFromJSON(request);  
            let context = new WriteContext(writeRequest,path);
            results = await this._service.write(context);
        } catch (err) {            
            if (err instanceof UaError) 
            {
                statusCode = (err as UaError).statusCode.value;
            } else {
                statusCode = StatusCodes.BadUnexpectedError;
            }
        }

        let responseHeader = this._getResponseHeader(statusCode);
        let response : WriteResponse = {
            ResponseHeader: responseHeader,
            Results: results
        };

        return response;
    }

    async call(request: any, path?: string) : Promise<any>
    {
        let statusCode = StatusCodes.Good;
        let results : Array<CallMethodResult> | undefined = undefined;

        try
        {            
            if (null == this._service) throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
            let callRequest = CallRequestFromJSON(request);  
            let context = new CallContext(callRequest,path);
            results = await this._service.call(context);
        } catch (err) {            
            if (err instanceof UaError) 
            {
                statusCode = (err as UaError).statusCode.value;
            } else {
                statusCode = StatusCodes.BadUnexpectedError;
            }
        }

        let responseHeader = this._getResponseHeader(statusCode);
        let response : CallResponse = {
            ResponseHeader: responseHeader,
            Results: results
        };

        return response;
    }

    async historyRead(request: any, path?: string) : Promise<any>
    {
        let statusCode = StatusCodes.Good;
        let results : Array<HistoryReadResult> | undefined = undefined;

        try
        {            
            if (null == this._service) throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
            let historyReadRequest = HistoryReadRequestFromJSON(request);  
            let context = new HistoryReadContext (historyReadRequest,path);
            results = await this._service.historyRead(context);
        } catch (err) {            
            if (err instanceof UaError) 
            {
                statusCode = (err as UaError).statusCode.value;
            } else {
                statusCode = StatusCodes.BadUnexpectedError;
            }
        }

        let responseHeader = this._getResponseHeader(statusCode);
        let response : HistoryReadResponse = {
            ResponseHeader: responseHeader,
            Results: results
        };

        return response;
    }

    private _getResponseHeader(code : number) : ResponseHeader
    {
        return {
            ServiceResult: { Code: code },
            Timestamp: new Date
        };
    } 
}

export let uaServerApi = new UaWebServerApi();