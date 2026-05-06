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
        typeDefinitionId: UaNodeId)
    {
        super(nodeId, browseName, displayName);
        this._dataType = dataType;
        this._valueRank = valueRank;
        this._accessLevel = accessLevel;
        this._userAccessLevel = userAccessLevel;
        this._historizing = historizing;
        this._value = UaVariant.null();
        this._typeDefinitionId = typeDefinitionId;
    }
    
    get nodeClass() : NodeClass 
    {
        return NodeClass.Variable;
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

    set value(val: UaVariant)
    {
        this._value = val;
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

    toJson() : any
    {
        let children = [];

        for (let item of this._children)
        {
            if (item.nodeClass != NodeClass.Variable) continue;
            children.push(item.toJson());
        }

        let ret = {
            nodeId : this._nodeId.toString(),
            nodeClass: NodeClass.Variable,
            name: this._browseName,
            displayName: this._displayName.text,            
            typeDefinition: this._typeDefinitionId.toString(),
            dataType: this._dataType.toString(),
            valueRank: this._valueRank,
            accessLevel: this._accessLevel,
            children: (children.length != 0) ? children : undefined
        }

        return ret;
    }
}