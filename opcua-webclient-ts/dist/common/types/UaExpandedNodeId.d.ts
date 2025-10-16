import { UaNodeId, UaNodeIdType } from ".";
export declare class UaExpandedNodeId {
    static nullExpandedNodeId: UaExpandedNodeId;
    private _nodeId;
    private _namespaceUri;
    private _serverIndex;
    constructor(value: number | string, namespace?: number, identifierType?: UaNodeIdType | null, namespaceUri?: null | string, serverIndex?: number);
    get namespaceUri(): string | null;
    get serverIndex(): number;
    isEmpty(): boolean;
    equal(other: UaExpandedNodeId): boolean;
    isLocalNodeId(): boolean;
    getNodeId(namespaceUris?: Array<string>): UaNodeId | null;
    toString(): string;
}
export declare function parseUaExpandedNodeId(value: string): UaExpandedNodeId;
