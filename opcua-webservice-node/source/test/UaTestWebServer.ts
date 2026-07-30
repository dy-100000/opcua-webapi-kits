import path from "path";
import { UaLocalizedText } from "opcua-webapi-ts";
import { UaExpressServer, UaServerConfigure, UaWebServer} from "../src";
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
        const apiSpecDir = path.join(__dirname, "..", "src", "api");
        const express = new UaExpressServer(4842, apiSpecDir);
        const server = new UaTestWebServer(express);

        try {
            await server.start();
        } catch (error) {
            console.error("Test server startup failed", error);
            process.exit(1);
        }

        await new Promise<void>((resolve) => {
            const shutdown = () => {
                process.off("SIGINT", shutdown);
                process.off("SIGTERM", shutdown);
                resolve();
            };

            process.once("SIGINT", shutdown);
            process.once("SIGTERM", shutdown);
        });
    }
}