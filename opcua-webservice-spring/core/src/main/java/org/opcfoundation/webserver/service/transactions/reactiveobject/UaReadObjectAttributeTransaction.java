package org.opcfoundation.webserver.service.transactions.reactiveobject;

import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.opcfoundation.webserver.addressspace.reactiveobject.UaReactiveObjectType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerReactiveObject;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.service.transactions.base.UaReadTransaction;
import org.opcfoundation.webserver.types.common.UaObjectId;
import org.opcfoundation.webserver.types.common.UaObjectIdentifier;
import org.opcfoundation.webserver.service.message.reactiveobject.ReadObjectAttributeRequest;
import org.opcfoundation.webserver.service.message.reactiveobject.ReadObjectAttributeResponse;
import org.opcfoundation.webapi.service.types.ReadContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UaReadObjectAttributeTransaction extends UaReadTransaction {
    private final UaObjectIdentifier objectId;
    private final NodeManagerReactiveObject nodeManager;

    public UaReadObjectAttributeTransaction(
            ReadContext context,
            List<Integer> handleIds,
            UaObjectIdentifier objectId,
            NodeManagerReactiveObject nodeManager)
    {
        super(context,handleIds);
        this.objectId = objectId;
        this.nodeManager = nodeManager;
    }

    public CompletableFuture<Void> execute()
    {
        // System.out.println("Read object" + objectId);

        try {
            UaReactiveObjectType objectType = nodeManager.findObjectType(objectId);
            if (null == objectType) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            UaObject instanceDeclaration = nodeManager.findInstanceDeclaration(objectId);

            ReadObjectAttributeRequest request = new ReadObjectAttributeRequest(new UaObjectId(
                    objectId.getId(),
                    instanceDeclaration));

            return objectType.onReadObjectAttributes(request).
                    thenAccept(this::setResults).
                    exceptionally(ex -> buildErrorResponse(ex.getCause()));
        } catch (Exception e) {
            buildErrorResponse(e);
        }

        return CompletableFuture.completedFuture(null);
    }

    private void setResults(ReadObjectAttributeResponse response)
    {
        List<ReadValueId> nodesToRead = getRequestedItems();

        for (ReadValueId item: nodesToRead)
        {
            Variant value = Variant.NULL_VALUE;
            StatusCode statusCode = StatusCode.GOOD;

            if (item.getAttributeId().intValue() == AttributeId.DisplayName.id())
            {
                value = new Variant(response.getDisplayName());
            } else if (item.getAttributeId().intValue() == AttributeId.Description.id()) {
                if (response.getDescription().isNull())
                {
                    statusCode = StatusCode.of(StatusCodes.Bad_NodeAttributesInvalid);
                } else {
                    value = new Variant(response.getDescription());
                }
            } else if (item.getAttributeId().intValue() == AttributeId.NodeId.id()) {
                value = new Variant(item.getNodeId());
            } else if (item.getAttributeId().intValue() == AttributeId.BrowseName.id()) {
                value = new Variant(new QualifiedName(0, response.getBrowseName()));
            } else if (item.getAttributeId().intValue() == AttributeId.NodeClass.id()) {
                value = new Variant(NodeClass.Object.getValue());
            } else if (item.getAttributeId().intValue() == AttributeId.WriteMask.id()) {
                value = new Variant(UInteger.valueOf(0));
            } else if (item.getAttributeId().intValue() == AttributeId.UserWriteMask.id()) {
                value = new Variant(UInteger.valueOf(0));
            } else {
                statusCode = StatusCode.of(StatusCodes.Bad_AttributeIdInvalid);
            }

            results.add(new DataValue(
                    value,
                    statusCode,
                    null,
                    null));
        }
    }
}
