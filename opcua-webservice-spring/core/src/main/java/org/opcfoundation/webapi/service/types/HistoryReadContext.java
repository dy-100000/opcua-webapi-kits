package org.opcfoundation.webapi.service.types;

import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

public class HistoryReadContext extends ServiceContext {
    final private List<HistoryReadValueId> nodesToRead;
    final private UaStructuredType historyReadDetails;
    final private Boolean releaseContinuationPoints;
    final private TimestampsToReturn timestampsToReturn;

    public HistoryReadContext(
            List<HistoryReadValueId> nodesToRead,
            UaStructuredType historyReadDetails,
            Boolean releaseContinuationPoints,
            TimestampsToReturn timestampsToReturn,
            RequestHeader header,
            @Nullable String serverUri,
            NativeWebRequest webRequest)
    {
        super(header,serverUri,webRequest);
        this.nodesToRead = nodesToRead;
        this.historyReadDetails = historyReadDetails;
        this.releaseContinuationPoints = releaseContinuationPoints;
        this.timestampsToReturn = timestampsToReturn;
    }

    public List<HistoryReadValueId> getNodesToRead() {
        return nodesToRead;
    }

    public UaStructuredType getHistoryReadDetails() {
        return historyReadDetails;
    }

    public Boolean getReleaseContinuationPoints() {
        return releaseContinuationPoints;
    }

    public TimestampsToReturn getTimestampsToReturn() {
        return timestampsToReturn;
    }
}
