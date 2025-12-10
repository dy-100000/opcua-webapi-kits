package org.opcfoundation.webserver.service.message.reactiveobject;

import org.opcfoundation.webserver.types.common.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.common.UaReferenceDescriptor;

import java.util.List;

public class BrowseObjectResponse {
    private final List<UaReferenceDescriptor> children;
    private final boolean containsMoreData;
    private final int taskMask;

    public BrowseObjectResponse(
            List<UaReferenceDescriptor> children,
            boolean containsMoreData)
    {
        this.children = children;
        this.containsMoreData = containsMoreData;
        this.taskMask = UaBrowseAdditionalInfo.ALL_TASK;
    }

    public BrowseObjectResponse(
            List<UaReferenceDescriptor> children,
            boolean containsMoreData,
            int taskMask)
    {
        this.children = children;
        this.containsMoreData = containsMoreData;
        this.taskMask = taskMask;
    }

    public List<UaReferenceDescriptor> getChildren() {
        return children;
    }

    public boolean containsMoreData() {
        return containsMoreData;
    }

    public int getTaskMask() {
        return taskMask;
    }
}
