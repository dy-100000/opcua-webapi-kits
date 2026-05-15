import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId } from "../types";
import { UaInstanceNode, UaMethod, UaVariable } from ".";

export class UaObject extends UaInstanceNode
{        
    private _typeDefinitionId: UaNodeId;
    private _eventNotifier : number;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        eventNotifier: number,
        typeDefinitionId: UaNodeId)
    {
        super(nodeId, browseName, displayName);
        this._eventNotifier = eventNotifier;
        this._typeDefinitionId = typeDefinitionId;
    }
    
    get nodeClass() : NodeClass 
    {
        return NodeClass.Object;
    }

    get eventNotifier() : number
    {
        return this._eventNotifier;
    }

    get typeDefinitionId() : UaNodeId
    {
        return this._typeDefinitionId;
    }  

    addMember(node : UaInstanceNode)
    {
        this._children.push(node);
    }

    get objectMembers() : Array<UaObject>
    {
        let ret : Array<UaObject> = [];

        for (let item of this._children)
        {
            if (NodeClass.Object != item.nodeClass) continue;
            ret.push(item as UaObject);
        }
        
        return ret;
    }

    get variableMembers() : Array<UaVariable>
    {
        let ret : Array<UaVariable> = [];

        for (let item of this._children)
        {
            if (NodeClass.Variable != item.nodeClass) continue;
            ret.push(item as UaVariable);
        }
        
        return ret;
    }

    get methodMembers() : Array<UaMethod>
    {
        let ret : Array<UaMethod> = [];

        for (let item of this._children)
        {
            if (NodeClass.Method != item.nodeClass) continue;
            ret.push(item as UaMethod);
        }
        
        return ret;
    }

    toJson() : any
    {
        let children = [];

        for (let item of this._children)
        {
            children.push(item.toJson());
        }

        let ret = {
            nodeId : this._nodeId.toString(),
            nodeClass: NodeClass.Object,
            name: this._browseName,
            displayName: this._displayName.text,
            description: (this._description) ? this._description.text : undefined,
            typeDefinitionId: this._typeDefinitionId.toString(),
            eventNotifier : this._eventNotifier,
            children: (children.length != 0) ? children : undefined
        }

        return ret;
    }
}