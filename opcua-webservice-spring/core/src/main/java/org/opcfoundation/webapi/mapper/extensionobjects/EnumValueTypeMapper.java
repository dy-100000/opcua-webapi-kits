package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.EnumValueType;

public class EnumValueTypeMapper implements StructureMapper {
    public NodeId dataTypeId()
    {
        return NodeIds.EnumValueType;
    }

    @Override
    public UaStructuredType toStructure(byte[] json) throws Exception
    {
        EnumValueType enumValueType = OBJECT_MAPPER.readValue(json, EnumValueType.class);

        return new org.eclipse.milo.opcua.stack.core.types.structured.EnumValueType(
                    enumValueType.getValue(),
                    (null == enumValueType.getDisplayName()) ? LocalizedText.NULL_VALUE : new LocalizedText(enumValueType.getDisplayName().getLocale(), enumValueType.getDisplayName().getText()),
                    (null == enumValueType.getDescription()) ? LocalizedText.NULL_VALUE : new LocalizedText(enumValueType.getDescription().getLocale(), enumValueType.getDescription().getText()));
    }

    @Override
    public byte[] toJson(UaStructuredType struct) throws Exception
    {
        org.eclipse.milo.opcua.stack.core.types.structured.EnumValueType enumValueType = (org.eclipse.milo.opcua.stack.core.types.structured.EnumValueType) struct;

        EnumValueType enumValueTypeWebApi = new EnumValueType();
        enumValueTypeWebApi.setValue(enumValueType.getValue());
        enumValueTypeWebApi.setDisplayName(UaTypeMapper.localizedTextFromMilo(enumValueType.getDisplayName()));
        enumValueTypeWebApi.setDescription(UaTypeMapper.localizedTextFromMilo(enumValueType.getDescription()));

        return OBJECT_MAPPER.writeValueAsBytes(enumValueTypeWebApi);
    }
}
