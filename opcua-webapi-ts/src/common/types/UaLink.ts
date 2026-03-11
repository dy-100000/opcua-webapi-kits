import { NodeClass } from "opcua-webapi";
import { UaNodeId } from "./UaNodeId";

export class UaLink
{
    private _targetId : UaNodeId;
    private _nodeClass : number;
    private _referenceTypeId : UaNodeId;
    private _isForward : boolean;

    constructor(
        targetId : UaNodeId,
        nodeClass : number,
        referenceTypeId : UaNodeId,
        isForward : boolean)
    {
        this._targetId = targetId;
        this._nodeClass = nodeClass;
        this._referenceTypeId = referenceTypeId;
        this._isForward = isForward;
    }

    get targetId() : UaNodeId
    {
        return this._targetId;
    }

    get nodeClass() : number
    {
        return this._nodeClass;
    }

    get referenceTypeId() : UaNodeId
    {
        return this._referenceTypeId;
    }

    get isForward() : boolean
    {
        return this._isForward;
    }
}