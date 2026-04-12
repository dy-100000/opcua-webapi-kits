import { ApplicationDescription, BrowseNextRequestFromJSON, BrowsePathResult, BrowseRequestFromJSON, BrowseResponse, BrowseResult, CallMethodResult, CallRequestFromJSON, CallResponse, DataValue, EndpointDescription, FindServersRequestFromJSON, FindServersResponse, GetEndpointsRequestFromJSON, GetEndpointsResponse, GetEndpointsResponseToJSONTyped, HistoryReadRequestFromJSON, HistoryReadResponse, HistoryReadResult, ReadRequestFromJSON, ReadResponse, ResponseHeader, StatusCode, StatusCodeFromJSON, StatusCodes, TranslateBrowsePathsToNodeIdsRequestFromJSON, TranslateBrowsePathsToNodeIdsResponse, WriteRequestFromJSON, WriteResponse } from "opcua-webapi";
import { GetEndpointContext } from "../types/contexts/GetEndpointContext";
import { UaWebService } from "./UaWebService";
import { makeUaStatusCode, UaBrowseDescription, UaCallMethodRequest, UaError, UaHistoryReadValueId, UaPayloadMapper, UaReadValueId, UaWriteValue } from "opcua-webapi-ts";
import { BrowseContext, BrowseNextContext, CallContext, FindServerContext, HistoryReadContext, ReadContext, WriteContext } from "../types";

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

    async getEndpoints(request: any, path?: string) : Promise<any>
    {
        let statusCode = StatusCodes.Good;
        let endpoints : Array<EndpointDescription> | undefined = undefined;

        try
        {            
            if (null == this._service) throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
            let getEndpointsRequest = GetEndpointsRequestFromJSON(request);            

            let context = new GetEndpointContext(
                getEndpointsRequest.EndpointUrl,
                getEndpointsRequest.LocaleIds,
                getEndpointsRequest.ProfileUris,
                path,
                getEndpointsRequest.RequestHeader);

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

            let context = new FindServerContext(
                findServersRequest.EndpointUrl,
                findServersRequest.LocaleIds,
                findServersRequest.ServerUris,
                findServersRequest.RequestHeader);      

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
            let serverConfigure = this._service.getServerConfigure();

            let browseRequest = BrowseRequestFromJSON(request);  

            if (!browseRequest.NodesToBrowse || browseRequest.NodesToBrowse.length == 0) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNothingToDo));
            }

            if (serverConfigure.browseRequestMaxSize > 0 && browseRequest.NodesToBrowse.length > serverConfigure.browseRequestMaxSize) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadTooManyOperations));
            }

            let browseDescriptions : Array<UaBrowseDescription> = [];
            for (let item of browseRequest.NodesToBrowse) {
                let browseDescription = UaBrowseDescription.fromStruct(item);
                if (null == browseDescription) throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));

                browseDescriptions.push(browseDescription);
            }

            let context = new BrowseContext(
                browseDescriptions,
                browseRequest.RequestedMaxReferencesPerNode,
                path,
                browseRequest.RequestHeader);

            let browseResults = await this._service.browse(context);
            results = [];

            for (let item of browseResults) {
                let browseResult = item.toStruct();
                results.push(browseResult);
            }

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
            let serverConfigure = this._service.getServerConfigure();

            let browseRequest = BrowseNextRequestFromJSON(request);  

            if (!browseRequest.ContinuationPoints || browseRequest.ContinuationPoints.length == 0) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNothingToDo));
            }

            if (serverConfigure.browseRequestMaxSize > 0 && browseRequest.ContinuationPoints.length > serverConfigure.browseRequestMaxSize) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadTooManyOperations));
            }

            let context = new BrowseNextContext(
                (browseRequest.ReleaseContinuationPoints) ? browseRequest.ReleaseContinuationPoints : false,
                browseRequest.ContinuationPoints,
                path,
                browseRequest.RequestHeader);

            let browseResults = await this._service.browseNext(context);
            results = [];

            for (let item of browseResults) {
                let browseResult = item.toStruct();
                results.push(browseResult);
            }
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

    async read(request: any, path?: string) : Promise<any>
    {
        let statusCode = StatusCodes.Good;
        let results : Array<DataValue> | undefined = undefined;

        try
        {            
            if (null == this._service) throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
            let serverConfigure = this._service.getServerConfigure();

            let readRequest = ReadRequestFromJSON(request);  

            if (!readRequest.NodesToRead || readRequest.NodesToRead.length == 0) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNothingToDo));
            }

            if (serverConfigure.readRequestMaxSize > 0 && readRequest.NodesToRead.length > serverConfigure.readRequestMaxSize) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadTooManyOperations));
            }

            let nodesToRead: Array<UaReadValueId> = [];
            for (let item of readRequest.NodesToRead) {
                let readValueId = UaReadValueId.fromStruct(item);
                if (null == readValueId) throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                nodesToRead.push(readValueId);
            }
            
            let context = new ReadContext(                
                nodesToRead,
                readRequest.TimestampsToReturn,
                readRequest.MaxAge,
                path,
                readRequest.RequestHeader);

            let dataValues = await this._service.read(context);
            results = [];

            for (let item of dataValues) {
                let dataValue = UaPayloadMapper.dataValueToWebApi(item);
                results.push(dataValue);
            }
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
            let serverConfigure = this._service.getServerConfigure();
            let writeRequest = WriteRequestFromJSON(request);  

            if (!writeRequest.NodesToWrite || writeRequest.NodesToWrite.length == 0) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNothingToDo));
            }

            if (serverConfigure.writeRequestMaxSize > 0 && writeRequest.NodesToWrite.length > serverConfigure.writeRequestMaxSize) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadTooManyOperations));
            }

            let nodesToWrite: Array<UaWriteValue> = [];
            if (writeRequest.NodesToWrite) {
                for (let item of writeRequest.NodesToWrite) {
                    let writeValue = UaWriteValue.fromStruct(item);
                    if (null == writeValue) throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    nodesToWrite.push(writeValue);
                }
            }

            let context = new WriteContext(
                nodesToWrite, 
                path, 
                writeRequest.RequestHeader);

            let statusCodes = await this._service.write(context);
            results = [];

            for (let item of statusCodes) {
                let statusCode = UaPayloadMapper.statusCodeToWebApi(item);                
                results.push(statusCode);
            }
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
            let serverConfigure = this._service.getServerConfigure();

            let callRequest = CallRequestFromJSON(request);

            if (!callRequest.MethodsToCall || callRequest.MethodsToCall.length == 0) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNothingToDo));
            }

            if (serverConfigure.callRequestMaxSize > 0 && callRequest.MethodsToCall.length > serverConfigure.callRequestMaxSize) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadTooManyOperations));
            }

            let methodsToCall: Array<UaCallMethodRequest> = [];
            for (let item of callRequest.MethodsToCall) {
                let methodToCall = UaCallMethodRequest.fromStruct(item);
                if (null == methodToCall) throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                methodsToCall.push(methodToCall);
            }

            let context = new CallContext(
                methodsToCall, 
                path, 
                callRequest.RequestHeader);

            let callMethodResults = await this._service.call(context);
            results = [];

            for (let item of callMethodResults) {
                let callMethodResult = item.toStruct();
                results.push(callMethodResult);
            }
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
            let serverConfigure = this._service.getServerConfigure();

            let historyReadRequest = HistoryReadRequestFromJSON(request); 

            if (!historyReadRequest.NodesToRead || 
                historyReadRequest.NodesToRead.length == 0 || 
                !historyReadRequest.HistoryReadDetails) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNothingToDo));
            }

            if (serverConfigure.historyReadRequestMaxSize > 0 && historyReadRequest.NodesToRead.length > serverConfigure.historyReadRequestMaxSize) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadTooManyOperations));
            }
 
            let nodesToRead: Array<UaHistoryReadValueId> = [];
            for (let item of historyReadRequest.NodesToRead) {
                let nodeToRead = UaHistoryReadValueId.fromStruct(item);
                if (null == nodeToRead) throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                nodesToRead.push(nodeToRead);
            }                       

            let historyReadDetails = UaPayloadMapper.extensionObjectFromWebApi(historyReadRequest.HistoryReadDetails);

            let context = new HistoryReadContext(                
                nodesToRead,
                historyReadDetails,
                historyReadRequest.TimestampsToReturn,
                historyReadRequest.ReleaseContinuationPoints,
                path,
                historyReadRequest.RequestHeader);
                
            let historyReadResults = await this._service.historyRead(context);
            results = [];

            for (let item of historyReadResults) {
                let historyReadResult = item.toStruct();
                results.push(historyReadResult);
            }
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