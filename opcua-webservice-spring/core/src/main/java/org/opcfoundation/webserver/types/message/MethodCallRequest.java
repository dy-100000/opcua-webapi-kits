package org.opcfoundation.webserver.types.message;

import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webserver.types.UaObjectId;

import java.util.List;

public class MethodCallRequest {
    private final UaObjectId objectId;
    private final String methodName;
    private final List<Variant> inputArguments;

    public MethodCallRequest(
            UaObjectId objectId,
            String methodName,
            List<Variant> inputArguments)
    {
        this.objectId = objectId;
        this.methodName = methodName;
        this.inputArguments = inputArguments;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<Variant> getInputArguments() {
        return inputArguments;
    }
}
