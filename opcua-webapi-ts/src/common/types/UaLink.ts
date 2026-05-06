import { UaLocalizedText } from "./UaLocalizedText";
import { UaNodeId } from "./UaNodeId";

export class UaLink
{
    private _targetId : UaNodeId;
    private _nodeClass : number;
    private _browseName : string;
    private _displayName : UaLocalizedText;
    private _description : UaLocalizedText | null;
    private _referenceTypeId : UaNodeId;
    private _isForward : boolean;

    constructor(
        targetId : UaNodeId,
        nodeClass : number,
        browseName : string,
        displayName : UaLocalizedText,
        referenceTypeId : UaNodeId,
        isForward : boolean)
    {
        this._targetId = targetId;
        this._browseName = browseName;
        this._displayName = displayName;
        this._description = null;
        this._nodeClass = nodeClass;
        this._referenceTypeId = referenceTypeId;
        this._isForward = isForward;
    }

    get targetId() : UaNodeId
    {
        return this._targetId;
    }

    get browseName() : string
    {
        return this._browseName;
    }

    get displayName() : UaLocalizedText
    {
        return this._displayName;
    }

    get description() : UaLocalizedText | null
    {
        return this._description;
    }

    set description(description: UaLocalizedText | null)
    {
        this._description = description;
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

    toJson() : any
    {
        return {
            targetId: this._targetId.toString(),
            nodeClass: this._nodeClass,
            reference: this._referenceTypeId.toString(),
            isForward: this._isForward,
            browseName: this._browseName,
            displayName: this._displayName.text,
            description: (this._description) ? this._description.text : undefined,
        }
    }
}