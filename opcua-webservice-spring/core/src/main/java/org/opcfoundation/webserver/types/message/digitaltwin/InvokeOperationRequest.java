package org.opcfoundation.webserver.types.message.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webserver.types.ServiceContext;

import java.util.List;

public class InvokeOperationRequest {
    private final String id;
    private final String operationName;
    private final List<Variant> inputArguments;
    private final ServiceContext context;

    public InvokeOperationRequest(
            ServiceContext context,
            String operationName,
            List<Variant> inputArguments)
    {
        this.id = context.getObjectId().getId();
        this.operationName = operationName;
        this.inputArguments = inputArguments;
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public String getOperationName() {
        return operationName;
    }

    public List<Variant> getInputArguments() {
        return inputArguments;
    }

    public ServiceContext getContext() {
        return context;
    }
}
