import { CallMethodRequest, Variant } from "opcua-webapi";
import { DataTypeIds } from "../nodes";
import { parseUaNodeIdOrNull, UaNodeId, UaVariant } from "../types";
import { UaPayloadMapper } from ".";

export class UaCallMethodRequest
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.CallMethodRequest);

    private _objectId: UaNodeId;
    private _methodId: UaNodeId;
    private _inputArguments: Array<UaVariant>;

    constructor(
        objectId: UaNodeId,
        methodId: UaNodeId,
        inputArguments?: Array<UaVariant>)
    {
        this._objectId = objectId;
        this._methodId = methodId;
        this._inputArguments = inputArguments ? inputArguments : [];
    }

    get objectId() : UaNodeId
    {
        return this._objectId;
    }

    get methodId() : UaNodeId
    {
        return this._methodId;
    }

    get inputArguments() : Array<UaVariant>
    {
        return this._inputArguments;
    }

    toStruct() : CallMethodRequest
    {
        let InputArguments: Array<Variant> = [];
        for (let item of this._inputArguments)
        {
            InputArguments.push(UaPayloadMapper.variantToWebApi(item));
        }

        let callMethodRequest : CallMethodRequest = {
            ObjectId: this._objectId.toString(),
            MethodId: this._methodId.toString(),
            InputArguments: InputArguments
        };

        return callMethodRequest;
    }

    static fromStruct(callMethodRequest : CallMethodRequest) : UaCallMethodRequest | null
    {
        let objectId = parseUaNodeIdOrNull(callMethodRequest.ObjectId); 
        let methodId = parseUaNodeIdOrNull(callMethodRequest.MethodId);
        let inputArguments: Array<UaVariant> = [];
        
        if (null == objectId || null == methodId) return null;

        if (callMethodRequest.InputArguments) {
            for (let item of callMethodRequest.InputArguments) {
                let variant = UaPayloadMapper.variantFromWebApi(item);
                inputArguments.push(variant);
            }
        }

        return new UaCallMethodRequest(
            objectId, 
            methodId, 
            inputArguments);
    }
}