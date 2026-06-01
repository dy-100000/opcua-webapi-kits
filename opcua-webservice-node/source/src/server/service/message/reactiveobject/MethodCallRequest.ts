import { UaVariant } from "opcua-webapi-ts";
import { UaObjectId } from "../../../types";

export class MethodCallRequest {
    private readonly _objectId: UaObjectId;
    private readonly _methodName: string;
    private readonly _inputArguments: Array<UaVariant>;

    constructor(
        objectId: UaObjectId,
        methodName: string,
        inputArguments: Array<UaVariant>,
    ) {
        this._objectId = objectId;
        this._methodName = methodName;
        this._inputArguments = inputArguments;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }

    get methodName(): string {
        return this._methodName;
    }

    get inputArguments(): Array<UaVariant> {
        return this._inputArguments;
    }
}