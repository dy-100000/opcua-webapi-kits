import { UaVariant } from "opcua-webapi-ts";

export class InvokeOperationResponse {
    private readonly _outputArguments: Array<UaVariant>;

    constructor(outputArguments: Array<UaVariant>) {
        this._outputArguments = outputArguments;
    }

    get outputArguments(): Array<UaVariant> {
        return this._outputArguments;
    }
}