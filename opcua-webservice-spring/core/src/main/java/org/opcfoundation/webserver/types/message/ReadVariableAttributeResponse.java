package org.opcfoundation.webserver.types.message;

import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.UaDataType;

public class ReadVariableAttributeResponse {
    final LocalizedText displayName;
    final LocalizedText description;
    final NodeId dataTypeId;
    final Integer valueRank;
    final Integer accessLevel;
    final Boolean historizing;

    public ReadVariableAttributeResponse(
            LocalizedText displayName,
            LocalizedText description,
            UaDataType dataType,
            Boolean writable,
            Boolean historizing,
            @Nullable Integer valueRank)
    {
        this.displayName = displayName;
        this.description = description;
        this.dataTypeId = dataType.nodeId();
        this.valueRank = (null == valueRank) ? ValueRank.Scalar.getValue() : valueRank;
        this.accessLevel = (writable) ? AccessLevel.CurrentWrite.getValue() | AccessLevel.CurrentRead.getValue() : AccessLevel.CurrentRead.getValue();
        this.historizing = historizing;
    }

    public LocalizedText getDisplayName() {
        return displayName;
    }

    public LocalizedText getDescription()
    {
        return description;
    }

    public NodeId getDataTypeId() {
        return dataTypeId;
    }

    public Integer getValueRank() {
        return valueRank;
    }

    public Integer getAccessLevel() {
        return accessLevel;
    }

    public Boolean getHistorizing() {
        return historizing;
    }
}
