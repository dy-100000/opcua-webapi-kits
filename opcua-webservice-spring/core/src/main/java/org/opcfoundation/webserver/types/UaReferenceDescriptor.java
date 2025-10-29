package org.opcfoundation.webserver.types;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.webserver.addressspace.nodes.UaInstanceNode;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.addressspace.nodes.UaVariable;

public class UaReferenceDescriptor {
    private final String id;
    private final NodeClass nodeClass;
    private final String browseName;
    private final LocalizedText displayName;
    private final NodeId typeDefinitionId;
    private final boolean isForward;
    private final NodeId referenceTypeId;
    private final NodeId instanceDeclarationId;

    public UaReferenceDescriptor(
            String id,
            NodeClass nodeClass,
            String browseName,
            LocalizedText displayName,
            NodeId typeDefinitionId,
            NodeId referenceTypeId,
            boolean isForward)
    {
        this.id = id;
        this.nodeClass = nodeClass;
        this.browseName = browseName;
        this.displayName = displayName;
        this.typeDefinitionId = typeDefinitionId;
        this.referenceTypeId = referenceTypeId;
        this.isForward = isForward;
        this.instanceDeclarationId = NodeId.NULL_VALUE;
    }

    public UaReferenceDescriptor(
            String id,
            UaInstanceNode instanceDeclaration,
            NodeId referenceTypeId,
            boolean isForward)
    {
        this.id = id;
        this.nodeClass = instanceDeclaration.nodeClass();
        this.browseName = instanceDeclaration.browseName();
        this.displayName = instanceDeclaration.displayName();
        this.referenceTypeId = referenceTypeId;
        this.isForward = isForward;
        this.instanceDeclarationId = instanceDeclaration.nodeId();

        if (NodeClass.Object == instanceDeclaration.nodeClass())
        {
            typeDefinitionId = ((UaObject)instanceDeclaration).typeDefinition().nodeId();
        } else if (NodeClass.Variable == instanceDeclaration.nodeClass()) {
            typeDefinitionId = ((UaVariable)instanceDeclaration).typeDefinition().nodeId();
        } else {
            typeDefinitionId = NodeId.NULL_VALUE;
        }
    }

    public String getId() { return id; }

    public NodeClass getNodeClass() {
        return nodeClass;
    }

    public String getBrowseName() {
        return browseName;
    }

    public LocalizedText getDisplayName() {
        return displayName;
    }

    public NodeId getTypeDefinitionId() {
        return typeDefinitionId;
    }

    public NodeId getReferenceTypeId() {
        return referenceTypeId;
    }

    public boolean isForward() { return isForward; }

    public NodeId getInstanceDeclarationId() {
        return instanceDeclarationId;
    }
}
