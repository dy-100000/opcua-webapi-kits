package org.opcfoundation.webserver.addressspace.transactions;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.addressspace.models.UaObjectType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerWebService;
import org.opcfoundation.webserver.addressspace.nodes.UaNode;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.addressspace.nodes.UaVariable;
import org.opcfoundation.webserver.service.transactions.UaReadTransaction;
import org.opcfoundation.webserver.types.UaObjectId;
import org.opcfoundation.webserver.types.UaObjectIdentifier;
import org.opcfoundation.webserver.types.UaChildId;
import org.opcfoundation.webserver.types.message.ReadVariableValueRequest;
import org.opcfoundation.webserver.types.message.ReadVariableValueResponse;
import org.opcfoundation.webapi.service.types.ReadContext;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class UaReadVariableValueTransaction extends UaReadTransaction {
    private final UaObjectIdentifier objectId;
    private final List<UaChildId> childIds;
    private @Nullable Map<UaChildId,Variant> methodArguments;
    private final NodeManagerWebService nodeManager;

    public UaReadVariableValueTransaction(
            ReadContext context,
            UaObjectIdentifier objectId,
            Map<Integer, UaChildId> handleIdsAndChildIds,
            NodeManagerWebService nodeManager)
    {
        super(context,new ArrayList<>(handleIdsAndChildIds.keySet()));
        this.objectId = objectId;
        this.childIds = new ArrayList<>(handleIdsAndChildIds.values());
        this.methodArguments = null;
        this.nodeManager = nodeManager;
    }

    public CompletableFuture<Void> execute()
    {
        try {
            UaObjectType objectType = nodeManager.findObjectType(objectId);
            if (null == objectType) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            ReadVariableValueRequest request = getReadVariableRequest(objectType);

            if (null == request)
            {
                return CompletableFuture.supplyAsync(() -> {
                    setResults(new ReadVariableValueResponse());
                    return null;
                });
            }

            return objectType.onReadVariablesValue(request).
                        thenAccept(this::setResults).
                        exceptionally(ex -> buildErrorResponse(ex.getCause()));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(buildErrorResponse(e));
        }
    }

    private void setResults(ReadVariableValueResponse response)
    {
        if (null != methodArguments)
        {
            for (Map.Entry<UaChildId,Variant> item: methodArguments.entrySet())
            {
                response.setValue(item.getKey(), item.getValue());
            }
        }

        DateTime sourceTimestamp = DateTime.NULL_VALUE;
        DateTime serverTimestamp = DateTime.NULL_VALUE;

        if (TimestampsToReturn.Source == timestampsToReturn)
        {
            sourceTimestamp = DateTime.now();
        } else if (TimestampsToReturn.Server == timestampsToReturn) {
            serverTimestamp =  DateTime.now();
        } else if (TimestampsToReturn.Both == timestampsToReturn) {
            sourceTimestamp = DateTime.now();
            serverTimestamp = sourceTimestamp;
        }

        for (UaChildId item: childIds)
        {
            DataValue result = response.getResults().get(item);

            if (null != result)
            {
                results.add(new DataValue(
                        result.value(),
                        result.statusCode(),
                        (result.statusCode().isBad()) ? DateTime.NULL_VALUE : sourceTimestamp,
                        (result.statusCode().isBad()) ? DateTime.NULL_VALUE : serverTimestamp));
            } else {
                results.add(new DataValue(Variant.NULL_VALUE, StatusCode.of(StatusCodes.Bad_NotReadable), null));
            }
        }
    }

    protected final @Nullable ReadVariableValueRequest getReadVariableRequest(UaObjectType objectType)
    {
        Set<UaChildId> variableIds = new HashSet<>();

        // Get argument value to read
        for (UaChildId item : childIds)
        {
            if (null != item.getSubElementId() &&
                    (item.getSubElementId().equals("InputArguments") || item.getSubElementId().equals("OutputArguments")))
            {
                UaNode memberNode = objectType.getMember(item.getPathId());

                if (null != memberNode &&
                        NodeClass.Method == memberNode.nodeClass())
                {
                    UaNode argumentNode = memberNode.getMember(item.getSubElementId());
                    if (null != argumentNode && argumentNode.nodeClass() == NodeClass.Variable)
                    {
                        if (null == methodArguments) methodArguments = new HashMap<>();
                        methodArguments.put(item, ((UaVariable)argumentNode).value());
                        continue;
                    }
                }
            }

            variableIds.add(item);
        }

        if (variableIds.isEmpty()) return null;

        UaObject instanceDeclaration = nodeManager.findInstanceDeclaration(objectId);
        return new ReadVariableValueRequest(
                new UaObjectId(
                        objectId.getId(),
                        instanceDeclaration),
                variableIds);
    }


}
