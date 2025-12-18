package org.opcfoundation.webapi.service.types;

import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;

public class ServiceContext {
    final private RequestHeader requestHeader;
    final private @Nullable String serverUri;
    final private @Nullable NativeWebRequest webRequest;

    public ServiceContext(
            RequestHeader header,
            @Nullable String serverUri,
            @Nullable NativeWebRequest webRequest)
    {
        this.webRequest = webRequest;
        this.requestHeader = header;
        this.serverUri = serverUri;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public @Nullable String getServerUri() {
        return serverUri;
    }

    public @Nullable NativeWebRequest getWebRequest() {
        return webRequest;
    }
}
