package org.opcfoundation.webserver.types;

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webapi.mapper.UaEncoder;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UaStructureUtilities {
    public static Variant toVariant(UaStructuredType structureData)
    {
        return Variant.ofExtensionObject(ExtensionObject.encode(UaEncoder.defaultEncoder, structureData));
    }

    public static Variant toVariant(UaStructuredType[] values)
    {
        return Variant.ofExtensionObjectArray(ExtensionObject.encodeArray(UaEncoder.defaultEncoder, values));
    }

    @Nullable
    public static UaStructuredType toStructure(Variant value)
    {
        if (value.isNull() || value.getValue().getClass().isArray() ||
                value.getDataType().isEmpty() || !value.getDataType().equals(OpcUaDataType.ExtensionObject)) return null;

        try
        {
            ExtensionObject extensionObject = (ExtensionObject)value.getValue();
            return extensionObject.decode(UaEncoder.defaultEncoder);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static UaStructuredType[] toStructureArray(Variant value)
    {
        if (value.isNull() || !value.getValue().getClass().isArray() ||
                value.getDataType().isEmpty() || !value.getDataType().equals(OpcUaDataType.ExtensionObject)) return null;

        try
        {
            ExtensionObject[] extensionObjects = (ExtensionObject[])value.getValue();
            List<UaStructuredType> ret = new ArrayList<>();

            for (ExtensionObject item: extensionObjects)
            {
                ret.add(item.decode(UaEncoder.defaultEncoder));
            }

            return ret.toArray(ret.toArray(new UaStructuredType[0]));
        } catch (Exception e) {
            return null;
        }
    }
}
