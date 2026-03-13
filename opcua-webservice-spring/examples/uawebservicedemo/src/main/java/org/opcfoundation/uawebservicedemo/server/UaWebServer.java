package org.opcfoundation.uawebservicedemo.server;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.webapi.service.types.FindServersContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UaWebServer extends org.opcfoundation.webserver.service.UaWebServer {
    public UaWebServer()
    {
        super();
    }

    @Override
    public void onStartUp() {
        System.out.println("Start up server with NodeManager ");

        // Initialize OPC UA Web Server configuration
        configureServer();

        // Add opc ua data manager
        addNodeManager(new EmployeeTwinSpace());
    }

    @Override
    public void onShutDown()
    {
        System.out.println("Shut down server with NodeManager");
    }

    private void configureServer()
    {
        UaServerConfigure configure = new UaServerConfigure();
        configure.setApplicationUri("EmployeeDemo");
        configure.setApplicationName(new LocalizedText("EmployeeDemo"));
        configure.setProductUri("EmployeeDemo");
        setServerConfigure(configure);
    }
}
