package org.opcfoundation.webserver.addressspace.nodes;

import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumValueType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;
import org.opcfoundation.webserver.types.common.UaStructureUtilities;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UaEnumDataType extends UaDataType {
    public UaEnumDataType(
            String dataTypeId,
            LocalizedText displayName,
            List<String> enumStrings,
            NodeManager nodeManager)
    {
        super(new NodeId(nodeManager.nsIndex(), dataTypeId),
                dataTypeId,
                displayName,
                false);

        setParentType(UaDataTypes.Enumeration);

        String enumStringsId = dataTypeId;
        enumStringsId += "-EnumStrings";

        UaVariable enumStringsVariable = setEnumStrings(
                new NodeId(nodeManager.nsIndex(), enumStringsId),
                enumStrings);

        nodeManager.addNode(this);
        nodeManager.addNode(enumStringsVariable);
    }

    public UaEnumDataType(
            String dataTypeId,
            LocalizedText displayName,
            EnumValueType[] enumValues,
            NodeManager nodeManager)
    {
        super(new NodeId(nodeManager.nsIndex(), dataTypeId),
                dataTypeId,
                displayName,
                false);

        setParentType(UaDataTypes.Enumeration);

        String enumValuesId = dataTypeId;
        enumValuesId += "-EnumValues";

        UaVariable enumValuesVariable = setEnumValues(
                new NodeId(nodeManager.nsIndex(), enumValuesId),
                Arrays.asList(enumValues));

        nodeManager.addNode(this);
        nodeManager.addNode(enumValuesVariable);
    }

    public @Nullable Integer parse(Object value)
    {
        return null;
    }

    private UaVariable setEnumStrings(
            NodeId nodeId,
            List<String> enumStrings)
    {
        UaVariable variable = new UaVariable(
                nodeId,
                "EnumStrings",
                new LocalizedText("EnumStrings"),
                NodeIds.LocalizedText,
                ValueRank.OneDimension.getValue(),
                AccessLevel.CurrentRead.getValue(),
                UaVariableTypes.PropertyType);

        LocalizedText[] enumString_LText = new LocalizedText[enumStrings.size()];
        for (int i = 0; i<enumStrings.size(); ++i)
        {
            enumString_LText[i] = new LocalizedText(enumStrings.get(i));
        }

        variable.setValue(new Variant(enumString_LText));
        addMemberNode(variable);
        return variable;
    }

    private UaVariable setEnumValues(
            NodeId nodeId,
            List<EnumValueType> enumValues)
    {
        UaVariable variable = new UaVariable(
                nodeId,
                "EnumValues",
                new LocalizedText("EnumValues"),
                NodeIds.EnumValueType,
                ValueRank.OneDimension.getValue(),
                AccessLevel.CurrentRead.getValue(),
                UaVariableTypes.PropertyType);

        List<UaStructuredType> structs = new ArrayList<>(enumValues);
        variable.setValue(UaStructureUtilities.toVariant(structs));

        addMemberNode(variable);
        return variable;
    }
}
