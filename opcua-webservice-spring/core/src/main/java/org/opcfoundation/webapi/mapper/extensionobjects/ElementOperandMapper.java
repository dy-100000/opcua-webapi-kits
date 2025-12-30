package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.opcfoundation.webapi.model.ElementOperand;

public class ElementOperandMapper implements StructureMapper {
    public NodeId dataTypeId()
    {
        return NodeIds.ElementOperand;
    }

    @Override
    public UaStructuredType toStructure(byte[] json) throws Exception
    {
        ElementOperand operand = OBJECT_MAPPER.readValue(json, ElementOperand.class);
        return new org.eclipse.milo.opcua.stack.core.types.structured.ElementOperand(UInteger.valueOf(operand.getIndex()));
    }

    @Override
    public byte[] toJson(UaStructuredType struct) throws Exception
    {
        org.eclipse.milo.opcua.stack.core.types.structured.ElementOperand operand = (org.eclipse.milo.opcua.stack.core.types.structured.ElementOperand) struct;

        ElementOperand operandWebApi = new ElementOperand();
        operandWebApi.setIndex(operand.getIndex().longValue());

        return OBJECT_MAPPER.writeValueAsBytes(operandWebApi);
    }
}
