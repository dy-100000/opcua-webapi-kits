package org.opcfoundation.webapi.service.types;

import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

public class GetEndpointContext extends ServiceContext {
    final private String endpointUrl;
    final private List<String> localeIds;
    final private List<String> profileUris;

    public GetEndpointContext(
            @Nullable String endpointUrl,
            List<String> localeIds,
            List<String> profileUris,
            RequestHeader header,
            @Nullable String serverUri,
            NativeWebRequest webRequest)
    {
        super(header, serverUri,webRequest);
        this.endpointUrl = (null == endpointUrl) ? "" : endpointUrl;
        this.localeIds = localeIds;
        this.profileUris = profileUris;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public List<String> getLocaleIds() {
        return localeIds;
    }

    public List<String> getProfileUris() {
        return profileUris;
    }
}
