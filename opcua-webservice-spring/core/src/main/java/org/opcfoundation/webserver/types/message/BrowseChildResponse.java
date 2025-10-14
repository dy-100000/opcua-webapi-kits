package org.opcfoundation.webserver.types.message;

import org.opcfoundation.webserver.types.UaChildDescriptor;

import java.util.List;

public class BrowseChildResponse {
    private List<UaChildDescriptor> children;
    private boolean containsMoreData;

    public BrowseChildResponse(
            List<UaChildDescriptor> children,
            boolean containsMoreData)
    {
        this.children = children;
        this.containsMoreData = containsMoreData;
    }

    public List<UaChildDescriptor> getChildren() {
        return children;
    }

    public boolean containsMoreData() {
        return containsMoreData;
    }
}
