package org.opcfoundation.webserver.types.message;

import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webserver.types.UaObjectId;
import org.opcfoundation.webserver.types.UaChildId;

import java.util.Map;

public class WriteVariableValueRequest {
    private final UaObjectId objectId;
    private final Map<UaChildId, Variant> variableValues;

    public WriteVariableValueRequest(
            UaObjectId objectId,
            Map<UaChildId, Variant> variableValues)
    {
        this.objectId = objectId;
        this.variableValues = variableValues;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public Map<UaChildId, Variant> getVariableValues() {
        return variableValues;
    }
}
