import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode } from "opcua-webapi-ts";
import { WriteContext } from "../../..";
import { UaWriteTransaction } from "../base/UaWriteTransaction";

export class UaWriteNodeTransaction extends UaWriteTransaction {
    constructor(
        context: WriteContext,
        handleIds: Array<number>,
    ) {
        super(context, handleIds);
    }

    override async execute(): Promise<void> {
        this._results.length = 0;

        for (const _handleId of this.handleIds) {
            this._results.push(makeUaStatusCode(StatusCodes.BadNotWritable));
        }
    }
}