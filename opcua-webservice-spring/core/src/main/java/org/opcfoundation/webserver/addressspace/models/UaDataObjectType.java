package org.opcfoundation.webserver.addressspace.models;

import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.*;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.types.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.UaObjectId;
import org.opcfoundation.webserver.types.UaReferenceDescriptor;
import org.opcfoundation.webserver.types.UaChildId;
import org.opcfoundation.webserver.types.message.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Deprecated
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
        UaVariable newVariable = addVariableNode(memberId, displayName, dataType,writable, historizing, valueRank, variableType, null);
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
        NodeId referenceTypeId = request.getBrowseDescription().getReferenceTypeId();

        List<UaInstanceNode> members = getMembers();
        final List<UaInstanceNode> membersToReturn = new ArrayList<>();

        for (UaInstanceNode item: members)
        {
            if (item.nodeClass() == NodeClass.Object)
            {
                if (!request.getAdditionalInfo().isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK)) continue;
            }

            if (item.nodeClass() == NodeClass.Method)
            {
                if (!request.getAdditionalInfo().isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_METHOD_TASK)) continue;
            }

            if (item.nodeClass() == NodeClass.Variable)
            {
                if (!request.getAdditionalInfo().isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK)) continue;

                NodeId variableTypeId = ((UaVariable) item).typeDefinition().nodeId();
                if (referenceTypeId.equals(NodeIds.HasProperty))
                {
                    if (!variableTypeId.equals(NodeIds.PropertyType)) continue;
                } else if (referenceTypeId.equals(NodeIds.HasComponent)) {
                    if (variableTypeId.equals(NodeIds.PropertyType)) continue;
                }
            }

            membersToReturn.add(item);
        }

        if (membersToReturn.isEmpty()) return CompletableFuture.completedFuture(new BrowseObjectResponse(new ArrayList<>(), false));

        BrowseObjectResponse response = processBrowseChildResponse(request.getObjectId(), membersToReturn);
        return CompletableFuture.completedFuture(response);
    }

    @Override
    public final CompletableFuture<BrowseMemberResponse> onBrowseMemberChildren(BrowseMemberRequest request)
    {
        NodeId referenceTypeId = request.getBrowseDescription().getReferenceTypeId();

        UaInstanceNode member = getMember(request.getChildId());
        if (null == member || NodeClass.Object == member.nodeClass()) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

        List<UaReferenceDescriptor> childDescriptors = new ArrayList<>();
        List<UaInstanceNode> members = member.getMembers();

        for (UaInstanceNode item: members)
        {
            if (item.nodeClass() != NodeClass.Variable) continue;

            NodeId variableTypeId = ((UaVariable)item).typeDefinition().nodeId();

            if (referenceTypeId.equals(NodeIds.HasProperty))
            {
                if (!variableTypeId.equals(NodeIds.PropertyType)) continue;
            } else if (referenceTypeId.equals(NodeIds.HasComponent)) {
                if (variableTypeId.equals(NodeIds.PropertyType)) continue;
            }

            UaReferenceDescriptor descriptor = new UaReferenceDescriptor(
                    item.browseName(),
                    item,
                    variableTypeId.equals(NodeIds.PropertyType) ? NodeIds.HasProperty : NodeIds.HasComponent,
                    true);

            childDescriptors.add(descriptor);
        }

        return CompletableFuture.completedFuture(new BrowseMemberResponse(childDescriptors));
    }

    @Override
    public final CompletableFuture<ReadMemberAttributeResponse> onReadMemberAttributes(ReadMemberAttributeRequest request)
    {
        return CompletableFuture.supplyAsync(() -> {
            UaInstanceNode memberNode = getMember(request.getChildId().getPathId());

            if (null != memberNode && null != request.getChildId().getSubElementId())
            {
                memberNode = memberNode.getMember(request.getChildId().getSubElementId());
            }

            if (null == memberNode) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
            return new ReadMemberAttributeResponse(memberNode);
        });
    }

    @Override
    public final CompletableFuture<ReadVariableValueResponse> onReadVariablesValue(ReadVariableValueRequest request)
    {
        return getVariableValues(request).thenApply(
                readVariableValueResponse -> processReadVariableValueResponse(request.getVariableIds(), readVariableValueResponse));
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
            UaObjectId objectId,
            List<UaInstanceNode> members)
    {
        List<UaReferenceDescriptor> childDescriptors = new ArrayList<>();

        for (UaInstanceNode item: members)
        {
            NodeId referenceType = (item.nodeClass() == NodeClass.Variable &&
                    ((UaVariable) item).typeDefinition().nodeId().equals(NodeIds.PropertyType)) ? NodeIds.HasProperty : NodeIds.HasComponent;

            UaReferenceDescriptor descriptor = new UaReferenceDescriptor(
                    (item.nodeClass() == NodeClass.Object) ? objectId.getId() : item.browseName(),
                    item,
                    referenceType,
                    true);

            childDescriptors.add(descriptor);
        }

        return new BrowseObjectResponse(childDescriptors, false);
    }

    private ReadVariableValueResponse processReadVariableValueResponse(
            Set<UaChildId> membersToRead,
            ReadVariableValueResponse response)
    {
        ReadVariableValueResponse readVariableValueResponse = new ReadVariableValueResponse(response.getResults());

        Map<UaChildId, DataValue> results = response.getResults();

        for (UaChildId item: membersToRead)
        {
            if (null == item.getSubElementName() || results.containsKey(item)) continue;

            UaInstanceNode node = getMember(item.getId());
            if (null == node) continue;

            UaInstanceNode subElementNode = node.getMember(item.getSubElementName());
            if (null == subElementNode || NodeClass.Variable != subElementNode.nodeClass()) continue;

            results.put(
                    item,
                    new DataValue(((UaVariable)subElementNode).value(), StatusCode.GOOD, null, null));
        }

        return readVariableValueResponse;
    }
}
