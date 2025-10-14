package org.opcfoundation.webserver.types;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webserver.addressspace.models.UaDataObjectType;
import org.opcfoundation.webserver.addressspace.models.UaMasterObjectType;
import org.opcfoundation.webserver.addressspace.models.UaObjectType;
import org.opcfoundation.webserver.addressspace.nodes.UaReferenceType;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaReferenceTypes;

public class UaChildObjectDescriptor {
    private final String id;
    private final LocalizedText displayName;
    private final NodeId typeId;

    public UaChildObjectDescriptor(
            String id,
            LocalizedText displayName,
            UaObjectType objectType)
    {
        this.id = id;
        this.displayName = (displayName.isNull()) ? new LocalizedText(id) : displayName;
        this.typeId = objectType.nodeId();
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
