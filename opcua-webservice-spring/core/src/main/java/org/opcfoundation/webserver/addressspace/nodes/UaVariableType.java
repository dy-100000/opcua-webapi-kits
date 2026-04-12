package org.opcfoundation.webserver.addressspace.nodes;

import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.springframework.lang.Nullable;

public class UaVariableType extends UaDefinitionNode {
    private final NodeId dataType;
    private final int valueRank;
    private Variant value;

    public UaVariableType(
            NodeId nodeId,
            String browseName,
            LocalizedText displayName,
            boolean isAbstract,
            NodeId dataType,
            int valueRank)
    {
        super(nodeId,browseName,displayName,isAbstract);
        this.dataType = dataType;
        this.valueRank = valueRank;
    }

    @Override
    public NodeClass nodeClass() {
        return NodeClass.VariableType;
    }

    public NodeId dataType() {
        return dataType;
    }

    public int valueRank() {
        return valueRank;
    }

    public Variant value() {
        return value;
    }

    public void setValue(Variant value)
    {
        this.value = value;
    }

    public void addMember(UaVariable member)
    {
        addMemberNode(member);
    }

    @Override
    public Variant getAttribute(int attributeId)
    {
        if (AttributeId.Value.id() == attributeId)
        {
            return value;
        } else if (AttributeId.DataType.id() == attributeId) {
            return new Variant(dataType);
        } else if (AttributeId.ValueRank.id() == attributeId) {
            return new Variant(valueRank);
        }

        return super.getAttribute(attributeId);
    }
}
