import { StatusCodes } from "opcua-webapi";
import {
    makeUaStatusCode,
    UaAddNodesItem,
    UaAddNodesResult,
    UaNodeId,
    UaStatusCode,
} from "opcua-webapi-ts";
import { AddNodesContext } from "../../..";
import { UaTransaction } from ".";

export class UaAddNodeTransaction extends UaTransaction<UaAddNodesItem, UaAddNodesResult> {
    protected _statusCode: UaStatusCode;
    protected _addedNodeId: UaNodeId | undefined;

    constructor(addNodesContext: AddNodesContext, handleId: number) {
        super(addNodesContext, handleId);
        this._statusCode = makeUaStatusCode(StatusCodes.Good);
        this._addedNodeId = undefined;
    }
    
    getItem(): UaAddNodesItem {
        return (this.serviceContext as AddNodesContext).nodesToAdd[this.handleId];
    }

    getResult(): UaAddNodesResult {
        return new UaAddNodesResult(this._statusCode, this._addedNodeId ?? UaNodeId.nullNodeId);
    }

    setStatusCode(statusCode: UaStatusCode): void {
        this._statusCode = statusCode;
    }

    async execute(): Promise<void> {
        this._statusCode = makeUaStatusCode(StatusCodes.BadNotReadable);
    }
}