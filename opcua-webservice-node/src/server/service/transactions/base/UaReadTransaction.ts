import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaDataValue, UaError, UaReadValueId, UaVariant } from "opcua-webapi-ts";
import { ReadContext } from "../../..";
import { UaTransaction2 } from ".";

export class UaReadTransaction extends UaTransaction2<UaReadValueId, UaDataValue> {
    protected readonly _timestampsToReturn: number;
    protected _results: Array<UaDataValue>;

    constructor(readContext: ReadContext, handleIds: Array<number>) {
        super(readContext, handleIds);
        this._timestampsToReturn = readContext.timestampsToReturn;
        this._results = [];
    }

    get timestampsToReturn(): number {
        return this._timestampsToReturn;
    }

    getItems(): Array<UaReadValueId> {
        return (this.serviceContext as ReadContext).nodesToRead;
    }

    getResults(): Array<UaDataValue> {
        return this._results;
    }

    async execute(): Promise<void> {
        for (const _handleId of this.handleIds) {
            this._results.push(
                new UaDataValue(
                    UaVariant.null(),
                    makeUaStatusCode(StatusCodes.BadNodeIdUnknown),
                ),
            );
        }
    }

    protected buildErrorResults(error: unknown): void {
        let statusCode = makeUaStatusCode(StatusCodes.BadUnexpectedError);
        if (error instanceof UaError) {
            statusCode = error.statusCode;
        } 

        this._results = [];        
        for (const _item of this.getRequestedItems()) {
            this._results.push(new UaDataValue(UaVariant.null(), statusCode));
        }
    }
}