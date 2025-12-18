package org.opcfoundation.webserver.addressspace.nodes;

import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.opcfoundation.webserver.types.common.UaStructureUtilities;
import org.springframework.lang.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;

import java.util.ArrayList;
import java.util.List;

public class UaMethod extends UaInstanceNode {
    private @Nullable List<Argument> inputArguments;
    private @Nullable List<Argument> outputArguments;

    public UaMethod(
            NodeId nodeId,
            String browseName,
            LocalizedText displayName)
    {
        super(nodeId, browseName, displayName);
        inputArguments = null;
        outputArguments = null;
    }

    @Override
    public NodeClass nodeClass() {
        return NodeClass.Method;
    }

    public @Nullable List<Argument> getInputArguments() {
        return inputArguments;
    }

    public @Nullable List<Argument> getOutputArguments() {
        return outputArguments;
    }

    public @Nullable UaVariable setInputArguments(NodeId nodeId, List<Argument> arguments) throws UaRuntimeException
    {
        if (arguments.isEmpty()) return null;
        if (null != this.inputArguments) throw new UaRuntimeException(StatusCodes.Bad_AlreadyExists);

        this.inputArguments = arguments;

        UaVariable inputArguments = new UaVariable(
                nodeId,
                "InputArguments",
                new LocalizedText("InputArguments"),
                NodeIds.Argument,
                ValueRank.OneDimension.getValue(),
                AccessLevel.CurrentRead.getValue(),
                UaVariableTypes.PropertyType);

        List<UaStructuredType> structs = new ArrayList<>(arguments);
        inputArguments.setValue(UaStructureUtilities.toVariant(structs));

        addMemberNode(inputArguments);
        return inputArguments;
    }

    public @Nullable UaVariable setOutputArguments(NodeId nodeId, List<Argument> arguments)
    {
        if (arguments.isEmpty()) return null;
        if (null != this.outputArguments) throw new UaRuntimeException(StatusCodes.Bad_AlreadyExists);

        this.outputArguments = arguments;

        UaVariable outputArguments = new UaVariable(
                nodeId,
                "OutputArguments",
                new LocalizedText("OutputArguments"),
                NodeIds.Argument,
                ValueRank.OneDimension.getValue(),
                AccessLevel.CurrentRead.getValue(),
                UaVariableTypes.PropertyType);

        List<UaStructuredType> structs = new ArrayList<>(arguments);
        outputArguments.setValue(UaStructureUtilities.toVariant(structs));

        addMemberNode(outputArguments);

        return outputArguments;
    }

    @Override
    public Variant getAttribute(int attributeId)
    {
        if (AttributeId.Executable.id() == attributeId)
        {
            return new Variant(true);
        } else if (AttributeId.UserExecutable.id() == attributeId) {
            return new Variant(true);
        }

        return super.getAttribute(attributeId);
    }
}
