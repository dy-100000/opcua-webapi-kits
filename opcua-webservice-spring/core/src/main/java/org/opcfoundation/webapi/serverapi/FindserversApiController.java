package org.opcfoundation.webapi.serverapi;

import jakarta.annotation.Nullable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.opcfoundation.webapi.mapper.UaPayloadMapper;
import org.opcfoundation.webapi.model.FindServersRequest;
import org.opcfoundation.webapi.model.FindServersResponse;
import org.opcfoundation.webapi.model.RequestHeader;
import org.opcfoundation.webapi.model.ResponseHeader;

import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.webapi.service.UaWebService;
import org.opcfoundation.webapi.service.types.FindServersContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
@Controller
@RequestMapping("${openapi.oPCUAWeb.base-path:}")
public class FindserversApiController implements FindserversApi {

    private final NativeWebRequest request;

    @Autowired
    private UaWebService service;

    @Autowired
    public FindserversApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public CompletableFuture<ResponseEntity<FindServersResponse>> findServers(
            String serverUri,
            FindServersRequest findServersRequest)
    {
        try
        {
            UaServerConfigure serverConfig = service.getServerConfigure();
            if (serverConfig.isServerUriPathSupported() && null != serverUri) throw new UaRuntimeException(StatusCodes.Bad_ServerUriInvalid);

            FindServersContext findServersContext = new FindServersContext(
                    (null == findServersRequest.getEndpointUrl()) ? "" : findServersRequest.getEndpointUrl(),
                    findServersRequest.getServerUris(),
                    findServersRequest.getLocaleIds(),
                    UaPayloadMapper.requestHeaderFromWebApi(findServersRequest.getRequestHeader()),
                    serverUri,
                    (getRequest().isPresent()) ? getRequest().get() : null);

            return service.findServers(findServersContext)
                    .thenApply(results -> findServersComplete(findServersRequest, results))
                    .exceptionally(ex-> getErrorResponse(findServersRequest.getRequestHeader(), ex.getCause()));

        } catch (Exception e) {
            return CompletableFuture.completedFuture(getErrorResponse(findServersRequest.getRequestHeader(), e));
        }
    }

    public ResponseEntity<FindServersResponse> findServersComplete(
            FindServersRequest findServersRequest,
            List<org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription> results)
    {
        StatusCode statusCode = StatusCode.GOOD;
        FindServersResponse response = new FindServersResponse();

        response.setServers(UaPayloadMapper.applicationDescriptionsFromMilo(results));

        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                findServersRequest.getRequestHeader(),
                statusCode);

        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<FindServersResponse> getErrorResponse(
            @Nullable RequestHeader requestHeader,
            Throwable exception)
    {
        StatusCode errorCode = StatusCode.of(StatusCodes.Bad_UnexpectedError);

        if (exception instanceof UaRuntimeException)
        {
            errorCode = ((UaRuntimeException)exception).getStatusCode();
        }

        FindServersResponse response = new FindServersResponse();
        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                requestHeader,
                errorCode);
        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }
}
