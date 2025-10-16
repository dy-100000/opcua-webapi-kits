"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UaExpandedNodeId = void 0;
exports.parseUaExpandedNodeId = parseUaExpandedNodeId;
/**
 * @module node-opcua-nodeid
 */
const _1 = require(".");
const _2 = require(".");
class UaExpandedNodeId {
    constructor(value, namespace, identifierType, namespaceUri, serverIndex) {
        this._nodeId = new _2.UaNodeId(value, namespace, identifierType);
        this._namespaceUri = (namespaceUri) ? namespaceUri : null;
        this._serverIndex = (serverIndex) ? serverIndex : 0;
    }
    get namespaceUri() {
        return this._namespaceUri;
    }
    get serverIndex() {
        return this._serverIndex;
    }
    isEmpty() {
        return this._nodeId.isEmpty();
    }
    equal(other) {
        if (!this._nodeId.equal(other._nodeId))
            return false;
        if (this._serverIndex !== other._serverIndex)
            return false;
        if (this._namespaceUri !== other._namespaceUri)
            return false;
        return true;
    }
    isLocalNodeId() {
        return (this._namespaceUri) ? false : true;
    }
    getNodeId(namespaceUris) {
        if (this.isLocalNodeId())
            return this._nodeId;
        // To be implemented
        return null;
    }
    toString() {
        // To be implemented  
        return this._nodeId.toString();
    }
}
exports.UaExpandedNodeId = UaExpandedNodeId;
UaExpandedNodeId.nullExpandedNodeId = new UaExpandedNodeId(0, 0, _2.UaNodeIdType.NUMERIC);
function parseUaExpandedNodeId(value) {
    let nodeId = (0, _1.parseUaNodeId)(value);
    return new UaExpandedNodeId(nodeId.value, nodeId.nsIndex, nodeId.identifierType, null, 0);
}
