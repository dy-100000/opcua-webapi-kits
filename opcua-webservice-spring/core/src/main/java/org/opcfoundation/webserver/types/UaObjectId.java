package org.opcfoundation.webserver.types;

import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;

public class UaObjectId {
    private final String id;
    private final @Nullable UaObject instanceDeclaration;

    public UaObjectId(String id, @Nullable UaObject instanceDeclaration)
    {
        this.id = id;
        this.instanceDeclaration = instanceDeclaration;
    }

    public String getId() {
        return id;
    }

    public @Nullable String getPathName()
    {
        return (null == instanceDeclaration) ? null : instanceDeclaration.browseName();
    }

    public @Nullable UaObject getInstanceDeclaration() {
        return instanceDeclaration;
    }

    @Override
    public String toString() {
        String ret = "Id: " + id;

        if (null != instanceDeclaration)
        {
            ret += " Path: " + instanceDeclaration.browseName();
        }

        return ret;
    }
}
