import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaCallMethodRequest, UaCallMethodResult, UaVariant } from "opcua-webapi-ts";
import { CallContext } from "../../..";
import { UaTransaction } from ".";

export class UaMethodCallTransaction extends UaTransaction<UaCallMethodRequest, UaCallMethodResult> {
    protected _statusCode = makeUaStatusCode(StatusCodes.Good);
    protected readonly _outputArguments: Array<UaVariant>;

    constructor(callContext: CallContext, index: number) {
        super(callContext, index);
        this._outputArguments = [];
    }

    getItem(): UaCallMethodRequest {
        return (this.serviceContext as CallContext).methodsToCall[this.handleId];
    }

    getResult(): UaCallMethodResult {
        return new UaCallMethodResult(this._statusCode, this._outputArguments, []);
    }

    async execute(): Promise<void> {
        this._statusCode = makeUaStatusCode(StatusCodes.BadNodeIdUnknown);
    }
}