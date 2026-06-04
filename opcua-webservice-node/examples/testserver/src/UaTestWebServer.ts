import { UaLocalizedText } from "opcua-webapi-ts";
import { UaExpressServer, UaServerConfigure, UaWebServer} from "opcua-webservice-node";
import { DigitalTwinSpaceTest } from "./testdigitaltwin";

export class UaTestWebServer extends UaWebServer 
{
    constructor(server : UaExpressServer)
    {
        super(server);
    }

    async onStartUp() : Promise<void>
    {
        let configure = new UaServerConfigure();
        configure.applicationUri = "test";
        configure.applicationName = new UaLocalizedText("test");
        configure.productUri = "test";

        this.serverConfigure = configure;
        this.addNodeManager(new DigitalTwinSpaceTest());
    }

    static async launch() : Promise<void>
    {
        console.log("Launch test");
        let express = new UaExpressServer(4842);
        let server = new UaTestWebServer(express);
        await server.start();
    }
}