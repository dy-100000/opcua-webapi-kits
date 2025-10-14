package org.opcfoundation.webserver.types.message;

import org.opcfoundation.webserver.types.UaChildObjectDescriptor;

import java.util.List;

public class GetObjectDirectoryChildResponse {
    private final List<UaChildObjectDescriptor> children;
    private final boolean containsMoreData;

    public GetObjectDirectoryChildResponse(
            List<UaChildObjectDescriptor> children,
            boolean containsMoreData)
    {
        this.children = children;
        this.containsMoreData = containsMoreData;
    }

    public List<UaChildObjectDescriptor> getChildren() {
        return children;
    }

    public boolean containsMoreData()
    {
        return containsMoreData;
    }
}
