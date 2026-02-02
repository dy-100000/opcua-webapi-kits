package org.opcfoundation.uawebservicedemo.models.employee.employeedata.event;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.uawebservicedemo.database.entity.EmployeeCheckIn;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.event.EventData;
import org.opcfoundation.webserver.digitaltwin.event.EventType;

import java.time.LocalDateTime;

public class EmployeeCheckInEventType extends EventType {
    public static final String CheckIn = "CheckIn";
    public static final String Location = "Location";

    public EmployeeCheckInEventType(DigitalTwinSpace space)
    {
        super("EmployeeCheckInEventType", new LocalizedText("EmployeeCheckInEventType"), space);
        addField(CheckIn, new LocalizedText(CheckIn), new LocalizedText("Is check in"), UaDataTypes.Boolean);
        addField(Location, new LocalizedText(Location), new LocalizedText("Location"), UaDataTypes.String);
    }

    public static EventData generateEventData(EmployeeCheckIn record)
    {
        EventData data = new EventData(record.getId().toString());
        data.setTime(record.getTime());
        data.setMessage(record.getRemark());
        data.setFieldData(CheckIn, Variant.ofBoolean(record.getCheckIn()));
        data.setFieldData(Location, Variant.ofString(record.getLocation()));

        return data;
    }
}
