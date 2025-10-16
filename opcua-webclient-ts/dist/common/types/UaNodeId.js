"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UaNodeId = exports.UaNodeIdType = void 0;
exports.parseUaNodeId = parseUaNodeId;
exports.parseUaNodeIdOrNull = parseUaNodeIdOrNull;
const _1 = require(".");
const opcua_webapi_1 = require("opcua-webapi");
var UaNodeIdType;
(function (UaNodeIdType) {
    UaNodeIdType[UaNodeIdType["NUMERIC"] = 1] = "NUMERIC";
    UaNodeIdType[UaNodeIdType["STRING"] = 2] = "STRING";
    UaNodeIdType[UaNodeIdType["GUID"] = 3] = "GUID";
    UaNodeIdType[UaNodeIdType["BYTESTRING"] = 4] = "BYTESTRING";
})(UaNodeIdType || (exports.UaNodeIdType = UaNodeIdType = {}));
class UaNodeId {
    constructor(value, namespace, identifierType) {
        this._value = (value) ? value : 0;
        this._nsIndex = (namespace) ? namespace : 0;
        if (typeof value === 'number') {
            this._identifierType = UaNodeIdType.NUMERIC;
        }
        else {
            this._identifierType = (identifierType) ? identifierType : UaNodeIdType.STRING;
        }
        if (this._nsIndex < 0 && this._nsIndex > 0xffff)
            throw new _1.UaError((0, _1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadNodeIdInvalid));
        if (this._identifierType === UaNodeIdType.NUMERIC) {
            if (typeof this._value !== "number" || this._value < 0 || this._value > 0xffffffff)
                throw new _1.UaError((0, _1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadNodeIdInvalid));
        }
        else if (this._identifierType === UaNodeIdType.STRING ||
            this._identifierType === UaNodeIdType.BYTESTRING ||
            this._identifierType === UaNodeIdType.GUID) {
            if (typeof this._value !== "string")
                throw new _1.UaError((0, _1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadNodeIdInvalid));
        }
        else {
            throw new _1.UaError((0, _1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadNodeIdInvalid));
        }
    }
    get nsIndex() {
        return this._nsIndex;
    }
    get value() {
        return this._value;
    }
    get identifierType() {
        return this._identifierType;
    }
    numericId() {
        return (this._identifierType === UaNodeIdType.NUMERIC) ? this._value : 0;
    }
    stringId() {
        return (this._identifierType !== UaNodeIdType.NUMERIC) ? this._value : "";
    }
    isEmpty() {
        return (0 === this._value) ? true : false;
    }
    equal(other) {
        if (null == other)
            return false;
        if (this._identifierType !== other._identifierType)
            return false;
        if (this._nsIndex !== other._nsIndex)
            return false;
        if (this._value !== other._value)
            return false;
        return true;
    }
    toString() {
        const namespacePart = (this._nsIndex == 0) ? "" : `ns=${this._nsIndex};`;
        let str;
        switch (this.identifierType) {
            case UaNodeIdType.NUMERIC:
                str = `${namespacePart}i=${this._value}`;
                break;
            case UaNodeIdType.STRING:
                str = `${namespacePart}s=${this._value}`;
                break;
            case UaNodeIdType.GUID:
                str = `${namespacePart}g=${this._value}`;
                break;
            default:
                str = `${namespacePart}b=${this._value}`;
                break;
        }
        return str;
    }
}
exports.UaNodeId = UaNodeId;
UaNodeId.nullNodeId = new UaNodeId(0, 0, UaNodeIdType.NUMERIC);
const regexNamespaceI = /ns=([0-9]+);i=([0-9]+)/;
const regexNamespaceS = /ns=([0-9]+);s=(.*)/;
const regexNamespaceB = /ns=([0-9]+);b=(.*)/;
const regexNamespaceG = /ns=([0-9]+);g=([0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12})/;
function parseUaNodeId(value) {
    if (value.length <= 2)
        throw new _1.UaError((0, _1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadNodeIdInvalid));
    let identifierType = UaNodeIdType.NUMERIC;
    let idValue;
    let nsIndex;
    let twoFirst = value.substring(0, 2);
    let matches;
    if (twoFirst === "i=") {
        identifierType = UaNodeIdType.NUMERIC;
        idValue = parseInt(value.substring(2), 10);
    }
    else if ((matches = regexNamespaceI.exec(value)) !== null) {
        identifierType = UaNodeIdType.NUMERIC;
        nsIndex = parseInt(matches[1], 10);
        idValue = parseInt(matches[2], 10);
    }
    else if ((matches = regexNamespaceS.exec(value)) !== null) {
        identifierType = UaNodeIdType.STRING;
        nsIndex = parseInt(matches[1], 10);
        idValue = matches[2];
    }
    else if (twoFirst === "s=") {
        identifierType = UaNodeIdType.STRING;
        idValue = value.substring(2);
    }
    else if ((matches = regexNamespaceB.exec(value)) !== null) {
        identifierType = UaNodeIdType.BYTESTRING;
        nsIndex = parseInt(matches[1], 10);
        idValue = matches[2];
    }
    else if ((matches = regexNamespaceG.exec(value)) !== null) {
        identifierType = UaNodeIdType.GUID;
        nsIndex = parseInt(matches[1], 10);
        idValue = matches[2];
    }
    else if (twoFirst === "b=") {
        identifierType = UaNodeIdType.BYTESTRING;
        idValue = value.substring(2);
    }
    else if (twoFirst === "g=") {
        identifierType = UaNodeIdType.GUID;
        idValue = value.substring(2);
    }
    else {
        throw new _1.UaError((0, _1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadNodeIdInvalid));
    }
    return new UaNodeId(idValue, nsIndex, identifierType);
}
function parseUaNodeIdOrNull(value) {
    if (!value)
        return null;
    try {
        return parseUaNodeId(value);
    }
    catch (e) {
        return null;
    }
}
