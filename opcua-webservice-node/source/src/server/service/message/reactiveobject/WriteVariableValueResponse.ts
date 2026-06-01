import { UaStatusCode } from "opcua-webapi-ts";
import { UaChildId } from "../../../types";
import { WriteVariableValueRequest } from "./WriteVariableValueRequest";

export class WriteVariableValueResponse {
    private readonly _results: Map<string, UaStatusCode>;

    constructor(results: Map<string, UaStatusCode> = new Map()) {
        this._results = results;
    }

    setOperationResults(request: WriteVariableValueRequest, code: UaStatusCode): void {
        for (const childId of request.variableValues.keys()) {
            this._results.set(childId, code);
        }
    }

    setOperationResult(id: string | UaChildId, code: UaStatusCode): void {
        this._results.set(this.toKey(id), code);
    }

    get results(): Map<string, UaStatusCode> {
        return this._results;
    }

    private toKey(id: string | UaChildId): string {
        if (typeof id === "string") {
            return id;
        }

        return id.serialize();
    }
}