package org.opcfoundation.uawebservicetest.testdigitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.event.EventData;
import org.opcfoundation.webserver.digitaltwin.event.EventType;

import java.time.LocalDateTime;

public class EventTestType extends EventType {
    public static final String Customized = "Customized";

    public EventTestType(DigitalTwinSpace space)
    {
        super("EventTestType", new LocalizedText("EventTestType"), space);
        addField(Customized, new LocalizedText(Customized), new LocalizedText("Customized field"), UaDataTypes.String);
    }

    public static EventData generateEventData(
            String eventId,
            LocalDateTime time,
            String message,
            String customized)
    {
        EventData data = new EventData("eventId");
        data.setTime(time);
        data.setMessage(message);
        data.setFieldData(Customized, Variant.ofString(customized));

        return data;
    }
}
