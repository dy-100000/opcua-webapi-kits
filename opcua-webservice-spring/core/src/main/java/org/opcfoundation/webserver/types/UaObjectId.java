package org.opcfoundation.webserver.types;

import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;

public class UaObjectId {
    private final String id;
    private final @Nullable UaObject instance;

    public UaObjectId(String id, @Nullable UaObject instance)
    {
        this.id = id;
        this.instance = instance;
    }

    public String getId() {
        return id;
    }

    public @Nullable UaObject getInstance() {
        return instance;
    }

    @Override
    public String toString() {
        String ret = "Id: " + id;

        if (null != instance)
        {
            ret += " Path: " + instance.browseName();
        }

        return ret;
    }
}
