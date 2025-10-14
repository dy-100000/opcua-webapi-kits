package org.opcfoundation.webapi.mapper.extensionobjects;

import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.EnumValueType;

public class EnumValueTypeWebApi extends EnumValueType implements ExtensionObjectWebApi {
    public EnumValueTypeWebApi(org.eclipse.milo.opcua.stack.core.types.structured.EnumValueType enumValue)
    {
        super();
        setValue(enumValue.getValue());
        setDisplayName(UaTypeMapper.localizedTextFromMilo(enumValue.getDisplayName()));
        setDescription(UaTypeMapper.localizedTextFromMilo(enumValue.getDescription()));
    }

    @Override
    public String getUaTypeId()
    {
        return "i=7594";
    }
}
