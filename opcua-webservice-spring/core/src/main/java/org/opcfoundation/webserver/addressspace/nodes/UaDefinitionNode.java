package org.opcfoundation.webserver.addressspace.nodes;

import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaReferenceTypes;
import org.springframework.lang.Nullable;

public abstract class UaDefinitionNode extends UaNode {
    private final boolean isAbstract;
    private @Nullable UaDefinitionNode parentType;

    public UaDefinitionNode(
            NodeId nodeId,
            String browseName,
            LocalizedText displayName,
            boolean isAbstract)
    {
        super(nodeId, browseName, displayName);
        this.isAbstract = isAbstract;
        parentType = null;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    @Nullable
    public UaDefinitionNode parentType() {
        return parentType;
    }

    public boolean isSubtypeOf(NodeId typeId)
    {
        if (typeId.equals(nodeId())) return true;
        if (null == parentType) return false;
        return parentType.isSubtypeOf(typeId);
    }

    public @Nullable UaDefinitionNode getParentType()
    {
        return parentType;
    }

    public void setParentType(UaDefinitionNode parentType) throws UaRuntimeException {
        if (nodeClass() != parentType.nodeClass()) throw new UaRuntimeException(StatusCodes.Bad_NodeClassInvalid);
        if (null != this.parentType) throw new UaRuntimeException(StatusCodes.Bad_AlreadyExists);

        this.parentType = parentType;
        this.addReference(new UaReference(parentType, UaReferenceTypes.HasSubType,false));
        parentType.addReference(new UaReference(this, UaReferenceTypes.HasSubType,true));
    }

    @Override
    public Variant getAttribute(int attributeId)
    {
        if (AttributeId.IsAbstract.id() == attributeId)
        {
            return new Variant(isAbstract);
        }

        return super.getAttribute(attributeId);
    }
}
