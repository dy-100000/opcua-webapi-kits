package org.opcfoundation.uawebservicetest;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicetest.testdigitaltwin.DigitalTwinSpaceTest;
import org.opcfoundation.uawebservicetest.testmodel.TestNodeManagerWebService;
import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.webserver.service.UaWebServer;
import org.springframework.stereotype.Service;

@Service
public class UaWebServerTest4 extends UaWebServer {
    public UaWebServerTest4()
    {
        super();
    }

    @Override
    public void onStartUp() {
        System.out.println("Start up server with NodeManager ");

        configureServer();
        addNodeManager(new DigitalTwinSpaceTest());
    }

    @Override
    public void onShutDown()
    {
        System.out.println("Shut down server with NodeManager");
    }

    private void configureServer()
    {
        UaServerConfigure configure = new UaServerConfigure();
        configure.setApplicationUri("test");
        configure.setApplicationName(LocalizedText.english("test"));
        configure.setProductUri("test");
        setServerConfigure(configure);
    }
}
