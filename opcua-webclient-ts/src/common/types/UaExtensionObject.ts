import { UaNodeId } from "."

export class UaExtensionObject {
    private _dataTypeId : UaNodeId;
    private _payload : any;

    constructor(dataTypeId: UaNodeId, payload: any)
    {       
        this._dataTypeId = UaNodeId.nullNodeId;
        this._payload = null;

        if (typeof payload === "object" && null !== payload)
        {
            this._dataTypeId = dataTypeId;
            this._payload = payload;
        }        
    }

    isValid(): boolean {
        return (this._dataTypeId.isEmpty()) ? false : true;
    }

    get payload() : any
    {
        return this._payload;
    }
    
    get dataTypeId() : UaNodeId
    {
        return this._dataTypeId;
    }

    toJson() : any
    {
        if (!this.isValid()) return { UaTypeId: UaNodeId.nullNodeId.toString() };

        let ret = this._payload;
        ret.UaTypeId = this._dataTypeId.toString();

        return ret;
    }
}