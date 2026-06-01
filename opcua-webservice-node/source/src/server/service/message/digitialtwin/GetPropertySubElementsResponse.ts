export class GetPropertySubElementsResponse {
    private readonly _subElementNames: Set<string>;

    constructor() {
        this._subElementNames = new Set();
    }

    add(subElementName: string): void {
        this._subElementNames.add(subElementName);
    }

    get subElementNames(): Set<string> {
        return this._subElementNames;
    }
}