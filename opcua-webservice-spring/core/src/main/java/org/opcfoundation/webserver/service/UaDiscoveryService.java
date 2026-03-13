package org.opcfoundation.webserver.service;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.webserver.types.common.UaApplicationDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class UaDiscoveryService {
    abstract public CompletableFuture<List<UaApplicationDescriptor>> find(String url, List<String> applicationUrisToFind) throws UaRuntimeException;

    public static List<ApplicationDescription> complete(
            List<UaApplicationDescriptor> descriptors,
            UaServerConfigure configure)
    {
        List<ApplicationDescription> descriptions = new ArrayList<>();

        for (UaApplicationDescriptor item : descriptors)
        {
            List<String> discoveryUrls = new ArrayList<>();
            discoveryUrls.add(item.getUrl());

            ApplicationDescription description = new ApplicationDescription(
                    item.getUri(),
                    configure.getProductUri(),
                    item.getName(),
                    configure.getApplicationType(),
                    null,
                    null,
                    discoveryUrls.toArray(discoveryUrls.toArray(new String[0])));

            descriptions.add(description);
        }

        return descriptions;
    }
}
