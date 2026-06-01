import { DataTypeIds, UaLocalizedText, UaNodeId, VariableTypeIds } from "opcua-webapi-ts";
import { UaVariableType } from "../UaVariableType";

export class UaVariableTypes {
    static readonly BaseVariableType = new UaVariableType(UaNodeId.from(VariableTypeIds.BaseVariableType), "BaseVariableType", new UaLocalizedText("BaseVariableType"), true, UaNodeId.from(DataTypeIds.BaseDataType), -1, null);
    static readonly BaseDataVariableType = new UaVariableType(UaNodeId.from(VariableTypeIds.BaseDataVariableType), "BaseDataVariableType", new UaLocalizedText("BaseDataVariableType"), false, UaNodeId.from(DataTypeIds.BaseDataType), -1, UaVariableTypes.BaseVariableType);
    static readonly PropertyType = new UaVariableType(UaNodeId.from(VariableTypeIds.PropertyType), "PropertyType", new UaLocalizedText("PropertyType"), false, UaNodeId.from(DataTypeIds.BaseDataType), -1, UaVariableTypes.BaseVariableType);

    static readonly DataItemType = new UaVariableType(UaNodeId.from(VariableTypeIds.DataItemType), "DataItemType", new UaLocalizedText("DataItemType"), false, UaNodeId.from(DataTypeIds.BaseDataType), -1, UaVariableTypes.BaseDataVariableType);
    static readonly BaseAnalogItemType = new UaVariableType(UaNodeId.from(VariableTypeIds.BaseAnalogType), "BaseAnalogType", new UaLocalizedText("BaseAnalogType"), false, UaNodeId.from(DataTypeIds.Number), -1, UaVariableTypes.DataItemType);
    static readonly DiscreteItemType = new UaVariableType(UaNodeId.from(VariableTypeIds.DiscreteItemType), "DiscreteItemType", new UaLocalizedText("DiscreteItemType"), false, UaNodeId.from(DataTypeIds.BaseDataType), -1, UaVariableTypes.DataItemType);

    static readonly TwoStateDiscreteType = new UaVariableType(UaNodeId.from(VariableTypeIds.TwoStateDiscreteType), "TwoStateDiscreteType", new UaLocalizedText("TwoStateDiscreteType"), false, UaNodeId.from(DataTypeIds.Boolean), -1, UaVariableTypes.DiscreteItemType);
    static readonly MultiStateDiscreteType = new UaVariableType(UaNodeId.from(VariableTypeIds.MultiStateDiscreteType), "MultiStateDiscreteType", new UaLocalizedText("MultiStateDiscreteType"), false, UaNodeId.from(DataTypeIds.UInt32), -1, UaVariableTypes.DiscreteItemType);
    static readonly MultiStateValueDiscreteType = new UaVariableType(UaNodeId.from(VariableTypeIds.MultiStateValueDiscreteType), "MultiStateValueDiscreteType", new UaLocalizedText("MultiStateValueDiscreteType"), false, UaNodeId.from(DataTypeIds.Number), -1, UaVariableTypes.DiscreteItemType);

    static readonly ArrayItemType = new UaVariableType(UaNodeId.from(VariableTypeIds.ArrayItemType), "ArrayItemType", new UaLocalizedText("ArrayItemType"), false, UaNodeId.from(DataTypeIds.BaseDataType), 1, UaVariableTypes.DataItemType);
    static readonly YArrayItemType = new UaVariableType(UaNodeId.from(VariableTypeIds.YArrayItemType), "YArrayItemType", new UaLocalizedText("YArrayItemType"), false, UaNodeId.from(DataTypeIds.BaseDataType), 1, UaVariableTypes.ArrayItemType);
    static readonly XYArrayItemType = new UaVariableType(UaNodeId.from(VariableTypeIds.XYArrayItemType), "XYArrayItemType", new UaLocalizedText("XYArrayItemType"), false, UaNodeId.from(DataTypeIds.BaseDataType), 1, UaVariableTypes.ArrayItemType);
    static readonly ImageItemType = new UaVariableType(UaNodeId.from(VariableTypeIds.ImageItemType), "ImageItemType", new UaLocalizedText("ImageItemType"), false, UaNodeId.from(DataTypeIds.BaseDataType), 2, UaVariableTypes.ArrayItemType);
    static readonly CubeItemType = new UaVariableType(UaNodeId.from(VariableTypeIds.CubeItemType), "CubeItemType", new UaLocalizedText("CubeItemType"), false, UaNodeId.from(DataTypeIds.BaseDataType), 3, UaVariableTypes.ArrayItemType);
    static readonly NDimensionArrayItemType = new UaVariableType(UaNodeId.from(VariableTypeIds.NDimensionArrayItemType), "NDimensionArrayItemType", new UaLocalizedText("NDimensionArrayItemType"), false, UaNodeId.from(DataTypeIds.BaseDataType), 0, UaVariableTypes.ArrayItemType);
}