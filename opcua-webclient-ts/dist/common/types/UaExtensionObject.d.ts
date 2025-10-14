import { ExtensionObject } from "node-opcua-extension-object";
import { NodeId } from "node-opcua-nodeid";
export declare class UaExtensionObject extends ExtensionObject {
    private _dataTypeId;
    private _payload;
    constructor(dataTypeId: NodeId, payload: any);
    isValid(): boolean;
    get payload(): any;
    get dataTypeId(): NodeId;
    toJson(): any;
}
