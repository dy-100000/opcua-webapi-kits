import { ApplicationDescription, DataValueToJSON, EndpointDescription, MessageSecurityMode, StatusCodes } from "opcua-webapi";
import { UaWebService , UaServerConfigure, HistoryReadContext } from "./server";
import { UaExpressServer } from "./UaExpressServer";
import { makeUaStatusCode, UaError, UaPayloadMapper } from "opcua-webapi-ts";
import { GetEndpointContext, BrowseContext, BrowseNextContext, CallContext, FindServerContext, ReadContext, uaServerApi, WriteContext } from "./server";
import { UaBrowseResult, UaCallMethodResult, UaDataValue, UaHistoryReadResult, UaStatusCode } from "opcua-webapi-ts";

export class UaWebServerBase implements UaWebService {

    private _server : UaExpressServer;
    private _configure : UaServerConfigure;

    constructor(server : UaExpressServer)
    {
        this._server = server;
        this._configure = new UaServerConfigure;   
        uaServerApi.registrateWebService(this);
    }

    // Can be override to customize the start up logic
    async onStartUp() : Promise<void> {}

    // Can be override to customize the shut down logic
    async onShutDown() : Promise<void> {}

    get serverConfigure() : UaServerConfigure
    {        
        return this._configure;
    }

    set serverConfigure(configure : UaServerConfigure)
    {
        this._configure = configure;
    }

    async start()
    {
        try
        {
            console.log('Starting OPC UA WebServer ...');
            await this.onStartUp();
            this._server.start();            
        } catch (err) {
            console.error('OPC UA WebServer failure', err);
        }
    }

    get expressServer() : UaExpressServer
    {
        return this._server;
    }

    getServerConfigure() : UaServerConfigure
    {
        return this._configure;
    }
    
    async getEndpoints(context : GetEndpointContext) : Promise<Array<EndpointDescription>>
    {
        return [ this._endpointDescription() ];
    }

    async findServers(context : FindServerContext) : Promise<Array<ApplicationDescription>>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    async browse(context : BrowseContext) : Promise<Array<UaBrowseResult>>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    async browseNext(context : BrowseNextContext) : Promise<Array<UaBrowseResult>>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    async read(context : ReadContext) : Promise<Array<UaDataValue>>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    async write(context : WriteContext) : Promise<Array<UaStatusCode>>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    async call(context : CallContext) : Promise<Array<UaCallMethodResult>>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    async historyRead(context : HistoryReadContext) : Promise<Array<UaHistoryReadResult>>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    private _endpointDescription() : EndpointDescription
    {
        let applicationDescription : ApplicationDescription = {
            ApplicationUri: this.serverConfigure.applicationUri,
            ProductUri: this.serverConfigure.productUri,
            ApplicationName: UaPayloadMapper.localizedTextToWebApi(this.serverConfigure.applicationName),
            ApplicationType: this.serverConfigure.applicationType
        };

        let ret : EndpointDescription = {
            EndpointUrl: "",
            Server: applicationDescription,
            ServerCertificate: "",
            SecurityMode: MessageSecurityMode.None,
            SecurityPolicyUri: "http://opcfoundation.org/UA/SecurityPolicy#None",
            SecurityLevel: 0
        };

        return ret;
    }
}