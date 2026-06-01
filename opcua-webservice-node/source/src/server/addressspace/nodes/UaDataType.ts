import { NodeClass } from "opcua-webapi";
import { DataTypeIds, UaLocalizedText, UaNodeId, UaVariantType } from "opcua-webapi-ts";
import { UaDefinitionNode } from "./UaDefinitionNode";

export class UaDataType extends UaDefinitionNode {
    private _valueType: UaVariantType | null;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        isAbstract: boolean) {
        super(nodeId, browseName, displayName, isAbstract);
        this._valueType = null;
    }

    get nodeClass(): NodeClass {
        return NodeClass.DataType;
    }

    getValueType(): UaVariantType | null {
        if (this._valueType !== null) {
            return this._valueType;
        }

        if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.Boolean))) {
            this._valueType = UaVariantType.Boolean;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.SByte))) {
            this._valueType = UaVariantType.SByte;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.Byte))) {
            this._valueType = UaVariantType.Byte;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.Int16))) {
            this._valueType = UaVariantType.Int16;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.UInt16))) {
            this._valueType = UaVariantType.UInt16;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.Int32))) {
            this._valueType = UaVariantType.Int32;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.UInt32))) {
            this._valueType = UaVariantType.UInt32;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.Int64))) {
            this._valueType = UaVariantType.Int64;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.UInt64))) {
            this._valueType = UaVariantType.UInt64;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.Float))) {
            this._valueType = UaVariantType.Float;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.Double))) {
            this._valueType = UaVariantType.Double;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.String))) {
            this._valueType = UaVariantType.String;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.DateTime))) {
            this._valueType = UaVariantType.DateTime;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.Guid))) {
            this._valueType = UaVariantType.Guid;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.ByteString))) {
            this._valueType = UaVariantType.ByteString;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.XmlElement))) {
            this._valueType = UaVariantType.XmlElement;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.NodeId))) {
            this._valueType = UaVariantType.NodeId;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.ExpandedNodeId))) {
            this._valueType = UaVariantType.ExpandedNodeId;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.StatusCode))) {
            this._valueType = UaVariantType.StatusCode;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.QualifiedName))) {
            this._valueType = UaVariantType.QualifiedName;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.LocalizedText))) {
            this._valueType = UaVariantType.LocalizedText;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.Enumeration))) {
            this._valueType = UaVariantType.Int32;
        } else if (this.isSubtypeOf(UaNodeId.from(DataTypeIds.Structure))) {
            this._valueType = UaVariantType.ExtensionObject;
        }

        return this._valueType;
    }
}