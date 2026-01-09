package org.opcfoundation.webserver.digitaltwin.element;

import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.springframework.lang.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.*;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.callback.ElementCollectionCallback;
import org.opcfoundation.webserver.service.message.digitaltwin.*;
import org.opcfoundation.webserver.service.message.reactiveobject.*;
import org.opcfoundation.webserver.types.common.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.common.UaChildId;
import org.opcfoundation.webserver.types.common.UaObjectId;
import org.opcfoundation.webserver.types.common.UaReferenceDescriptor;
import org.opcfoundation.webserver.types.digitaltwin.ObjectServiceContext;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class ElementCollectionType extends ElementType implements ElementCollectionCallback {
    public ElementCollectionType(String typeId,
                               LocalizedText displayName,
                               DigitalTwinSpace twinSpace)
    {
        super(
                typeId,
                displayName,
                UaObjectTypes.ElementCollectionType,
                twinSpace);
    }

    public UaVariable addPropertyElement(
            String        name,
            LocalizedText displayName,
            LocalizedText description,
            UaDataType    dataType,
            boolean       writable)
    {
        return addPropertyElement(
                name,
                displayName,
                description,
                dataType,
                writable,
                false,
                -1,
                UaVariableTypes.PropertyType,
                true);
    }

    public UaVariable addPropertyElement(
            String                         name,
            LocalizedText                  displayName,
            LocalizedText                  description,
            UaDataType                     dataType,
            boolean                        writable,
            boolean                        historizing,
            @Nullable Integer              valueRank,
            @Nullable UaVariableType       variableType,
            boolean                        mandatory)
    {
        UaVariable newVariable = addVariableNode(name, displayName, dataType,writable, historizing, valueRank, variableType);
        if (description.isNotNull()) newVariable.setDescription(description);
        newVariable.setModellingRule((mandatory) ? UaModellingRule.Mandatory : UaModellingRule.Optional);
        return newVariable;
    }

    public void addSubElementOfProperty(UaVariable property, String subElementName, Variant value)
    {
        UaVariable subElement = property.addMember(subElementName);
        if (null != subElement)
        {
            subElement.setValue(value);
            nodeManager.addNode(subElement);
        }
    }

    public UaMethod addOperationElement(
            String                   name,
            LocalizedText            displayName,
            LocalizedText            description,
            @Nullable List<Argument> inputArguments,
            @Nullable List<Argument> outputArguments,
            boolean                  mandatory)
    {
        UaMethod newMethod = addMethodNode(name, displayName, inputArguments, outputArguments);
        if (description.isNotNull()) newMethod.setDescription(description);
        newMethod.setModellingRule((mandatory) ? UaModellingRule.Mandatory : UaModellingRule.Optional);
        return newMethod;
    }

    public UaObject addElementCollection(
            ElementCollectionType type,
            String                name,
            LocalizedText         displayName,
            LocalizedText         description,
            boolean               mandatory)
    {
        UaObject newObject = addObjectNode(name, displayName, type);
        if (description.isNotNull()) newObject.setDescription(description);
        newObject.setModellingRule((mandatory) ? UaModellingRule.Mandatory : UaModellingRule.Optional);
        return newObject;
    }

    public UaObject addReferenceElement(
            ReferenceElementType type,
            String               name,
            LocalizedText        displayName,
            LocalizedText        description,
            boolean              mandatory)
    {
        UaObject newObject = addObjectNode(name, displayName, type);
        if (description.isNotNull()) newObject.setDescription(description);
        newObject.setModellingRule((mandatory) ? UaModellingRule.Mandatory : UaModellingRule.Optional);
        return newObject;
    }

    public UaObject addEventElement(
            EventElementType type,
            String           name,
            LocalizedText    displayName,
            LocalizedText    description,
            boolean          mandatory)
    {
        UaObject newObject = addObjectNode(name, displayName, type);
        if (description.isNotNull()) newObject.setDescription(description);
        newObject.setModellingRule((mandatory) ? UaModellingRule.Mandatory : UaModellingRule.Optional);
        return newObject;
    }

    public UaObject addElementList(
            ElementListType type,
            String          name,
            LocalizedText   displayName,
            LocalizedText   description,
            boolean         mandatory)
    {
        UaObject newObject = addObjectNode(name, displayName, type);
        if (description.isNotNull()) newObject.setDescription(description);
        newObject.setModellingRule((mandatory) ? UaModellingRule.Mandatory : UaModellingRule.Optional);
        return newObject;
    }

    @Override
    public final CompletableFuture<ReadObjectAttributeResponse> onReadObjectAttributes(ReadObjectAttributeRequest request)
    {
        UaObject instanceDeclaration = request.getObjectId().getInstance();

        if (null == instanceDeclaration)
        {
            ObjectServiceContext context = new ObjectServiceContext(request.getObjectId());
            GetDescriptorRequest getDescriptorRequest = new GetDescriptorRequest(context);

            return onGetDescriptor(getDescriptorRequest).thenApply(response -> {
                return new ReadObjectAttributeResponse(request.getObjectId().getId(), response.getDisplayName(), response.getDescription());
            });
        } else {
            return CompletableFuture.completedFuture(
                    new ReadObjectAttributeResponse(
                            instanceDeclaration.browseName(),
                            instanceDeclaration.displayName(),
                            instanceDeclaration.description()));
        }
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

        ObjectServiceContext context = new ObjectServiceContext(request.getObjectId());
        GetElementsRequest getElementsRequest = new GetElementsRequest(context);

        return onGetElements(getElementsRequest).
                thenApply(response -> processBrowseObjectChildrenResponse(request.getObjectId(),membersToReturn, response));
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
            UaInstanceNode memberNode = getMember(request.getChildId().getId());

            if (null != memberNode && null != request.getChildId().getSubElementName())
            {
                memberNode = memberNode.getMember(request.getChildId().getSubElementName());
            }

            if (null == memberNode) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            return new ReadMemberAttributeResponse(memberNode);
        });
    }

    @Override
    public final CompletableFuture<ReadVariableValueResponse> onReadVariablesValue(ReadVariableValueRequest request)
    {
        final Set<String> propertyNames = new HashSet<>();
        final Set<UaChildId> subElementNames = new HashSet<>();

        for (UaChildId item: request.getVariableIds())
        {
            if (null == item.getSubElementName())
            {
                propertyNames.add(item.getId());
            } else {
                subElementNames.add(item);
            }
        }

        ObjectServiceContext context = new ObjectServiceContext(request.getObjectId());
        ReadPropertyValuesRequest readPropertyValuesRequest = new ReadPropertyValuesRequest(
                context,
                propertyNames);

        if (propertyNames.isEmpty())
        {
            return CompletableFuture.completedFuture(processReadVariableValueResponse(subElementNames, new ReadPropertyValuesResponse()));
        }

        return onReadPropertyValues(readPropertyValuesRequest).
                thenApply(readPropertyValuesResponse -> {
                    return processReadVariableValueResponse(subElementNames, readPropertyValuesResponse); });
    }

    @Override
    public final CompletableFuture<WriteVariableValueResponse> onWriteVariablesValue(WriteVariableValueRequest request)
    {
        Map<String, Variant> elementValues = new HashMap<>();

        for (Map.Entry<UaChildId, Variant> item : request.getVariableValues().entrySet())
        {
            UaChildId childId = item.getKey();
            if (null != childId.getSubElementName()) continue;

            UaNode memberNode = getMember(childId.getId());
            if (null == memberNode || NodeClass.Variable != memberNode.nodeClass()) continue;

            UaVariable variableToWrite = (UaVariable) memberNode;
            if ((AccessLevel.CurrentWrite.getValue() & variableToWrite.accessLevel()) == 0) continue;

            elementValues.put(childId.getId(), item.getValue());
        }

        if (elementValues.isEmpty())
        {
            return CompletableFuture.completedFuture(new WriteVariableValueResponse());
        }

        ObjectServiceContext context = new ObjectServiceContext(request.getObjectId());
        WritePropertyValuesRequest writePropertyValuesRequest = new WritePropertyValuesRequest(
                context,
                elementValues);

        return onWritePropertyValues(writePropertyValuesRequest)
                .thenApply(response -> {
                    return new WriteVariableValueResponse(response.getResults());
                });
    }

    @Override
    public CompletableFuture<MethodCallResponse> onMethodCall(MethodCallRequest request)
    {
        ObjectServiceContext context = new ObjectServiceContext(request.getObjectId());
        InvokeOperationRequest invokeOperationRequest = new InvokeOperationRequest(
                context,
                request.getMethodName(),
                request.getInputArguments());

        return onInvokeOperation(invokeOperationRequest)
                .thenApply(response -> {
                    return new MethodCallResponse(response.getOutputArguments());
                });
    }

    private BrowseObjectResponse processBrowseObjectChildrenResponse(
            UaObjectId objectId,
            List<UaInstanceNode> members,
            GetElementsResponse response)
    {
        List<UaReferenceDescriptor> childDescriptors = new ArrayList<>();

        for (UaInstanceNode item: members)
        {
            if (item.modellingRule() == UaModellingRule.Optional &&
                    !response.getElementNames().contains(item.browseName())) continue;

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
            Set<UaChildId> subElementNames,
            ReadPropertyValuesResponse response)
    {
        ReadVariableValueResponse readVariableValueResponse = new ReadVariableValueResponse(response.getResults());
        Map<UaChildId, DataValue> results = response.getResults();

        for (UaChildId item: subElementNames)
        {
            if (null == item.getSubElementName()) continue;

            UaInstanceNode node = getMember(item.getId());
            if (null == node || NodeClass.Variable != node.nodeClass()) continue;

            UaInstanceNode subElementNode = node.getMember(item.getSubElementName());
            if (null == subElementNode || NodeClass.Variable != subElementNode.nodeClass()) continue;

            results.put(
                    new UaChildId(item.getId(), item.getSubElementName()),
                    new DataValue(((UaVariable)subElementNode).value(), StatusCode.GOOD, null, null));
        }

        return readVariableValueResponse;
    }

    @Override
    public final List<UaInstanceNode> getElements()
    {
        return getMembers();
    }
}
