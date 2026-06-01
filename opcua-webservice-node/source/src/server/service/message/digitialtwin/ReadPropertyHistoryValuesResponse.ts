import { UaDataValue } from "opcua-webapi-ts";

export class ReadPropertyHistoryValuesResponse {
    private readonly _dataValues: Array<UaDataValue>;
    private _containsMoreData: boolean;

    constructor() {
        this._dataValues = [];
        this._containsMoreData = false;
    }

    addDataValue(value: UaDataValue): void {
        this._dataValues.push(value);
    }

    get dataValues(): Array<UaDataValue> {
        return this._dataValues;
    }

    set containsMoreData(containsMoreData: boolean) {
        this._containsMoreData = containsMoreData;
    }

    get containsMoreData(): boolean {
        return this._containsMoreData;
    }
}