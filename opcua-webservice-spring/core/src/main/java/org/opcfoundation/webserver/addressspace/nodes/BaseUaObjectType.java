package org.opcfoundation.webserver.addressspace.nodes;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.springframework.lang.Nullable;

public class BaseUaObjectType extends UaDefinitionNode {
    public BaseUaObjectType(
            NodeId nodeId,
            String browseName,
            LocalizedText displayName,
            boolean isAbstract,
            @Nullable BaseUaObjectType parentType)
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
