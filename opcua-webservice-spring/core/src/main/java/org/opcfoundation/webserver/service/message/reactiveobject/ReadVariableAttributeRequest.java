package org.opcfoundation.webserver.service.message.reactiveobject;

import org.opcfoundation.webserver.types.common.UaObjectId;

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
