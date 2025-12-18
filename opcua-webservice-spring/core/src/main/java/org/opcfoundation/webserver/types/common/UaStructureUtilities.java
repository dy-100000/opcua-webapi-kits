package org.opcfoundation.webserver.types.common;

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webapi.mapper.extensionobjects.ExtensionObjectEncoder;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UaStructureUtilities {
    public static Variant toVariant(UaStructuredType structure)
    {
        try
        {
            ExtensionObject extensionObject = ExtensionObject.encode(ExtensionObjectEncoder.Encoder.getEncodingContext(), structure);
            return Variant.ofExtensionObject(extensionObject);
        } catch (Exception e) {
            return Variant.NULL_VALUE;
        }
    }

    public static Variant toVariant(List<UaStructuredType> structures)
    {
        try {
            ExtensionObject[] extensionObjects = new ExtensionObject[structures.size()];

            int index = 0;
            for (UaStructuredType item : structures) {
                extensionObjects[index] = ExtensionObject.encode(ExtensionObjectEncoder.Encoder.getEncodingContext(), item);
                index++;
            }

            return Variant.ofExtensionObjectArray(extensionObjects);
        } catch (Exception e) {
            return Variant.NULL_VALUE;
        }
    }

    @Nullable
    public static UaStructuredType toStructure(Variant value)
    {
        try
        {
            if (null == value.getValue() ||
                    value.getValue().getClass().isArray()) return null;

            if (value.getDataType().isEmpty() ||
                    !value.getDataType().get().equals(OpcUaDataType.ExtensionObject)) return null;

            ExtensionObject extensionObject = (ExtensionObject)value.getValue();
            return extensionObject.decode(ExtensionObjectEncoder.Encoder.getEncodingContext());
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static List<UaStructuredType> toStructureArray(Variant value)
    {
        try
        {
            if (null == value.getValue() ||
                    !value.getValue().getClass().isArray()) return null;

            if (value.getDataType().isEmpty() ||
                    !value.getDataType().get().equals(OpcUaDataType.ExtensionObject)) return null;

            ExtensionObject[] extensionObjects = (ExtensionObject[])value.getValue();
            List<UaStructuredType> ret = new ArrayList<>();

            for (ExtensionObject item : extensionObjects) {
                ret.add(item.decode(ExtensionObjectEncoder.Encoder.getEncodingContext()));
            }

            return ret;
        } catch (Exception e) {
            return null;
        }
    }
}
