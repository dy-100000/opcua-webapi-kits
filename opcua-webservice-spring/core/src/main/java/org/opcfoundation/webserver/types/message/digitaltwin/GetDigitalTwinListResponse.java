package org.opcfoundation.webserver.types.message.digitaltwin;

import org.opcfoundation.webserver.types.DigitalTwinDescriptor;

import java.util.List;

public class GetDigitalTwinListResponse {
    private final List<DigitalTwinDescriptor> digitalTwins;
    private final boolean containsMoreData;

    public GetDigitalTwinListResponse(
            List<DigitalTwinDescriptor> digitalTwins,
            boolean containsMoreData)
    {
        this.digitalTwins = digitalTwins;
        this.containsMoreData = containsMoreData;
    }

    public List<DigitalTwinDescriptor> getDigitalTwins() {
        return digitalTwins;
    }

    public boolean containsMoreData()
    {
        return containsMoreData;
    }
}
