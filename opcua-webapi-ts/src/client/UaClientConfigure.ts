import { Configuration } from "opcua-webapi";

export class UaClientConfiguration {
    private _apiConfig : Configuration
    private _defaultTimeout : number;

    constructor(apiConfig : Configuration) {        
        this._apiConfig = apiConfig;      
        this._defaultTimeout = 0;
    }

    get apiConfig() : Configuration
    {
        return this._apiConfig;
    }

    get defaultTimeout(): number
    {
        return this._defaultTimeout;
    }

    set defaultTimeout(timeout: number)
    {
        if (timeout < 1000)
        {
            this._defaultTimeout = 0;
        } else {
            this._defaultTimeout = timeout;
        }
    } 
}