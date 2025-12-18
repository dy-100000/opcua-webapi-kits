package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ExtensionObjectEncoder {
    public final static ExtensionObjectEncoder Encoder = new ExtensionObjectEncoder();

    private final Map<String, StructureMapper> mappers;
    private final DefaultEncodingContext encodingContext;

    public ExtensionObjectEncoder()
    {
        mappers = new HashMap<>();
        encodingContext = new DefaultEncodingContext();

        add(new RangeMapper());
        add(new EUInformationMapper());
        add(new ArgumentMapper());
        add(new EnumValueTypeMapper());
    }

    public DefaultEncodingContext getEncodingContext() {
        return encodingContext;
    }

    public void add(StructureMapper mapper)
    {
        mappers.put(mapper.dataTypeId().toParseableString(),mapper);
    }

    @Nullable
    public ExtensionObject fromExtensionObjectWebApi(org.opcfoundation.webapi.model.ExtensionObject extensionObject)
    {
        try
        {
            if (null == extensionObject.getUaTypeId() || null == extensionObject.getUaBody()) return null;

            StructureMapper mapper = mappers.get(extensionObject.getUaTypeId());
            if (null == mapper) return null;

            UaStructuredType structure = mapper.toStructure(extensionObject.getUaBody());

            return ExtensionObject.encode(encodingContext, structure);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public org.opcfoundation.webapi.model.ExtensionObject toExtensionObjectWebApi(ExtensionObject extensionObject)
    {
        try
        {
            UaStructuredType structure = extensionObject.decode(encodingContext);
            String dataTypeId = structure.getTypeId().toParseableString();

            StructureMapper mapper = mappers.get(dataTypeId);
            if (null == mapper) return null;

            byte[] jsonString = mapper.toJson(structure);

            org.opcfoundation.webapi.model.ExtensionObject ret = new org.opcfoundation.webapi.model.ExtensionObject();
            ret.setUaTypeId(dataTypeId);
            ret.setUaBody(jsonString);
            return ret;
        } catch (Exception e) {
            return null;
        }
    }

}
