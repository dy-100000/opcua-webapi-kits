import { UaStatusCode } from "opcua-webapi-ts";
import { UaChildId } from "../../../types/common/UaChildId";

export class WritePropertyListValuesResponse {
    private readonly _results: Map<string, UaStatusCode>;

    constructor() {
        this._results = new Map();
    }

    setWriteValueResult(propertyId: string | UaChildId, code: UaStatusCode): void {
        this._results.set(this.toKey(propertyId), code);
    }

    get results(): Map<string, UaStatusCode> {
        return this._results;
    }

    private toKey(propertyId: string | UaChildId): string {
        if (typeof propertyId === "string") {
            return propertyId;
        }

        return propertyId.toString();
    }
}