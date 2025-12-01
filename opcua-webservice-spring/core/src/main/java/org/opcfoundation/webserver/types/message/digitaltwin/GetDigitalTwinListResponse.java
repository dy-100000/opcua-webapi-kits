package org.opcfoundation.webserver.types.message.digitaltwin;

import org.opcfoundation.webserver.types.DigitalTwinDescriptor;

import java.util.ArrayList;
import java.util.List;

public class GetDigitalTwinListResponse {
    private final List<DigitalTwinDescriptor> digitalTwins;
    private boolean containsMoreData;

    public GetDigitalTwinListResponse()
    {
        this.digitalTwins = new ArrayList<>();
        this.containsMoreData = false;
    }

    public void add(DigitalTwinDescriptor descriptor)
    {
        digitalTwins.add(descriptor);
    }

    public List<DigitalTwinDescriptor> getDigitalTwins() {
        return digitalTwins;
    }

    public boolean containsMoreData()
    {
        return containsMoreData;
    }

    public void setContainsMoreData(boolean containsMoreData) {
        this.containsMoreData = containsMoreData;
    }
}
