import { DataTypeIds, UaLocalizedText, UaNodeId, VariableTypeIds } from "opcua-webapi-ts";
import { UaVariableType } from "../UaVariableType";

export class UaVariableTypes {
    static readonly BaseVariableType = new UaVariableType(UaNodeId.from(VariableTypeIds.BaseVariableType), "BaseVariableType", new UaLocalizedText("BaseVariableType"), true, UaNodeId.from(DataTypeIds.BaseDataType), -1);
    static readonly BaseDataVariableType = new UaVariableType(UaNodeId.from(VariableTypeIds.BaseDataVariableType), "BaseDataVariableType", new UaLocalizedText("BaseDataVariableType"), false, UaNodeId.from(DataTypeIds.BaseDataType), -1);
    static readonly PropertyType = new UaVariableType(UaNodeId.from(VariableTypeIds.PropertyType), "PropertyType", new UaLocalizedText("PropertyType"), false, UaNodeId.from(DataTypeIds.BaseDataType), -1);

    static readonly DataItemType = new UaVariableType(UaNodeId.from(VariableTypeIds.DataItemType), "DataItemType", new UaLocalizedText("DataItemType"), false, UaNodeId.from(DataTypeIds.BaseDataType), -1);
    static readonly BaseAnalogItemType = new UaVariableType(UaNodeId.from(VariableTypeIds.BaseAnalogType), "BaseAnalogType", new UaLocalizedText("BaseAnalogType"), false, UaNodeId.from(DataTypeIds.Number), -1);
}