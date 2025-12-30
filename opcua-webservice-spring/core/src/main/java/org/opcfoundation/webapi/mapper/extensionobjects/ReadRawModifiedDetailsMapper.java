package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.ReadRawModifiedDetails;

public class ReadRawModifiedDetailsMapper implements StructureMapper {
    public NodeId dataTypeId()
    {
        return NodeIds.ReadRawModifiedDetails;
    }

    @Override
    public UaStructuredType toStructure(byte[] json) throws Exception
    {
        ReadRawModifiedDetails details = OBJECT_MAPPER.readValue(json, ReadRawModifiedDetails.class);

        DateTime startTime = UaTypeMapper.dateTimeFromWebApi(details.getStartTime());
        DateTime endTime = UaTypeMapper.dateTimeFromWebApi(details.getEndTime());

        if (null == startTime || null == endTime) throw new UaRuntimeException(StatusCodes.Bad_DecodingError);

        return new org.eclipse.milo.opcua.stack.core.types.structured.ReadRawModifiedDetails(
                details.getIsReadModified(),
                startTime,
                endTime,
                UInteger.valueOf(details.getNumValuesPerNode()),
                details.getReturnBounds());
    }

    @Override
    public byte[] toJson(UaStructuredType struct) throws Exception
    {
        org.eclipse.milo.opcua.stack.core.types.structured.ReadRawModifiedDetails details = (org.eclipse.milo.opcua.stack.core.types.structured.ReadRawModifiedDetails) struct;

        ReadRawModifiedDetails detailsWebApi = new ReadRawModifiedDetails();
        detailsWebApi.setIsReadModified(details.getIsReadModified());
        detailsWebApi.setStartTime(UaTypeMapper.dateTimeFromMilo(details.getStartTime()));
        detailsWebApi.setEndTime(UaTypeMapper.dateTimeFromMilo(details.getEndTime()));
        detailsWebApi.setNumValuesPerNode(details.getNumValuesPerNode().longValue());
        detailsWebApi.setReturnBounds(details.getReturnBounds());

        return OBJECT_MAPPER.writeValueAsBytes(detailsWebApi);
    }
}
