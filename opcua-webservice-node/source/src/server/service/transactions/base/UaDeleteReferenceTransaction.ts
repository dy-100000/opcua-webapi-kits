import { StatusCodes } from "opcua-webapi";
import {
    makeUaStatusCode,
    UaDeleteReferencesItem,
    UaStatusCode,
} from "opcua-webapi-ts";
import { DeleteReferencesContext } from "../../..";
import { UaTransaction } from ".";

export class UaDeleteReferenceTransaction extends UaTransaction<UaDeleteReferencesItem, UaStatusCode> {
    protected _statusCode: UaStatusCode;

    constructor(deleteReferencesContext: DeleteReferencesContext, handleId: number) {
        super(deleteReferencesContext, handleId);
        this._statusCode = makeUaStatusCode(StatusCodes.Good);
    }
    
    getItem(): UaDeleteReferencesItem {
        return (this.serviceContext as DeleteReferencesContext).referencesToDelete[this.handleId];
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