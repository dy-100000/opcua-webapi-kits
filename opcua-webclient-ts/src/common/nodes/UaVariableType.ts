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