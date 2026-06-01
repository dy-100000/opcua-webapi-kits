import { Argument, Attributes, NodeClass, StatusCodes } from "opcua-webapi";
import { DataTypeIds, UaAccessLevel, UaArgument, UaError, UaExtensionObject, UaLocalizedText, UaNodeId, UaVariant, makeUaStatusCode } from "opcua-webapi-ts";
import { UaVariableTypes } from "./builtin";
import { UaInstanceNode } from "./UaInstanceNode";
import { UaVariable } from "./UaVariable";

export class UaMethod extends UaInstanceNode {
    static readonly InputArguments = "InputArguments";
    static readonly OutputArguments = "OutputArguments";

    private _inputArguments: Array<UaArgument> | null;
    private _outputArguments: Array<UaArgument> | null;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText) {
        super(nodeId, browseName, displayName);
        this._inputArguments = null;
        this._outputArguments = null;
    }

    get nodeClass(): NodeClass {
        return NodeClass.Method;
    }

    get inputArguments(): Array<UaArgument> | null {
        return this._inputArguments;
    }

    get outputArguments(): Array<UaArgument> | null {
        return this._outputArguments;
    }

    setInputArguments(
        nodeId: UaNodeId, 
        inputArguments: Array<UaArgument>): UaVariable | null {

        if (inputArguments.length === 0) return null;
        
        if (this._inputArguments !== null) throw new UaError(makeUaStatusCode(StatusCodes.BadAlreadyExists));

        this._inputArguments = inputArguments;

        const variable = new UaVariable(
            nodeId,
            UaMethod.InputArguments,
            new UaLocalizedText(UaMethod.InputArguments),
            UaNodeId.from(DataTypeIds.Argument),
            1,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType
        );

        let extensionObjects : Array<UaExtensionObject> = [];
        for (const item of inputArguments) {
            extensionObjects.push(item.toExtensionObject());
        }

        variable.value = UaVariant.extensionObjects(extensionObjects);

        this.addMemberNode(variable);
        return variable;
    }

    setOutputArguments(nodeId: UaNodeId, outputArguments: Array<UaArgument>): UaVariable | null {
        if (outputArguments.length === 0) return null;
        
        if (this._outputArguments !== null) throw new UaError(makeUaStatusCode(StatusCodes.BadAlreadyExists));

        this._outputArguments = outputArguments;

        const variable = new UaVariable(
            nodeId,
            UaMethod.OutputArguments,
            new UaLocalizedText(UaMethod.OutputArguments),
            UaNodeId.from(DataTypeIds.Argument),
            1,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType
        );

        let extensionObjects : Array<UaExtensionObject> = [];
        for (const item of outputArguments) {
            extensionObjects.push(item.toExtensionObject());
        }

        variable.value = UaVariant.extensionObjects(extensionObjects);

        this.addMemberNode(variable);
        return variable;
    }

    getAttribute(attributeId: number): UaVariant {
        if (Attributes.Executable === attributeId || Attributes.UserExecutable === attributeId) {
            return UaVariant.boolean(true);
        }

        return super.getAttribute(attributeId);
    }
}