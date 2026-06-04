import { UaLocalizedText } from "opcua-webapi-ts";
import { UaExpressServer, UaServerConfigure, UaWebServer} from "opcua-webservice-node";
import { EmployeeTwinSpace } from "./models";

export class UaTestWebServer extends UaWebServer 
{
    constructor(server : UaExpressServer)
    {
        super(server);
    }

    async onStartUp() : Promise<void>
    {
        let configure = new UaServerConfigure();
        configure.applicationUri = "demo";
        configure.applicationName = new UaLocalizedText("demo");
        configure.productUri = "demo";

        this.serverConfigure = configure;
        this.addNodeManager(new EmployeeTwinSpace());
    }

    static async launch() : Promise<void>
    {
        console.log("Launch demo");
        let express = new UaExpressServer(4840);
        let server = new UaTestWebServer(express);
        await server.start();
    }
}