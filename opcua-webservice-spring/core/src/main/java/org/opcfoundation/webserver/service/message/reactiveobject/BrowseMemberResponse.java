package org.opcfoundation.webserver.service.message.reactiveobject;

import org.opcfoundation.webserver.types.common.UaReferenceDescriptor;

import java.util.List;

public class BrowseMemberResponse {
    private final List<UaReferenceDescriptor> children;

    public BrowseMemberResponse(List<UaReferenceDescriptor> children)
    {
        this.children = children;
    }

    public List<UaReferenceDescriptor> getChildren() {
        return children;
    }
}
