package org.opcfoundation.webserver.service.message.reactiveobject;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

public class ReadObjectAttributeResponse {
    private final String browseName;
    private final LocalizedText displayName;
    private final LocalizedText description;
    // To add writeMask

    public ReadObjectAttributeResponse(
            String browseName,
            LocalizedText displayName,
            LocalizedText description)
    {
        this.browseName = browseName;
        this.displayName = displayName;
        this.description = description;
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
}
