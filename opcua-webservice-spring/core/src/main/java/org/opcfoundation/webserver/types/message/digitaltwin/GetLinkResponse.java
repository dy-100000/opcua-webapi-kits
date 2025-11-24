package org.opcfoundation.webserver.types.message.digitaltwin;

import org.opcfoundation.webserver.types.ReferenceTargetDescriptor;

import java.util.List;

public class GetLinkResponse {
    private final List<ReferenceTargetDescriptor> targets;
    private final boolean containsMoreData;

    public GetLinkResponse(
            List<ReferenceTargetDescriptor> targets,
            boolean containsMoreData)
    {
        this.targets = targets;
        this.containsMoreData = containsMoreData;
    }

    public List<ReferenceTargetDescriptor> getTargets() {
        return targets;
    }

    public boolean containsMoreData()
    {
        return containsMoreData;
    }
}
