package org.opcfoundation.webserver.types.message;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.UaInstanceNode;
import org.opcfoundation.webserver.addressspace.nodes.UaVariable;

public class ReadMemberAttributeResponse {
    private final NodeClass nodeClass;
    private final String browseName;
    private final LocalizedText displayName;
    private final LocalizedText description;
    private final @Nullable NodeId dataTypeId;
    private final @Nullable Integer valueRank;
    private final @Nullable Integer accessLevel;
    private final @Nullable Boolean historizing;
    private final @Nullable Variant value;

    public ReadMemberAttributeResponse(
            NodeClass nodeClass,
            String browseName,
            LocalizedText displayName,
            LocalizedText description,
            @Nullable NodeId dataTypeId,
            @Nullable Integer valueRank,
            @Nullable Integer accessLevel,
            @Nullable Boolean historizing,
            @Nullable Variant value)
    {
        this.nodeClass = nodeClass;
        this.browseName = browseName;
        this.displayName = displayName;
        this.description = description;
        this.dataTypeId = dataTypeId;
        this.valueRank = valueRank;
        this.accessLevel = accessLevel;
        this.historizing = historizing;
        this.value = value;
    }

    public ReadMemberAttributeResponse(UaInstanceNode instanceDeclaration)
    {
        this.nodeClass = instanceDeclaration.nodeClass();
        this.browseName = instanceDeclaration.browseName();
        this.displayName = instanceDeclaration.displayName();
        this.description = instanceDeclaration.description();

        if (NodeClass.Variable == instanceDeclaration.nodeClass()) {
            UaVariable variable = (UaVariable)instanceDeclaration;
            this.dataTypeId = variable.dataType();
            this.valueRank = variable.valueRank();
            this.accessLevel = variable.accessLevel();
            this.historizing = variable.historizing();
            this.value = variable.value();
        } else {
            this.dataTypeId = null;
            this.valueRank = null;
            this.accessLevel = null;
            this.historizing = null;
            this.value = null;
        }
    }

    public NodeClass getNodeClass() {
        return nodeClass;
    }

    public String getBrowseName() {
        return browseName;
    }

    public LocalizedText getDisplayName() {
        return displayName;
    }

    public LocalizedText getDescription() {
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

    public @Nullable Variant getValue() {
        return value;
    }
}
