package org.opcfoundation.webserver.types.common;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

public class UaApplicationDescriptor {
    private final String uri;
    private final LocalizedText name;
    private final String url;

    public UaApplicationDescriptor(
            String uri,
            LocalizedText name,
            String serverUrl)
    {
        this.uri = uri;
        this.name = name;
        this.url = serverUrl + "/" + uri;
    }

    public String getUri() {
        return uri;
    }

    public LocalizedText getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
