package org.opcfoundation.webserver.types.message;

import org.opcfoundation.webserver.types.UaChildVariableDescriptor;

import java.util.List;

@Deprecated
public class GetVariableDirectoryChildResponse {
    private final List<UaChildVariableDescriptor> children;
    private final boolean containsMoreData;

    public GetVariableDirectoryChildResponse(
            List<UaChildVariableDescriptor> children,
            boolean containsMoreData)
    {
        this.children = children;
        this.containsMoreData = containsMoreData;
    }

    public List<UaChildVariableDescriptor> getChildren() {
        return children;
    }

    public boolean containsMoreData()
    {
        return containsMoreData;
    }
}
