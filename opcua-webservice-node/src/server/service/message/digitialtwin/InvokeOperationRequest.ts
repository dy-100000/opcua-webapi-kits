import { UaVariant } from "opcua-webapi-ts";
import { ObjectServiceContext } from "../../../types/digitaltwin/ObjectServiceContext";

export class InvokeOperationRequest {
    private readonly _id: string;
    private readonly _operationName: string;
    private readonly _inputArguments: Array<UaVariant>;
    private readonly _context: ObjectServiceContext;

    constructor(
        context: ObjectServiceContext,
        operationName: string,
        inputArguments: Array<UaVariant>) {
        this._id = context.objectId.id;
        this._operationName = operationName;
        this._inputArguments = inputArguments;
        this._context = context;
    }

    get id(): string {
        return this._id;
    }

    get operationName(): string {
        return this._operationName;
    }

    get inputArguments(): Array<UaVariant> {
        return this._inputArguments;
    }

    get context(): ObjectServiceContext {
        return this._context;
    }
}