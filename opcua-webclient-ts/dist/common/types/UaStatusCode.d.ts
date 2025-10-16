export declare class UaStatusCode {
    private static _codesDictionary;
    private _code;
    constructor(code?: number);
    get value(): number;
    get name(): string;
    toString(): string;
    checkBit(mask: number): boolean;
    /** returns true if the overflow bit is set */
    get hasOverflowBit(): boolean;
    /** returns true if the semanticChange bit is set */
    get hasSemanticChangedBit(): boolean;
    /** returns true if the structureChange bit is set */
    get hasStructureChangedBit(): boolean;
    equals(other: UaStatusCode): boolean;
    isGood(): boolean;
    isNotGood(): boolean;
    isBad(): boolean;
    isUncertain(): boolean;
}
export declare const extraStatusCodeBits: {
    [key: string]: number;
};
export declare function makeUaStatusCode(code?: number): UaStatusCode;
