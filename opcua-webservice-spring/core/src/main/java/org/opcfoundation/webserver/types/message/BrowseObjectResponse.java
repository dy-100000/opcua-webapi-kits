package org.opcfoundation.webserver.types.message;

import org.opcfoundation.webserver.types.UaReferenceDescriptor;

import java.util.List;

public class BrowseObjectResponse {
    private List<UaReferenceDescriptor> children;
    private boolean containsMoreData;

    public BrowseObjectResponse(
            List<UaReferenceDescriptor> children,
            boolean containsMoreData)
    {
        this.children = children;
        this.containsMoreData = containsMoreData;
    }

    public List<UaReferenceDescriptor> getChildren() {
        return children;
    }

    public boolean containsMoreData() {
        return containsMoreData;
    }
}
