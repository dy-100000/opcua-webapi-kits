package org.opcfoundation.webapi.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.webapi.mapper.extensionobjects.ArgumentWebApi;
import org.opcfoundation.webapi.mapper.extensionobjects.EUInformationWebApi;
import org.opcfoundation.webapi.mapper.extensionobjects.EnumValueTypeWebApi;
import org.opcfoundation.webapi.mapper.extensionobjects.RangeWebApi;
import org.opcfoundation.webapi.model.DataValue;
import org.opcfoundation.webapi.model.LocalizedText;
import org.opcfoundation.webapi.model.StatusCode;
import org.opcfoundation.webapi.model.Variant;

import java.math.BigInteger;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UaTypeMapper {
    public static @Nullable NodeId nodeIdFromWebApi(@Nullable String nodeId)
    {
        if (null == nodeId) return null;
        return NodeId.parseOrNull(nodeId);
    }

    public static List<ByteString> byteStringsFromWebApi(List<byte[]> byteStrings)
    {
        ArrayList<ByteString> results = new ArrayList<>();

        for (byte[] item : byteStrings)
        {
            results.add(new ByteString(item));
        }

        return results;
    }

    public static LocalizedText localizedTextFromMilo(org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText text)
    {
        LocalizedText ret = new LocalizedText();
        ret.setText(text.getText());

        if (null != text.getLocale() && !text.getLocale().isEmpty())
        {
            ret.setLocale(text.getLocale());
        }

        return ret;
    }

    public static org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode statusCodeFromWebApi(StatusCode statusCode)
    {
        return new org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode(
                (null == statusCode.getCode()) ? 0 : statusCode.getCode());
    }

    public static StatusCode statusCodeFromMilo(org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode statusCode)
    {
        StatusCode ret = new StatusCode();
        ret.setCode(statusCode.getValue());
        return ret;
    }

    public static List<StatusCode> statusCodesFromMilo(List<org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode> statusCodes)
    {
        ArrayList<StatusCode> results = new ArrayList<>();

        for (org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode item: statusCodes)
        {
            results.add(statusCodeFromMilo(item));
        }

        return results;
    }

    public static @Nullable DateTime dateTimeFromWebApi(@Nullable OffsetDateTime timestamp)
    {
        if (null == timestamp) return null;
        return new DateTime(timestamp.toInstant());
    }

    public static OffsetDateTime dateTimeFromMilo(DateTime timestamp)
    {
        return OffsetDateTime.ofInstant(timestamp.getJavaInstant(), ZoneOffset.UTC);
    }

    public static @Nullable Object extensionObjectFromMilo(ExtensionObject extensionObject)
    {
        try {
            UaStructuredType structureData = extensionObject.decode(UaEncoder.defaultEncoder);
            if (structureData instanceof org.eclipse.milo.opcua.stack.core.types.structured.Range range)
            {
                return new RangeWebApi(range);
            } else if (structureData instanceof org.eclipse.milo.opcua.stack.core.types.structured.EUInformation euInformation) {
                return new EUInformationWebApi(euInformation);
            } else if (structureData instanceof org.eclipse.milo.opcua.stack.core.types.structured.Argument argument) {
                return new ArgumentWebApi(argument);
            } else if (structureData instanceof org.eclipse.milo.opcua.stack.core.types.structured.EnumValueType enumValue) {
                return new EnumValueTypeWebApi(enumValue);
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public static @Nullable Object[] extensionObjectsFromMilo(ExtensionObject[] extensionObjects)
    {
        Object[] ret = new Object[extensionObjects.length];
        for (int i=0; i<extensionObjects.length; ++i)
        {
            Object structObject = extensionObjectFromMilo(extensionObjects[i]);
            if (null == structObject) return null;
            ret[i] = structObject;
        }
        return ret;
    }

    public static Variant variantFromMilo(org.eclipse.milo.opcua.stack.core.types.builtin.Variant value)
    {
        Variant ret = new Variant();
        ret.setUaType(0);
        ret.setValue(null);

        Optional<OpcUaDataType> dataTypeOptional = value.getDataType();
        Object val = value.getValue();

        if (dataTypeOptional.isEmpty() || null == val) return ret;

        OpcUaDataType dataType = dataTypeOptional.get();
        boolean isArray = val.getClass().isArray();

        ret.setUaType(dataType.getTypeId());

        if (OpcUaDataType.Boolean == dataType ||
                OpcUaDataType.String == dataType ||
                OpcUaDataType.Float == dataType ||
                OpcUaDataType.Double == dataType ||
                OpcUaDataType.Int32 == dataType ||
                OpcUaDataType.Int16 == dataType ||
                OpcUaDataType.Int64 == dataType ||
                OpcUaDataType.SByte == dataType)
        {
            ret.setValue(val);
        } else if (OpcUaDataType.UInt32 == dataType) {
            if (!isArray)
            {
                UInteger uIntegerValue = (UInteger)val;
                ret.setValue(uIntegerValue.longValue());
            } else {
                UInteger[] uIntegerValues = (UInteger[])val;
                Long[] longValues = new Long[uIntegerValues.length];

                for (int i=0; i<uIntegerValues.length; ++i)
                {
                    longValues[i] = uIntegerValues[i].longValue();
                }
                ret.setValue(longValues);
            }
        } else if (OpcUaDataType.UInt16 == dataType) {
            if (!isArray)
            {
                UShort uShortValue = (UShort)val;
                ret.setValue(uShortValue.intValue());
            } else {
                UShort[] uShortValues = (UShort[])val;
                Integer[] intValues = new Integer[uShortValues.length];

                for (int i=0; i<uShortValues.length; ++i)
                {
                    intValues[i] = uShortValues[i].intValue();
                }
                ret.setValue(intValues);
            }
        } else if (OpcUaDataType.Byte == dataType) {
            if (!isArray)
            {
                UByte uByteValue = (UByte)val;
                ret.setValue(uByteValue.shortValue());
            } else {
                UByte[] uByteValues = (UByte[])val;
                Short[] shortValues = new Short[uByteValues.length];

                for (int i=0; i<uByteValues.length; ++i)
                {
                    shortValues[i] = uByteValues[i].shortValue();
                }
                ret.setValue(shortValues);
            }
        } else if (OpcUaDataType.UInt64 == dataType) {
            if (!isArray)
            {
                ULong uLongValue = (ULong)val;
                ret.setValue(uLongValue.toBigInteger());
            } else {
                ULong[] uLongValues = (ULong[])val;
                BigInteger[] bigIntValues = new BigInteger[uLongValues.length];

                for (int i=0; i<uLongValues.length; ++i)
                {
                    bigIntValues[i] = uLongValues[i].toBigInteger();
                }
                ret.setValue(bigIntValues);
            }
        } else if (OpcUaDataType.NodeId == dataType) {
            if (!isArray)
            {
                NodeId nodeIdValue = (NodeId)val;
                ret.setValue(nodeIdValue.toParseableString());
            } else {
                NodeId[] nodeIdValues = (NodeId[])val;
                String[] stringValues = new String[nodeIdValues.length];

                for (int i=0; i<nodeIdValues.length; ++i)
                {
                    stringValues[i] = nodeIdValues[i].toParseableString();
                }
                ret.setValue(stringValues);
            }
        } else if (OpcUaDataType.DateTime == dataType) {
            if (!isArray)
            {
                DateTime dateTimeValue = (DateTime)val;
                ret.setValue(dateTimeValue.toIso8601String());
            } else {
                DateTime[] dateTimeValues = (DateTime[])val;
                String[] stringValues = new String[dateTimeValues.length];

                for (int i=0; i<dateTimeValues.length; ++i)
                {
                    stringValues[i] = dateTimeValues[i].toIso8601String();
                }
                ret.setValue(stringValues);
            }
        } else if (OpcUaDataType.StatusCode == dataType) {
            if (!isArray)
            {
                org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode statusCodeValue = (org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode)val;
                StatusCode statusCode = new StatusCode();
                statusCode.setCode(statusCodeValue.getValue());
                ret.setValue(statusCode);
            } else {
                org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode[] statusCodeValues = (org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode[])val;
                StatusCode[] statusCodes = new StatusCode[statusCodeValues.length];

                for (int i=0; i<statusCodeValues.length; ++i)
                {
                    statusCodes[i] = new StatusCode();
                    statusCodes[i].setCode(statusCodeValues[i].getValue());
                }
                ret.setValue(statusCodes);
            }
        } else if (OpcUaDataType.Guid == dataType) {
            if (!isArray)
            {
                UUID guidValue = (UUID)val;
                ret.setValue(guidValue.toString());
            } else {
                UUID[] guidValues = (UUID[])val;
                String[] stringValues = new String[guidValues.length];

                for (int i=0; i<guidValues.length; ++i)
                {
                    stringValues[i] = guidValues[i].toString();
                }
                ret.setValue(stringValues);
            }
        } else if (OpcUaDataType.LocalizedText == dataType) {
            if (!isArray)
            {
                org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText localizedTextValue = (org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText)val;
                LocalizedText localizedText = new LocalizedText();
                localizedText.setText(localizedTextValue.getText());
                localizedText.setLocale(localizedTextValue.getLocale());
                ret.setValue(localizedText);
            } else {
                org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText[] localizedTextValues = (org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText[])val;
                LocalizedText[] localizedTexts = new LocalizedText[localizedTextValues.length];

                for (int i=0; i<localizedTextValues.length; ++i)
                {
                    localizedTexts[i] = new LocalizedText();
                    localizedTexts[i].setText(localizedTextValues[i].getText());
                    localizedTexts[i].setLocale(localizedTextValues[i].getLocale());
                }
                ret.setValue(localizedTexts);
            }
        } else if (OpcUaDataType.QualifiedName == dataType) {
            if (!isArray)
            {
                QualifiedName qualifiedNameValue = (QualifiedName)val;
                ret.setValue(qualifiedNameValue.getName());
            } else {
                QualifiedName[] qualifiedNameValues = (QualifiedName[])val;
                String[] stringValues = new String[qualifiedNameValues.length];

                for (int i=0; i<qualifiedNameValues.length; ++i)
                {
                    stringValues[i] = qualifiedNameValues[i].getName();
                }
                ret.setValue(stringValues);
            }
        } else if (OpcUaDataType.ExpandedNodeId == dataType) {
            if (!isArray)
            {
                ExpandedNodeId expandedNodeIdValue = (ExpandedNodeId)val;
                ret.setValue(expandedNodeIdValue.toParseableString());
            } else {
                ExpandedNodeId[] expandedNodeIdValues = (ExpandedNodeId[])val;
                String[] stringValues = new String[expandedNodeIdValues.length];

                for (int i=0; i<expandedNodeIdValues.length; ++i)
                {
                    stringValues[i] = expandedNodeIdValues[i].toParseableString();
                }
                ret.setValue(stringValues);
            }
        } else if (OpcUaDataType.ByteString == dataType) {
            if (!isArray)
            {
                ByteString byteStringValue = (ByteString)val;
                String stringValue = (byteStringValue.isNull()) ? "" : new String(byteStringValue.bytes());
                ret.setValue(stringValue);
            } else {
                ByteString[] byteStringValues = (ByteString[])val;
                String[] stringValues = new String[byteStringValues.length];

                for (int i=0; i<byteStringValues.length; ++i)
                {
                    String stringValue = (byteStringValues[i].isNull()) ? "" : new String(byteStringValues[i].bytes());
                    stringValues[i] = stringValue;
                }
                ret.setValue(stringValues);
            }
        } else if (OpcUaDataType.ExtensionObject == dataType) {
            if (!isArray)
            {
                ExtensionObject extensionObjectValue = (ExtensionObject)val;
                Object structureObject = extensionObjectFromMilo(extensionObjectValue);
                if (null != structureObject)
                {
                    ret.setValue(structureObject);
                } else {
                    ret.setUaType(0);
                }
            } else {
                ExtensionObject[] extensionObjects = (ExtensionObject[])val;
                Object[] structureObjects = extensionObjectsFromMilo(extensionObjects);

                if (null != structureObjects)
                {
                    ret.setValue(structureObjects);
                } else {
                    ret.setUaType(0);
                }
            }
        }

        return ret;
    }

    public static org.eclipse.milo.opcua.stack.core.types.builtin.Variant variantFromWebApi(Variant value) throws UaRuntimeException
    {
        org.eclipse.milo.opcua.stack.core.types.builtin.Variant ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.NULL_VALUE;

        try {
            Integer dataType = value.getUaType();
            Object val = value.getValue();

            if (null == dataType || 0 == dataType || null == val) return ret;

            boolean isArray = val instanceof List<?>;

            if (OpcUaDataType.Boolean.getTypeId() == dataType) {
                if (!isArray) {
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofBoolean((Boolean) val);
                } else {
                    List<?> boolArray = (List<?>)val;
                    Boolean[] booleans = new Boolean[boolArray.size()];
                    int index = 0;

                    for (Object item : boolArray)
                    {
                        booleans[index] = (Boolean) item;
                        index++;
                    }
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofBooleanArray(booleans);
                }
            } else if (OpcUaDataType.Float.getTypeId() == dataType) {
                if (!isArray) {
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofFloat(((Number)val).floatValue());
                } else {
                    List<?> numberArray = (List<?>)val;
                    Float[] numbers = new Float[numberArray.size()];

                    int index = 0;
                    for (Object item : numberArray)
                    {
                        numbers[index] = ((Number) item).floatValue();
                        index++;
                    }
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofFloatArray(numbers);
                }
            } else if (OpcUaDataType.Double.getTypeId() == dataType) {
                if (!isArray) {
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofDouble(((Number) val).doubleValue());
                } else {
                    List<?> numberArray = (List<?>)val;
                    Double[] numbers = new Double[numberArray.size()];

                    int index = 0;
                    for (Object item : numberArray)
                    {
                        numbers[index] = ((Number) item).doubleValue();
                        index++;
                    }
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofDoubleArray(numbers);
                }
            } else if (OpcUaDataType.String.getTypeId() == dataType) {
                if (!isArray) {
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofString((String) val);
                } else {
                    List<?> stringArray = (List<?>)val;
                    String[] strings = new String[stringArray.size()];

                    int index = 0;
                    for (Object item : stringArray)
                    {
                        strings[index] = (String) item;
                        index++;
                    }
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofStringArray(strings);
                }
            } else if (OpcUaDataType.Int32.getTypeId() == dataType) {
                if (!isArray) {
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofInt32(((Number) val).intValue());
                } else {
                    List<?> numberArray = (List<?>)val;
                    Integer[] numbers = new Integer[numberArray.size()];

                    int index = 0;
                    for (Object item : numberArray)
                    {
                        numbers[index] = ((Number) item).intValue();
                        index++;
                    }
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofInt32Array(numbers);
                }
            } else if (OpcUaDataType.Int16.getTypeId() == dataType) {
                if (!isArray) {
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofInt16(((Number) val).shortValue());
                } else {
                    List<?> numberArray = (List<?>)val;
                    Short[] numbers = new Short[numberArray.size()];

                    int index = 0;
                    for (Object item : numberArray)
                    {
                        numbers[index] = ((Number) item).shortValue();
                        index++;
                    }
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofInt16Array(numbers);
                }
            } else if (OpcUaDataType.Int64.getTypeId() == dataType) {
                if (!isArray) {
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofInt64(((Number)val).longValue());
                } else {
                    List<?> numberArray = (List<?>)val;
                    Long[] numbers = new Long[numberArray.size()];

                    int index = 0;
                    for (Object item : numberArray)
                    {
                        numbers[index] = ((Number) item).longValue();
                        index++;
                    }
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofInt64Array(numbers);
                }
            } else if (OpcUaDataType.SByte.getTypeId() == dataType) {
                if (!isArray) {
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofSByte(((Number) val).byteValue());
                } else {
                    List<?> numberArray = (List<?>)val;
                    Byte[] numbers = new Byte[numberArray.size()];

                    int index = 0;
                    for (Object item : numberArray)
                    {
                        numbers[index] = ((Number) item).byteValue();
                        index++;
                    }
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofSByteArray(numbers);
                }
            } else if (OpcUaDataType.UInt32.getTypeId() == dataType) {
                if (!isArray) {
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofUInt32(UInteger.valueOf(((Number)val).longValue()));
                } else {
                    List<?> numberArray = (List<?>)val;
                    UInteger[] numbers = new UInteger[numberArray.size()];

                    int index = 0;
                    for (Object item : numberArray)
                    {
                        numbers[index] = UInteger.valueOf(((Number)item).longValue());
                        index++;
                    }
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofUInt32Array(numbers);
                }
            } else if (OpcUaDataType.UInt16.getTypeId() == dataType) {
                if (!isArray) {
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofUInt16(UShort.valueOf(((Number)val).intValue()));
                } else {
                    List<?> numberArray = (List<?>)val;
                    UShort[] numbers = new UShort[numberArray.size()];

                    int index = 0;
                    for (Object item : numberArray)
                    {
                        numbers[index] = UShort.valueOf(((Number)item).intValue());
                        index++;
                    }
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofUInt16Array(numbers);
                }
            } else if (OpcUaDataType.UInt64.getTypeId() == dataType) {
                if (!isArray) {
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofUInt64(ULong.valueOf(((Number)val).longValue()));
                } else {
                    List<?> numberArray = (List<?>)val;
                    ULong[] numbers = new ULong[numberArray.size()];

                    int index = 0;
                    for (Object item : numberArray)
                    {
                        numbers[index] = ULong.valueOf(((Number)item).longValue());
                        index++;
                    }
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofUInt64Array(numbers);
                }
            } else if (OpcUaDataType.Byte.getTypeId() == dataType) {
                if (!isArray) {
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofByte(UByte.valueOf(((Number)val).shortValue()));
                } else {
                    List<?> numberArray = (List<?>)val;
                    UByte[] numbers = new UByte[numberArray.size()];

                    int index = 0;
                    for (Object item : numberArray)
                    {
                        numbers[index] = UByte.valueOf(((Number)item).shortValue());
                        index++;
                    }
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofByteArray(numbers);
                }
            } else if (OpcUaDataType.NodeId.getTypeId() == dataType) {
                if (!isArray) {
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofNodeId(NodeId.parse((String) val));
                } else {
                    List<?> stringArray = (List<?>)val;
                    NodeId[] nodeIds = new NodeId[stringArray.size()];

                    int index = 0;
                    for (Object item : stringArray)
                    {
                        nodeIds[index] = NodeId.parse((String) item);
                        index++;
                    }

                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofNodeIdArray(nodeIds);
                }
            } else if (OpcUaDataType.ExpandedNodeId.getTypeId() == dataType) {
                if (!isArray) {
                    String stringVal = (String) val;
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofExpandedNodeId(ExpandedNodeId.parse(stringVal));
                } else {
                    List<?> stringArray = (List<?>)val;
                    ExpandedNodeId[] nodeIds = new ExpandedNodeId[stringArray.size()];

                    int index = 0;
                    for (Object item : stringArray)
                    {
                        nodeIds[index] = ExpandedNodeId.parse((String) item);
                        index++;
                    }
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofExpandedNodeIdArray(nodeIds);
                }
            } else if (OpcUaDataType.DateTime.getTypeId() == dataType) {
                if (!isArray) {
                    String stringVal = (String)val;
                    DateTime dateTime = new DateTime(Instant.parse(stringVal));
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofDateTime(dateTime);
                } else {
                    List<?> stringArray = (List<?>)val;
                    DateTime[] dateTimes = new DateTime[stringArray.size()];

                    int index = 0;
                    for (Object item : stringArray)
                    {
                        dateTimes[index] = new DateTime(Instant.parse((String) item));
                        index++;
                    }
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofDateTimeArray(dateTimes);
                }
            } else if (OpcUaDataType.QualifiedName.getTypeId() == dataType) {
                if (!isArray) {
                    QualifiedName qNameVal = new QualifiedName(0,(String) val);
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofQualifiedName(qNameVal);
                } else {
                    List<?> stringArray = (List<?>)val;
                    QualifiedName[] qNames = new QualifiedName[stringArray.size()];

                    int index = 0;
                    for (Object item : stringArray)
                    {
                        qNames[index] = new QualifiedName(0,(String)item);
                        index++;
                    }

                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofQualifiedNameArray(qNames);
                }
            } else if (OpcUaDataType.ByteString.getTypeId() == dataType) {
                if (!isArray) {
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofByteString(ByteString.of(((String)val).getBytes()));
                } else {
                    List<?> stringArray = (List<?>)val;
                    ByteString[] byteStrings = new ByteString[stringArray.size()];

                    int index = 0;
                    for (Object item : stringArray)
                    {
                        byteStrings[index] = ByteString.of(((String)item).getBytes());
                        index++;
                    }

                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofByteStringArray(byteStrings);
                }
            } else if (OpcUaDataType.Guid.getTypeId() == dataType) {
                if (!isArray) {
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofGuid(UUID.fromString((String)val));
                } else {
                    List<?> stringArray = (List<?>)val;
                    UUID[] guids = new UUID[stringArray.size()];

                    int index = 0;
                    for (Object item : stringArray)
                    {
                        guids[index] = UUID.fromString((String)item);
                        index++;
                    }

                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofGuidArray(guids);
                }
            } else if (OpcUaDataType.LocalizedText.getTypeId() == dataType) {
                ObjectMapper mapper = new ObjectMapper();
                if (!isArray)
                {
                    LocalizedText localizedText = mapper.convertValue(val, LocalizedText.class);
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofLocalizedText(
                            new org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText(localizedText.getLocale(), localizedText.getText()));
                } else {
                    List<?> objectArray = (List<?>)val;
                    org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText[] localizedTexts =
                            new org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText[objectArray.size()];

                    int index = 0;
                    for (Object item : objectArray)
                    {
                        LocalizedText localizedText = mapper.convertValue(item, LocalizedText.class);
                        localizedTexts[index] = new org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText(
                                localizedText.getLocale(), localizedText.getText());
                        index++;
                    }

                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofLocalizedTextArray(localizedTexts);
                }
            } else if (OpcUaDataType.StatusCode.getTypeId() == dataType) {
                ObjectMapper mapper = new ObjectMapper();
                if (!isArray)
                {
                    StatusCode statusCode = mapper.convertValue(val, StatusCode.class);
                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofStatusCode(statusCodeFromWebApi(statusCode));
                } else {
                    List<?> objectArray = (List<?>)val;
                    org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode[] statusCodes =
                            new org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode[objectArray.size()];

                    int index = 0;
                    for (Object item : objectArray)
                    {
                        StatusCode statusCode = mapper.convertValue(item, StatusCode.class);
                        statusCodes[index] = statusCodeFromWebApi(statusCode);
                        index++;
                    }

                    ret = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.ofStatusCodeArray(statusCodes);
                }
            } else if (OpcUaDataType.ExtensionObject.getTypeId() == dataType) {
                throw new UaRuntimeException(StatusCodes.Bad_DecodingError);
            }
        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_DecodingError);
        }

        return ret;
    }

    public static List<Variant> variantsFromMilo(List<org.eclipse.milo.opcua.stack.core.types.builtin.Variant> values)
    {
        ArrayList<Variant> results = new ArrayList<>();

        for (org.eclipse.milo.opcua.stack.core.types.builtin.Variant item: values)
        {
            results.add(variantFromMilo(item));
        }

        return results;
    }

    public static List<org.eclipse.milo.opcua.stack.core.types.builtin.Variant> variantsFromWebApi(List<Variant> values) throws UaRuntimeException
    {
        ArrayList<org.eclipse.milo.opcua.stack.core.types.builtin.Variant> results = new ArrayList<>();

        for (Variant item : values)
        {
            results.add(variantFromWebApi(item));
        }

        return results;
    }

    public static DataValue dataValueFromMilo(org.eclipse.milo.opcua.stack.core.types.builtin.DataValue dataValue)
    {
        DataValue ret = new DataValue();

        Variant variantValue = variantFromMilo(dataValue.value());
        ret.setValue(variantValue.getValue());
        ret.setUaType(variantValue.getUaType());

        if (0 != dataValue.statusCode().getValue())
        {
            StatusCode statusCode = new StatusCode();
            statusCode.setCode(dataValue.statusCode().getValue());
            ret.setStatusCode(statusCode);
        }

        if (null != dataValue.getSourceTime() && !dataValue.getSourceTime().isNull())
        {
            OffsetDateTime timestamp = dateTimeFromMilo(dataValue.getSourceTime());
            ret.setSourceTimestamp(timestamp);
        }

        if (null != dataValue.getServerTime() && !dataValue.getServerTime().isNull())
        {
            OffsetDateTime timestamp = dateTimeFromMilo(dataValue.getServerTime());
            ret.setServerTimestamp(timestamp);
        }

        return ret;
    }

    public static org.eclipse.milo.opcua.stack.core.types.builtin.DataValue dataValueFromWebApi(DataValue dataValue) throws UaRuntimeException
    {
        Variant variantValue = new Variant();

        if (null != dataValue.getUaType())
        {
            variantValue.setUaType(dataValue.getUaType());
        }

        if (null != dataValue.getValue())
        {
            variantValue.setValue(dataValue.getValue());
        } else {
            variantValue.setUaType(0);
        }

        org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode statusCode = org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode.GOOD;
        if (null != dataValue.getStatusCode() && null != dataValue.getStatusCode().getCode())
        {
            statusCode = new org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode(dataValue.getStatusCode().getCode());
        }

        DateTime sourceTime = null;
        if (null != dataValue.getSourceTimestamp())
        {
            sourceTime = dateTimeFromWebApi(dataValue.getSourceTimestamp());
        }

        DateTime serverTime = null;
        if (null != dataValue.getServerTimestamp())
        {
            serverTime = dateTimeFromWebApi(dataValue.getServerTimestamp());
        }

        return new org.eclipse.milo.opcua.stack.core.types.builtin.DataValue(
                variantFromWebApi(variantValue),
                statusCode,
                sourceTime,
                serverTime);
    }

    public static List<DataValue> dataValuesFromMilo(List<org.eclipse.milo.opcua.stack.core.types.builtin.DataValue> dataValues)
    {
        ArrayList<DataValue> ret = new ArrayList<>();

        for (org.eclipse.milo.opcua.stack.core.types.builtin.DataValue item : dataValues)
        {
            ret.add(dataValueFromMilo(item));
        }

        return ret;
    }
}
