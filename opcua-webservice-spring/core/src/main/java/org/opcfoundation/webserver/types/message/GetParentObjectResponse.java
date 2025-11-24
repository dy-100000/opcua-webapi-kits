package org.opcfoundation.webserver.types.message;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webserver.addressspace.models.UaObjectType;

@Deprecated
public class GetParentObjectResponse {
    private final String id;
    private final LocalizedText displayName;
    private final NodeId typeId;

    public GetParentObjectResponse()
    {
        id = "";
        displayName = LocalizedText.NULL_VALUE;
        typeId = NodeId.NULL_VALUE;
    }

    public GetParentObjectResponse(
            String id,
            LocalizedText displayName,
            NodeId typeDefinitionId)
    {
        this.id = id;
        this.displayName = (displayName.isNull()) ? new LocalizedText(id) : displayName;
        this.typeId = typeDefinitionId;
    }

    public GetParentObjectResponse(
            String id,
            LocalizedText displayName,
            UaObjectType objectType)
    {
        this.id = id;
        this.displayName = (displayName.isNull()) ? new LocalizedText(id) : displayName;
        this.typeId = objectType.nodeId();
    }

    public boolean isEmpty()
    {
        return id.isEmpty() || typeId.isNull();
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
