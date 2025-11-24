package org.opcfoundation.webserver.types.message.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

public class GetDescriptorResponse {
    private final LocalizedText displayName;
    private final LocalizedText description;

    public GetDescriptorResponse(String displayName)
    {
        this.displayName = new LocalizedText(displayName);
        this.description = LocalizedText.NULL_VALUE;
    }

    public GetDescriptorResponse(LocalizedText displayName, LocalizedText description)
    {
        this.displayName = displayName;
        this.description = description;
    }

    public LocalizedText getDisplayName() {
        return displayName;
    }

    public LocalizedText getDescription() {
        return description;
    }
}
