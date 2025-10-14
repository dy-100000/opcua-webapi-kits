package org.opcfoundation.webapi.service.types;

import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.jspecify.annotations.Nullable;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

public class ReadContext extends ServiceContext {

    final private List<ReadValueId> nodesToRead;
    final private Double maxAge;
    final private TimestampsToReturn timestampsToReturn;

    public ReadContext(
            List<ReadValueId> nodesToRead,
            Double maxAge,
            TimestampsToReturn timestampsToReturn,
            RequestHeader header,
            @Nullable String serverUri,
            NativeWebRequest webRequest)
    {
        super(header,serverUri,webRequest);
        this.nodesToRead = nodesToRead;
        this.maxAge = maxAge;
        this.timestampsToReturn = timestampsToReturn;
    }

    public List<ReadValueId> getNodesToRead() {
        return nodesToRead;
    }

    public Double getMaxAge() {
        return maxAge;
    }

    public TimestampsToReturn getTimestampsToReturn() {
        return timestampsToReturn;
    }
}
