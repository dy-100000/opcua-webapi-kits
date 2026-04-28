package org.opcfoundation.webserver.addressspace.nodes;

import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumValueType;
import org.springframework.lang.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;
import org.opcfoundation.webserver.types.common.UaStructureUtilities;

import java.util.ArrayList;
import java.util.List;

public class UaDataType extends UaDefinitionNode {
    private @Nullable OpcUaDataType valueType;

    public UaDataType(
            NodeId nodeId,
            String browseName,
            LocalizedText displayName,
            boolean isAbstract)
    {
        super(nodeId,browseName,displayName,isAbstract);
        valueType = null;
    }

    @Override
    public NodeClass nodeClass() {
        return NodeClass.DataType;
    }

    public OpcUaDataType getValueType()
    {
        if (null != valueType) return valueType;

        if (isSubtypeOf(NodeIds.Boolean))
        {
            valueType = OpcUaDataType.Boolean;
        } else if (isSubtypeOf(NodeIds.SByte)) {
            valueType = OpcUaDataType.SByte;
        } else if (isSubtypeOf(NodeIds.Int16)) {
            valueType = OpcUaDataType.Int16;
        } else if (isSubtypeOf(NodeIds.UInt16)) {
            valueType = OpcUaDataType.UInt16;
        } else if (isSubtypeOf(NodeIds.Int32)) {
            valueType = OpcUaDataType.Int32;
        } else if (isSubtypeOf(NodeIds.UInt32)) {
            valueType = OpcUaDataType.UInt32;
        } else if (isSubtypeOf(NodeIds.Int64)) {
            valueType = OpcUaDataType.Int64;
        } else if (isSubtypeOf(NodeIds.UInt64)) {
            valueType = OpcUaDataType.UInt64;
        } else if (isSubtypeOf(NodeIds.Float)) {
            valueType = OpcUaDataType.Float;
        } else if (isSubtypeOf(NodeIds.Double)) {
            valueType = OpcUaDataType.Double;
        } else if (isSubtypeOf(NodeIds.String)) {
            valueType = OpcUaDataType.String;
        } else if (isSubtypeOf(NodeIds.DateTime)) {
            valueType = OpcUaDataType.DateTime;
        } else if (isSubtypeOf(NodeIds.Guid)) {
            valueType = OpcUaDataType.Guid;
        } else if (isSubtypeOf(NodeIds.ByteString)) {
            valueType = OpcUaDataType.ByteString;
        } else if (isSubtypeOf(NodeIds.XmlElement)) {
            valueType = OpcUaDataType.XmlElement;
        } else if (isSubtypeOf(NodeIds.NodeId)) {
            valueType = OpcUaDataType.NodeId;
        } else if (isSubtypeOf(NodeIds.ExpandedNodeId)) {
            valueType = OpcUaDataType.ExpandedNodeId;
        } else if (isSubtypeOf(NodeIds.StatusCode)) {
            valueType = OpcUaDataType.StatusCode;
        } else if (isSubtypeOf(NodeIds.QualifiedName)) {
            valueType = OpcUaDataType.QualifiedName;
        } else if (isSubtypeOf(NodeIds.LocalizedText)) {
            valueType = OpcUaDataType.LocalizedText;
        } else if (isSubtypeOf(NodeIds.DataValue)) {
            valueType = OpcUaDataType.DataValue;
        } else if (isSubtypeOf(NodeIds.DiagnosticInfo)) {
            valueType = OpcUaDataType.DiagnosticInfo;
        } else if (isSubtypeOf(NodeIds.Enumeration)) {
            valueType = OpcUaDataType.Int32;
        } else if (isSubtypeOf(NodeIds.Structure)) {
            valueType = OpcUaDataType.ExtensionObject;
        }

        return valueType;
    }
}
