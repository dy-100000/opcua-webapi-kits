package org.opcfoundation.webserver.addressspace.nodes.builtin;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.nodes.UaDataType;

public class UaDataTypes {
    public final static UaDataType BaseDataType = new UaDataType(NodeIds.BaseDataType,"BaseDataType", new LocalizedText("BaseDataType"), true, null);
    public final static UaDataType Boolean = new UaDataType(NodeIds.Boolean,"Boolean", new LocalizedText("Boolean"), false, BaseDataType);

    public final static UaDataType Number = new UaDataType(NodeIds.Number,"Number", new LocalizedText("Number"), true, BaseDataType);
    public final static UaDataType Integer = new UaDataType(NodeIds.Integer,"Integer", new LocalizedText("Integer"), true, Number);
    public final static UaDataType SByte = new UaDataType(NodeIds.SByte,"SByte", new LocalizedText("SByte"), false, Integer);
    public final static UaDataType Int16 = new UaDataType(NodeIds.Int16,"Int16", new LocalizedText("Int16"), false, Integer);
    public final static UaDataType Int32 = new UaDataType(NodeIds.Int32,"Int32", new LocalizedText("Int32"), false, Integer);
    public final static UaDataType Int64 = new UaDataType(NodeIds.Int64,"Int64", new LocalizedText("Int64"), false, Integer);

    public final static UaDataType UInteger = new UaDataType(NodeIds.UInteger,"UInteger", new LocalizedText("UInteger"), true, Number);
    public final static UaDataType Byte = new UaDataType(NodeIds.Byte,"Byte", new LocalizedText("Byte"), false, UInteger);
    public final static UaDataType UInt16 = new UaDataType(NodeIds.UInt16,"UInt16", new LocalizedText("UInt16"), false, UInteger);
    public final static UaDataType UInt32 = new UaDataType(NodeIds.UInt32,"UInt32", new LocalizedText("UInt32"), false, UInteger);
    public final static UaDataType UInt64 = new UaDataType(NodeIds.UInt64,"UInt64", new LocalizedText("UInt64"), false, UInteger);

    public final static UaDataType Float = new UaDataType(NodeIds.Float,"Float", new LocalizedText("Float"), false, Number);
    public final static UaDataType Double = new UaDataType(NodeIds.Double,"Double", new LocalizedText("Double"), false, Number);
    public final static UaDataType Duration = new UaDataType(NodeIds.Duration,"Duration", new LocalizedText("Duration"), false, Double);
    public final static UaDataType Decimal = new UaDataType(NodeIds.Decimal,"Decimal", new LocalizedText("Decimal"), false, Number);

    public final static UaDataType String = new UaDataType(NodeIds.String,"String", new LocalizedText("String"), false, BaseDataType);
    public final static UaDataType DateString = new UaDataType(NodeIds.DateString,"DateString", new LocalizedText("DateString"), false, String);
    public final static UaDataType NormalizedString = new UaDataType(NodeIds.NormalizedString,"NormalizedString", new LocalizedText("NormalizedString"), false, String);
    public final static UaDataType NumericRange = new UaDataType(NodeIds.NumericRange,"NumericRange", new LocalizedText("NumericRange"), false, String);
    public final static UaDataType UriString = new UaDataType(NodeIds.UriString,"UriString", new LocalizedText("UriString"), false, String);

    public final static UaDataType ByteString = new UaDataType(NodeIds.ByteString,"ByteString", new LocalizedText("ByteString"), false, BaseDataType);
    public final static UaDataType Image = new UaDataType(NodeIds.Image,"Image", new LocalizedText("Image"), true, ByteString);
    public final static UaDataType ImageBMP = new UaDataType(NodeIds.ImageBMP,"ImageBMP", new LocalizedText("ImageBMP"), false, Image);
    public final static UaDataType ImageGIF = new UaDataType(NodeIds.ImageGIF,"ImageGIF", new LocalizedText("ImageGIF"), false, Image);
    public final static UaDataType ImageJPG = new UaDataType(NodeIds.ImageJPG,"ImageJPG", new LocalizedText("ImageJPG"), false, Image);
    public final static UaDataType ImagePNG = new UaDataType(NodeIds.ImagePNG,"ImagePNG", new LocalizedText("ImagePNG"), false, Image);
    public final static UaDataType AudioDataType = new UaDataType(NodeIds.AudioDataType,"AudioDataType", new LocalizedText("AudioDataType"), false, ByteString);

    public final static UaDataType Enumeration = new UaDataType(NodeIds.Enumeration,"Enumeration", new LocalizedText("Enumeration"), true, BaseDataType);

    public final static UaDataType DateTime = new UaDataType(NodeIds.DateTime,"DateTime", new LocalizedText("DateTime"), false, BaseDataType);
    public final static UaDataType UtcTime = new UaDataType(NodeIds.UtcTime,"UtcTime", new LocalizedText("UtcTime"), false, DateTime);

    public final static UaDataType NodeId = new UaDataType(NodeIds.NodeId,"NodeId", new LocalizedText("NodeId"), false, BaseDataType);
    public final static UaDataType ExpandedNodeId = new UaDataType(NodeIds.ExpandedNodeId,"ExpandedNodeId", new LocalizedText("ExpandedNodeId"), false, BaseDataType);
    public final static UaDataType Guid = new UaDataType(NodeIds.Guid,"Guid", new LocalizedText("Guid"), false, BaseDataType);
    public final static UaDataType LocalizedText = new UaDataType(NodeIds.LocalizedText,"LocalizedText", new LocalizedText("LocalizedText"), false, BaseDataType);
    public final static UaDataType QualifiedName = new UaDataType(NodeIds.QualifiedName,"QualifiedName", new LocalizedText("QualifiedName"), false, BaseDataType);
    public final static UaDataType StatusCode = new UaDataType(NodeIds.StatusCode,"StatusCode", new LocalizedText("StatusCode"), false, BaseDataType);

    public final static UaDataType Structure = new UaDataType(NodeIds.Structure,"Structure", new LocalizedText("Structure"), true, BaseDataType);
    public final static UaDataType Range = new UaDataType(NodeIds.Range,"Range", new LocalizedText("Range"), false, Structure);
    public final static UaDataType EUInformation = new UaDataType(NodeIds.EUInformation,"EUInformation", new LocalizedText("EUInformation"), false, Structure);
    public final static UaDataType EnumValueType = new UaDataType(NodeIds.EnumValueType,"EnumValueType", new LocalizedText("EnumValueType"), false, Structure);
}
