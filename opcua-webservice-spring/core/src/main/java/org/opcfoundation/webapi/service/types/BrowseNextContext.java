package org.opcfoundation.webapi.service.types;

import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

public class BrowseNextContext extends ServiceContext {
    private final List<ByteString> continuationPoints;
    private final boolean releaseContinuationPoints;

    public BrowseNextContext(
            List<ByteString> continuationPoints,
            boolean releaseContinuationPoints,
            @Nullable String serverUri,
            @Nullable RequestHeader header,
            @Nullable NativeWebRequest webRequest)
    {
        super(serverUri,header,webRequest);
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
