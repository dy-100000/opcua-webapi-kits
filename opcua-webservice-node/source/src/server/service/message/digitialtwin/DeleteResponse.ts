import { StatusCodes } from "opcua-webapi";
import { UaStatusCode } from "opcua-webapi-ts";

export class DeleteResponse {
    private readonly _statusCode: UaStatusCode;

    constructor(statusCode?: UaStatusCode)
    {
        this._statusCode = statusCode || UaStatusCode.from(StatusCodes.Good);
    }

    get statusCode(): UaStatusCode {
        return this._statusCode;
    }
}