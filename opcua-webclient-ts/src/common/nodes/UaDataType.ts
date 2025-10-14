import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId } from "../types";
import { UaDefintionNode, DataTypeIds } from ".";

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

    setEnumValues(enumValues : Map<number, UaLocalizedText>)
    {
        this._enumValues = enumValues;
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
            this._valueType = (this._parentType) ? (this._parentType as UaDataType)._valueType : 0;
        }
    }
}