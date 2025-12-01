package org.opcfoundation.webserver.types.message.digitaltwin;

import org.opcfoundation.webserver.types.DigitalTwinDescriptor;
import org.opcfoundation.webserver.types.ReferenceTargetDescriptor;

import java.util.ArrayList;
import java.util.List;

public class GetLinkResponse {
    private final List<ReferenceTargetDescriptor> targets;
    private boolean containsMoreData;

    public GetLinkResponse()
    {
        this.targets = new ArrayList<>();
        containsMoreData = false;
    }

    public void add(ReferenceTargetDescriptor descriptor)
    {
        targets.add(descriptor);
    }

    public List<ReferenceTargetDescriptor> getTargets() {
        return targets;
    }

    public boolean containsMoreData()
    {
        return containsMoreData;
    }

    public void setContainsMoreData(boolean containsMoreData) {
        this.containsMoreData = containsMoreData;
    }
}
