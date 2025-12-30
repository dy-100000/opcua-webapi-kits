package org.opcfoundation.webserver.service.message.reactiveobject;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList;

import java.util.List;

public class ReadHistoryEventResponse {
    private List<HistoryEventFieldList> events;
    private Boolean containsMoreData;

    public ReadHistoryEventResponse(List<HistoryEventFieldList> events, Boolean containsMoreData)
    {
        this.events = events;
        this.containsMoreData = containsMoreData;
    }

    public List<HistoryEventFieldList> getEvents() {
        return events;
    }

    public Boolean getContainsMoreData() {
        return containsMoreData;
    }
}
