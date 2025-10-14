import { Configuration } from "opcua-webapi";

export class UaClientConfiguration {
    private _apiConfig : Configuration
    private _defaultTimeout : number;

    constructor(apiConfig : Configuration) {        
        this._apiConfig = apiConfig;      
        this._defaultTimeout = 60000;
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
        if (timeout < 5000)
        {
            this._defaultTimeout = 5000;
        } else {
            this._defaultTimeout = timeout;
        }
    } 
}