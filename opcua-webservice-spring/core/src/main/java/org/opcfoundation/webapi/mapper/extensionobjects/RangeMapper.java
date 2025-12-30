package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.springframework.lang.Nullable;
import org.opcfoundation.webapi.model.Range;

public class RangeMapper implements StructureMapper {
    public NodeId dataTypeId()
    {
        return NodeIds.Range;
    }

    @Override
    public UaStructuredType toStructure(byte[] json) throws Exception
    {
        Range range = OBJECT_MAPPER.readValue(json, Range.class);
        return new org.eclipse.milo.opcua.stack.core.types.structured.Range(range.getLow(), range.getHigh());
    }

    @Override
    public byte[] toJson(UaStructuredType struct) throws Exception
    {
        org.eclipse.milo.opcua.stack.core.types.structured.Range range = (org.eclipse.milo.opcua.stack.core.types.structured.Range) struct;
        Range rangeWebApi = new Range();
        rangeWebApi.setLow(range.getLow());
        rangeWebApi.setHigh(range.getHigh());
        return OBJECT_MAPPER.writeValueAsBytes(rangeWebApi);
    }
}
