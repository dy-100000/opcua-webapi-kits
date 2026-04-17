package org.opcfoundation.webserver.service.message.digitaltwin;
import org.opcfoundation.webserver.digitaltwin.event.EventData;

import java.util.ArrayList;
import java.util.List;

public class ReadEventsResponse {
    List<EventData> eventsData;
    boolean containsMoreData;

    public ReadEventsResponse()
    {
        eventsData = new ArrayList<>();
        containsMoreData = false;
    }

    public void addEventData(EventData eventData)
    {
        eventsData.add(eventData);
    }

    public List<EventData> getEventsData() {
        return eventsData;
    }

    public void setContainsMoreData(boolean containsMoreData) {
        this.containsMoreData = containsMoreData;
    }

    public boolean containsMoreData() {
        return containsMoreData;
    }
}
