package org.opcfoundation.webserver.types.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.springframework.lang.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.digitaltwin.digitaltwin.DigitalTwinType;
import org.opcfoundation.webserver.digitaltwin.element.ElementType;

public class ReferenceTargetDescriptor {
    private final String id;
    private final LocalizedText displayName;
    private final NodeId typeId;
    private @Nullable final UaObject instanceDeclaration;

    public ReferenceTargetDescriptor(
            String digitalTwinId,
            LocalizedText displayName,
            DigitalTwinType digitalTwinType)
    {
        this.id = digitalTwinId;
        this.displayName = displayName;
        this.typeId = digitalTwinType.nodeId();
        this.instanceDeclaration = null;
    }

    public ReferenceTargetDescriptor(
            String elementId,
            LocalizedText displayName,
            ElementType elementType)
    {
        this.id = elementId;
        this.displayName = displayName;
        this.typeId = elementType.nodeId();
        this.instanceDeclaration = null;
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

    public @Nullable UaObject getInstanceDeclaration() {
        return instanceDeclaration;
    }
}
