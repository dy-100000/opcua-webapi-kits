import { UaStatusCode } from "opcua-webapi-ts";
import { UaChildId } from "../../../types/common/UaChildId";

export class WritePropertyValuesResponse {
    private readonly _results: Map<string, UaStatusCode>;

    constructor() {
        this._results = new Map();
    }

    setWriteValueResult(propertyName: string | UaChildId, code: UaStatusCode): void {
        this._results.set(this.toKey(propertyName), code);
    }

    get results(): Map<string, UaStatusCode> {
        return this._results;
    }

    private toKey(propertyName: string | UaChildId): string {
        if (typeof propertyName === "string") {
            return propertyName;
        }

        return propertyName.toString();
    }
}