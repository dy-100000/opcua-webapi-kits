import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId } from "../types";
import { UaInstanceNode, UaVariable } from ".";
import { UaArgument } from "../structure";

export class UaMethod extends UaInstanceNode
{        
    public static InputArguments = "InputArguments";
    public static OutputArguments = "OutputArguments";

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText)
    {
        super(nodeId, browseName, displayName);
    }
    
    get nodeClass() : NodeClass 
    {
        return NodeClass.Object;
    }

    addMember(node : UaVariable)
    {
        this._children.push(node);
    }

    get inputArguments() : Array<UaArgument> | null
    {
        for (let item of this._children)
        {
            if (NodeClass.Variable == item.nodeClass &&
                UaMethod.InputArguments == item.browseName)
            {
                let extensionObjects = (item as UaVariable).value.toExtensionObjects();
                if (null == extensionObjects) return null;

                let ret : Array<UaArgument> = [];
                for (let item2 of extensionObjects)
                {
                    let argument = UaArgument.fromExtensionObject(item2);
                    if (null == argument) return null;

                    ret.push(argument);
                }

                return ret;
            }
        }

        return null;
    }

    get outputArguments() : Array<UaArgument> | null
    {
        for (let item of this._children)
        {
            if (NodeClass.Variable == item.nodeClass &&
                UaMethod.OutputArguments == item.browseName)
            {
                let extensionObjects = (item as UaVariable).value.toExtensionObjects();
                if (null == extensionObjects) return null;

                let ret : Array<UaArgument> = [];
                for (let item2 of extensionObjects)
                {
                    let argument = UaArgument.fromExtensionObject(item2);
                    if (null == argument) return null;

                    ret.push(argument);
                }

                return ret;
            }
        }

        return null;
    }
}