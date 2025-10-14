package org.opcfoundation.uawebgatewaydemo;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.opcfoundation.webserver.service.UaWebServerBase;
import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.webapi.service.types.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class UaWebGatewayService extends UaWebServerBase {
    private final UaClientManager clientManager;

    public UaWebGatewayService()
    {
        super();
        clientManager = new UaClientManager();

        UaServerConfigure serverConfigure = new UaServerConfigure();
        serverConfigure.setApplicationUri("UaWebGateway");
        serverConfigure.setApplicationName(LocalizedText.english("UaWebGateway"));
        serverConfigure.setProductUri("UaWebGateway");
        setServerConfigure(serverConfigure);
    }

    @Override
    public void onStartUp() {
        System.out.println("Connection to UA Servers ...");

        // Set OPC UA connection parameter
        String serverUri = "Server-1";
        String endpointUrl = "opc.tcp://127.0.0.1:48010";

        try
        {
            OpcUaClient client = OpcUaClient.create(endpointUrl);
            client.connect();

            clientManager.addClient(serverUri,client);

        } catch (UaException e) {
            System.out.println("Fail to connect to " + serverUri);
        }
    }

    @Override
    public void onShutDown() {
        System.out.println("Disconnect with UA Servers...");
        clientManager.disconnectAll();
    }

    @Override
    public CompletableFuture<List<BrowseResult>> browse(BrowseContext context) throws UaRuntimeException
    {
        OpcUaClient client = null;

        if (null != context.getServerUri())
        {
            client = clientManager.getClient(context.getServerUri());
        }

        if (null == client) throw new UaRuntimeException(StatusCodes.Bad_ServerUriInvalid);

        return client.browseAsync(
                context.getView(),
                context.getRequestedMaxReferencesPerNode(),
                context.getNodesToBrowse()).thenApply(response->
        {
            StatusCode statusCode = response.getResponseHeader().getServiceResult();
            if (!statusCode.isGood()) throw new UaRuntimeException(statusCode.getValue());
            if (null == response.getResults()) throw new UaRuntimeException(StatusCodes.Bad_UnknownResponse);
            return Arrays.asList(response.getResults());
        });
    }

    @Override
    public CompletableFuture<List<BrowseResult>> browseNext(BrowseNextContext context) throws UaRuntimeException
    {
        OpcUaClient client = null;

        if (null != context.getServerUri())
        {
            client = clientManager.getClient(context.getServerUri());
        }

        if (null == client) throw new UaRuntimeException(StatusCodes.Bad_ServerUriInvalid);

        return client.browseNextAsync(
                context.getReleaseContinuationPoints(),
                context.getContinuationPoints()).thenApply(response->
        {
            StatusCode statusCode = response.getResponseHeader().getServiceResult();
            if (!statusCode.isGood()) throw new UaRuntimeException(statusCode.getValue());
            if (null == response.getResults()) throw new UaRuntimeException(StatusCodes.Bad_UnknownResponse);
            return Arrays.asList(response.getResults());
        });
    }

    @Override
    public CompletableFuture<List<BrowsePathResult>> translate(TranslateContext context) throws UaRuntimeException
    {
        OpcUaClient client = null;
        if (null != context.getServerUri())
        {
            client = clientManager.getClient(context.getServerUri());
        }

        if (null == client) throw new UaRuntimeException(StatusCodes.Bad_ServerUriInvalid);

        return client.translateBrowsePathsAsync(context.getNodesToTranslate()).thenApply(response->
        {
            StatusCode statusCode = response.getResponseHeader().getServiceResult();
            if (!statusCode.isGood()) throw new UaRuntimeException(statusCode.getValue());
            if (null == response.getResults()) throw new UaRuntimeException(StatusCodes.Bad_UnknownResponse);
            return Arrays.asList(response.getResults());
        });
    }

    public CompletableFuture<List<DataValue>> read(ReadContext context) throws UaRuntimeException
    {
        OpcUaClient client = null;

        if (null != context.getServerUri())
        {
            client = clientManager.getClient(context.getServerUri());
        }

        if (null == client) throw new UaRuntimeException(StatusCodes.Bad_ServerUriInvalid);

        return client.readAsync(
                context.getMaxAge(),
                context.getTimestampsToReturn(),
                context.getNodesToRead()).thenApply(response->
        {
            StatusCode statusCode = response.getResponseHeader().getServiceResult();
            if (!statusCode.isGood()) throw new UaRuntimeException(statusCode.getValue());
            if (null == response.getResults()) throw new UaRuntimeException(StatusCodes.Bad_UnknownResponse);
            return Arrays.asList(response.getResults());
        });
    }

    public CompletableFuture<List<StatusCode>> write(WriteContext context) throws UaRuntimeException
    {
        OpcUaClient client = null;

        if (null != context.getServerUri())
        {
            client = clientManager.getClient(context.getServerUri());
        }

        if (null == client) throw new UaRuntimeException(StatusCodes.Bad_ServerUriInvalid);

        return client.writeAsync(context.getNodesToWrite()).thenApply(response->
        {
            StatusCode statusCode = response.getResponseHeader().getServiceResult();
            if (!statusCode.isGood()) throw new UaRuntimeException(statusCode.getValue());
            if (null == response.getResults()) throw new UaRuntimeException(StatusCodes.Bad_UnknownResponse);
            return Arrays.asList(response.getResults());
        });
    }

    public CompletableFuture<List<CallMethodResult>> call(CallContext context) throws UaRuntimeException
    {
        OpcUaClient client = null;

        if (null != context.getServerUri())
        {
            client = clientManager.getClient(context.getServerUri());
        }

        if (null == client) throw new UaRuntimeException(StatusCodes.Bad_ServerUriInvalid);

        return client.callAsync(context.getMethodsToCall()).thenApply(response->
        {
            StatusCode statusCode = response.getResponseHeader().getServiceResult();
            if (!statusCode.isGood()) throw new UaRuntimeException(statusCode.getValue());
            if (null == response.getResults()) throw new UaRuntimeException(StatusCodes.Bad_UnknownResponse);
            return Arrays.asList(response.getResults());
        });
    }

    @Override
    public CompletableFuture<List<ApplicationDescription>> findServers(FindServersContext context) throws UaRuntimeException
    {
        ArrayList<ApplicationDescription> descriptors = new ArrayList<>();
        Set<String> uris = clientManager.getServerUris();
        Set<String> urisToFind = new HashSet<>(context.getServerUris());

        // When the searched uris is empty or contains uri of current server,
        // then add self descriptor of this server
        if (urisToFind.isEmpty() || urisToFind.contains(getServerConfigure().getApplicationUri()))
        {
            // Add descriptor of self-descriptor of UaWebGateway
            ApplicationDescription selfDescription = new ApplicationDescription(
                    getServerConfigure().getApplicationUri(),
                    getServerConfigure().getProductUri(),
                    getServerConfigure().getApplicationName(),
                    getServerConfigure().getApplicationType(),
                    null,
                    null,
                    null);

            descriptors.add(selfDescription);
        }

        for (String uri : uris)
        {
            // When the searched uris is empty or contains uri of uri,
            // then add the descriptor of server
            if (!urisToFind.isEmpty())
            {
                if (!urisToFind.contains(uri)) continue;
            }

            OpcUaClient client = clientManager.getClient(uri);
            if (null == client) continue;

            ApplicationDescription clientConfig = client.getConfig().getEndpoint().getServer();
            ApplicationDescription serverDescriptor = new ApplicationDescription(
                    uri,
                    clientConfig.getProductUri(),
                    clientConfig.getApplicationName(),
                    clientConfig.getApplicationType(),
                    null,
                    null,
                    null);

            descriptors.add(serverDescriptor);
        }

        return CompletableFuture.completedFuture(descriptors);
    }

    @Override
    public CompletableFuture<List<EndpointDescription>> getEndpoints(GetEndpointContext context) throws UaRuntimeException
    {
        List<EndpointDescription> results = new ArrayList<>();
        results.add(getEndpointDescription());
        return CompletableFuture.completedFuture(results);
    }
}
