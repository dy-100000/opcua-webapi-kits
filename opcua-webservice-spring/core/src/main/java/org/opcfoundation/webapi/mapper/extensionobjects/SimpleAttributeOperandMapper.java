package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.SimpleAttributeOperand;

public class SimpleAttributeOperandMapper implements StructureMapper {
    public NodeId dataTypeId()
    {
        return NodeIds.SimpleAttributeOperand;
    }

    @Override
    public UaStructuredType toStructure(byte[] json) throws Exception
    {
        SimpleAttributeOperand operand = OBJECT_MAPPER.readValue(json, SimpleAttributeOperand.class);
        return UaTypeMapper.simpleAttributeOperandFromWebApi(operand);
    }

    @Override
    public byte[] toJson(UaStructuredType struct) throws Exception
    {
        org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand operand = (org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand) struct;

        SimpleAttributeOperand operandWebApi = UaTypeMapper.simpleAttributeOperandFromMilo(operand);
        return OBJECT_MAPPER.writeValueAsBytes(operandWebApi);
    }
}
