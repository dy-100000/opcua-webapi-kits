import { UaNodeId } from "."

export class UaExtensionObject {
    private _typeId : UaNodeId;
    private _body : any;

    constructor(typeId: UaNodeId, body: any)
    {       
        this._typeId = typeId;
        this._body = body;      
    }

    isValid(): boolean {
        return (this._typeId.isEmpty()) ? false : true;
    }

    get body() : any
    {
        return this._body;
    }
    
    get typeId() : UaNodeId
    {
        return this._typeId;
    }
}