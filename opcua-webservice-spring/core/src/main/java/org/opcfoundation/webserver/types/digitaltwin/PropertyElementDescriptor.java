package org.opcfoundation.webserver.types.digitaltwin;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webserver.addressspace.nodes.UaVariableType;
import org.springframework.lang.Nullable;

public class PropertyElementDescriptor {
    private final String id;
    private final LocalizedText displayName;
    private final NodeId typeId;

    public PropertyElementDescriptor(
            String propertyId,
            LocalizedText displayName,
            @Nullable UaVariableType variableType)
    {
        this.id = propertyId;
        this.displayName = displayName;
        this.typeId = (null != variableType) ? variableType.nodeId() : NodeIds.BaseDataVariableType;
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
