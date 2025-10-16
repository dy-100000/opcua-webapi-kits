"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UaArrayType = exports.UaVariantType = exports.UaVariant = void 0;
const opcua_webapi_1 = require("opcua-webapi");
const _1 = require(".");
class UaVariant {
    constructor() {
        this._value = null;
        this._type = UaVariantType.Null;
        this._arrayType = UaArrayType.Scalar;
    }
    get value() {
        return this._value;
    }
    get type() {
        return this._type;
    }
    get arrayType() {
        return this._arrayType;
    }
    isScalar() {
        return (UaArrayType.Scalar == this._arrayType) ? true : false;
    }
    isArray() {
        return (UaArrayType.Array == this._arrayType) ? true : false;
    }
    isNull() {
        return (UaVariantType.Null == this._type) ? true : false;
    }
    static null() {
        return new UaVariant();
    }
    static boolean(value) {
        let val = new UaVariant();
        val._type = UaVariantType.Boolean;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }
    static integer(value, type) {
        if (type && UaVariantType.SByte > type && UaVariantType.UInt64 < type)
            throw new _1.UaError((0, _1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadTypeMismatch));
        let val = new UaVariant();
        val._type = (type) ? type : UaVariantType.Int32;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }
    static float(value) {
        let val = new UaVariant();
        val._type = UaVariantType.Float;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }
    static double(value) {
        let val = new UaVariant();
        val._type = UaVariantType.Double;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }
    static string(value) {
        let val = new UaVariant();
        val._type = UaVariantType.String;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }
    static dateTime(value) {
        let val = new UaVariant();
        val._type = UaVariantType.DateTime;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }
    static guid(value) {
        let val = new UaVariant();
        val._type = UaVariantType.Guid;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }
    static byteString(value) {
        let val = new UaVariant();
        val._type = UaVariantType.ByteString;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }
    static nodeId(value) {
        let val = new UaVariant();
        val._type = UaVariantType.NodeId;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }
    static expandedNodeId(value) {
        let val = new UaVariant();
        val._type = UaVariantType.ExpandedNodeId;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }
    static statusCode(value) {
        let val = new UaVariant();
        val._type = UaVariantType.StatusCode;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }
    static qualifiedName(value) {
        let val = new UaVariant();
        val._type = UaVariantType.QualifiedName;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }
    static localizedText(value) {
        let val = new UaVariant();
        val._type = UaVariantType.LocalizedText;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }
    static extensionObject(value) {
        let val = new UaVariant();
        val._type = UaVariantType.ExtensionObject;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }
    static booleans(value) {
        let val = new UaVariant();
        val._type = UaVariantType.Boolean;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
    static integers(value, type) {
        if (type && UaVariantType.SByte > type && UaVariantType.UInt64 < type)
            throw new _1.UaError((0, _1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadTypeMismatch));
        let val = new UaVariant();
        val._type = (type) ? type : UaVariantType.Int32;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
    static floats(value) {
        let val = new UaVariant();
        val._type = UaVariantType.Float;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
    static doubles(value) {
        let val = new UaVariant();
        val._type = UaVariantType.Double;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
    static strings(value) {
        let val = new UaVariant();
        val._type = UaVariantType.String;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
    static dateTimes(value) {
        let val = new UaVariant();
        val._type = UaVariantType.DateTime;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
    static guids(value) {
        let val = new UaVariant();
        val._type = UaVariantType.Guid;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
    static byteStrings(value) {
        let val = new UaVariant();
        val._type = UaVariantType.ByteString;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
    static nodeIds(value) {
        let val = new UaVariant();
        val._type = UaVariantType.NodeId;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
    static expandedNodeIds(value) {
        let val = new UaVariant();
        val._type = UaVariantType.ExpandedNodeId;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
    static statusCodes(value) {
        let val = new UaVariant();
        val._type = UaVariantType.StatusCode;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
    static qualifiedNames(value) {
        let val = new UaVariant();
        val._type = UaVariantType.QualifiedName;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
    static localizedTexts(value) {
        let val = new UaVariant();
        val._type = UaVariantType.LocalizedText;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
    static extensionObjects(value) {
        let val = new UaVariant();
        val._type = UaVariantType.ExtensionObject;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
}
exports.UaVariant = UaVariant;
var UaVariantType;
(function (UaVariantType) {
    UaVariantType[UaVariantType["Null"] = 0] = "Null";
    UaVariantType[UaVariantType["Boolean"] = 1] = "Boolean";
    UaVariantType[UaVariantType["SByte"] = 2] = "SByte";
    UaVariantType[UaVariantType["Byte"] = 3] = "Byte";
    UaVariantType[UaVariantType["Int16"] = 4] = "Int16";
    UaVariantType[UaVariantType["UInt16"] = 5] = "UInt16";
    UaVariantType[UaVariantType["Int32"] = 6] = "Int32";
    UaVariantType[UaVariantType["UInt32"] = 7] = "UInt32";
    UaVariantType[UaVariantType["Int64"] = 8] = "Int64";
    UaVariantType[UaVariantType["UInt64"] = 9] = "UInt64";
    UaVariantType[UaVariantType["Float"] = 10] = "Float";
    UaVariantType[UaVariantType["Double"] = 11] = "Double";
    UaVariantType[UaVariantType["String"] = 12] = "String";
    UaVariantType[UaVariantType["DateTime"] = 13] = "DateTime";
    UaVariantType[UaVariantType["Guid"] = 14] = "Guid";
    UaVariantType[UaVariantType["ByteString"] = 15] = "ByteString";
    UaVariantType[UaVariantType["XmlElement"] = 16] = "XmlElement";
    UaVariantType[UaVariantType["NodeId"] = 17] = "NodeId";
    UaVariantType[UaVariantType["ExpandedNodeId"] = 18] = "ExpandedNodeId";
    UaVariantType[UaVariantType["StatusCode"] = 19] = "StatusCode";
    UaVariantType[UaVariantType["QualifiedName"] = 20] = "QualifiedName";
    UaVariantType[UaVariantType["LocalizedText"] = 21] = "LocalizedText";
    UaVariantType[UaVariantType["ExtensionObject"] = 22] = "ExtensionObject";
})(UaVariantType || (exports.UaVariantType = UaVariantType = {}));
var UaArrayType;
(function (UaArrayType) {
    UaArrayType[UaArrayType["Scalar"] = 0] = "Scalar";
    UaArrayType[UaArrayType["Array"] = 1] = "Array";
    UaArrayType[UaArrayType["Matrix"] = 2] = "Matrix";
})(UaArrayType || (exports.UaArrayType = UaArrayType = {}));
