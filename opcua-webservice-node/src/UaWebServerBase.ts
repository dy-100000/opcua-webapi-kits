import { ApplicationDescription, DataValue, EndpointDescription, MessageSecurityMode, StatusCodes } from "opcua-webapi";
import { UaWebService } from "./server/service/UaWebService";
import { GetEndpointContext } from "./server/types/contexts/GetEndpointContext";
import { UaServerConfigure } from "./server/types/common/UaServerConfigure";
import { UaExpressServer } from "./UaExpressServer";
import { makeUaStatusCode, UaError, UaPayloadMapper } from "opcua-webclient-ts";
import { ReadContext, uaServerApi } from "./server";

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

    async read(context : ReadContext) : Promise<Array<DataValue>>
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