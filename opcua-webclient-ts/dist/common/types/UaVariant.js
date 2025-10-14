"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UaVariant = void 0;
const node_opcua_status_code_1 = require("node-opcua-status-code");
const UaError_1 = require("./UaError");
class UaVariant {
    constructor() {
        this._value = null;
        this._type = UaVariantType.Null;
        this._arrayType = UaVariantArrayType.Scalar;
    }
    value() {
        return this._value;
    }
    type() {
        return this._type;
    }
    arrayType() {
        return this._arrayType;
    }
    isNull() {
        return (UaVariantType.Null == this._type) ? true : false;
    }
    setNull() {
        this._type = UaVariantType.Null;
        this._value = null;
        this._arrayType = UaVariantArrayType.Scalar;
    }
    setBoolean(value) {
        this._type = UaVariantType.Boolean;
        this._value = value;
        this._arrayType = UaVariantArrayType.Scalar;
    }
    setInt(value, type) {
        if (type && UaVariantType.SByte > type && UaVariantType.UInt64 < type)
            throw new UaError_1.UaError(node_opcua_status_code_1.StatusCodes.BadTypeMismatch);
        this._type = (type) ? type : UaVariantType.Int32;
        this._value = value;
        this._arrayType = UaVariantArrayType.Scalar;
    }
    setFloat(value) {
        this._type = UaVariantType.Float;
        this._value = value;
        this._arrayType = UaVariantArrayType.Scalar;
    }
    setDouble(value) {
        this._type = UaVariantType.Double;
        this._value = value;
        this._arrayType = UaVariantArrayType.Scalar;
    }
    setString(value) {
        this._type = UaVariantType.String;
        this._value = value;
        this._arrayType = UaVariantArrayType.Scalar;
    }
    setDateTime(value) {
        this._type = UaVariantType.DateTime;
        this._value = value;
        this._arrayType = UaVariantArrayType.Scalar;
    }
    setGuid(value) {
        this._type = UaVariantType.Guid;
        this._value = value;
        this._arrayType = UaVariantArrayType.Scalar;
    }
    setByteString(value) {
        this._type = UaVariantType.ByteString;
        this._value = value;
        this._arrayType = UaVariantArrayType.Scalar;
    }
    setNodeId(value) {
        this._type = UaVariantType.NodeId;
        this._value = value;
        this._arrayType = UaVariantArrayType.Scalar;
    }
    setExpandedNodeId(value) {
        this._type = UaVariantType.ExpandedNodeId;
        this._value = value;
        this._arrayType = UaVariantArrayType.Scalar;
    }
    setStatusCode(value) {
        this._type = UaVariantType.StatusCode;
        this._value = value;
        this._arrayType = UaVariantArrayType.Scalar;
    }
    setQualifiedName(value) {
        this._type = UaVariantType.QualifiedName;
        this._value = value;
        this._arrayType = UaVariantArrayType.Scalar;
    }
    setLocalizedText(text, locale) {
        this._type = UaVariantType.LocalizedText;
        this._value = {};
        this._arrayType = UaVariantArrayType.Scalar;
    }
}
exports.UaVariant = UaVariant;
