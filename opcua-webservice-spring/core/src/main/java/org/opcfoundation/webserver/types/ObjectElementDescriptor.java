package org.opcfoundation.webserver.types;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

import org.opcfoundation.webserver.digitaltwin.element.ElementType;

public class ObjectElementDescriptor {
    private final String id;
    private final LocalizedText displayName;
    private final NodeId typeId;

    public ObjectElementDescriptor(
            String elementId,
            LocalizedText displayName,
            ElementType elementType)
    {
        this.id = elementId;
        this.displayName = displayName;
        this.typeId = elementType.nodeId();
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
