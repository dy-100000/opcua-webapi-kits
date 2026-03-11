import { ApplicationDescription, BrowsePathResult, BrowseResult, CallMethodResult, DataValue, EndpointDescription, HistoryReadResult, HistoryUpdateResult, MessageSecurityMode, StatusCode, StatusCodes } from "opcua-webapi";
import { UaWebService , UaServerConfigure, HistoryReadContext, HistoryUpdateContext} from "./server";
import { UaExpressServer } from "./UaExpressServer";
import { makeUaStatusCode, UaError, UaPayloadMapper } from "opcua-webclient-ts";
import { GetEndpointContext, BrowseContext, BrowseNextContext, CallContext, FindServerContext, ReadContext, TranslateContext, uaServerApi, WriteContext } from "./server";

export class UaWebServerBase implements UaWebService {

    private _server : UaExpressServer;
    private _configure : UaServerConfigure;

    constructor(server : UaExpressServer)
    {
        this._server = server;
        this._configure = new UaServerConfigure;   
        uaServerApi.registrateWebService(this);
    }

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

    async onStartUp() : Promise<void> {}

    async onShutDown() : Promise<void> {}

    getServerConfigure() : UaServerConfigure
    {
        return this._configure;
    }
    
    async getEndpoints(context : GetEndpointContext) : Promise<Array<EndpointDescription>>
    {
        return [ this._endpointDescription() ];
    }

    findServers(context : FindServerContext) : Promise<Array<ApplicationDescription>>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    browse(context : BrowseContext) : Promise<Array<BrowseResult>>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    browseNext(context : BrowseNextContext) : Promise<Array<BrowseResult>>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    translate(context : TranslateContext) : Promise<Array<BrowsePathResult>>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    read(context : ReadContext) : Promise<Array<DataValue>>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    write(context : WriteContext) : Promise<Array<StatusCode>>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    call(context : CallContext) : Promise<Array<CallMethodResult>>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    historyRead(context : HistoryReadContext) : Promise<Array<HistoryReadResult>>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    historyUpdate(context : HistoryUpdateContext) : Promise<Array<HistoryUpdateResult>>
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