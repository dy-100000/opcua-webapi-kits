package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.HistoryEvent;
import org.opcfoundation.webapi.model.HistoryEventFieldList;

import java.util.ArrayList;
import java.util.List;

public class HistoryEventDataMapper implements StructureMapper {
    public NodeId dataTypeId()
    {
        return NodeIds.HistoryEvent;
    }

    @Override
    public UaStructuredType toStructure(byte[] json) throws Exception
    {
        HistoryEvent event = OBJECT_MAPPER.readValue(json, HistoryEvent.class);

        List<org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList> events = new ArrayList<>();

        for (HistoryEventFieldList item : event.getEvents())
        {
            events.add(UaTypeMapper.historyEventFieldListFromWebApi(item));
        }

        return new org.eclipse.milo.opcua.stack.core.types.structured.HistoryEvent(events.toArray(new org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList[0]));
    }

    @Override
    public byte[] toJson(UaStructuredType struct) throws Exception
    {
        org.eclipse.milo.opcua.stack.core.types.structured.HistoryEvent event = (org.eclipse.milo.opcua.stack.core.types.structured.HistoryEvent) struct;

        List<HistoryEventFieldList> events = new ArrayList<>();

        if (null != event.getEvents())
        {
            for (org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList item: event.getEvents())
            {
                events.add(UaTypeMapper.historyEventFieldListFromMilo(item));
            }
        }

        HistoryEvent eventWebApi = new HistoryEvent();
        eventWebApi.setEvents(events);

        return OBJECT_MAPPER.writeValueAsBytes(eventWebApi);
    }
}
