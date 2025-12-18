package org.opcfoundation.webserver.types.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.springframework.lang.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.UaInstanceNode;
import org.opcfoundation.webserver.digitaltwin.element.ElementType;

public class ElementDescriptor {
    private final String id;
    private final LocalizedText displayName;
    private final NodeId typeId;
    private final @Nullable UaInstanceNode instance;

    public ElementDescriptor(String id, UaInstanceNode submodel)
    {
        this.id = id;
        this.displayName = LocalizedText.NULL_VALUE;
        this.typeId = NodeId.NULL_VALUE;
        this.instance = submodel;
    }

    public ElementDescriptor(String id, LocalizedText displayName, ElementType elementType)
    {
        this.id = id;
        this.displayName = displayName;
        this.typeId = elementType.nodeId();
        this.instance = null;
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

    public @Nullable UaInstanceNode getInstance() {
        return instance;
    }
}
