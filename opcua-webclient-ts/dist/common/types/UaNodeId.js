"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UaNodeId = exports.UaNodeIdType = void 0;
exports.coerceUaNodeId = coerceUaNodeId;
exports.resolveNodeId = resolveNodeId;
exports.sameNodeId = sameNodeId;
const _1 = require(".");
/**
 * `NodeIdType` an enumeration that specifies the possible types of a `NodeId` value.
 */
var UaNodeIdType;
(function (UaNodeIdType) {
    UaNodeIdType[UaNodeIdType["NUMERIC"] = 1] = "NUMERIC";
    UaNodeIdType[UaNodeIdType["STRING"] = 2] = "STRING";
    UaNodeIdType[UaNodeIdType["GUID"] = 3] = "GUID";
    UaNodeIdType[UaNodeIdType["BYTESTRING"] = 4] = "BYTESTRING";
})(UaNodeIdType || (exports.UaNodeIdType = UaNodeIdType = {}));
// function defaultValue(identifierType: NodeIdType.BYTESTRING): "";
// function defaultValue(identifierType: NodeIdType.STRING): "";
// function defaultValue(identifierType: NodeIdType.NUMERIC): 0;
// function defaultValue(identifierType: NodeIdType.GUID): string;
function defaultValue(identifierType) {
    switch (identifierType) {
        case UaNodeIdType.GUID: return _1.emptyGuid;
        case UaNodeIdType.BYTESTRING: return "";
        case UaNodeIdType.STRING: return "";
        case UaNodeIdType.NUMERIC: return 0;
        default:
            throw new Error("invalid identifierType");
    }
}
/**
 *
 * This class holds a OPC-UA node identifier.
 *
 * Nodes are unambiguously identified using a constructed
 * identifier called the NodeId. Some Servers may accept
 * alternative NodeIds in addition to the canonical NodeId
 * represented in this Attribute.
 *
 * A Server shall persist the NodeId of a Node, that is,
 * it shall not generate new
 * NodeIds when rebooting.
 *
 */
class UaNodeId {
    /**
     * construct a node Id from a type, a value and a namespace index.
     *
     * @param identifierType   - the nodeID type
     * @param value            - the node id value. The type of Value depends on identifierType.
     * @param namespace        - the index of the related namespace (optional , default value = 0 )
     *
     * @example
     *
     * ```javascript
     * const nodeId = new NodeId(NodeIdType.NUMERIC,123,1);
     * ```
     */
    constructor(identifierType, value, namespace) {
        if (identifierType === null || identifierType === undefined) {
            this.identifierType = UaNodeIdType.NUMERIC;
            this.value = 0;
            this.namespace = 0;
            return;
        }
        this.identifierType = identifierType;
        this.value = value || defaultValue(identifierType);
        this.namespace = namespace || 0;
        // namespace shall be a UInt16
        (0, _1.ua_assert)(this.namespace >= 0 && this.namespace <= 0xffff, "NodeId: invalid namespace value");
        (0, _1.ua_assert)(this.identifierType !== UaNodeIdType.NUMERIC ||
            (this.value !== null && this.value >= 0 && this.value <= 0xffffffff));
        (0, _1.ua_assert)(this.identifierType !== UaNodeIdType.GUID || (0, _1.isValidGuid)(this.value), "NodeId: Guid is invalid");
        (0, _1.ua_assert)(this.identifierType !== UaNodeIdType.STRING || typeof this.value === "string", "cannot  empty string");
        if (this.identifierType === UaNodeIdType.GUID) {
            this.value = (0, _1.normalizeGuid)(value);
        }
    }
    /**
     * get the string representation of the nodeID.
     *
     * @example
     *
     * by default, toString will return the "ns=" representation
     *
     * ```javascript
     * const nodeid = new NodeId(NodeIdType.NUMERIC, 123,1);
     * console.log(nodeid.toString());
     * ```
     *
     *  ```
     *  >"ns=1;i=123"
     *  ```
     * @example
     *
     *  toString can also be used to make the nsu= version of the nodeid.
     *
     *  ```javascript
     *  const namespaceArray = ["http://opcfoundation.com/UA/","http://mynamespace2"];
     *  const nodeid = new NodeId(NodeIdType.STRING, "Hello",1);
     *  console.log(nodeid.toString({namespaceArray}));
     *  ```
     *  ```
     *  >"nsu=http://mynamespace;i=123"
     *  ```
     * @example
     *
     *  passing an addressSpace to the toString options will decorate the nodeId
     *  with the BrowseName of the node.
     *
     *  ```javascript
     * const addressSpace = getAddressSpace();
     * const nodeid = new NodeId(NodeIdType.NUMERIC, 123,1);
     * console.log(nodeid.toString({addressSpace}));
     * ```
     * ```
     * >"nsu=http://mynamespace;i=123 (MyBrowseName)"
     * ```
     *
     *
     * @param [options.addressSpace] {AddressSpace}
     * @return {String}
     */
    toString(namespaceArray) {
        const namespacePart = namespaceArray
            ? this.namespace == 0
                ? ""
                : `nsu=${namespaceArray[this.namespace] || `<unknown namespace with index ${this.namespace}>`};`
            : `ns=${this.namespace};`;
        let str;
        const _this = this;
        switch (_this.identifierType) {
            case UaNodeIdType.NUMERIC:
                str = `${namespacePart}i=${_this.value}`;
                break;
            case UaNodeIdType.STRING:
                str = `${namespacePart}s=${_this.value}`;
                break;
            case UaNodeIdType.GUID:
                str = `${namespacePart}g=${(0, _1.normalizeGuid)(_this.value)}`;
                break;
            default:
                str = `${namespacePart}b=${_this.value}`;
                break;
        }
        return str;
    }
    /**
     * returns true if the NodeId is null or empty
     */
    isEmpty() {
        const _this = this;
        switch (_this.identifierType) {
            case UaNodeIdType.NUMERIC:
                return _this.value === 0;
            case UaNodeIdType.STRING:
                return !_this.value;
            case UaNodeIdType.GUID:
                return !_this.value || _this.value === _1.emptyGuid;
            default:
                return !_this.value;
        }
    }
}
exports.UaNodeId = UaNodeId;
UaNodeId.UaNodeIdType = UaNodeIdType;
/**
 * a fixed instance of a null NodeId
 */
UaNodeId.nullNodeId = new UaNodeId(UaNodeIdType.NUMERIC, 0, 0);
/**
 * @private
 */
const regexNamespaceI = /ns=([0-9]+);i=([0-9]+)/;
const regexNamespaceS = /ns=([0-9]+);s=(.*)/;
const regexNamespaceB = /ns=([0-9]+);b=(.*)/;
const regexNamespaceG = /ns=([0-9]+);g=([0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12})/;
const regexNSU = /nsu=(.*);(.*)/;
/**
 * Convert a value into a nodeId:
 *
 * @description:
 *    - if nodeId is a string of form : "i=1234"  => nodeId({value=1234, identifierType: NodeIdType.NUMERIC})
 *    - if nodeId is a string of form : "s=foo"   => nodeId({value="foo", identifierType: NodeIdType.STRING})
 *    - if nodeId is a string of form : "b=ABCD=" => nodeId({value=decodeBase64("ABCD="), identifierType: NodeIdType.BYTESTRING})
 *    - if nodeId is a {@link NodeId} :  coerceNodeId returns value
 *
 */
function coerceUaNodeId(value, namespaceOptions) {
    let matches;
    let twoFirst;
    if (value instanceof UaNodeId) {
        return value;
    }
    value = value || 0;
    let namespace = (typeof namespaceOptions === "number" ? namespaceOptions : namespaceOptions === null || namespaceOptions === void 0 ? void 0 : namespaceOptions.defaultNamespaceIndex) || 0;
    const namespaceArray = (namespaceOptions === null || namespaceOptions === void 0 ? void 0 : namespaceOptions.namespaceArray) || undefined;
    let identifierType = UaNodeIdType.NUMERIC;
    if (typeof value === "string") {
        identifierType = UaNodeIdType.STRING;
        twoFirst = value.substring(0, 2);
        if (twoFirst === "i=") {
            identifierType = UaNodeIdType.NUMERIC;
            value = parseInt(value.substring(2), 10);
        }
        else if (twoFirst === "s=") {
            identifierType = UaNodeIdType.STRING;
            value = value.substring(2);
        }
        else if (twoFirst === "b=") {
            identifierType = UaNodeIdType.BYTESTRING;
            value = value.substring(2);
        }
        else if (twoFirst === "g=") {
            identifierType = UaNodeIdType.GUID;
            value = (0, _1.normalizeGuid)(value.substring(2));
            (0, _1.ua_assert)((0, _1.isValidGuid)(value));
        }
        else if ((0, _1.isValidGuid)(value)) {
            identifierType = UaNodeIdType.GUID;
            value = (0, _1.normalizeGuid)(value);
        }
        else if ((matches = regexNamespaceI.exec(value)) !== null) {
            identifierType = UaNodeIdType.NUMERIC;
            namespace = parseInt(matches[1], 10);
            value = parseInt(matches[2], 10);
        }
        else if ((matches = regexNamespaceS.exec(value)) !== null) {
            identifierType = UaNodeIdType.STRING;
            namespace = parseInt(matches[1], 10);
            value = matches[2];
        }
        else if ((matches = regexNamespaceB.exec(value)) !== null) {
            identifierType = UaNodeIdType.BYTESTRING;
            namespace = parseInt(matches[1], 10);
            value = matches[2];
        }
        else if ((matches = regexNamespaceG.exec(value)) !== null) {
            identifierType = UaNodeIdType.GUID;
            namespace = parseInt(matches[1], 10);
            value = (0, _1.normalizeGuid)(matches[2]);
        }
        else {
            // eslint-disable-next-line no-empty
            if (namespaceArray && (matches = regexNSU.exec(value)) !== null) {
                const namespaceIndex = namespaceArray.indexOf(matches[1]);
                if (namespaceIndex === -1) {
                    throw new Error("Cannot find namespace with index " + matches[1] + " in " + namespaceArray.join(","));
                }
                const nid = coerceUaNodeId(matches[2], namespace);
                nid.namespace = namespaceIndex;
                return nid;
            }
            else {
                throw new Error("String cannot be coerced to a nodeId : " + value);
            }
        }
    }
    else if (value instanceof Buffer) {
        identifierType = UaNodeIdType.BYTESTRING;
    }
    else if (value instanceof Object) {
        // it could be a Enum or a NodeId Like object
        const tmp = value;
        value = tmp.value;
        namespace = namespace || tmp.namespace;
        identifierType = tmp.identifierType || identifierType;
        return new UaNodeId(identifierType, value, namespace);
    }
    return new UaNodeId(identifierType, value, namespace);
}
/**
 * resolveNodeId can be helpful to convert a wellknown Node Name to a nodeid
 * if a wellknown node name cannot be detected, the function falls back to
 * calling coerceNodeId {@link coerceNodeId}.
 *
 * @example
 * ```javascript
 * const nodeId = resolveNodeId("ObjectsFolder");
 * console.log(nodeId.toString());
 * ```
 * ```text
 * >ns=0;i=85
 * ```
 *
 * ```javascript
 * const nodeId = resolveNodeId("HasComponent");
 * console.log(nodeId.toString());
 * ```
 * ```text
 * >ns=0;i=33
 * ```
 *
 * ```javascript
 * const nodeId = resolveNodeId("ns=1;i=4444");
 * console.log(nodeId.toString());
 * ```
 * ```text
 * >ns=1;i=4444
 * ```
 *
 */
function resolveNodeId(nodeIdOrString, options) {
    return coerceUaNodeId(nodeIdOrString, options);
}
UaNodeId.resolveNodeId = resolveNodeId;
/**
 *
 * The sameNodeId function is used to compare two NodeId objects to
 * determine if they are identical. This comparison is based on the
 * identifier type, namespace, and value of the NodeId objects.
 *

 *
 * @return {boolean} Returns true if the two NodeId objects are
 * identical, otherwise returns false.
 *
 * @example
 * ```javascript
 * const nodeId1: NodeId = new NodeId(NodeIdType.STRING, "example", 1);
 * const nodeId2: NodeId = coerceNodeId("ns=1;s=example");
 * const areSame = sameNodeId(nodeId1, nodeId2); // returns true
 * ```
 */
function sameNodeId(n1, n2) {
    if (n1.identifierType !== n2.identifierType) {
        return false;
    }
    if (n1.namespace !== n2.namespace) {
        return false;
    }
    switch (n1.identifierType) {
        case UaNodeIdType.NUMERIC:
        case UaNodeIdType.STRING:
        case UaNodeIdType.GUID:
            return n1.value === n2.value;
        case UaNodeIdType.BYTESTRING:
            return n1.value === n2.value;
        default:
            throw new Error("Invalid identifier type");
    }
}
UaNodeId.sameNodeId = sameNodeId;
