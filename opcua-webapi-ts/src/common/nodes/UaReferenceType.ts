import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId } from "../types";
import { UaDefintionNode } from ".";

export class UaReferenceType extends UaDefintionNode
{    
    private _inverseName: UaLocalizedText;
    private _symmetric: boolean;
    
    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        isAbstract: boolean,
        inverseName: UaLocalizedText,
        symmetric: boolean)
    {
        super(nodeId, browseName, displayName, isAbstract);

        this._inverseName = inverseName;
        this._symmetric = symmetric;
    }
    
    get nodeClass() : NodeClass 
    {
        return NodeClass.ReferenceType;
    }

    get inverseName() : UaLocalizedText
    {
        return this._inverseName;
    }

    get symmetric() : boolean
    {
        return this._symmetric;
    }

    toJson() : any
    {
        let ret = {
            nodeId : this._nodeId.toString(),
            nodeClass: NodeClass.ReferenceType,
            name: this._browseName,
            displayName: this._displayName.text,
            description: (this._description) ? this._description.text : undefined,
            isAbstract: this._isAbstract
        }

        return ret;
    }
}