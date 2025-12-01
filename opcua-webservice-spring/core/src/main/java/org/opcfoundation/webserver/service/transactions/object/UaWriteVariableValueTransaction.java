package org.opcfoundation.webserver.service.transactions.object;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webserver.addressspace.models.UaObjectType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerWebService;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.service.transactions.base.UaWriteTransaction;
import org.opcfoundation.webserver.types.UaObjectId;
import org.opcfoundation.webserver.types.UaObjectIdentifier;
import org.opcfoundation.webserver.types.UaChildId;
import org.opcfoundation.webserver.types.WriteVariableValue;
import org.opcfoundation.webserver.types.message.WriteVariableValueRequest;
import org.opcfoundation.webserver.types.message.WriteVariableValueResponse;
import org.opcfoundation.webapi.service.types.WriteContext;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class UaWriteVariableValueTransaction extends UaWriteTransaction {
    private final UaObjectIdentifier objectId;
    private final List<WriteVariableValue> variableValues;
    private final NodeManagerWebService nodeManager;

    public UaWriteVariableValueTransaction(
            WriteContext context,
            UaObjectIdentifier objectId,
            Map<Integer, WriteVariableValue> handleIdsAndVariableValues,
            NodeManagerWebService nodeManager)
    {
        super(context,new ArrayList<>(handleIdsAndVariableValues.keySet()));
        this.objectId = objectId;
        this.variableValues = new ArrayList<>(handleIdsAndVariableValues.values());
        this.nodeManager = nodeManager;
    }

    @Override
    public CompletableFuture<Void> execute()
    {
        try {
            UaObjectType objectType = nodeManager.findObjectType(objectId);
            if (null == objectType) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            UaObject instanceDeclaration = nodeManager.findInstanceDeclaration(objectId);
            Map<UaChildId, Variant> valuesToWrite = new HashMap<>();

            for (WriteVariableValue item: variableValues)
            {
                if (!item.getValue().statusCode().isGood()) continue;
                valuesToWrite.put(item.getVariableId(), item.getValue().getValue());
            }

            WriteVariableValueRequest request = new WriteVariableValueRequest(
                    new UaObjectId(
                            objectId.getId(),
                            instanceDeclaration),
                    valuesToWrite);

            return objectType.onWriteVariablesValue(request).
                        thenAccept(this::setResults).
                        exceptionally(ex -> buildErrorResponse(ex.getCause()));


        } catch (Exception e) {
            buildErrorResponse(e);
        }

        return CompletableFuture.completedFuture(null);
    }

    private void setResults(WriteVariableValueResponse response) {
        for (WriteVariableValue item: variableValues)
        {
            if (!item.getValue().statusCode().isGood())
            {
                results.add(StatusCode.of(StatusCodes.Bad_NotWritable));
                continue;
            }

            StatusCode result = response.getResults().get(item.getVariableId());
            results.add(Objects.requireNonNullElseGet(result, () -> StatusCode.of(StatusCodes.Bad_NotWritable)));
        }
    }


}
