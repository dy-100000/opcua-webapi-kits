package org.opcfoundation.webserver.service.transactions.reactiveobject;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryData;
import org.opcfoundation.webapi.mapper.extensionobjects.ExtensionObjectEncoder;
import org.opcfoundation.webapi.service.types.HistoryReadContext;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerReactiveObject;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.addressspace.reactiveobject.UaReactiveObjectType;
import org.opcfoundation.webserver.service.message.reactiveobject.ReadHistoryDataRequest;
import org.opcfoundation.webserver.service.message.reactiveobject.ReadHistoryDataResponse;
import org.opcfoundation.webserver.service.transactions.base.UaHistoryReadTransaction;
import org.opcfoundation.webserver.types.common.UaChildId;
import org.opcfoundation.webserver.types.common.UaHistoryReadContinuationPoint;
import org.opcfoundation.webserver.types.common.UaInstanceIdentifier;
import org.opcfoundation.webserver.types.common.UaObjectId;

import java.util.concurrent.CompletableFuture;

public class UaReadDataHistoryTransaction extends UaHistoryReadTransaction {
    private final NodeManagerReactiveObject nodeManager;
    private final UaInstanceIdentifier objectIdentifier;
    private final int offset;

    public UaReadDataHistoryTransaction(
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
            if (null == objectIdentifier.getChildId() ||
                    null != objectIdentifier.getChildId().getPathL2() ||
                        null != objectIdentifier.getChildId().getMethodNode()) throw new UaRuntimeException(StatusCodes.Bad_NodeIdInvalid);

            ReadHistoryDataRequest request = new ReadHistoryDataRequest(
                    new UaObjectId(objectIdentifier.getObjectId().getId(), instanceDeclaration),
                    new UaChildId(objectIdentifier.getChildId().getPath()),
                    details,
                    offset);

            return objectType.onReadHistoryData(request).
                    thenAccept(this::setResults).
                    exceptionally(ex -> buildErrorResponse(ex.getCause()));
        } catch (Exception e) {
            buildErrorResponse(e);
        }

        return CompletableFuture.completedFuture(null);
    }

    private void setResults(ReadHistoryDataResponse response) {

        HistoryData historyDataValue = new HistoryData(response.getValues().toArray(new DataValue[0]));
        historyData = ExtensionObject.encode(ExtensionObjectEncoder.Encoder.getEncodingContext(),historyDataValue);

        if (response.getContainsMoreData() && !response.getValues().isEmpty())
        {
            UaHistoryReadContinuationPoint cp = new UaHistoryReadContinuationPoint();
            cp.setOffset(offset + response.getValues().size());
            continuationPoint = cp.toByteString();
        }
    }
}
