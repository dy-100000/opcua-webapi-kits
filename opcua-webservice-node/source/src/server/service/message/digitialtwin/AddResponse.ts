export class AddResponse {
    private readonly _newId: string;

    constructor(newId: string)
    {
        this._newId = newId;
    }

    get newId() : string
    {
        return this._newId;
    }
}