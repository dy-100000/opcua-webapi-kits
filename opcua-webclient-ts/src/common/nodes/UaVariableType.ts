import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId, UaVariant } from "../types";
import { UaDefintionNode, UaVariable } from ".";

export class UaVariableType extends UaDefintionNode
{        
    private _dataType : UaNodeId;
    private _valueRank : number;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        isAbstract: boolean,
        dataType : UaNodeId,
        valueRank : number)
    {
        super(nodeId, browseName, displayName,isAbstract);
        this._dataType = dataType;
        this._valueRank = valueRank;
    }
    
    get nodeClass() : NodeClass 
    {
        return NodeClass.VariableType;
    }

    get dataType() : UaNodeId
    {
        return this._dataType;
    }

    get valueRank() : number
    {
        return this._valueRank;
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
            nodeClass: NodeClass.VariableType,
            name: this._browseName,
            displayName: this._displayName.text,
            description: (this._description) ? this._description.text : undefined,
            isAbstract: this._isAbstract,
            children: (children.length != 0) ? children : undefined
        }

        return ret;
    }
}