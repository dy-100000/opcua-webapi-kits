package org.opcfoundation.webserver.types.digitaltwin;

import org.opcfoundation.webserver.types.common.UaObjectId;

import java.util.ArrayList;
import java.util.List;

public class ObjectServiceContext {
    private final UaObjectId objectId;

    public ObjectServiceContext(UaObjectId objectId)
    {
        this.objectId = objectId;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public String getCurrentObjectName()
    {
        return (null == objectId.getInstance()) ? objectId.getId() : objectId.getInstance().browseName();
    }

    /*
    public List<String> getCurrentObjectPath()
    {
        // To be implemented
        return new ArrayList<>();
    }*/
}
