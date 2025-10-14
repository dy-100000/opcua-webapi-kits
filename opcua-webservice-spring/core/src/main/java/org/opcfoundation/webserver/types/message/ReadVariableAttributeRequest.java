package org.opcfoundation.webserver.types.message;

import org.opcfoundation.webserver.types.UaObjectId;

public class ReadVariableAttributeRequest {
    private final UaObjectId objectId;
    private final String variableId;

    public ReadVariableAttributeRequest(
            UaObjectId objectId,
            String variableId)
    {
        this.objectId = objectId;
        this.variableId = variableId;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public String getVariableId() {
        return variableId;
    }
}
