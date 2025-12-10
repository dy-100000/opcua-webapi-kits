package org.opcfoundation.webserver.service.message.reactiveobject;

import org.opcfoundation.webserver.types.common.UaObjectId;

public class ReadObjectAttributeRequest {
    private final UaObjectId objectId;

    public ReadObjectAttributeRequest(UaObjectId objectId)
    {
        this.objectId = objectId;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }
}
