"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UaExtensionObject = void 0;
const node_opcua_extension_object_1 = require("node-opcua-extension-object");
const node_opcua_nodeid_1 = require("node-opcua-nodeid");
class UaExtensionObject extends node_opcua_extension_object_1.ExtensionObject {
    constructor(dataTypeId, payload) {
        super();
        this._dataTypeId = node_opcua_nodeid_1.NodeId.nullNodeId;
        this._payload = null;
        if (typeof payload === "object" && null !== payload) {
            this._dataTypeId = dataTypeId;
            this._payload = payload;
        }
    }
    isValid() {
        return (this._dataTypeId.isEmpty) ? false : true;
    }
    get payload() {
        return this._payload;
    }
    get dataTypeId() {
        return this._dataTypeId;
    }
    toJson() {
        if (!this.isValid)
            return { UaTypeId: node_opcua_nodeid_1.NodeId.nullNodeId.toString() };
        let ret = this._payload;
        ret.UaTypeId = this._dataTypeId.toString();
        return ret;
    }
}
exports.UaExtensionObject = UaExtensionObject;
