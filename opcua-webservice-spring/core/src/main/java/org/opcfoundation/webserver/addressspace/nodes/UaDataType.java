package org.opcfoundation.webserver.addressspace.nodes;

import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumValueType;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;
import org.opcfoundation.webserver.types.common.UaStructureUtilities;

public class UaDataType extends UaDefinitionNode {
    private @Nullable UaVariable enumVariable;

    public UaDataType(
            NodeId nodeId,
            String browseName,
            LocalizedText displayName,
            boolean isAbstract,
            @Nullable UaDataType parentType)
    {
        super(nodeId,browseName,displayName,isAbstract);
        if (null != parentType) setParentType(parentType);
        enumVariable = null;
    }

    @Override
    public NodeClass nodeClass() {
        return NodeClass.DataType;
    }

    public boolean isEnumDataType()
    {
        return null != enumVariable;
    }

    public UaVariable setEnumStrings(
            NodeId nodeId,
            LocalizedText[] enumStrings)
    {
        if (!isSubtypeOf(NodeIds.Enumeration) || null != enumVariable) throw new UaRuntimeException(StatusCodes.Bad_TypeDefinitionInvalid);
        enumVariable = new UaVariable(
                nodeId,
                "EnumStrings",
                new LocalizedText("EnumStrings"),
                NodeIds.LocalizedText,
                ValueRank.OneDimension.getValue(),
                AccessLevel.CurrentRead.getValue(),
                UaVariableTypes.PropertyType);

        enumVariable.setValue(new Variant(enumStrings));
        addMemberNode(enumVariable);
        return enumVariable;
    }

    public UaVariable setEnumValues(
            NodeId nodeId,
            EnumValueType[] enumValues)
    {
        if (!isSubtypeOf(NodeIds.Enumeration) || null != enumVariable) throw new UaRuntimeException(StatusCodes.Bad_TypeDefinitionInvalid);
        enumVariable = new UaVariable(
                nodeId,
                "EnumValues",
                new LocalizedText("EnumValues"),
                NodeIds.EnumValueType,
                ValueRank.OneDimension.getValue(),
                AccessLevel.CurrentRead.getValue(),
                UaVariableTypes.PropertyType);

        enumVariable.setValue(UaStructureUtilities.toVariant(enumValues));
        addMemberNode(enumVariable);
        return enumVariable;
    }
}
