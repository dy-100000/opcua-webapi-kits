package org.opcfoundation.webapi.service.types;

import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;

public class ServiceContext {
    final private @Nullable String serverUri;
    final private @Nullable RequestHeader requestHeader;
    final private @Nullable NativeWebRequest webRequest;

    public ServiceContext(
            @Nullable String serverUri,
            @Nullable RequestHeader header,
            @Nullable NativeWebRequest webRequest)
    {
        this.serverUri = serverUri;
        this.requestHeader = header;
        this.webRequest = webRequest;
    }

    public @Nullable String getServerUri() {
        return serverUri;
    }

    public @Nullable RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public @Nullable NativeWebRequest getWebRequest() {
        return webRequest;
    }
}
