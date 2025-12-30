package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.ReadEventDetails;

public class ReadEventDetailsMapper implements StructureMapper {
    public NodeId dataTypeId()
    {
        return NodeIds.ReadEventDetails2;
    }

    @Override
    public UaStructuredType toStructure(byte[] json) throws Exception
    {
        ReadEventDetails details = OBJECT_MAPPER.readValue(json, ReadEventDetails.class);

        DateTime startTime = UaTypeMapper.dateTimeFromWebApi(details.getStartTime());
        DateTime endTime = UaTypeMapper.dateTimeFromWebApi(details.getEndTime());

        if (null == startTime || null == endTime) throw new UaRuntimeException(StatusCodes.Bad_DecodingError);
        if (null == details.getFilter()) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

        return new org.eclipse.milo.opcua.stack.core.types.structured.ReadEventDetails(
                UInteger.valueOf(details.getNumValuesPerNode()),
                startTime,
                endTime,
                UaTypeMapper.eventFilterFromWebApi(details.getFilter()));
    }

    @Override
    public byte[] toJson(UaStructuredType struct) throws Exception
    {
        org.eclipse.milo.opcua.stack.core.types.structured.ReadEventDetails details = (org.eclipse.milo.opcua.stack.core.types.structured.ReadEventDetails) struct;

        ReadEventDetails detailsWebApi = new ReadEventDetails();
        detailsWebApi.setStartTime(UaTypeMapper.dateTimeFromMilo(details.getStartTime()));
        detailsWebApi.setEndTime(UaTypeMapper.dateTimeFromMilo(details.getEndTime()));
        detailsWebApi.setNumValuesPerNode(details.getNumValuesPerNode().longValue());
        detailsWebApi.setFilter(UaTypeMapper.eventFilterFromMilo(details.getFilter()));

        return OBJECT_MAPPER.writeValueAsBytes(detailsWebApi);
    }
}
