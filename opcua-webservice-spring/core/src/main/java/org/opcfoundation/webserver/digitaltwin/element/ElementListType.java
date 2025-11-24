package org.opcfoundation.webserver.digitaltwin.element;

import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.webserver.addressspace.nodes.*;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.callback.ElementListCallback;
import org.opcfoundation.webserver.types.ObjectElementDescriptor;
import org.opcfoundation.webserver.types.PropertyElementDescriptor;
import org.opcfoundation.webserver.types.ServiceContext;
import org.opcfoundation.webserver.types.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.UaChildId;
import org.opcfoundation.webserver.types.UaReferenceDescriptor;
import org.opcfoundation.webserver.types.message.*;
import org.opcfoundation.webserver.types.message.digitaltwin.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class ElementListType extends ElementType implements ElementListCallback {
    public ElementListType(String typeId,
                           LocalizedText displayName,
                           DigitalTwinSpace namespace)
    {
        super(
                typeId,
                displayName,
                UaObjectTypes.ElementType,
                namespace);
    }

    @Override
    public final CompletableFuture<ReadObjectAttributeResponse> onReadObjectAttributes(ReadObjectAttributeRequest request)
    {
        UaObject instanceDeclaration = request.getObjectId().getInstance();

        if (null == instanceDeclaration)
        {
            ServiceContext context = new ServiceContext(request.getObjectId());
            GetDescriptorRequest getDescriptorRequest = new GetDescriptorRequest(context);

            return onGetDescriptor(getDescriptorRequest).thenApply(response -> {
                return new ReadObjectAttributeResponse(response.getDisplayName(), response.getDescription());
            });
        } else {
            ReadObjectAttributeResponse response = new ReadObjectAttributeResponse(
                    instanceDeclaration.displayName(),
                    instanceDeclaration.description());

            return CompletableFuture.completedFuture(new ReadObjectAttributeResponse(response.getDisplayName(), response.getDescription()));
        }
    }

    @Override
    public final CompletableFuture<BrowseObjectResponse> onBrowseObjectChildren(BrowseObjectRequest request)
    {
        ServiceContext context = new ServiceContext(request.getObjectId());

        if (request.getAdditionalInfo().isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK) &&
                supportObjectElementList())
        {
            GetObjectElementListRequest getObjectElementListRequest = new GetObjectElementListRequest(
                    context,
                    request.getAdditionalInfo().getMaxReferencesPerNode(),
                    request.getAdditionalInfo().getReferenceOffset());

            return onGetObjectElementList(getObjectElementListRequest).
                    thenApply(this::processBrowseObjectChildrenResponse);
        } else if (request.getAdditionalInfo().isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK) &&
                supportPropertyElementList() && !request.getBrowseDescription().getReferenceTypeId().equals(NodeIds.HasProperty))
        {
            GetPropertyElementListRequest getPropertyElementListRequest = new GetPropertyElementListRequest(
                    context,
                    request.getAdditionalInfo().getMaxReferencesPerNode(),
                    request.getAdditionalInfo().getReferenceOffset());

            return onGetPropertyElementList(getPropertyElementListRequest).
                    thenApply(this::processBrowseObjectChildrenResponse);
        } else {
            return CompletableFuture.completedFuture(new BrowseObjectResponse(new ArrayList<>(), false));
        }
    }

    @Override
    public final CompletableFuture<BrowseMemberResponse> onBrowseMemberChildren(BrowseMemberRequest request)
    {
        UaNode hasComponentType = nodeManager.getNode(NodeIds.HasComponent);

        if (null == hasComponentType || hasComponentType.nodeClass() != NodeClass.ReferenceType)
        {
            throw new UaRuntimeException(StatusCodes.Bad_InternalError);
        }

        ServiceContext context = new ServiceContext(request.getObjectId());
        GetPropertySubElementsRequest getPropertySubElementsRequest= new GetPropertySubElementsRequest(
                context,
                request.getChildId());

        return onGetPropertySubElements(getPropertySubElementsRequest).
                thenApply(this::processBrowseMemberChildren);
    }

    @Override
    public final CompletableFuture<ReadMemberAttributeResponse> onReadMemberAttributes(ReadMemberAttributeRequest request)
    {
        ServiceContext context = new ServiceContext(request.getObjectId());
        GetPropertyDescriptorRequest getPropertyDescriptorRequest = new GetPropertyDescriptorRequest(
                context,
                request.getChildId().getId(),
                request.getChildId().getSubElementName());

        return onGetPropertyDescriptor(getPropertyDescriptorRequest).thenApply(this::processReadMemberAttributeResponse);
    }

    @Override
    public final CompletableFuture<ReadVariableValueResponse> onReadVariablesValue(ReadVariableValueRequest request)
    {
        final Set<String> propertyIds = new HashSet<>();
        final Set<UaChildId> subPropertyIds = new HashSet<>();

        for (UaChildId item: request.getVariableIds())
        {
            if (null == item.getSubElementName())
            {
                propertyIds.add(item.getId());
            } else {
                subPropertyIds.add(item);
            }
        }

        ServiceContext context = new ServiceContext(request.getObjectId());
        ReadPropertyListValueRequest readPropertyValuesRequest = new ReadPropertyListValueRequest(
                context,
                propertyIds,
                subPropertyIds);

        return onReadPropertyValues(readPropertyValuesRequest).
                thenApply(readPropertyValuesResponse -> {
                    return new ReadVariableValueResponse(readPropertyValuesResponse.getResults()); });
    }

    @Override
    public final CompletableFuture<WriteVariableValueResponse> onWriteVariablesValue(WriteVariableValueRequest request)
    {
        Map<String, Variant> propertyIdAndValues = new HashMap<>();
        Map<UaChildId, Variant> subPropertyIdsAndValues = new HashMap<>();

        for (Map.Entry<UaChildId, Variant> item : request.getVariableValues().entrySet())
        {
            UaChildId childId = item.getKey();

            if (null == childId.getSubElementName())
            {
                UaNode memberNode = getMember(childId.getId());
                if (null == memberNode || NodeClass.Variable != memberNode.nodeClass()) continue;

                UaVariable variableToWrite = (UaVariable) memberNode;
                if ((AccessLevel.CurrentWrite.getValue() & variableToWrite.accessLevel()) == 0) continue;

                propertyIdAndValues.put(childId.getId(), item.getValue());
            } else {
                subPropertyIdsAndValues.put(childId, item.getValue());
            }
        }

        if (propertyIdAndValues.isEmpty() && subPropertyIdsAndValues.isEmpty())
        {
            return CompletableFuture.completedFuture(new WriteVariableValueResponse());
        }

        ServiceContext context = new ServiceContext(request.getObjectId());
        WritePropertyListValuesRequest writePropertyValuesRequest = new WritePropertyListValuesRequest(
                context,
                propertyIdAndValues,
                subPropertyIdsAndValues);

        return onWritePropertyValues(writePropertyValuesRequest)
                .thenApply(response -> {
                    return new WriteVariableValueResponse(response.getResults());
                });
    }

    private BrowseObjectResponse processBrowseObjectChildrenResponse(GetObjectElementListResponse response)
    {
        List<UaReferenceDescriptor> childDescriptors = new ArrayList<>();

        for (ObjectElementDescriptor item: response.getElements())
        {
            UaReferenceDescriptor descriptor = new UaReferenceDescriptor(
                    item.getId(),
                    NodeClass.Object,
                    item.getId(),
                    item.getDisplayName(),
                    item.getTypeId(),
                    NodeIds.HasComponent,
                    true);

            childDescriptors.add(descriptor);
        }

        return new BrowseObjectResponse(
                childDescriptors,
                response.containsMoreData(),
                UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK);
    }

    private BrowseObjectResponse processBrowseObjectChildrenResponse(GetPropertyElementListResponse response)
    {
        List<UaReferenceDescriptor> childDescriptors = new ArrayList<>();

        for (PropertyElementDescriptor item: response.getElements())
        {
            UaReferenceDescriptor descriptor = new UaReferenceDescriptor(
                    item.getId(),
                    NodeClass.Variable,
                    item.getId(),
                    item.getDisplayName(),
                    item.getTypeId(),
                    NodeIds.HasComponent,
                    true);

            childDescriptors.add(descriptor);
        }

        return new BrowseObjectResponse(
                childDescriptors,
                response.containsMoreData(),
                UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK);
    }

    private BrowseMemberResponse processBrowseMemberChildren(GetPropertySubElementsResponse response)
    {
        List<UaReferenceDescriptor> childDescriptors = new ArrayList<>();

        for (String item: response.getSubElementNames())
        {
            UaReferenceDescriptor descriptor = new UaReferenceDescriptor(
                    item,
                    NodeClass.Variable,
                    item,
                    new LocalizedText(item),
                    NodeIds.PropertyType,
                    NodeIds.HasProperty,
                    true);

            childDescriptors.add(descriptor);
        }

        return new BrowseMemberResponse(childDescriptors);
    }

    private ReadMemberAttributeResponse processReadMemberAttributeResponse(GetPropertyDescriptorResponse response)
    {
        return new ReadMemberAttributeResponse(
                NodeClass.Variable,
                response.getDisplayName(),
                response.getDescription(),
                response.getDataTypeId(),
                response.getValueRank(),
                response.getAccessLevel(),
                response.getHistorizing());
    }
}
