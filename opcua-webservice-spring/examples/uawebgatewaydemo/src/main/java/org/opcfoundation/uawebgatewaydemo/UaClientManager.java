package org.opcfoundation.uawebgatewaydemo;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class UaClientManager {
    private final Map<String, OpcUaClient> clients;

    public UaClientManager()
    {
        clients = new HashMap<>();
    }

    Set<String> getServerUris()
    {
        return clients.keySet();
    }

    public @Nullable OpcUaClient getClient(String serverUri)
    {
        return clients.get(serverUri);
    }

    public void addClient(String serverUri, OpcUaClient client)
    {
        if (serverUri.isEmpty() || clients.containsKey(serverUri)) return;
        clients.put(serverUri,client);
    }

    public void disconnectAll()
    {
        for (Map.Entry<String, OpcUaClient> item : clients.entrySet())
        {
            try {
                item.getValue().disconnect();
            } catch (UaException e) {
                System.out.println("Fail to disconnect with " + item.getKey());
            }
        }
    }
}
