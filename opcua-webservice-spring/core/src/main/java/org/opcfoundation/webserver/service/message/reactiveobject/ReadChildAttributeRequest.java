package org.opcfoundation.webserver.service.message.reactiveobject;

import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.UaVariableType;
import org.opcfoundation.webserver.types.common.UaChildId;
import org.opcfoundation.webserver.types.common.UaObjectId;

public class ReadChildAttributeRequest {
    private final UaObjectId objectId;
    private final UaChildId childId;
    private final @Nullable UaVariableType childVariableType;

    public ReadChildAttributeRequest(
            UaObjectId objectId,
            UaChildId childId,
            @Nullable UaVariableType childVariableType)
    {
        this.objectId = objectId;
        this.childId = childId;
        this.childVariableType = childVariableType;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public UaChildId getChildId() {
        return childId;
    }

    public @Nullable UaVariableType getChildVariableType() {
        return childVariableType;
    }
}
