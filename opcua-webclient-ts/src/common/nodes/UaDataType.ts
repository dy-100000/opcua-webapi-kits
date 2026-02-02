import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId, UaVariant, UaVariantType } from "../types";
import { UaDefintionNode, DataTypeIds, UaVariable } from ".";
import { UaEnumValueType } from "../structure";

export class UaDataType extends UaDefintionNode
{
    private _valueType: number;
    private _enumValues: Map<number, UaLocalizedText> | null;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        isAbstract: boolean,
        writeMask?: number | null)
    {
        super(nodeId, browseName, displayName, isAbstract,writeMask);

        this._valueType = 0;
        this._enumValues = null;
    }

    get nodeClass(): NodeClass
    {
        return NodeClass.DataType;
    }

    get valueType() : number
    {
        return this._valueType;
    }

    get enumValues() : Map<number, UaLocalizedText> | null
    {
        return this._enumValues;
    }

    setEnumVariable(node : UaVariable)
    {
        if ("EnumStrings" == node.browseName ||
            "EnumValues" == node.browseName)
        {            
            this.setEnumValues(node.value);
        }
    }

    setEnumValues(value : UaVariant)
    {
        if (UaVariantType.LocalizedText == value.type)
        {
            let enumStrings = value.toLocalizedTexts();
            if (null == enumStrings) return;

            this._enumValues = new Map;            
            let index = 0;

            for (let item of enumStrings)
            {
                this._enumValues.set(index, item);
                index++;
            }
        } else if (UaVariantType.ExtensionObject == value.type) {
            let extensionObjects = value.toExtensionObjects();
            if (null == extensionObjects) return;

            let enumValues = new Map;

            for (let item of extensionObjects)
            {
                let enumValue = UaEnumValueType.fromExtensionObject(item);
                if (!enumValue) return;
                enumValues.set(enumValue.value, enumValue.displayName);
            }

            this._enumValues = enumValues;
        }
    }

    classify()
    {
        let isBasicDataType = false;

        if (0 == this._nodeId.nsIndex)
        {
            if (DataTypeIds.Boolean <= this._nodeId.numericId() && 
                DataTypeIds.Structure >= this._nodeId.numericId()) isBasicDataType = true;
            
            if (DataTypeIds.Enumeration == this._nodeId.numericId()) isBasicDataType = true;
        }

        if (isBasicDataType)
        {
            this._valueType = this._nodeId.numericId();
        } else {
            this._valueType = (this.parentType()) ? (this.parentType() as UaDataType)._valueType : 0;
        }
    }
}