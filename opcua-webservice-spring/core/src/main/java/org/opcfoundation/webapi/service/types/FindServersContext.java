package org.opcfoundation.webapi.service.types;

import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.jspecify.annotations.Nullable;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

public class FindServersContext extends ServiceContext {
    private final String endpointUrl;
    private final List<String> serverUris;
    private final List<String> localeIds;

    public FindServersContext(
            String endpointUrl,
            List<String> serverUris,
            List<String> localeIds,
            RequestHeader header,
            @Nullable String serverUri,
            NativeWebRequest webRequest)
    {
        super(header,serverUri,webRequest);
        this.endpointUrl = endpointUrl;
        this.serverUris = serverUris;
        this.localeIds = localeIds;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public List<String> getServerUris() {
        return serverUris;
    }

    public List<String> getLocaleIds() {
        return localeIds;
    }
}
