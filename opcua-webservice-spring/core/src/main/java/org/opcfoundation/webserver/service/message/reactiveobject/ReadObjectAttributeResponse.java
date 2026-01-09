package org.opcfoundation.webserver.service.message.reactiveobject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;

public class ReadObjectAttributeResponse {
    private final String browseName;
    private final LocalizedText displayName;
    private final LocalizedText description;
    private UByte eventNotifier;
    // To add writeMask

    public ReadObjectAttributeResponse(
            String browseName,
            LocalizedText displayName,
            LocalizedText description)
    {
        this.browseName = browseName;
        this.displayName = displayName;
        this.description = description;
        eventNotifier = UByte.valueOf(0);
    }

    public ReadObjectAttributeResponse(
            String browseName,
            LocalizedText displayName,
            LocalizedText description,
            UByte eventNotifier)
    {
        this.browseName = browseName;
        this.displayName = displayName;
        this.description = description;
        this.eventNotifier = eventNotifier;
    }

    public String getBrowseName() {
        return browseName;
    }

    public LocalizedText getDisplayName() {
        return displayName;
    }

    public LocalizedText getDescription()
    {
        return description;
    }

    public UByte getEventNotifier() {
        return eventNotifier;
    }
}
