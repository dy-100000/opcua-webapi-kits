import { StatusCode } from "node-opcua-status-code";
export declare class UaError extends Error {
    private readonly _statusCode;
    constructor(statusCode: StatusCode);
    get statusCode(): StatusCode;
}
