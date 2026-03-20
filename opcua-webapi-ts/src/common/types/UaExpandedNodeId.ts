/**
 * @module node-opcua-nodeid
 */
import { parseUaNodeId } from ".";
import { UaNodeId } from ".";

export class UaExpandedNodeId {
    private _nodeId: UaNodeId;
    private _namespaceUri: string | null;
    private _serverIndex: number;

    constructor(        
        nodeId : UaNodeId,
        namespaceUri?: string | null,
        serverIndex?: number) 
    {
        this._nodeId = nodeId;
        this._namespaceUri = (namespaceUri) ? namespaceUri : null;
        this._serverIndex = (serverIndex) ? serverIndex : 0;
    }

    get namespaceUri() : string | null
    {
        return this._namespaceUri;
    }

    get serverIndex() : number
    {
        return this._serverIndex;
    }

    isEmpty(): boolean {
        return this._nodeId.isEmpty();        
    }

    equal(other: UaExpandedNodeId) : boolean
    {
        if (!this._nodeId.equal(other._nodeId)) return false;
        if (this._serverIndex !== other._serverIndex) return false;
        if (this._namespaceUri !== other._namespaceUri) return false; 
        return true;
    }

    isLocalNodeId() : boolean
    {
        return (this._namespaceUri) ? false : true;
    }

    getNodeId(namespaceUris?: Array<string>) : UaNodeId | null
    {
        if (this.isLocalNodeId()) return this._nodeId;

        // To be implemented
        return null;
    }

    toString(): string {      
        // To be implemented  
        return this._nodeId.toString();
    }
}

export function parseUaExpandedNodeId(value: string): UaExpandedNodeId {
    let nodeId = parseUaNodeId(value);
    return new UaExpandedNodeId(nodeId, null, 0);
}

export function parseUaExpandedNodeIdOrNull(value: string | null | undefined): UaExpandedNodeId | null {
    if (!value) return null;
    try
    {
        return parseUaExpandedNodeId(value);
    } catch(e) {
        return null;
    }    
}
