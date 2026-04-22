import { UaDataValue } from "opcua-webapi-ts";

export class ReadHistoryDataResponse {
    private readonly _values: Array<UaDataValue>;
    private readonly _containsMoreData: boolean;

    constructor(values: Array<UaDataValue>, containsMoreData: boolean) {
        this._values = values;
        this._containsMoreData = containsMoreData;
    }

    get values(): Array<UaDataValue> {
        return this._values;
    }

    get containsMoreData(): boolean {
        return this._containsMoreData;
    }
}