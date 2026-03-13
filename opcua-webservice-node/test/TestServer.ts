import { UaDataValue, UaLocalizedText, UaPayloadMapper, UaVariant } from "opcua-webclient-ts";
import { UaExpressServer } from "../src/UaExpressServer";
import { UaWebServerBase } from "../src/UaWebServerBase";
import { UaServerConfigure } from "../src/server/types/common/UaServerConfigure";
import { ReadContext } from "../src/server";
import { DataValue } from "opcua-webapi";

export class TestServer extends UaWebServerBase
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
    }

    async onShutDown() : Promise<void>
    {}

    async read(context : ReadContext) : Promise<Array<DataValue>>
    {
        let result : Array<DataValue> = [];

        for (let item of context.nodesToRead)
        {
            let value = UaVariant.string((item.NodeId) ? item.NodeId : "null");            
            result.push(UaPayloadMapper.dataValueToWebApi(new UaDataValue(value)));
        }

        return result;
    }

    static async launch() : Promise<void>
    {
        console.log("Launch test");
        let express = new UaExpressServer(4840);
        let server = new TestServer(express);
        await server.start();
    }
}