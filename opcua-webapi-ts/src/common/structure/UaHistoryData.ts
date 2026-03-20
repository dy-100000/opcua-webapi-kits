import { DataValue,HistoryData, HistoryDataFromJSON, HistoryDataToJSONTyped } from "opcua-webapi";
import { UaDataValue, UaExtensionObject, UaNodeId } from "../types";
import { UaPayloadMapper } from "../payload";
import { DataTypeIds } from "../nodes";

export class UaHistoryData
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.HistoryData);

    private _dataValues : Array<UaDataValue>

    constructor(dataValues : Array<UaDataValue>)
    {        
        this._dataValues = dataValues;
    }

    get dataValues() : Array<UaDataValue>
    {
        return this._dataValues;
    }

    toStruct() : HistoryData
    {
        let dataValues : Array<DataValue> = [];

        for (let item of this._dataValues)
        {
            dataValues.push(UaPayloadMapper.dataValueToWebApi(item));
        }

        let historyData : HistoryData = { DataValues: dataValues };    
        return historyData;
    }
    
    static fromStruct(historyData : HistoryData) : UaHistoryData | null
    {
        let dataValues : Array<UaDataValue> = [];

        if (historyData.DataValues)
        {
            for (let item of historyData.DataValues)
            {
                dataValues.push(UaPayloadMapper.dataValueFromWebApi(item));
            }   
        }    

        return new UaHistoryData(dataValues);
    }

    toExtensionObject() : UaExtensionObject
    {        
        return new UaExtensionObject(UaHistoryData.dataTypeId, HistoryDataToJSONTyped(this.toStruct()));
    } 

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaHistoryData | null
    {
        if (!UaHistoryData.dataTypeId.equal(extensionObject.typeId)) return null;      
        let historyData : HistoryData = HistoryDataFromJSON(extensionObject.body);
        return UaHistoryData.fromStruct(historyData);
    }
}