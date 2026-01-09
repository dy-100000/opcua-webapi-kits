import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId } from "../types";
import { UaDefintionNode } from ".";

export class UaObjectType extends UaDefintionNode
{        
    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        isAbstract: boolean)
    {
        super(nodeId, browseName, displayName, isAbstract);
    }
    
    get nodeClass() : NodeClass 
    {
        return NodeClass.ObjectType;
    }
}