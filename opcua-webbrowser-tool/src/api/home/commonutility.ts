import { UaValueType } from "./modellingapitypes";
import { typeManager } from "./typemanager";

export const WriteMask = {
    None                    : 0,
    AccessLevel             : 1,
    BrowseName              : 4,
    DataType                : 16,
    Description             : 32,
    DisplayName             : 64,
    EventNotifier           : 128,
    Executable              : 256,
    Historizing             : 512,
    NodeId                  : 16384,
    ValueRank               : 524288,
    Value                   : 2097152
}

export class CommonUtility
{
    public static isConfigurable(field : number, writeMask: number) : boolean
    {
        return (field & writeMask) == 0 ? false : true;
    }
}