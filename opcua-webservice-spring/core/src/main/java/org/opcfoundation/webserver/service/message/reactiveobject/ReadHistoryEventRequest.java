package org.opcfoundation.webserver.service.message.reactiveobject;

import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.opcfoundation.webserver.types.common.UaObjectId;

public class ReadHistoryEventRequest {
    private final UaObjectId objectId;
    private final UaStructuredType details;
    private final int offset;

    public ReadHistoryEventRequest(
            UaObjectId objectId,
            UaStructuredType details,
            int offset)
    {
        this.objectId = objectId;
        this.details = details;
        this.offset = offset;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public UaStructuredType getDetails() {
        return details;
    }

    public int getOffset() {
        return offset;
    }
}
