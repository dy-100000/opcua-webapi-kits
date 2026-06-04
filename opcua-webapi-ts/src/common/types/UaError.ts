import { UaStatusCode } from ".";

export class UaError extends Error {
    private readonly _statusCode : UaStatusCode;

    constructor(statusCode: UaStatusCode) {
        super();
        this._statusCode = statusCode;
        this.name = statusCode.name;
        this.message = statusCode.name;
    }

    get statusCode(): UaStatusCode
    {
        return this._statusCode;
    }

    static from(code : number) : UaError
    {
        return new UaError(new UaStatusCode(code));
    }
}