package org.opcfoundation.uawebservicetest;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.service.UaDiscoveryService;
import org.opcfoundation.webserver.types.common.UaApplicationDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TestDiscoveryService extends UaDiscoveryService {
    public TestDiscoveryService()
    {
        super();
    }

    @Override
    public CompletableFuture<List<UaApplicationDescriptor>> find(String url, List<String> applicationUrisToFind)
    {
        List<UaApplicationDescriptor> descriptors = new ArrayList<>();

        if (applicationUrisToFind.isEmpty())
        {
            for (int i=0; i<5; ++i)
            {
                descriptors.add(new UaApplicationDescriptor(
                        "App" + i,
                        LocalizedText.english("Application" + i),
                        url));
            }
        } else {
            for (String item : applicationUrisToFind)
            {
                if (item.isEmpty()) continue;

                descriptors.add(new UaApplicationDescriptor(
                        item,
                        LocalizedText.english("App-" + item),
                        url));
            }
        }

        return CompletableFuture.completedFuture(descriptors);
    }
}
