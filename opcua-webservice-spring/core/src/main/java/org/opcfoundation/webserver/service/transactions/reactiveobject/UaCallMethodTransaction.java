package org.opcfoundation.webserver.service.transactions.reactiveobject;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.opcfoundation.webserver.addressspace.reactiveobject.UaReactiveObjectType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerReactiveObject;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.service.transactions.base.UaMethodCallTransaction;
import org.opcfoundation.webserver.types.common.UaInstanceIdentifier;
import org.opcfoundation.webserver.types.common.UaObjectId;
import org.opcfoundation.webserver.service.message.reactiveobject.MethodCallRequest;
import org.opcfoundation.webapi.service.types.CallContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UaCallMethodTransaction extends UaMethodCallTransaction {
    private final NodeManagerReactiveObject nodeManager;
    private final UaInstanceIdentifier objectIdentifier;
    private final UaInstanceIdentifier methodIdentifier;

    public UaCallMethodTransaction(
            CallContext callContext,
            int index,
            UaInstanceIdentifier objectIdentifier,
            UaInstanceIdentifier methodIdentifier,
            NodeManagerReactiveObject nodeManager)
    {
        super(callContext, index);
        this.objectIdentifier = objectIdentifier;
        this.methodIdentifier = methodIdentifier;
        this.nodeManager = nodeManager;
    }

    public CompletableFuture<Void> execute()
    {
        try
        {
            UaReactiveObjectType objectType = nodeManager.findObjectType(objectIdentifier.getObjectId());
            UaObject instanceDeclaration = nodeManager.findInstanceDeclaration(objectIdentifier.getObjectId());
            if (null == objectType || null == methodIdentifier.getChildId()) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            CallMethodRequest callRequest = getItem();
            List<Variant> inputArguments = new ArrayList<>();

            if (null != callRequest.getInputArguments())
            {
                inputArguments = Arrays.asList(callRequest.getInputArguments());
            }

            MethodCallRequest request = new MethodCallRequest(
                    new UaObjectId(objectIdentifier.getObjectId().getId(), instanceDeclaration),
                    methodIdentifier.getChildId().getPath(),
                    inputArguments);

            return objectType.onMethodCall(request).
                        thenAccept(response-> outputArguments = response.getOutputArguments()).
                        exceptionally(ex -> buildErrorResponse(ex.getCause()));
        } catch (Exception e) {
            buildErrorResponse(e);
        }

        return CompletableFuture.completedFuture(null);
    }
}
