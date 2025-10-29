package org.opcfoundation.webserver.addressspace.models;

import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.*;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.types.UaReferenceDescriptor;
import org.opcfoundation.webserver.types.UaChildId;
import org.opcfoundation.webserver.types.message.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class UaDataObjectType extends UaSubmodelType {
    public UaDataObjectType(String objectTypeId,
                            LocalizedText displayName,
                            @Nullable UaDataObjectType parentType,
                            NodeManager nodeManager)
    {
        super(objectTypeId,
              displayName,
              (null == parentType) ? UaObjectTypes.BaseObjectType : parentType,
              nodeManager);
    }

    // Developer need to override this method to process read request
    public CompletableFuture<ReadVariableValueResponse> getVariableValues(ReadVariableValueRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    // If developer need to process write value request, this method can be overridden
    public CompletableFuture<WriteVariableValueResponse> setVariableValues(WriteVariableValueRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    // If developer want to ignore optional members, this method can be overridden
    public CompletableFuture<GetOptionalMemberResponse> getAbsentMembers(GetOptionalMemberRequest request)
    {
        return CompletableFuture.completedFuture(new GetOptionalMemberResponse(new HashSet<>()));
    }

    // If developer need to process method call request, this method can be overridden
    public CompletableFuture<MethodCallResponse> methodCall(MethodCallRequest request)
    {
        return CompletableFuture.supplyAsync(()->{
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    protected UaObject addObject(
            String memberId,
            boolean mandatory,
            LocalizedText displayName,
            LocalizedText description,
            UaDataObjectType objectType)
    {
        UaObject newObject = addObjectNode(memberId, displayName, objectType);
        if (description.isNotNull()) newObject.setDescription(description);
        newObject.setModellingRule((mandatory) ? UaModellingRule.Mandatory : UaModellingRule.Optional);
        return newObject;
    }

    protected UaVariable addVariable(
            String memberId,
            boolean mandatory,
            LocalizedText displayName,
            LocalizedText description,
            UaDataType dataType,
            boolean writable,
            boolean historizing,
            @Nullable Integer valueRank,
            @Nullable UaVariableType variableType)
    {
        UaVariable newVariable = addVariableNode(memberId, displayName, dataType,writable, historizing, valueRank, variableType);
        if (description.isNotNull()) newVariable.setDescription(description);
        newVariable.setModellingRule((mandatory) ? UaModellingRule.Mandatory : UaModellingRule.Optional);
        return newVariable;
    }

    protected UaMethod addMethod(
            String memberId,
            boolean mandatory,
            LocalizedText displayName,
            LocalizedText description,
            @Nullable List<Argument> inputArguments,
            @Nullable List<Argument> outputArguments)
    {
        UaMethod newMethod = addMethodNode(memberId, displayName, inputArguments, outputArguments);
        if (description.isNotNull()) newMethod.setDescription(description);
        newMethod.setModellingRule((mandatory) ? UaModellingRule.Mandatory : UaModellingRule.Optional);
        return newMethod;
    }

    @Override
    public final CompletableFuture<BrowseObjectResponse> onBrowseObjectChildren(BrowseObjectRequest request)
    {
        UaNode hasComponentType = nodeManager.getNode(NodeIds.HasComponent);
        UaNode hasPropertyType = nodeManager.getNode(NodeIds.HasProperty);

        if (null == hasComponentType || hasComponentType.nodeClass() != NodeClass.ReferenceType ||
                null == hasPropertyType || hasPropertyType.nodeClass() != NodeClass.ReferenceType)
        {
            throw new UaRuntimeException(StatusCodes.Bad_InternalError);
        }

        List<UaInstanceNode> members = getMembers();
        final List<UaInstanceNode> membersToReturn = new ArrayList<>();
        int nodeClassMask = request.getBrowseDescription().getNodeClassMask().intValue();
        NodeId referenceTypeId = request.getBrowseDescription().getReferenceTypeId();

        for (UaInstanceNode item: members)
        {
            if ((item.nodeClass().getValue() & nodeClassMask) == 0) continue;

            if (item.nodeClass() == NodeClass.Object || item.nodeClass() == NodeClass.Method)
            {
                if (!((UaReferenceType)hasComponentType).isSubtypeOf(referenceTypeId)) continue;
            } else if (item.nodeClass() == NodeClass.Variable) {
                UaVariable variableMember = (UaVariable)item;

                if (variableMember.typeDefinition().nodeId().equals(NodeIds.PropertyType))
                {
                    if (!((UaReferenceType)hasPropertyType).isSubtypeOf(referenceTypeId)) continue;
                } else {
                    if (!((UaReferenceType)hasComponentType).isSubtypeOf(referenceTypeId)) continue;
                }
            }

            membersToReturn.add(item);
        }

        GetOptionalMemberRequest optionalMemberRequest = new GetOptionalMemberRequest(request.getObjectId());

        return getAbsentMembers(optionalMemberRequest).
                thenApply(response -> processBrowseChildResponse(membersToReturn, response.getAbsentMembers()));
    }

    @Override
    public final CompletableFuture<ReadChildAttributeResponse> onReadChildAttributes(ReadChildAttributeRequest request)
    {
        return CompletableFuture.supplyAsync(() -> {
            UaNode memberNode = getMember(request.getChildId().getPathId());

            if (null != memberNode && null != request.getChildId().getSubElementId())
            {
                memberNode = memberNode.getMember(request.getChildId().getSubElementId());
            }

            if (null == memberNode) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            if (memberNode.nodeClass() == NodeClass.Variable)
            {
                UaVariable variableMember = (UaVariable)memberNode;

                return new ReadChildAttributeResponse(
                        NodeClass.Variable,
                        memberNode.displayName(),
                        memberNode.description(),
                        variableMember.dataType(),
                        variableMember.valueRank(),
                        variableMember.accessLevel(),
                        variableMember.historizing());
            } else {
                return new ReadChildAttributeResponse(
                        memberNode.nodeClass(),
                        memberNode.displayName(),
                        memberNode.description(),
                        null,
                        null,
                        null,
                        null);
            }
        });
    }

    @Override
    public final CompletableFuture<ReadVariableValueResponse> onReadVariablesValue(ReadVariableValueRequest request)
    {
        return getVariableValues(request);
    }

    @Override
    public final CompletableFuture<WriteVariableValueResponse> onWriteVariablesValue(WriteVariableValueRequest request)
    {
        Map<UaChildId, Variant> variableValues = new HashMap<>();

        for (Map.Entry<UaChildId, Variant> item : request.getVariableValues().entrySet())
        {
            UaChildId childId = item.getKey();
            UaNode memberNode = getMember(childId.getPathId());

            if (null == memberNode || NodeClass.Variable != memberNode.nodeClass()) continue;

            if (null != childId.getSubElementId())
            {
                memberNode = memberNode.getMember(childId.getSubElementId());
                if (null == memberNode || NodeClass.Variable != memberNode.nodeClass()) continue;
            }

            UaVariable variableToWrite = (UaVariable) memberNode;
            if ((AccessLevel.CurrentWrite.getValue() & variableToWrite.accessLevel()) == 0) continue;

            variableValues.put(childId, item.getValue());
        }

        if (variableValues.isEmpty())
        {
            return CompletableFuture.completedFuture(new WriteVariableValueResponse());
        }

        return setVariableValues(new WriteVariableValueRequest(request.getObjectId(), variableValues));
    }

    @Override
    public CompletableFuture<MethodCallResponse> onMethodCall(MethodCallRequest request)
    {
        return methodCall(request);
    }

    private BrowseObjectResponse processBrowseChildResponse(
            List<UaInstanceNode> members,
            Set<String> absentMembers)
    {
        List<UaReferenceDescriptor> childDescriptors = new ArrayList<>();

        for (UaInstanceNode item: members)
        {
            if (item.modellingRule() == UaModellingRule.Optional &&
                    absentMembers.contains(item.browseName())) continue;

            NodeId referenceType = (item.nodeClass() == NodeClass.Variable &&
                    ((UaVariable) item).typeDefinition().nodeId().equals(NodeIds.PropertyType)) ? NodeIds.HasProperty : NodeIds.HasComponent;

            UaReferenceDescriptor descriptor = new UaReferenceDescriptor(
                    item.browseName(),
                    item,
                    referenceType,
                    true);

            childDescriptors.add(descriptor);
        }

        return new BrowseObjectResponse(childDescriptors, false);
    }
}
