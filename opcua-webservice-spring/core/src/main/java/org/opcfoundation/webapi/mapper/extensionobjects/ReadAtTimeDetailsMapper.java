package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.ReadAtTimeDetails;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReadAtTimeDetailsMapper implements StructureMapper {
    public NodeId dataTypeId()
    {
        return NodeIds.ReadAtTimeDetails;
    }

    @Override
    public UaStructuredType toStructure(byte[] json) throws Exception
    {
        ReadAtTimeDetails details = OBJECT_MAPPER.readValue(json, ReadAtTimeDetails.class);

        List<DateTime> reqTimes = new ArrayList<>();

        for (OffsetDateTime item : details.getReqTimes())
        {
            DateTime time = UaTypeMapper.dateTimeFromWebApi(item);
            if (null == time) throw new UaRuntimeException(StatusCodes.Bad_DecodingError);
            reqTimes.add(time);
        }

        return new org.eclipse.milo.opcua.stack.core.types.structured.ReadAtTimeDetails(
                reqTimes.toArray(new DateTime[0]),
                details.getUseSimpleBounds());
    }

    @Override
    public byte[] toJson(UaStructuredType struct) throws Exception
    {
        org.eclipse.milo.opcua.stack.core.types.structured.ReadAtTimeDetails details = (org.eclipse.milo.opcua.stack.core.types.structured.ReadAtTimeDetails) struct;

        List<OffsetDateTime> reqTimes = new ArrayList<>();

        if (null == details.getReqTimes())
        {
            for (DateTime item: details.getReqTimes())
            {
                reqTimes.add(UaTypeMapper.dateTimeFromMilo(item));
            }
        }

        ReadAtTimeDetails detailsWebApi = new ReadAtTimeDetails();
        detailsWebApi.setReqTimes(reqTimes);
        detailsWebApi.setUseSimpleBounds(details.getUseSimpleBounds());

        return OBJECT_MAPPER.writeValueAsBytes(detailsWebApi);
    }
}
