"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.extraStatusCodeBits = exports.UaStatusCode = void 0;
exports.makeUaStatusCode = makeUaStatusCode;
const opcua_webapi_1 = require("opcua-webapi");
class UaStatusCode {
    constructor(code) {
        this._code = (code) ? code : 0;
    }
    get value() {
        return this._code;
    }
    get name() {
        if (null == UaStatusCode._codesDictionary) {
            UaStatusCode._codesDictionary = new Map;
            for (const key in opcua_webapi_1.StatusCodes) {
                const value = opcua_webapi_1.StatusCodes[key];
                UaStatusCode._codesDictionary.set(value, key);
            }
        }
        let name = UaStatusCode._codesDictionary.get(this._code);
        return (name) ? name : "";
    }
    toString() {
        return this.name + " (0x" + this.value.toString(16).toUpperCase() + ")";
    }
    checkBit(mask) {
        return (this.value & mask) === mask;
    }
    /** returns true if the overflow bit is set */
    get hasOverflowBit() {
        return this.checkBit(exports.extraStatusCodeBits.Overflow);
    }
    /** returns true if the semanticChange bit is set */
    get hasSemanticChangedBit() {
        return this.checkBit(exports.extraStatusCodeBits.SemanticChanged);
    }
    /** returns true if the structureChange bit is set */
    get hasStructureChangedBit() {
        return this.checkBit(exports.extraStatusCodeBits.StructureChanged);
    }
    equals(other) {
        return this.value === other.value;
    }
    isGood() {
        return this.value < 0x10000000;
    }
    isNotGood() {
        return !this.isGood();
    }
    isBad() {
        return this.value >= 0x80000000;
    }
    isUncertain() {
        return (this.value < 0x80000000 && this.value >= 0x10000000);
    }
}
exports.UaStatusCode = UaStatusCode;
UaStatusCode._codesDictionary = null;
exports.extraStatusCodeBits = {
    StructureChanged: 0x1 << 15,
    SemanticChanged: 0x1 << 14,
    InfoTypeDataValue: 0x1 << 10, // 0x0400,
    LimitLow: 0x1 << 8,
    LimitHigh: 0x2 << 8,
    LimitConstant: 0x3 << 8,
    Overflow: 0x1 << 7, // 1 << 7
    HistorianCalculated: 0x1 << 0,
    HistorianInterpolated: 0x2 << 0,
    HistorianPartial: 0x1 << 2,
    HistorianExtraData: 0x1 << 3,
    HistorianMultiValue: 0x1 << 4
};
function makeUaStatusCode(code) {
    return new UaStatusCode(code);
}
