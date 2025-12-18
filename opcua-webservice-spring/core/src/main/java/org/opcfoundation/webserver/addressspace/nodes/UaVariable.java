package org.opcfoundation.webserver.addressspace.nodes;

import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.springframework.lang.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaReferenceTypes;

import java.util.List;
import java.util.Map;

public class UaVariable extends UaInstanceNode {
    private final UaVariableType typeDefinition;
    private final NodeId dataType;
    private int valueRank;
    private int accessLevel;
    private boolean historizing;
    private Variant value;

    public UaVariable(
            NodeId nodeId,
            String browseName,
            LocalizedText displayName,
            NodeId dataType,
            int valueRank,
            int accessLevel,
            UaVariableType typeDefinition)
    {
        super(nodeId, browseName, displayName);

        this.typeDefinition = typeDefinition;
        this.addReference(new UaReference(typeDefinition, UaReferenceTypes.HasTypeDefinition, true));

        this.dataType = dataType;
        this.valueRank = valueRank;
        this.accessLevel = accessLevel;
        this.value = Variant.NULL_VALUE;
    }

    @Override
    public NodeClass nodeClass() {
        return NodeClass.Variable;
    }

    public UaVariableType typeDefinition() {
        return typeDefinition;
    }

    public NodeId dataType() {
        return dataType;
    }

    public int valueRank() {
        return valueRank;
    }

    public void setValueRank(int valueRank) {
        this.valueRank = valueRank;
    }

    public int accessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public boolean historizing() { return historizing; }

    public void setHistorizing(boolean historizing) { this.historizing = historizing; }

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

    public @Nullable UaVariable addMember(String name)
    {
        UaInstanceNode member = typeDefinition.getMember(name);
        if (null == member || NodeClass.Variable != member.nodeClass()) return null;

        UaVariable memberVariable = (UaVariable)member;

        String newMemberIdValue = nodeId().getIdentifier().toString();
        newMemberIdValue += "-";
        newMemberIdValue += memberVariable.browseName();

        UaVariable newVariable = new UaVariable(
                new NodeId(nodeId().getNamespaceIndex(),newMemberIdValue),
                memberVariable.browseName(),
                memberVariable.displayName(),
                memberVariable.dataType(),
                memberVariable.valueRank(),
                AccessLevel.CurrentRead.getValue(),
                memberVariable.typeDefinition());

        addMemberNode(newVariable);

        return newVariable;
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
        } else if (AttributeId.AccessLevel.id() == attributeId) {
            return new Variant(UByte.valueOf(accessLevel));
        } else if (AttributeId.UserAccessLevel.id() == attributeId) {
            return new Variant(UByte.valueOf(accessLevel));
        } else if (AttributeId.MinimumSamplingInterval.id() == attributeId) {
            return new Variant((double)0);
        } else if (AttributeId.Historizing.id() == attributeId) {
            return new Variant(false);
        }

        return super.getAttribute(attributeId);
    }
}
