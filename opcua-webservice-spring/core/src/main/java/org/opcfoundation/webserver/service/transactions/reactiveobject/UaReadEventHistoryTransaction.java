package org.opcfoundation.webserver.service.transactions.reactiveobject;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEvent;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList;
import org.opcfoundation.webapi.mapper.extensionobjects.ExtensionObjectEncoder;
import org.opcfoundation.webapi.service.types.HistoryReadContext;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerReactiveObject;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.addressspace.reactiveobject.UaReactiveObjectType;
import org.opcfoundation.webserver.service.message.reactiveobject.ReadHistoryEventRequest;
import org.opcfoundation.webserver.service.message.reactiveobject.ReadHistoryEventResponse;
import org.opcfoundation.webserver.service.transactions.base.UaHistoryReadTransaction;
import org.opcfoundation.webserver.types.common.UaHistoryReadContinuationPoint;
import org.opcfoundation.webserver.types.common.UaInstanceIdentifier;
import org.opcfoundation.webserver.types.common.UaObjectId;
import java.util.concurrent.CompletableFuture;

public class UaReadEventHistoryTransaction extends UaHistoryReadTransaction {
    private final NodeManagerReactiveObject nodeManager;
    private final UaInstanceIdentifier objectIdentifier;
    private final int offset;

    public UaReadEventHistoryTransaction(
            HistoryReadContext historyReadContext,
            int index,
            UaInstanceIdentifier objectIdentifier,
            NodeManagerReactiveObject nodeManager)
    {
        super(historyReadContext, index);
        this.objectIdentifier = objectIdentifier;
        this.nodeManager = nodeManager;

        if (getItem().getContinuationPoint().isNull())
        {
            offset = 0;
        } else {
            UaHistoryReadContinuationPoint cp = UaHistoryReadContinuationPoint.fromByteString(getItem().getContinuationPoint());
            if (null == cp)
            {
                offset = -1;
            } else {
                offset = cp.getOffset();
            }
        }
    }

    public CompletableFuture<Void> execute()
    {
        try
        {
            UaReactiveObjectType objectType = nodeManager.findObjectType(objectIdentifier.getObjectId());
            UaObject instanceDeclaration = nodeManager.findInstanceDeclaration(objectIdentifier.getObjectId());
            if (null == objectType) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
            if (0 > offset) throw new UaRuntimeException(StatusCodes.Bad_ContinuationPointInvalid);

            ReadHistoryEventRequest request = new ReadHistoryEventRequest(
                    new UaObjectId(objectIdentifier.getObjectId().getId(), instanceDeclaration),
                    details,
                    offset);

            return objectType.onReadHistoryEvent(request).
                    thenAccept(this::setResults).
                    exceptionally(ex -> buildErrorResponse(ex.getCause()));
        } catch (Exception e) {
            buildErrorResponse(e);
        }

        return CompletableFuture.completedFuture(null);
    }

    private void setResults(ReadHistoryEventResponse response) {
        HistoryEvent historyEvent = new HistoryEvent(response.getEvents().toArray(new HistoryEventFieldList[0]));
        historyData = ExtensionObject.encode(ExtensionObjectEncoder.Encoder.getEncodingContext(),historyEvent);

        if (response.getContainsMoreData() && !response.getEvents().isEmpty())
        {
            UaHistoryReadContinuationPoint cp = new UaHistoryReadContinuationPoint();
            cp.setOffset(offset + response.getEvents().size());
            continuationPoint = cp.toByteString();
        }
    }
}
