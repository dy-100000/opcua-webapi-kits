package org.opcfoundation.webserver.addressspace.nodes;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.springframework.lang.Nullable;

public class UaReferenceType extends UaDefinitionNode {
    private final LocalizedText inverseName;
    private final boolean symmetric;

    public UaReferenceType(
            NodeId nodeId,
            String browseName,
            LocalizedText displayName,
            boolean isAbstract,
            LocalizedText inverseName,
            boolean symmetric)
    {
        super(nodeId,browseName,displayName,isAbstract);
        this.inverseName = inverseName;
        this.symmetric = symmetric;
    }

    @Override
    public NodeClass nodeClass() {
        return NodeClass.ReferenceType;
    }

    public LocalizedText inverseName() {
        return (symmetric) ? displayName : inverseName;
    }

    public boolean isSymmetric() {
        return symmetric;
    }

    public void setParentReferenceType(UaReferenceType referenceType)
    {
        setParentType(referenceType);
    }
}
