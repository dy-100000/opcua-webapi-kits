import { EndpointDescription, GetEndpointsRequestFromJSON, GetEndpointsResponse, GetEndpointsResponseToJSONTyped, ResponseHeader, StatusCode, StatusCodeFromJSON, StatusCodes } from "opcua-webapi";
import { GetEndpointContext } from "../types/contexts/GetEndpointContext";
import { UaWebService } from "./UaWebService";
import { makeUaStatusCode, UaError } from "opcua-webclient-ts";

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

    async getEndpoint(request: any) : Promise<any>
    {
        let statusCode = StatusCodes.Good;
        let endpoints : Array<EndpointDescription> | undefined = undefined;

        try
        {            
            if (null == this._service) throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));

            let getEndpointsRequest = GetEndpointsRequestFromJSON(request);            

            let context = new GetEndpointContext ("",getEndpointsRequest);
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

    private _getResponseHeader(code : number) : ResponseHeader
    {
        return {
            ServiceResult: { Code: code },
            Timestamp: new Date
        };
    } 
}

export let uaServerApi = new UaWebServerApi();