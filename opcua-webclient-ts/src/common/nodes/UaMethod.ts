import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId } from "../types";
import { UaInstanceNode, UaVariable } from ".";
import { UaArgument } from "../structure";

export class UaMethod extends UaInstanceNode
{        
    public static InputArguments = "InputArguments";
    public static OutputArguments = "OutputArguments";

    private _inputArguments : Array<UaArgument> | null;
    private _outputArguments : Array<UaArgument> | null;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText)
    {
        super(nodeId, browseName, displayName);

        this._inputArguments = null;
        this._outputArguments = null;
    }
    
    get nodeClass() : NodeClass 
    {
        return NodeClass.Method;
    }

    addMember(node : UaVariable)
    {
        this._children.push(node);

        let isInputArgument : boolean = null;

        if (UaMethod.InputArguments == node.browseName)
        {
            isInputArgument = true;
        } else if (UaMethod.OutputArguments == node.browseName) {
            isInputArgument = false;
        }

        if (null == isInputArgument) return;

        let extensionObjects = node.value.toExtensionObjects();
        if (null == extensionObjects) return null;

        let args : Array<UaArgument> = [];
        for (let item2 of extensionObjects)
        {
            let argument = UaArgument.fromExtensionObject(item2);
            if (null == argument) return null;

            args.push(argument);
        }

        if (isInputArgument)
        {
            this._inputArguments = args;
        } else {
            this._outputArguments = args;
        }
    }

    get inputArguments() : Array<UaArgument> | null
    {        
        return this._inputArguments;
    }

    get outputArguments() : Array<UaArgument> | null
    { 
        return this._outputArguments;
    }

    toJson() : any
    {
        let inputArgs = [];
        let outputArgs = [];

        if (this._inputArguments)
        {
            for (let item of this._inputArguments)
            {
                inputArgs.push(item.toJson());
            }
        }

        if (this._outputArguments)
        {
            for (let item of this._outputArguments)
            {
                outputArgs.push(item.toJson());
            }
        }
     
        let ret = {
            nodeId : this._nodeId.toString(),
            nodeClass: NodeClass.Method,
            name: this._browseName,
            displayName: this._displayName.text,
            description: (this._description) ? this._description.text : undefined,
            inputArguments: inputArgs,
            outputArguments: outputArgs
        }

        return ret;
    }
}