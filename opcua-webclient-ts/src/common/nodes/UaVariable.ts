import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId, UaVariant } from "../types";
import { UaInstanceNode } from ".";

export class UaVariable extends UaInstanceNode
{        
    private _typeDefinitionId: UaNodeId;
    private _dataType : UaNodeId;
    private _valueRank : number;
    private _accessLevel : number;
    private _userAccessLevel : number;
    private _historizing : boolean;
    private _value : UaVariant

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        dataType : UaNodeId,
        valueRank : number,
        accessLevel : number,
        userAccessLevel : number,
        historizing : boolean,
        typeDefinitionId: UaNodeId,
        value: UaVariant | undefined)
    {
        super(nodeId, browseName, displayName);
        this._dataType = dataType;
        this._valueRank = valueRank;
        this._accessLevel = accessLevel;
        this._userAccessLevel = userAccessLevel;
        this._historizing = historizing;
        this._value = (value) ? value : UaVariant.null(),
        this._typeDefinitionId = typeDefinitionId;
    }
    
    get nodeClass() : NodeClass 
    {
        return NodeClass.Object;
    }

    get dataType() : UaNodeId
    {
        return this._dataType;
    }

    get valueRank() : number
    {
        return this._valueRank;
    }

    get accessLevel() : number
    {
        return this._accessLevel;
    }

    get userAccessLevel() : number
    {
        return this._userAccessLevel;
    }

    get historizing() : boolean
    {
        return this._historizing;
    }

    get value() : UaVariant
    {
        return this._value;
    }

    get typeDefinitionId() : UaNodeId
    {
        return this._typeDefinitionId;
    }

    addMember(node : UaVariable)
    {
        this._children.push(node);
    }

    get variableMembers() : Array<UaVariable>
    {
        let ret : Array<UaVariable> = [];

        for (let item of this._children)
        {            
            ret.push(item as UaVariable);
        }
        
        return ret;
    }
}