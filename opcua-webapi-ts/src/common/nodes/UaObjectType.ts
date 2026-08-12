import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId } from "../types";
import { UaDefintionNode, UaObject, UaMethod, UaVariable, UaInstanceNode } from ".";

export class UaObjectType extends UaDefintionNode
{        
    private _isObjectMemberRead : boolean;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        isAbstract: boolean)
    {
        super(nodeId, browseName, displayName, isAbstract);
        this._isObjectMemberRead = false;
    }
    
    get nodeClass() : NodeClass 
    {
        return NodeClass.ObjectType;
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

    get isObjectMemberRead() : boolean
    {
        return this._isObjectMemberRead;
    }

    set isObjectMemberRead(value : boolean)
    {
        this._isObjectMemberRead = value;
    }

    toJson() : any
    {
        let variables = [];
        let methods = [];

        for (let item of this._children)
        {
            if (item.nodeClass == NodeClass.ObjectType) continue;
            if (NodeClass.Variable == item.nodeClass) {
                variables.push(item.toJson());
            }
            else if (NodeClass.Method == item.nodeClass) {
                methods.push(item.toJson());
            }
        }

        let ret = {
            nodeId : this._nodeId.toString(),
            nodeClass: NodeClass.ObjectType,
            name: this._browseName,            
            displayName: this._displayName.text,
            description: (this._description) ? this._description.text : undefined,
            isAbstract: this._isAbstract,
            variables: (variables.length != 0) ? variables : undefined,
            methods: (methods.length != 0) ? methods : undefined
        }

        return ret;
    }
}