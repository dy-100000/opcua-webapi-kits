import { UaNodeId } from ".";
export declare class UaExtensionObject {
    private _dataTypeId;
    private _payload;
    constructor(dataTypeId: UaNodeId, payload: any);
    isValid(): boolean;
    get payload(): any;
    get dataTypeId(): UaNodeId;
    toJson(): any;
}
