import { StatusCodes } from "opcua-webapi"

export class UaStatusCode {

    private static _codesDictionary : Map<number,string> = null;
    private _code : number;
    
    constructor(code? : number)
    {
        this._code = (code) ? code : StatusCodes.Good;
    }  

    public get value(): number
    {
        return this._code;
    }

    public get name(): string
    {
        if (null == UaStatusCode._codesDictionary)
        {
            UaStatusCode._codesDictionary = new Map;
            for (const key in StatusCodes)
            {
                const value = StatusCodes[key as keyof typeof StatusCodes];
                UaStatusCode._codesDictionary.set(value, key);
            }
        }

        let name = UaStatusCode._codesDictionary.get(this._code);

        return (name) ? name : "";
    }  

    public toString(): string 
    {        
        return this.name + " (0x" + this.value.toString(16).toUpperCase() + ")";
    }

    public checkBit(mask: number): boolean {
        return (this.value & mask) === mask;
    }

    /** returns true if the overflow bit is set */
    public get hasOverflowBit(): boolean {
        return this.checkBit(extraStatusCodeBits.Overflow);
    }

    /** returns true if the semanticChange bit is set */
    public get hasSemanticChangedBit(): boolean {
        return this.checkBit(extraStatusCodeBits.SemanticChanged);
    }

    /** returns true if the structureChange bit is set */
    public get hasStructureChangedBit(): boolean {
        return this.checkBit(extraStatusCodeBits.StructureChanged);
    }

    public equals(other: UaStatusCode): boolean {
        return this.value === other.value;
    }

    public isGood(): boolean {
        return this.value < 0x10000000;
    }

    public isNotGood(): boolean {
        return !this.isGood();
    }

    public isBad(): boolean {
        return this.value >= 0x80000000;
    }   

    public isUncertain(): boolean {
        return (this.value < 0x80000000 && this.value >= 0x10000000);
    }
}

export const extraStatusCodeBits: { [key: string]: number } = {
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

export function makeUaStatusCode(code? : number) : UaStatusCode
{
    return new UaStatusCode(code);
}