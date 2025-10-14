package org.opcfoundation.uawebservicedemo.server;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.uawebservicedemo.models.EmployeeDataManager;
import org.springframework.stereotype.Service;

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
        addNodeManager(new EmployeeDataManager());
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
        configure.setApplicationName(LocalizedText.english("EmployeeDemo"));
        configure.setProductUri("EmployeeDemo");
        setServerConfigure(configure);
    }
}
