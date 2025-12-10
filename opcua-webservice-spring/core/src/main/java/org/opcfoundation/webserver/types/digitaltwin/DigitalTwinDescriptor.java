package org.opcfoundation.webserver.types.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webserver.digitaltwin.digitaltwin.DigitalTwinType;

public class DigitalTwinDescriptor {
    private final String id;
    private final LocalizedText displayName;
    private final NodeId typeId;

    public DigitalTwinDescriptor(
            String id,
            LocalizedText displayName,
            DigitalTwinType digitalTwinType)
    {
        this.id = id;
        this.displayName = displayName;
        this.typeId = digitalTwinType.nodeId();
    }

    public String getId() {
        return id;
    }

    public LocalizedText getDisplayName() {
        return displayName;
    }

    public NodeId getTypeId() {
        return typeId;
    }
}
