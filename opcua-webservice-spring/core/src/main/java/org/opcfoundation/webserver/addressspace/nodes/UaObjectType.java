package org.opcfoundation.webserver.addressspace.nodes;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.springframework.lang.Nullable;

public class UaObjectType extends UaDefinitionNode {
    public UaObjectType(
            NodeId nodeId,
            String browseName,
            LocalizedText displayName,
            boolean isAbstract,
            @Nullable UaObjectType parentType)
    {
        super(nodeId,browseName,displayName,isAbstract);
        if (null != parentType) setParentType(parentType);
    }

    @Override
    public NodeClass nodeClass() {
        return NodeClass.ObjectType;
    }

    public void addMember(UaInstanceNode member)
    {
        addMemberNode(member);
    }
}
