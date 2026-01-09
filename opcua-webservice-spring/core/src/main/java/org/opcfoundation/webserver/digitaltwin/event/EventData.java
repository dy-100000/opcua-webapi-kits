package org.opcfoundation.webserver.digitaltwin.event;

import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

public class EventData {
    private final String eventId;
    private DateTime time;
    private String message;

    private final Map<String, Variant> eventData;

    public EventData(String eventId)
    {
        eventData = new HashMap<>();
        this.eventId = eventId;
        this.time = DateTime.NULL_VALUE;
        this.message = "";
    }

    public String getEventId() {
        return eventId;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = new DateTime(time.atZone(ZoneOffset.UTC).toInstant());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFieldData(String field, Variant data) {
        eventData.put(field, data);
    }

    public Map<String, Variant> getEventData() {
        return eventData;
    }
}
