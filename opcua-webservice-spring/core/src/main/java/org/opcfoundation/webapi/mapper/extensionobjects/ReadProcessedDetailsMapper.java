package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.AggregateConfiguration;
import org.opcfoundation.webapi.model.ReadProcessedDetails;

import java.util.ArrayList;
import java.util.List;

public class ReadProcessedDetailsMapper implements StructureMapper {
    public NodeId dataTypeId()
    {
        return NodeIds.ReadProcessedDetails;
    }

    @Override
    public UaStructuredType toStructure(byte[] json) throws Exception
    {
        ReadProcessedDetails details = OBJECT_MAPPER.readValue(json, ReadProcessedDetails.class);

        DateTime startTime = UaTypeMapper.dateTimeFromWebApi(details.getStartTime());
        DateTime endTime = UaTypeMapper.dateTimeFromWebApi(details.getEndTime());

        if (null == startTime || null == endTime) throw new UaRuntimeException(StatusCodes.Bad_DecodingError);

        List<NodeId> aggregateTypes = new ArrayList<>();

        for (String item : details.getAggregateType())
        {
            NodeId typeId = NodeId.parse(item);
            aggregateTypes.add(typeId);
        }

        if (aggregateTypes.isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

        org.eclipse.milo.opcua.stack.core.types.structured.AggregateConfiguration aggregateConfiguration;

        if (null == details.getAggregateConfiguration())
        {
            aggregateConfiguration = new org.eclipse.milo.opcua.stack.core.types.structured.AggregateConfiguration(
                    true,
                    true,
                    UByte.valueOf(0),
                    UByte.valueOf(100),
                    false);
        } else {
            aggregateConfiguration = new org.eclipse.milo.opcua.stack.core.types.structured.AggregateConfiguration(
                    details.getAggregateConfiguration().getUseServerCapabilitiesDefaults(),
                    details.getAggregateConfiguration().getTreatUncertainAsBad(),
                    UByte.valueOf(details.getAggregateConfiguration().getPercentDataBad()),
                    UByte.valueOf(details.getAggregateConfiguration().getPercentDataGood()),
                    details.getAggregateConfiguration().getUseSlopedExtrapolation());
        }

        return new org.eclipse.milo.opcua.stack.core.types.structured.ReadProcessedDetails(
                startTime,
                endTime,
                details.getProcessingInterval(),
                aggregateTypes.toArray(new NodeId[0]),
                aggregateConfiguration);
    }

    @Override
    public byte[] toJson(UaStructuredType struct) throws Exception
    {
        org.eclipse.milo.opcua.stack.core.types.structured.ReadProcessedDetails details = (org.eclipse.milo.opcua.stack.core.types.structured.ReadProcessedDetails) struct;

        List<String> aggregateTypes = new ArrayList<>();

        if (null != details.getAggregateType())
        {
            for (NodeId item: details.getAggregateType())
            {
                aggregateTypes.add(item.toParseableString());
            }
        }

        AggregateConfiguration aggregateConfiguration = new AggregateConfiguration();
        aggregateConfiguration.useServerCapabilitiesDefaults(details.getAggregateConfiguration().getUseServerCapabilitiesDefaults());
        aggregateConfiguration.setTreatUncertainAsBad(details.getAggregateConfiguration().getTreatUncertainAsBad());
        aggregateConfiguration.setPercentDataBad(details.getAggregateConfiguration().getPercentDataBad().intValue());
        aggregateConfiguration.setPercentDataGood(details.getAggregateConfiguration().getPercentDataGood().intValue());
        aggregateConfiguration.useSlopedExtrapolation(details.getAggregateConfiguration().getUseSlopedExtrapolation());

        ReadProcessedDetails detailsWebApi = new ReadProcessedDetails();
        detailsWebApi.setStartTime(UaTypeMapper.dateTimeFromMilo(details.getStartTime()));
        detailsWebApi.setEndTime(UaTypeMapper.dateTimeFromMilo(details.getEndTime()));
        detailsWebApi.setProcessingInterval(details.getProcessingInterval());
        detailsWebApi.setAggregateType(aggregateTypes);
        detailsWebApi.setAggregateConfiguration(aggregateConfiguration);

        return OBJECT_MAPPER.writeValueAsBytes(detailsWebApi);
    }
}
