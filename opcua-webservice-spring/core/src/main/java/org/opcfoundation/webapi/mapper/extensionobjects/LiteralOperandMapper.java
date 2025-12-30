package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.LiteralOperand;

public class LiteralOperandMapper implements StructureMapper {
    public NodeId dataTypeId()
    {
        return NodeIds.LiteralOperand;
    }

    @Override
    public UaStructuredType toStructure(byte[] json) throws Exception
    {
        LiteralOperand operand = OBJECT_MAPPER.readValue(json, LiteralOperand.class);

        if (null == operand.getValue()) throw new Exception();

        Variant variant = UaTypeMapper.variantFromWebApi(operand.getValue());
        return new org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand(variant);
    }

    @Override
    public byte[] toJson(UaStructuredType struct) throws Exception
    {
        org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand operand = (org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand) struct;

        org.opcfoundation.webapi.model.Variant variant = UaTypeMapper.variantFromMilo(operand.getValue());

        LiteralOperand operandWebApi = new LiteralOperand();
        operandWebApi.setValue(variant);

        return OBJECT_MAPPER.writeValueAsBytes(operandWebApi);
    }
}
