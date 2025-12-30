package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.EventFilter;
import org.opcfoundation.webapi.model.ReadEventDetails2;
import org.opcfoundation.webapi.model.ReadRawModifiedDetails;
import org.springframework.lang.Nullable;

public class ReadEventDetails2Mapper implements StructureMapper {
    public NodeId dataTypeId()
    {
        return NodeIds.ReadEventDetails2;
    }

    @Override
    public UaStructuredType toStructure(byte[] json) throws Exception
    {
        ReadEventDetails2 details = OBJECT_MAPPER.readValue(json, ReadEventDetails2.class);

        DateTime startTime = UaTypeMapper.dateTimeFromWebApi(details.getStartTime());
        DateTime endTime = UaTypeMapper.dateTimeFromWebApi(details.getEndTime());

        if (null == startTime || null == endTime) throw new UaRuntimeException(StatusCodes.Bad_DecodingError);
        if (null == details.getFilter()) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

        return new org.eclipse.milo.opcua.stack.core.types.structured.ReadEventDetails2(
                UInteger.valueOf(details.getNumValuesPerNode()),
                startTime,
                endTime,
                UaTypeMapper.eventFilterFromWebApi(details.getFilter()),
                details.getReadModified());
    }

    @Override
    public byte[] toJson(UaStructuredType struct) throws Exception
    {
        org.eclipse.milo.opcua.stack.core.types.structured.ReadEventDetails2 details = (org.eclipse.milo.opcua.stack.core.types.structured.ReadEventDetails2) struct;

        ReadEventDetails2 detailsWebApi = new ReadEventDetails2();
        detailsWebApi.setReadModified(details.getReadModified());
        detailsWebApi.setStartTime(UaTypeMapper.dateTimeFromMilo(details.getStartTime()));
        detailsWebApi.setEndTime(UaTypeMapper.dateTimeFromMilo(details.getEndTime()));
        detailsWebApi.setNumValuesPerNode(details.getNumValuesPerNode().longValue());
        detailsWebApi.setFilter(UaTypeMapper.eventFilterFromMilo(details.getFilter()));

        return OBJECT_MAPPER.writeValueAsBytes(detailsWebApi);
    }
}
