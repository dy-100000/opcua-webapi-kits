package org.opcfoundation.webserver.service.message.reactiveobject;

import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webserver.types.common.UaObjectId;
import org.opcfoundation.webserver.types.common.UaChildId;

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
