import { StatusCodes } from "opcua-webapi";
import {
    makeUaStatusCode,
    UaDeleteNodesItem,
    UaStatusCode,
} from "opcua-webapi-ts";
import { DeleteNodesContext } from "../../..";
import { UaTransaction } from ".";

export class UaDeleteNodeTransaction extends UaTransaction<UaDeleteNodesItem, UaStatusCode> {
    protected _statusCode: UaStatusCode;

    constructor(deleteNodesContext: DeleteNodesContext, handleId: number) {
        super(deleteNodesContext, handleId);
        this._statusCode = makeUaStatusCode(StatusCodes.Good);
    }
    
    getItem(): UaDeleteNodesItem {
        return (this.serviceContext as DeleteNodesContext).nodesToDelete[this.handleId];
    }

    getResult(): UaStatusCode {
        return this._statusCode;
    }

    setStatusCode(statusCode: UaStatusCode): void {
        this._statusCode = statusCode;
    }

    async execute(): Promise<void> {
        this._statusCode = makeUaStatusCode(StatusCodes.BadNotReadable);
    }
}