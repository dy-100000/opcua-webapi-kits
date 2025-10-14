package org.opcfoundation.webserver.service;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.webapi.service.UaWebService;
import org.opcfoundation.webapi.service.types.GetEndpointContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class UaWebServerBase implements UaWebService {

    private UaServerConfigure configure;

    public UaWebServerBase()
    {
        configure = null;
    }

    @Override
    public UaServerConfigure getServerConfigure() {
        if (null == configure) configure = new UaServerConfigure();
        return configure;
    }

    public void setServerConfigure(UaServerConfigure configure) {
        this.configure = configure;
    }

    public abstract void onStartUp() throws UaRuntimeException;

    public abstract void onShutDown() throws UaRuntimeException;

    public EndpointDescription getEndpointDescription()
    {
        org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription applicationDescription = new org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription(
                getServerConfigure().getApplicationUri(),
                getServerConfigure().getProductUri(),
                getServerConfigure().getApplicationName(),
                getServerConfigure().getApplicationType(),
                null,
                null,
                null);

        return new org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription(
                "",
                applicationDescription,
                ByteString.NULL_VALUE,
                MessageSecurityMode.None,
                SecurityPolicy.None.getUri(),
                new org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy[0],
                null,
                UByte.valueOf(0));
    }

    public void startUp() throws UaRuntimeException
    {
        onStartUp();
    }

    public void shutDown() throws UaRuntimeException
    {
        onShutDown();
    }

    @PostConstruct
    public void initializeService()
    {
        startUp();
    }

    @PreDestroy
    public void finalizeService()
    {
        shutDown();
    }

    @Override
    public CompletableFuture<List<EndpointDescription>> getEndpoints(GetEndpointContext context) throws UaRuntimeException
    {
        List<EndpointDescription> results = new ArrayList<>();
        results.add(getEndpointDescription());
        return CompletableFuture.completedFuture(results);
    }
}
