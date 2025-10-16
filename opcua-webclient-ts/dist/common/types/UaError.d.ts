import { UaStatusCode } from ".";
export declare class UaError extends Error {
    private readonly _statusCode;
    constructor(statusCode: UaStatusCode);
    get statusCode(): UaStatusCode;
}
