export class GetElementsResponse {
    private readonly _elementNames: Set<string>;

    constructor() {
        this._elementNames = new Set();
    }

    add(elementName: string): void {
        this._elementNames.add(elementName);
    }

    get elementNames(): Set<string> {
        return this._elementNames;
    }
}