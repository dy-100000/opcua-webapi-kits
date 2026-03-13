package org.opcfoundation.webserver.addressspace.nodes;

import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.springframework.lang.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaReferenceTypes;

import java.util.List;

public class UaObject extends UaInstanceNode {
    private final UaObjectType typeDefinition;

    public UaObject(
            NodeId nodeId,
            String browseName,
            LocalizedText displayName,
            UaObjectType typeDefinition)
    {
        super(nodeId, browseName, displayName);

        this.typeDefinition = typeDefinition;
        this.addReference(new UaReference(typeDefinition, UaReferenceTypes.HasTypeDefinition, true));
    }

    @Override
    public NodeClass nodeClass() {
        return NodeClass.Object;
    }

    public UaObjectType typeDefinition() {
        return typeDefinition;
    }

    public @Nullable UaNode parentNode()
    {
        List<UaReference> referenceList = getReferences(BrowseDirection.Inverse);

        for (UaReference item: referenceList)
        {
            if (item.reference().nodeId().equals(NodeIds.HasComponent))
            {
                return item.linkedNode();
            };
        }

        return null;
    }

    public void addMember(UaInstanceNode member)
    {
        addMemberNode(member);
    }

    public void organizes(UaNode node)
    {
        this.addReference(new UaReference(node, UaReferenceTypes.Organizes, true));
        node.addReference(new UaReference(this,UaReferenceTypes.Organizes, false));
    }

    @Override
    public Variant getAttribute(int attributeId)
    {
        if (AttributeId.EventNotifier.id() == attributeId)
        {
            return new Variant(UByte.valueOf(0));
        }

        return super.getAttribute(attributeId);
    }
}
