package org.opcfoundation.webserver.types.message;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

public class ReadObjectAttributeResponse {
    private final LocalizedText displayName;
    private final LocalizedText description;
    // To add writeMask

    public ReadObjectAttributeResponse(
            LocalizedText displayName,
            LocalizedText description)
    {
        this.displayName = displayName;
        this.description = description;
    }

    public LocalizedText getDisplayName() {
        return displayName;
    }

    public LocalizedText getDescription()
    {
        return description;
    }
}
