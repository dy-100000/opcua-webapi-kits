import { DataTypeIds, UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaDataType } from "../UaDataType";

export class UaDataTypes {
    static readonly BaseDataType = new UaDataType(UaNodeId.from(DataTypeIds.BaseDataType), "BaseDataType", new UaLocalizedText("BaseDataType"), true);
    static readonly Boolean = new UaDataType(UaNodeId.from(DataTypeIds.Boolean), "Boolean", new UaLocalizedText("Boolean"), false);

    static readonly Number = new UaDataType(UaNodeId.from(DataTypeIds.Number), "Number", new UaLocalizedText("Number"), true);
    static readonly Integer = new UaDataType(UaNodeId.from(DataTypeIds.Integer), "Integer", new UaLocalizedText("Integer"), true);
    static readonly SByte = new UaDataType(UaNodeId.from(DataTypeIds.SByte), "SByte", new UaLocalizedText("SByte"), false);
    static readonly Int16 = new UaDataType(UaNodeId.from(DataTypeIds.Int16), "Int16", new UaLocalizedText("Int16"), false);
    static readonly Int32 = new UaDataType(UaNodeId.from(DataTypeIds.Int32), "Int32", new UaLocalizedText("Int32"), false);
    static readonly Int64 = new UaDataType(UaNodeId.from(DataTypeIds.Int64), "Int64", new UaLocalizedText("Int64"), false);

    static readonly UInteger = new UaDataType(UaNodeId.from(DataTypeIds.UInteger), "UInteger", new UaLocalizedText("UInteger"), true);
    static readonly Byte = new UaDataType(UaNodeId.from(DataTypeIds.Byte), "Byte", new UaLocalizedText("Byte"), false);
    static readonly UInt16 = new UaDataType(UaNodeId.from(DataTypeIds.UInt16), "UInt16", new UaLocalizedText("UInt16"), false);
    static readonly UInt32 = new UaDataType(UaNodeId.from(DataTypeIds.UInt32), "UInt32", new UaLocalizedText("UInt32"), false);
    static readonly UInt64 = new UaDataType(UaNodeId.from(DataTypeIds.UInt64), "UInt64", new UaLocalizedText("UInt64"), false);

    static readonly Float = new UaDataType(UaNodeId.from(DataTypeIds.Float), "Float", new UaLocalizedText("Float"), false);
    static readonly Double = new UaDataType(UaNodeId.from(DataTypeIds.Double), "Double", new UaLocalizedText("Double"), false);
    static readonly Duration = new UaDataType(UaNodeId.from(DataTypeIds.Duration), "Duration", new UaLocalizedText("Duration"), false);
    static readonly Decimal = new UaDataType(UaNodeId.from(DataTypeIds.Decimal), "Decimal", new UaLocalizedText("Decimal"), false);

    static readonly String = new UaDataType(UaNodeId.from(DataTypeIds.String), "String", new UaLocalizedText("String"), false);
    static readonly DateString = new UaDataType(UaNodeId.from(DataTypeIds.DateString), "DateString", new UaLocalizedText("DateString"), false);
    static readonly NormalizedString = new UaDataType(UaNodeId.from(DataTypeIds.NormalizedString), "NormalizedString", new UaLocalizedText("NormalizedString"), false);
    static readonly NumericRange = new UaDataType(UaNodeId.from(DataTypeIds.NumericRange), "NumericRange", new UaLocalizedText("NumericRange"), false);
    static readonly UriString = new UaDataType(UaNodeId.from(DataTypeIds.UriString), "UriString", new UaLocalizedText("UriString"), false);

    static readonly ByteString = new UaDataType(UaNodeId.from(DataTypeIds.ByteString), "ByteString", new UaLocalizedText("ByteString"), false);
    static readonly Image = new UaDataType(UaNodeId.from(DataTypeIds.Image), "Image", new UaLocalizedText("Image"), true);
    static readonly ImageBMP = new UaDataType(UaNodeId.from(DataTypeIds.ImageBMP), "ImageBMP", new UaLocalizedText("ImageBMP"), false);
    static readonly ImageGIF = new UaDataType(UaNodeId.from(DataTypeIds.ImageGIF), "ImageGIF", new UaLocalizedText("ImageGIF"), false);
    static readonly ImageJPG = new UaDataType(UaNodeId.from(DataTypeIds.ImageJPG), "ImageJPG", new UaLocalizedText("ImageJPG"), false);
    static readonly ImagePNG = new UaDataType(UaNodeId.from(DataTypeIds.ImagePNG), "ImagePNG", new UaLocalizedText("ImagePNG"), false);
    static readonly AudioDataType = new UaDataType(UaNodeId.from(DataTypeIds.AudioDataType), "AudioDataType", new UaLocalizedText("AudioDataType"), false);

    static readonly Enumeration = new UaDataType(UaNodeId.from(DataTypeIds.Enumeration), "Enumeration", new UaLocalizedText("Enumeration"), true);

    static readonly DateTime = new UaDataType(UaNodeId.from(DataTypeIds.DateTime), "DateTime", new UaLocalizedText("DateTime"), false);
    static readonly UtcTime = new UaDataType(UaNodeId.from(DataTypeIds.UtcTime), "UtcTime", new UaLocalizedText("UtcTime"), false);

    static readonly NodeId = new UaDataType(UaNodeId.from(DataTypeIds.NodeId), "NodeId", new UaLocalizedText("NodeId"), false);
    static readonly ExpandedNodeId = new UaDataType(UaNodeId.from(DataTypeIds.ExpandedNodeId), "ExpandedNodeId", new UaLocalizedText("ExpandedNodeId"), false);
    static readonly Guid = new UaDataType(UaNodeId.from(DataTypeIds.Guid), "Guid", new UaLocalizedText("Guid"), false);
    static readonly LocalizedText = new UaDataType(UaNodeId.from(DataTypeIds.LocalizedText), "LocalizedText", new UaLocalizedText("LocalizedText"), false);
    static readonly QualifiedName = new UaDataType(UaNodeId.from(DataTypeIds.QualifiedName), "QualifiedName", new UaLocalizedText("QualifiedName"), false);
    static readonly StatusCode = new UaDataType(UaNodeId.from(DataTypeIds.StatusCode), "StatusCode", new UaLocalizedText("StatusCode"), false);

    static readonly Structure = new UaDataType(UaNodeId.from(DataTypeIds.Structure), "Structure", new UaLocalizedText("Structure"), true);
    static readonly Range = new UaDataType(UaNodeId.from(DataTypeIds.Range), "Range", new UaLocalizedText("Range"), false);
    static readonly EUInformation = new UaDataType(UaNodeId.from(DataTypeIds.EUInformation), "EUInformation", new UaLocalizedText("EUInformation"), false);
    static readonly EnumValueType = new UaDataType(UaNodeId.from(DataTypeIds.EnumValueType), "EnumValueType", new UaLocalizedText("EnumValueType"), false);
}