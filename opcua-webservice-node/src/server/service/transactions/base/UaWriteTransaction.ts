import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaStatusCode, UaWriteValue } from "opcua-webapi-ts";
import { WriteContext } from "../../..";
import { UaTransaction2 } from ".";

export class UaWriteTransaction extends UaTransaction2<UaWriteValue, UaStatusCode> {
    protected readonly _results: Array<UaStatusCode>;

    constructor(writeContext: WriteContext, handleIds: Array<number>) {
        super(writeContext, handleIds);
        this._results = [];
    }

    getItems(): Array<UaWriteValue> {
        return (this.serviceContext as WriteContext).nodesToWrite;
    }

    getResults(): Array<UaStatusCode> {
        return this._results;
    }

    async execute(): Promise<void> {
        for (const _handleId of this.handleIds) {
            this._results.push(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
        }
    }
}