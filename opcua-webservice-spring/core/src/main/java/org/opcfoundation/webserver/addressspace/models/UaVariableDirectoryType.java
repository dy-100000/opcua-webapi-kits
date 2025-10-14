package org.opcfoundation.webserver.addressspace.models;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.webserver.addressspace.nodes.UaInstanceNode;
import org.opcfoundation.webserver.addressspace.nodes.UaNode;
import org.opcfoundation.webserver.addressspace.nodes.UaReferenceType;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.types.UaChildDescriptor;
import org.opcfoundation.webserver.types.UaChildVariableDescriptor;
import org.opcfoundation.webserver.types.message.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class UaVariableDirectoryType extends UaSubmodelType {
    public UaVariableDirectoryType(
            String objectTypeId,
            LocalizedText displayName,
            NodeManager nodeManager)
    {
        super(objectTypeId, displayName, UaObjectTypes.FolderType, nodeManager);
    }

    // Developer need to override this method to return child variable information
    public abstract CompletableFuture<GetVariableDirectoryChildResponse> getChildren(GetVariableDirectoryChildRequest request);

    // Developer need to override this method to return variable descriptions
    public abstract CompletableFuture<ReadVariableAttributeResponse> getChildVariableDescriptor(ReadVariableAttributeRequest request);

    // Developer need to override this method to process read request
    public abstract CompletableFuture<ReadVariableValueResponse> getVariableValues(ReadVariableValueRequest request);

    // If developer need to process write value request, this method can be overridden
    public CompletableFuture<WriteVariableValueResponse> setVariableValues(WriteVariableValueRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    @Override
    public final CompletableFuture<BrowseChildResponse> onBrowseObjectChildren(BrowseChildrenRequest request)
    {
        boolean isReferenceAllowed = false;

        UaNode referenceType = nodeManager.getNode(NodeIds.HasComponent);
        if (null != referenceType && referenceType.nodeClass() == NodeClass.ReferenceType)
        {
            isReferenceAllowed = ((UaReferenceType)referenceType).isSubtypeOf(request.getReferenceId());
        }

        if (!isReferenceAllowed ||
                (request.getNodeClassMask() & NodeClass.Variable.getValue()) == 0)
            return CompletableFuture.completedFuture(new BrowseChildResponse(new ArrayList<>(), false));

        GetVariableDirectoryChildRequest getChildRequest = new GetVariableDirectoryChildRequest(
                request.getObjectId(),
                request.getLimit(),
                request.getOffset());

        return getChildren(getChildRequest).
                thenApply(this::processBrowseChildResponse);
    }

    @Override
    public final CompletableFuture<ReadChildAttributeResponse> onReadChildAttributes(ReadChildAttributeRequest request)
    {
        if (null != request.getChildId().getSubElementId())
        {
            return CompletableFuture.supplyAsync(() -> {
                if (null == request.getChildVariableType()) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

                UaInstanceNode subVariable = request.getChildVariableType().getMember(request.getChildId().getSubElementId());
                if (null == subVariable || NodeClass.Variable != subVariable.nodeClass())
                    throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

                return new ReadChildAttributeResponse(subVariable);
            });
        }

        ReadVariableAttributeRequest readAttributeRequest = new ReadVariableAttributeRequest(
                request.getObjectId(),
                request.getChildId().getPathId());

        return getChildVariableDescriptor(readAttributeRequest).thenApply(this::processReadVariableAttributesResponse);
    }

    @Override
    public final CompletableFuture<ReadVariableValueResponse> onReadVariablesValue(ReadVariableValueRequest request)
    {
        return getVariableValues(request);
    }

    @Override
    public final CompletableFuture<WriteVariableValueResponse> onWriteVariablesValue(WriteVariableValueRequest request)
    {
        return setVariableValues(request);
    }

    private BrowseChildResponse processBrowseChildResponse(GetVariableDirectoryChildResponse response)
    {
        List<UaChildDescriptor> childDescriptors = new ArrayList<>();

        for (UaChildVariableDescriptor item: response.getChildren())
        {
            UaChildDescriptor descriptor = new UaChildDescriptor(
                    item.getId(),
                    NodeClass.Variable,
                    item.getId(),
                    item.getDisplayName(),
                    item.getTypeId(),
                    NodeIds.HasComponent);

            childDescriptors.add(descriptor);
        }

        return new BrowseChildResponse(childDescriptors, response.containsMoreData());
    }

    private ReadChildAttributeResponse processReadVariableAttributesResponse(ReadVariableAttributeResponse response)
    {
        return new ReadChildAttributeResponse(
                NodeClass.Variable,
                response.getDisplayName(),
                response.getDescription(),
                response.getDataTypeId(),
                response.getValueRank(),
                response.getAccessLevel(),
                response.getHistorizing());
    }
}
