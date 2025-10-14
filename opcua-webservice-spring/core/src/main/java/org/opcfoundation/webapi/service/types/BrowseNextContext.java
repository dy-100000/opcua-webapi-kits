package org.opcfoundation.webapi.service.types;

import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.jspecify.annotations.Nullable;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

public class BrowseNextContext extends ServiceContext {
    private final List<ByteString> continuationPoints;
    private final boolean releaseContinuationPoints;

    public BrowseNextContext(
            List<ByteString> continuationPoints,
            boolean releaseContinuationPoints,
            RequestHeader header,
            @Nullable String serverUri,
            NativeWebRequest webRequest)
    {
        super(header,serverUri,webRequest);
        this.continuationPoints = continuationPoints;
        this.releaseContinuationPoints = releaseContinuationPoints;
    }

    public List<ByteString> getContinuationPoints() {
        return continuationPoints;
    }

    public boolean getReleaseContinuationPoints() {
        return releaseContinuationPoints;
    }
}
