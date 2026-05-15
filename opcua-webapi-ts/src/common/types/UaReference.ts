
import { UaLocalizedText, UaNodeId } from "../types";

export class UaReference
{
    private _nodeId: UaNodeId;
    private _nodeClass: number;
    private _browseName: string;
    private _displayName: UaLocalizedText;
    private _referenceTypeId: UaNodeId;
    private _typeDefinitionId: UaNodeId;
    private _description: UaLocalizedText;

    constructor(
        nodeId: UaNodeId,
        nodeClass: number,
        browseName?: string,
        displayName?: UaLocalizedText,
        referenceTypeId?: UaNodeId,
        typeDefinitionId?: UaNodeId)
    {
        this._nodeId = nodeId;
        this._nodeClass = nodeClass;
        this._browseName = (browseName) ? browseName : "";
        this._displayName = (displayName) ? displayName : UaLocalizedText.nullText;
        this._referenceTypeId = (referenceTypeId) ? referenceTypeId : UaNodeId.nullNodeId;
        this._typeDefinitionId = (typeDefinitionId) ? typeDefinitionId : UaNodeId.nullNodeId;
    }

    get nodeId() : UaNodeId
    {
        return this._nodeId;
    }

    get nodeClass() : number
    {
        return this._nodeClass;
    }

    get browseName() : string
    {
        return this._browseName;
    }

    get displayName() : UaLocalizedText
    {
        return this._displayName;
    }

    set displayName(value: UaLocalizedText)
    {
        this._displayName = value;
    }

    get referenceTypeId() : UaNodeId
    {
        return this._referenceTypeId;
    }

    set referenceTypeId(value: UaNodeId)
    {
        this._referenceTypeId = value;
    }

    get typeDefinitionId() : UaNodeId
    {
        return this._typeDefinitionId;
    }

    set typeDefinitionId(value: UaNodeId)
    {
        this._typeDefinitionId = value;
    }

    get description() : UaLocalizedText
    {
        return this._description;
    }

    set description(value: UaLocalizedText)
    {
        this._description = value;
    }

    toJson() : any
    {
        let ret = {
            nodeId : this._nodeId.toString(),
            nodeClass: this._nodeClass,
            name: this._browseName,
            displayName: this._displayName.text,
            description: (this._description) ? this._description.text : undefined,
            typeDefinitionId : (!this._typeDefinitionId.isEmpty()) ? this._typeDefinitionId.toString() : undefined,
        }
        
        return ret;
    }
}