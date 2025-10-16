export declare enum UaNodeIdType {
    NUMERIC = 1,
    STRING = 2,
    GUID = 3,
    BYTESTRING = 4
}
export declare class UaNodeId {
    static nullNodeId: UaNodeId;
    private _identifierType;
    private _value;
    private _nsIndex;
    constructor(value: number | string, namespace?: number, identifierType?: UaNodeIdType);
    get nsIndex(): number;
    get value(): string | number;
    get identifierType(): UaNodeIdType;
    numericId(): number;
    stringId(): string;
    isEmpty(): boolean;
    equal(other: UaNodeId | null): boolean;
    toString(): string;
}
export declare function parseUaNodeId(value: string): UaNodeId;
export declare function parseUaNodeIdOrNull(value: string | null | undefined): UaNodeId | null;
