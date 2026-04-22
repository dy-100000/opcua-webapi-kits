import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaDataValue, UaStatusCode, UaVariant } from "opcua-webapi-ts";
import { UaChildId } from "../../../types";

export class ReadVariableValueResponse {
    private readonly _results: Map<string, UaDataValue>;

    constructor() {
        this._results = new Map();
    }

    setValue(path: string | UaChildId, value: UaVariant): void {
        this._results.set(
            this.toKey(path),
            new UaDataValue(value, makeUaStatusCode(StatusCodes.Good)),
        );
    }

    setError(path: string | UaChildId, error: UaStatusCode): void {
        this._results.set(
            this.toKey(path),
            new UaDataValue(UaVariant.null(), error),
        );
    }

    get results(): Map<string, UaDataValue> {
        return this._results;
    }

    private toKey(path: string | UaChildId): string {
        if (typeof path === "string") {
            return path;
        }

        return path.serialize();
    }
}