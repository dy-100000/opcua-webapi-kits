"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ExpandedNodeId = void 0;
exports.coerceExpandedNodeId = coerceExpandedNodeId;
const _1 = require(".");
/**
 * An ExpandedNodeId extends the NodeId structure.
 *
 * An ExpandedNodeId extends the NodeId structure by allowing the NamespaceUri to be
 * explicitly specified instead of using the NamespaceIndex. The NamespaceUri is optional. If it
 * is specified then the NamespaceIndex inside the NodeId shall be ignored.
 *
 * The ExpandedNodeId is encoded by first encoding a NodeId as described in Clause 5 .2.2.9
 * and then encoding NamespaceUri as a String.
 *
 * An instance of an ExpandedNodeId may still use the NamespaceIndex instead of the
 * NamespaceUri. In this case, the NamespaceUri is not encoded in the stream. The presence of
 * the NamespaceUri in the stream is indicated by setting the NamespaceUri flag in the encoding
 * format byte for the NodeId.
 *
 * If the NamespaceUri is present then the encoder shall encode the NamespaceIndex as 0 in
 * the stream when the NodeId portion is encoded. The unused NamespaceIndex is included in
 * the stream for consistency,
 *
 * An ExpandedNodeId may also have a ServerIndex which is encoded as a UInt32 after the
 * NamespaceUri. The ServerIndex flag in the NodeId encoding byte indicates whether the
 * ServerIndex is present in the stream. The ServerIndex is omitted if it is equal to zero.
 *
 *
 *
 */
class ExpandedNodeId extends _1.UaNodeId {
    static fromNodeId(nodeId, namespaceUri, serverIndex) {
        return new ExpandedNodeId(nodeId.identifierType, nodeId.value, nodeId.namespace, namespaceUri, serverIndex);
    }
    constructor(identifierType, value, namespace, namespaceUri, serverIndex) {
        super(identifierType, value, namespace);
        this.namespaceUri = namespaceUri || null;
        this.serverIndex = serverIndex || 0;
    }
    toString() {
        let str = _1.UaNodeId.prototype.toString.call(this);
        if (this.namespaceUri) {
            str += ";namespaceUri:" + this.namespaceUri;
        }
        if (this.serverIndex) {
            str += ";serverIndex:" + this.serverIndex;
        }
        return str;
    }
}
exports.ExpandedNodeId = ExpandedNodeId;
ExpandedNodeId.nullExpandedNodeId = new ExpandedNodeId(_1.UaNodeIdType.NUMERIC, 0, 0);
function coerceExpandedNodeId(value) {
    const n = (0, _1.coerceUaNodeId)(value);
    return new ExpandedNodeId(n.identifierType, n.value, n.namespace, /*namespaceUri*/ null, /*serverIndex*/ 0);
}
