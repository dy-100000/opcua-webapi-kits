package org.opcfoundation.webserver.addressspace.nodes;

import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.springframework.lang.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaReferenceTypes;

import java.util.ArrayList;
import java.util.List;

public abstract class UaNode {
    private final NodeId nodeId;
    protected String browseName;
    protected LocalizedText displayName;
    protected UInteger writeMask;
    protected LocalizedText description;
    protected List<UaReference> references;

    public UaNode(
            NodeId nodeId,
            String browseName,
            LocalizedText displayName)
    {
        this.nodeId = nodeId;
        this.browseName = browseName;
        this.displayName = displayName;
        this.writeMask = UInteger.valueOf(0);
        this.description = LocalizedText.NULL_VALUE;
        this.references = new ArrayList<>();
    }

    public NodeId nodeId() {
        return nodeId;
    }

    public abstract NodeClass nodeClass();

    public boolean isDefinitionNode()
    {
        return (NodeClass.ObjectType == nodeClass() ||
                NodeClass.VariableType == nodeClass() ||
                NodeClass.DataType == nodeClass() ||
                NodeClass.ReferenceType == nodeClass());
    }

    public boolean isInstanceNode()
    {
        return (NodeClass.Object == nodeClass() ||
                NodeClass.Variable == nodeClass() ||
                NodeClass.Method == nodeClass());
    }

    public String browseName() {
        return browseName;
    }

    public void browseName(String browseName) {
        this.browseName = browseName;
    }

    public LocalizedText displayName() {
        return displayName;
    }

    public void setDisplayName(LocalizedText displayName) {
        this.displayName = displayName;
    }

    public UInteger writeMask() {
        return writeMask;
    }

    public void setWriteMask(UInteger writeMask) {
        this.writeMask = writeMask;
    }

    public LocalizedText description() {
        return description;
    }

    public void setDescription(LocalizedText description) {
        this.description = description;
    }

    public Variant getAttribute(int attributeId)
    {
        Variant value = Variant.NULL_VALUE;

        if (AttributeId.NodeId.id() == attributeId) {
            value = new Variant(nodeId);
        } else if (AttributeId.NodeClass.id() == attributeId) {
            value = new Variant(nodeClass().getValue());
        } else if (AttributeId.BrowseName.id() == attributeId) {
            value = new Variant(new QualifiedName(0,browseName));
        } else if (AttributeId.DisplayName.id() == attributeId) {
            value = new Variant(displayName);
        } else if (AttributeId.Description.id() == attributeId) {
            if (null != description) value = new Variant(description);
        } else if (AttributeId.WriteMask.id() == attributeId) {
            value = new Variant(writeMask);
        } else if (AttributeId.UserWriteMask.id() == attributeId) {
            value = new Variant(0);
        }

        return value;
    }

    public List<UaReference> getReferences(BrowseDirection direction)
    {
        List<UaReference> ret = new ArrayList<>();
        if (null == this.references) return ret;

        for (UaReference item: references)
        {
            if ((BrowseDirection.Forward == direction && !item.isForward()) ||
                (BrowseDirection.Inverse == direction && item.isForward())) continue;

            ret.add(item);
        }

        return ret;
    }

    public List<UaInstanceNode> getMembers()
    {
        List<UaReference> referenceList = getReferences(BrowseDirection.Forward);
        ArrayList<UaInstanceNode> members = new ArrayList<>();

        for (UaReference item: referenceList)
        {
            if (!item.linkedNode().isInstanceNode()) continue;
            if (!item.reference().isSubtypeOf(NodeIds.Aggregates)) continue;
            members.add((UaInstanceNode)item.linkedNode());
        }

        return members;
    }

    public boolean hasMember()
    {
        for (UaReference item: references)
        {
            if (item.isForward() &&
                    item.linkedNode().isInstanceNode() &&
                    item.reference().isSubtypeOf(NodeIds.Aggregates)) return true;
        }

        return false;
    }

    public @Nullable UaInstanceNode getMember(String path)
    {
        for (UaReference item : references)
        {
            if (item.isForward() &&
                    item.linkedNode().browseName().equals(path) &&
                    item.linkedNode().isInstanceNode() &&
                    item.reference().isSubtypeOf(NodeIds.Aggregates)) return (UaInstanceNode)item.linkedNode();
        }

        return null;
    }

    protected void addReference(UaReference reference)
    {
        references.add(reference);
    }

    protected void addMemberNode(UaInstanceNode member)
    {
        UaReferenceType reference = UaReferenceTypes.HasComponent;

        if (NodeClass.Variable == member.nodeClass()) {
            if (((UaVariable)member).typeDefinition().nodeId().equals(NodeIds.PropertyType))
            {
                reference = UaReferenceTypes.HasProperty;
            }
        }

        addReference(new UaReference(member, reference, true));
        member.addReference(new UaReference(this,reference, false));
    }
}
