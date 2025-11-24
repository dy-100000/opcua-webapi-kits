package org.opcfoundation.webserver.types;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;
import org.opcfoundation.webserver.addressspace.nodes.UaVariableType;

@Deprecated
public class UaChildVariableDescriptor {
    private final String id;
    private final LocalizedText displayName;
    private final NodeId typeId;

    public UaChildVariableDescriptor(
            String id,
            LocalizedText displayName)
    {
        this.id = id;
        this.displayName = (displayName.isNull()) ? new LocalizedText(id) : displayName;
        this.typeId = UaVariableTypes.BaseDataVariableType.nodeId();
    }

    public UaChildVariableDescriptor(
            String id,
            LocalizedText displayName,
            UaVariableType variableType)
    {
        this.id = id;
        this.displayName = (displayName.isNull()) ? new LocalizedText(id) : displayName;
        this.typeId = variableType.nodeId();
    }

    public String getId() {
        return id;
    }

    public NodeId getTypeId() {
        return typeId;
    }

    public LocalizedText getDisplayName() {
        return displayName;
    }

    @Override
    public String toString()
    {
        String ret = "Id: " + id;
        ret += " TypeId: " + typeId.toParseableString();
        ret += " DisplayName: " + displayName.getText();
        return ret;
    }
}
