package org.opcfoundation.webserver.types.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.springframework.lang.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.digitaltwin.submodel.SubmodelTypeBase;

public class SubmodelDescriptor {
    private final String id;
    private final LocalizedText displayName;
    private final NodeId typeId;
    private final @Nullable UaObject instance;

    public SubmodelDescriptor(String id, UaObject submodel)
    {
        this.id = id;
        this.displayName = LocalizedText.NULL_VALUE;
        this.typeId = NodeId.NULL_VALUE;
        this.instance = submodel;
    }

    public SubmodelDescriptor(String id, LocalizedText displayName, SubmodelTypeBase submodelType)
    {
        this.id = id;
        this.displayName = displayName;
        this.typeId = submodelType.nodeId();
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

    public @Nullable UaObject getInstance() {
        return instance;
    }
}
