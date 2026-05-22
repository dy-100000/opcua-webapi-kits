import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaDataValue, UaStatusCode, UaVariant } from "opcua-webapi-ts";
import { UaChildId } from "../../../types/common/UaChildId";

export class ReadPropertyListValueResponse {
    private readonly _results: Map<string, UaDataValue>;

    constructor(results: Map<string, UaDataValue> = new Map()) {
        this._results = results;
    }

    setValue(propertyId: string | UaChildId, value: UaVariant): void {
        this._results.set(
            this.toKey(propertyId),
            new UaDataValue(value, makeUaStatusCode(StatusCodes.Good)),
        );
    }

    setError(propertyId: string | UaChildId, errorCode: UaStatusCode): void {
        this._results.set(
            this.toKey(propertyId),
            new UaDataValue(UaVariant.null(), errorCode),
        );
    }

    get results(): Map<string, UaDataValue> {
        return this._results;
    }

    private toKey(propertyId: string | UaChildId): string {
        if (typeof propertyId === "string") {
            return propertyId;
        }

        return propertyId.toString();
    }
}