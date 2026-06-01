import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaDataValue, UaStatusCode, UaVariant } from "opcua-webapi-ts";
import { UaChildId } from "../../../types/common/UaChildId";

export class ReadPropertyValuesResponse {
    private readonly _results: Map<string, UaDataValue>;

    constructor(results: Map<string, UaDataValue> = new Map()) {
        this._results = results;
    }

    setValue(elementName: string | UaChildId, value: UaVariant): void {
        this._results.set(
            this.toKey(elementName),
            new UaDataValue(value, makeUaStatusCode(StatusCodes.Good)),
        );
    }

    setError(elementName: string | UaChildId, errorCode: UaStatusCode): void {
        this._results.set(
            this.toKey(elementName),
            new UaDataValue(UaVariant.null(), errorCode),
        );
    }

    get results(): Map<string, UaDataValue> {
        return this._results;
    }

    private toKey(elementName: string | UaChildId): string {
        if (typeof elementName === "string") {
            return elementName;
        }

        return elementName.toString();
    }
}