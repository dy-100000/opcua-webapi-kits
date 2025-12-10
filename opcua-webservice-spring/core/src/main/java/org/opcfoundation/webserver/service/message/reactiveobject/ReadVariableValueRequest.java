package org.opcfoundation.webserver.service.message.reactiveobject;

import org.opcfoundation.webserver.types.common.UaObjectId;
import org.opcfoundation.webserver.types.common.UaChildId;

import java.util.Set;

public class ReadVariableValueRequest {
    private final UaObjectId objectId;
    private final Set<UaChildId> variableIds;

    public ReadVariableValueRequest(
            UaObjectId objectId,
            Set<UaChildId> variableIds)
    {
        this.objectId = objectId;
        this.variableIds = variableIds;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public Set<UaChildId> getVariableIds() {
        return variableIds;
    }
}
