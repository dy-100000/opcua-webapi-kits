import { Configuration } from "opcua-webapi";
import { UaClientConfiguration, UaWebClient } from "opcua-webapi-ts";
import { UaInternalClientNative } from "./UaInternalClientNative";

export class UaInternalClient extends UaWebClient
{
    constructor(path?: string)
    {
        super(new UaClientConfiguration(new Configuration()));
        this.api = new UaInternalClientNative(path);
    }
}