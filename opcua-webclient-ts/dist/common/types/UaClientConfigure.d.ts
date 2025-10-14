import { Configuration } from "opcua-webapi";
export declare class UaClientConfiguration {
    private readonly _apiConfig;
    private _defaultTimeout;
    constructor(apiConfig: Configuration);
    get apiConfig(): Configuration;
    get defaultTimeout(): number;
    set defaultTimeout(timeout: number);
}
