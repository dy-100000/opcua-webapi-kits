package org.opcfoundation.webserver.service.message.reactiveobject;

import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webserver.addressspace.nodes.UaVariableType;
import org.opcfoundation.webserver.types.common.UaChildId;
import org.opcfoundation.webserver.types.common.UaObjectId;
import org.springframework.lang.Nullable;

public class ReadHistoryDataRequest {
    private final UaObjectId objectId;
    private final UaChildId childId;
    private UaStructuredType details;
    private final int offset;

    public ReadHistoryDataRequest(
            UaObjectId objectId,
            UaChildId childId,
            UaStructuredType details,
            int offset)
    {
        this.objectId = objectId;
        this.childId = childId;
        this.details = details;
        this.offset = offset;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public UaChildId getChildId() {
        return childId;
    }

    public UaStructuredType getDetails() {
        return details;
    }

    public int getOffset() {
        return offset;
    }
}
