package org.opcfoundation.webserver.addressspace.nodes;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumValueType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;

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
                false,
                UaDataTypes.Enumeration);

        String enumStringsId = dataTypeId;
        enumStringsId += "-EnumStrings";

        LocalizedText[] enumString_LText = new LocalizedText[enumStrings.size()];
        for (int i = 0; i<enumStrings.size(); ++i)
        {
            enumString_LText[i] = new LocalizedText(enumStrings.get(i));
        }

        UaVariable enumStringsVariable = setEnumStrings(
                new NodeId(nodeManager.nsIndex(), enumStringsId),
                enumString_LText);

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
                false,
                UaDataTypes.Enumeration);

        String enumValuesId = dataTypeId;
        enumValuesId += "-EnumValues";

        UaVariable enumValuesVariable = setEnumValues(
                new NodeId(nodeManager.nsIndex(), enumValuesId),
                Arrays.asList(enumValues));

        nodeManager.addNode(enumValuesVariable);
    }
}
