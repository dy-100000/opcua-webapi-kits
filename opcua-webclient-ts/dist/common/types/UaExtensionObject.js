"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UaExtensionObject = void 0;
const _1 = require(".");
class UaExtensionObject {
    constructor(dataTypeId, payload) {
        this._dataTypeId = _1.UaNodeId.nullNodeId;
        this._payload = null;
        if (typeof payload === "object" && null !== payload) {
            this._dataTypeId = dataTypeId;
            this._payload = payload;
        }
    }
    isValid() {
        return (this._dataTypeId.isEmpty()) ? false : true;
    }
    get payload() {
        return this._payload;
    }
    get dataTypeId() {
        return this._dataTypeId;
    }
    toJson() {
        if (!this.isValid())
            return { UaTypeId: _1.UaNodeId.nullNodeId.toString() };
        let ret = this._payload;
        ret.UaTypeId = this._dataTypeId.toString();
        return ret;
    }
}
exports.UaExtensionObject = UaExtensionObject;
