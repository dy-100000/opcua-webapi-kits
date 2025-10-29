package org.opcfoundation.webserver.addressspace.models;

import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.addressspace.nodes.*;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;

import java.util.ArrayList;
import java.util.List;

public abstract class UaObjectType extends BaseUaObjectType implements UaObjectCallback {
    protected final NodeManager nodeManager;

    public UaObjectType(
            String objectTypeId,
            LocalizedText displayName,
            BaseUaObjectType parentType,
            NodeManager nodeManager)
    {
        super(new NodeId(nodeManager.nsIndex(),objectTypeId),
                objectTypeId,
                displayName,
                false,
                parentType);

        if (objectTypeId.isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_NodeIdRejected);
        if (displayName.isNull()) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);
        this.nodeManager = nodeManager;
    }

    public boolean isGetParentSupported() { return false; }

    public boolean isGetLinkSupported() { return false; }

    protected UaObject addObjectNode(
            String memberId,
            LocalizedText displayName,
            UaObjectType objectType)
    {
        if (memberId.isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_NodeIdRejected);
        if (displayName.isNull()) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

        String objectId = browseName();
        objectId += "-";
        objectId += memberId;

        UaObject newObject = new UaObject(
                new NodeId(nodeManager.nsIndex(), objectId),
                memberId,
                displayName,
                objectType);

        this.addMember(newObject);
        nodeManager.addNode(newObject);

        List<UaInstanceNode> members = objectType.getMembers();
        for (UaInstanceNode item: members)
        {
            String memberIdOfObject = objectId;
            memberIdOfObject += "-";
            memberIdOfObject += item.browseName();

            UaInstanceNode newMember = copyNode(item,memberIdOfObject);
            newObject.addMember(newMember);
        }

        return newObject;
    }

    protected UaVariable addVariableNode(
            String memberId,
            LocalizedText displayName,
            UaDataType dataType,
            boolean writable,
            boolean historizing,
            @Nullable Integer valueRank,
            @Nullable UaVariableType variableType)
    {
        if (memberId.isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_NodeIdRejected);
        if (displayName.isNull()) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

        String variableId = browseName();
        variableId += "-";
        variableId += memberId;

        UaVariable newVariable = new UaVariable(
                new NodeId(nodeManager.nsIndex(), variableId),
                memberId,
                displayName,
                dataType.nodeId(),
                (null == valueRank) ? ValueRank.Scalar.getValue() : valueRank,
                (writable) ? AccessLevel.CurrentWrite.getValue() | AccessLevel.CurrentRead.getValue() : AccessLevel.CurrentRead.getValue(),
                (null == variableType) ? UaVariableTypes.PropertyType : variableType);

        newVariable.setHistorizing(historizing);

        this.addMember(newVariable);
        nodeManager.addNode(newVariable);

        if (null != variableType)
        {
            List<UaInstanceNode> members = variableType.getMembers();
            for (UaInstanceNode item: members)
            {
                if (NodeClass.Variable != item.nodeClass()) continue;
                String memberIdOfVariable = variableId;
                memberIdOfVariable += "-";
                memberIdOfVariable += item.browseName();

                UaInstanceNode newMember = copyNode(item,memberIdOfVariable);
                newVariable.addMember((UaVariable) newMember);
            }
        }

        return newVariable;
    }

    protected UaMethod addMethodNode(
            String memberId,
            LocalizedText displayName,
            @Nullable List<Argument> inputArguments,
            @Nullable List<Argument> outputArguments)
    {
        if (memberId.isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_NodeIdRejected);
        if (displayName.isNull()) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

        String methodId = browseName();
        methodId += "-";
        methodId += memberId;

        UaMethod newMethod = new UaMethod(
                new NodeId(nodeManager.nsIndex(), methodId),
                memberId,
                displayName);

        this.addMember(newMethod);
        nodeManager.addNode(newMethod);

        if (null != inputArguments && !inputArguments.isEmpty())
        {
            String inputArgumentId = methodId;
            inputArgumentId += "-InputArguments";
            UaVariable argumentVariable = newMethod.setInputArguments(
                    new NodeId(nodeManager.nsIndex(), inputArgumentId),
                    inputArguments);

            if (null != argumentVariable)
            {
                argumentVariable.setModellingRule(UaModellingRule.Mandatory);
                nodeManager.addNode(argumentVariable);
            }
        }

        if (null != outputArguments && !outputArguments.isEmpty())
        {
            String outputArgumentId = methodId;
            outputArgumentId += "-OutputArguments";
            UaVariable argumentVariable = newMethod.setOutputArguments(
                    new NodeId(nodeManager.nsIndex(), outputArgumentId),
                    outputArguments);

            if (null != argumentVariable)
            {
                argumentVariable.setModellingRule(UaModellingRule.Mandatory);
                nodeManager.addNode(argumentVariable);
            }
        }

        return newMethod;
    }

    private UaInstanceNode copyNode(UaInstanceNode node, String newInstanceId)
    {
        if (NodeClass.Object == node.nodeClass())
        {
            UaObject sourceObject = (UaObject)node;
            UaObject newObject = new UaObject(
                    new NodeId(nodeManager.nsIndex(), newInstanceId),
                    sourceObject.browseName(),
                    sourceObject.displayName(),
                    sourceObject.typeDefinition());

            newObject.setWriteMask(sourceObject.writeMask());
            newObject.setDescription(sourceObject.description());
            newObject.setModellingRule(sourceObject.modellingRule());

            nodeManager.addNode(newObject);

            List<UaInstanceNode> members = sourceObject.getMembers();
            for (UaInstanceNode item: members)
            {
                String memberId = newInstanceId;
                memberId += "-";
                memberId += item.browseName();

                UaInstanceNode member = copyNode(item, memberId);
                newObject.addMember(member);
            }

            return newObject;
        } else if (NodeClass.Variable == node.nodeClass()) {
            UaVariable sourceVariable = (UaVariable)node;
            UaVariable newVariable = new UaVariable(
                    new NodeId(nodeManager.nsIndex(), newInstanceId),
                    sourceVariable.browseName(),
                    sourceVariable.displayName(),
                    sourceVariable.dataType(),
                    sourceVariable.valueRank(),
                    sourceVariable.accessLevel(),
                    sourceVariable.typeDefinition());

            newVariable.setWriteMask(sourceVariable.writeMask());
            newVariable.setDescription(sourceVariable.description());
            newVariable.setModellingRule(sourceVariable.modellingRule());
            newVariable.setValue(sourceVariable.value());
            newVariable.setHistorizing(sourceVariable.historizing());

            nodeManager.addNode(newVariable);

            List<UaInstanceNode> members = sourceVariable.getMembers();
            for (UaInstanceNode item: members)
            {
                if (NodeClass.Variable != item.nodeClass()) continue;

                String memberId = newInstanceId;
                memberId += "-";
                memberId += item.browseName();

                UaInstanceNode member = copyNode(item, memberId);
                newVariable.addMember((UaVariable) member);
            }

            return newVariable;
        } else {
            UaMethod sourceMethod = (UaMethod)node;
            UaMethod newMethod = new UaMethod(
                    new NodeId(nodeManager.nsIndex(), newInstanceId),
                    sourceMethod.browseName(),
                    sourceMethod.displayName());

            newMethod.setWriteMask(sourceMethod.writeMask());
            newMethod.setDescription(sourceMethod.description());
            newMethod.setModellingRule(sourceMethod.modellingRule());

            nodeManager.addNode(newMethod);

            if (null != sourceMethod.getInputArguments())
            {
                String inputArgumentsId = newInstanceId;
                inputArgumentsId += "-InputArguments";

                UaVariable argumentVariable = newMethod.setInputArguments(
                        new NodeId(nodeManager.nsIndex(), inputArgumentsId),
                        sourceMethod.getInputArguments());

                if (null != argumentVariable) nodeManager.addNode(argumentVariable);
            }

            if (null != sourceMethod.getOutputArguments())
            {
                String outputArgumentsId = newInstanceId;
                outputArgumentsId += "-OutputArguments";

                UaVariable argumentVariable = newMethod.setOutputArguments(
                        new NodeId(nodeManager.nsIndex(), outputArgumentsId),
                        sourceMethod.getOutputArguments());

                if (null != argumentVariable) nodeManager.addNode(argumentVariable);
            }

            return newMethod;
        }
    }
}
