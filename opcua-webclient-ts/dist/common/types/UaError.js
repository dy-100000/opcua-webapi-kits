"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UaError = void 0;
class UaError extends Error {
    constructor(statusCode) {
        super();
        this._statusCode = statusCode;
        this.name = statusCode.name;
        this.message = statusCode.name;
    }
    get statusCode() {
        return this._statusCode;
    }
}
exports.UaError = UaError;
