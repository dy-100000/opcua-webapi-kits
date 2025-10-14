"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UaClientConfiguration = void 0;
class UaClientConfiguration {
    constructor(apiConfig) {
        this._apiConfig = apiConfig;
        this._defaultTimeout = 60000;
    }
    get apiConfig() {
        return this._apiConfig;
    }
    get defaultTimeout() {
        return this._defaultTimeout;
    }
    set defaultTimeout(timeout) {
        if (timeout < 5000) {
            this._defaultTimeout = 5000;
        }
        else {
            this._defaultTimeout = timeout;
        }
    }
}
exports.UaClientConfiguration = UaClientConfiguration;
