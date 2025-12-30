package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.DataValue;
import org.opcfoundation.webapi.model.HistoryData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryDataMapper implements StructureMapper {
    public NodeId dataTypeId()
    {
        return NodeIds.HistoryData;
    }

    @Override
    public UaStructuredType toStructure(byte[] json) throws Exception
    {
        HistoryData data = OBJECT_MAPPER.readValue(json, HistoryData.class);
        List<org.eclipse.milo.opcua.stack.core.types.builtin.DataValue> dataValues = UaTypeMapper.dataValuesFromWebApi(data.getDataValues());

        return new org.eclipse.milo.opcua.stack.core.types.structured.HistoryData(dataValues.toArray(new org.eclipse.milo.opcua.stack.core.types.builtin.DataValue[0]));
    }

    @Override
    public byte[] toJson(UaStructuredType struct) throws Exception
    {
        org.eclipse.milo.opcua.stack.core.types.structured.HistoryData data = (org.eclipse.milo.opcua.stack.core.types.structured.HistoryData) struct;

        List<DataValue> dataValues = new ArrayList<>();

        if (null != data.getDataValues())
        {
            dataValues = UaTypeMapper.dataValuesFromMilo(Arrays.asList(data.getDataValues()));
        }

        HistoryData dataWebApi = new HistoryData();
        dataWebApi.setDataValues(dataValues);

        return OBJECT_MAPPER.writeValueAsBytes(dataWebApi);
    }
}
