import { StatusCodes } from "opcua-webapi";
import {
    makeUaStatusCode,
    UaAddReferencesItem,
    UaStatusCode,
} from "opcua-webapi-ts";
import { AddReferencesContext } from "../../..";
import { UaTransaction } from ".";

export class UaAddReferenceTransaction extends UaTransaction<UaAddReferencesItem, UaStatusCode> {
    protected _statusCode: UaStatusCode;

    constructor(addReferencesContext: AddReferencesContext, handleId: number) {
        super(addReferencesContext, handleId);
        this._statusCode = makeUaStatusCode(StatusCodes.Good);
    }
    
    getItem(): UaAddReferencesItem {
        return (this.serviceContext as AddReferencesContext).referencesToAdd[this.handleId];
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