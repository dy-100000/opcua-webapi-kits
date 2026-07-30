import { StatusCodes } from "opcua-webapi";
import { UaStatusCode } from "opcua-webapi-ts";

export class WriteObjectAttributeResponse {
    private readonly _writeDisplayNameStatusCode: UaStatusCode;
    private readonly _writeDescriptionStatusCode: UaStatusCode;

    constructor(writeDisplayNameStatusCode: UaStatusCode, writeDescriptionStatusCode: UaStatusCode)
    {
        this._writeDisplayNameStatusCode = writeDisplayNameStatusCode;
        this._writeDescriptionStatusCode = writeDescriptionStatusCode;
    }

    get writeDisplayNameStatusCode(): UaStatusCode {
        return this._writeDisplayNameStatusCode;
    }

    get writeDescriptionStatusCode(): UaStatusCode {
        return this._writeDescriptionStatusCode;
    }
}