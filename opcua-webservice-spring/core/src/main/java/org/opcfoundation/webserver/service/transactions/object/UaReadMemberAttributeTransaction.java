package org.opcfoundation.webserver.service.transactions.object;

import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.opcfoundation.webserver.addressspace.models.UaObjectType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerWebService;
import org.opcfoundation.webserver.addressspace.nodes.*;
import org.opcfoundation.webserver.service.transactions.base.UaReadTransaction;
import org.opcfoundation.webserver.types.UaChildId;
import org.opcfoundation.webapi.service.types.ReadContext;
import org.opcfoundation.webserver.types.UaChildIdentifier;
import org.opcfoundation.webserver.types.UaObjectId;
import org.opcfoundation.webserver.types.UaObjectIdentifier;
import org.opcfoundation.webserver.types.message.ReadMemberAttributeRequest;
import org.opcfoundation.webserver.types.message.ReadMemberAttributeResponse;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class UaReadMemberAttributeTransaction extends UaReadTransaction {
    private final UaObjectIdentifier objectId;
    private final UaChildIdentifier memberId;
    private final NodeManagerWebService nodeManager;

    public UaReadMemberAttributeTransaction(
            ReadContext context,
            UaObjectIdentifier objectId,
            UaChildIdentifier memberId,
            List<Integer> handleIds,
            NodeManagerWebService nodeManager)
    {
        super(context,handleIds);
        this.objectId = objectId;
        this.memberId = memberId;
        this.nodeManager = nodeManager;
    }

    public CompletableFuture<Void> execute()
    {
        try {
            UaObjectType objectType = nodeManager.findObjectType(objectId);
            if (null == objectType) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            UaObject instanceDeclaration = nodeManager.findInstanceDeclaration(objectId);
            ReadMemberAttributeRequest request = new ReadMemberAttributeRequest(
                    new UaObjectId(
                            objectId.getId(),
                            instanceDeclaration),
                    new UaChildId(memberId.getPath(), memberId.getPathL2()));

            return objectType.onReadMemberAttributes(request).
                    thenAccept(this::setResults).
                    exceptionally(ex -> buildErrorResponse(ex.getCause()));

        } catch (Exception e) {
            return CompletableFuture.completedFuture(buildErrorResponse(e));
        }
    }

    private void setResults(ReadMemberAttributeResponse response)
    {
        List<ReadValueId> nodesToRead = getRequestedItems();

        for (ReadValueId item: nodesToRead)
        {
            Variant value = Variant.NULL_VALUE;
            StatusCode statusCode = StatusCode.GOOD;

            if (item.getAttributeId().intValue() == AttributeId.NodeClass.id()) {
                value = new Variant(response.getNodeClass().getValue());
            } else if (item.getAttributeId().intValue() == AttributeId.DisplayName.id()) {
                value = new Variant(response.getDisplayName());
            } else if (item.getAttributeId().intValue() == AttributeId.Description.id()) {
                if (!response.getDescription().isNull())
                {
                    value = new Variant(response.getDescription());
                } else {
                    statusCode = StatusCode.of(StatusCodes.Bad_NodeAttributesInvalid);
                }
            } else if (item.getAttributeId().intValue() == AttributeId.DataType.id()) {
                if (null != response.getDataTypeId()) {
                    value = new Variant(response.getDataTypeId());
                } else {
                    statusCode = StatusCode.of(StatusCodes.Bad_NodeAttributesInvalid);
                }
            } else if (item.getAttributeId().intValue() == AttributeId.ValueRank.id()) {
                if (null != response.getValueRank()) {
                    value = new Variant(response.getValueRank());
                } else {
                    statusCode = StatusCode.of(StatusCodes.Bad_NodeAttributesInvalid);
                }
            } else if (item.getAttributeId().intValue() == AttributeId.AccessLevel.id()) {
                if (null != response.getAccessLevel()) {
                    value = new Variant(UByte.valueOf(response.getAccessLevel()));
                } else {
                    statusCode = StatusCode.of(StatusCodes.Bad_NodeAttributesInvalid);
                }
            } else if (item.getAttributeId().intValue() == AttributeId.UserAccessLevel.id()) {
                if (null != response.getAccessLevel()) {
                    value = new Variant(UByte.valueOf(response.getAccessLevel()));
                } else {
                    statusCode = StatusCode.of(StatusCodes.Bad_NodeAttributesInvalid);
                }
            } else if (item.getAttributeId().intValue() == AttributeId.Historizing.id()) {
                if (null != response.getHistorizing()) {
                    value = new Variant(response.getHistorizing());
                } else {
                    statusCode = StatusCode.of(StatusCodes.Bad_NodeAttributesInvalid);
                }
            } else if (item.getAttributeId().intValue() == AttributeId.NodeId.id()) {
                value = new Variant(item.getNodeId());
            } else if (item.getAttributeId().intValue() == AttributeId.BrowseName.id()) {
                value = new Variant(new QualifiedName(0, memberId.getPath()));
            } else if (item.getAttributeId().intValue() == AttributeId.Executable.id()) {
                value = new Variant(Boolean.TRUE);
            } else if (item.getAttributeId().intValue() == AttributeId.UserExecutable.id()) {
                value = new Variant(Boolean.TRUE);
            } else if (item.getAttributeId().intValue() == AttributeId.WriteMask.id()) {
                value = new Variant(UInteger.valueOf(0));
            } else if (item.getAttributeId().intValue() == AttributeId.UserWriteMask.id()) {
                value = new Variant(UInteger.valueOf(0));
            }  else {
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
