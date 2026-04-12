package org.opcfoundation.webserver.digitaltwin.event;

import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.addressspace.nodes.UaObjectType;
import org.opcfoundation.webserver.addressspace.nodes.UaDataType;
import org.opcfoundation.webserver.addressspace.nodes.UaVariable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.springframework.lang.Nullable;

public class EventType extends UaObjectType {
    protected final NodeManager nodeManager;

    public EventType(
            String typeId,
            LocalizedText displayName,
            DigitalTwinSpace twinSpace)
    {
        this(typeId, displayName,null, twinSpace);
    }

    public EventType(
        String typeId,
        LocalizedText displayName,
        @Nullable EventType parentType,
        DigitalTwinSpace twinSpace)
    {
        super(new NodeId(twinSpace.nsIndex(),typeId),
                typeId,
                displayName,
                false);

        setParentType((null == parentType) ? UaObjectTypes.BaseEventType : parentType);

        if (typeId.isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_NodeIdRejected);
        if (displayName.isNull()) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

        nodeManager = twinSpace;
        twinSpace.addNode(this);
    }

    public UaVariable addField(
            String        name,
            LocalizedText displayName,
            LocalizedText description,
            UaDataType    dataType)
    {
        String variableId = browseName();
        variableId += "-";
        variableId += name;

        UaVariable newVariable = new UaVariable(
                new NodeId(nodeManager.nsIndex(), variableId),
                name,
                displayName,
                dataType.nodeId(),
                ValueRank.Scalar.getValue(),
                AccessLevel.CurrentRead.getValue(),
                UaVariableTypes.PropertyType);

        if (description.isNotNull()) newVariable.setDescription(description);

        addMember(newVariable);
        nodeManager.addNode(newVariable);

        return newVariable;
    }
}
