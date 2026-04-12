import { StatusCodes } from "opcua-webapi";
import {
    makeUaStatusCode,
    UaExtensionObject,
    UaHistoryReadResult,
    UaHistoryReadValueId,
    UaStatusCode,
} from "opcua-webapi-ts";
import { HistoryReadContext } from "../../..";
import { UaTransaction } from ".";

export class UaHistoryReadTransaction extends UaTransaction<UaHistoryReadValueId, UaHistoryReadResult> {
    protected _statusCode: UaStatusCode;
    protected _continuationPoint: string | undefined;
    protected _historyData: UaExtensionObject | undefined;
    protected readonly _details: UaExtensionObject;

    constructor(historyReadContext: HistoryReadContext, handleId: number) {
        super(historyReadContext, handleId);
        this._statusCode = makeUaStatusCode(StatusCodes.Good);
        this._continuationPoint = undefined;
        this._historyData = undefined;
        this._details = historyReadContext.historyReadDetails;
    }

    get details(): UaExtensionObject {
        return this._details;
    }

    getItem(): UaHistoryReadValueId {
        return (this.serviceContext as HistoryReadContext).nodesToRead[this.handleId];
    }

    getResult(): UaHistoryReadResult {
        return new UaHistoryReadResult(
            this._statusCode,
            this._historyData,
            this._continuationPoint,
        );
    }

    setStatusCode(statusCode: UaStatusCode): void {
        this._statusCode = statusCode;
    }

    async execute(): Promise<void> {
        this._statusCode = makeUaStatusCode(StatusCodes.BadNotReadable);
    }
}