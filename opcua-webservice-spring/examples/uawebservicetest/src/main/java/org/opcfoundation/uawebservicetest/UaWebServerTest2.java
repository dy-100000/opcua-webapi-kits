package org.opcfoundation.uawebservicetest;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.service.UaWebServer;
import org.opcfoundation.webapi.service.UaServerConfigure;

//@Service
public class UaWebServerTest2 extends UaWebServer {
    public UaWebServerTest2()
    {
        super();
    }

    @Override
    public void onStartUp() {
        System.out.println("Server start up");

        UaServerConfigure configure = new UaServerConfigure();
        configure.setApplicationUri("test");
        configure.setApplicationName(LocalizedText.english("test"));
        configure.setProductUri("test");
        configure.setSupportServerUriPath(true);
        setServerConfigure(configure);
    }

    @Override
    public void onShutDown()
    {
        System.out.println("Server shut down");
    }
}
