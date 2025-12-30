package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.Argument;

public class ArgumentMapper implements StructureMapper {
    public NodeId dataTypeId()
    {
        return NodeIds.Argument;
    }

    @Override
    public UaStructuredType toStructure(byte[] json) throws Exception
    {
        Argument argument = OBJECT_MAPPER.readValue(json, Argument.class);

        if (null == argument.getDataType()) throw new Exception();
        NodeId typeId = NodeId.parse(argument.getDataType());

        return new org.eclipse.milo.opcua.stack.core.types.structured.Argument(
                    argument.getName(),
                    typeId,
                    argument.getValueRank(),
                    null,
                    (null == argument.getDescription()) ? LocalizedText.NULL_VALUE : new LocalizedText(argument.getDescription().getLocale(), argument.getDescription().getText()));
    }

    @Override
    public byte[] toJson(UaStructuredType struct) throws Exception
    {
        org.eclipse.milo.opcua.stack.core.types.structured.Argument argument = (org.eclipse.milo.opcua.stack.core.types.structured.Argument) struct;

        Argument argumentWebApi = new Argument();
        argumentWebApi.setName(argument.getName());
        argumentWebApi.setDataType(argument.getDataType().toParseableString());
        argumentWebApi.setValueRank(argument.getValueRank());
        argumentWebApi.setDescription(UaTypeMapper.localizedTextFromMilo(argument.getDescription()));

        return OBJECT_MAPPER.writeValueAsBytes(argumentWebApi);
    }
}
