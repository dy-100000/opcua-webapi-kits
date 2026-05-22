package org.opcfoundation.webserver.service.message.reactiveobject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.opcfoundation.webserver.types.common.UaObjectId;

public class ReadObjectAttributeResponse {
    private final String browseName;
    private final LocalizedText displayName;
    private final LocalizedText description;
    private final UByte eventNotifier;
    // To add writeMask

    public ReadObjectAttributeResponse(
            UaObjectId objectId,
            LocalizedText displayName,
            LocalizedText description)
    {
        this(objectId,displayName,description, UByte.valueOf(0));
    }

    public ReadObjectAttributeResponse(
            UaObjectId objectId,
            LocalizedText displayName,
            LocalizedText description,
            UByte eventNotifier)
    {
        this.browseName = (null == objectId.getInstance()) ? objectId.getId() : objectId.getInstance().browseName();
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
