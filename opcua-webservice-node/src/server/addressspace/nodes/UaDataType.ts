import { EnumValueType, NodeClass, StatusCodes } from "opcua-webapi";
import { DataTypeIds, UaAccessLevel, UaEnumValueType, UaError, UaExtensionObject, UaLocalizedText, UaNodeId, UaVariant, makeUaStatusCode } from "opcua-webapi-ts";
import { UaVariableTypes } from "./builtin";
import { UaDefinitionNode } from "./UaDefinitionNode";
import { UaVariable } from "./UaVariable";

export class UaDataType extends UaDefinitionNode {
    private _enumVariable: UaVariable | null;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        isAbstract: boolean) {
        super(nodeId, browseName, displayName, isAbstract);
        this._enumVariable = null;
    }

    get nodeClass(): NodeClass {
        return NodeClass.DataType;
    }

    isEnumDataType(): boolean {
        return this._enumVariable !== null;
    }

    setEnumStrings(nodeId: UaNodeId, enumStrings: UaLocalizedText[]): UaVariable {
        if (!this.isSubtypeOf(UaNodeId.from(DataTypeIds.Enumeration)) || 
        this._enumVariable !== null || enumStrings.length === 0) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadTypeDefinitionInvalid));
        }

        this._enumVariable = new UaVariable(
            nodeId,
            "EnumStrings",
            new UaLocalizedText("EnumStrings"),
            UaNodeId.from(DataTypeIds.LocalizedText),
            1,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType
        );

        this._enumVariable.value = UaVariant.localizedTexts(enumStrings);
        this.addMemberNode(this._enumVariable);
        return this._enumVariable;
    }

    setEnumValues(nodeId: UaNodeId, enumValues: UaEnumValueType[]): UaVariable {
        if (!this.isSubtypeOf(UaNodeId.from(DataTypeIds.Enumeration)) || 
        this._enumVariable !== null || enumValues.length === 0) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadTypeDefinitionInvalid));
        }

        this._enumVariable = new UaVariable(
            nodeId,
            "EnumValues",
            new UaLocalizedText("EnumValues"),
            UaNodeId.from(DataTypeIds.EnumValueType),
            1,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType
        );

        let extensionObjects : Array<UaExtensionObject> = [];
        
        for (const item of enumValues) {
            extensionObjects.push(item.toExtensionObject());
        }       

        this._enumVariable.value = UaVariant.extensionObjects(extensionObjects);
        this.addMemberNode(this._enumVariable);
        return this._enumVariable;
    }
}