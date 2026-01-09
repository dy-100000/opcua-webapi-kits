package org.opcfoundation.uawebservicetest.testdigitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.event.EventType;

public class EventTestType extends EventType {
    public static final String Customized = "Customized";

    public EventTestType(DigitalTwinSpace space)
    {
        super("EventTestType", new LocalizedText("EventTestType"), space);
        addField(Customized, new LocalizedText(Customized), new LocalizedText("Customized field"), UaDataTypes.String);
    }
}
