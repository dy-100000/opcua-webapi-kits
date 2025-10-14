package org.opcfoundation.webserver.types;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

public class UaNodeAttributes {
    private final @Nullable LocalizedText displayName;
    private final @Nullable LocalizedText description;
    private final @Nullable NodeId dataTypeId;
    private final @Nullable Integer valueRank;
    private final @Nullable Integer accessLevel;
    private final @Nullable Boolean historizing;

    public UaNodeAttributes(
        @Nullable LocalizedText displayName,
        @Nullable LocalizedText description,
        @Nullable NodeId dataTypeId,
        @Nullable Integer valueRank,
        @Nullable Integer accessLevel,
        @Nullable Boolean historizing)
    {
        this.displayName = displayName;
        this.description = description;
        this.dataTypeId = dataTypeId;
        this.valueRank = valueRank;
        this.accessLevel = accessLevel;
        this.historizing = historizing;
    }

    public @Nullable LocalizedText getDisplayName() {
        return displayName;
    }

    public @Nullable LocalizedText getDescription() {
        return description;
    }

    public @Nullable NodeId getDataTypeId() {
        return dataTypeId;
    }

    public @Nullable Integer getValueRank() {
        return valueRank;
    }

    public @Nullable Integer getAccessLevel() {
        return accessLevel;
    }

    public @Nullable Boolean getHistorizing() {
        return historizing;
    }
}
