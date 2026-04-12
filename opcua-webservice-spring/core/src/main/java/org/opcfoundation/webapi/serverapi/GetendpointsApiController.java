package org.opcfoundation.webapi.serverapi;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.springframework.lang.Nullable;
import org.opcfoundation.webapi.mapper.UaPayloadMapper;
import org.opcfoundation.webapi.model.GetEndpointsRequest;
import org.opcfoundation.webapi.model.GetEndpointsResponse;
import org.opcfoundation.webapi.model.RequestHeader;
import org.opcfoundation.webapi.model.ResponseHeader;

import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.webapi.service.UaWebService;
import org.opcfoundation.webapi.service.types.GetEndpointContext;
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
public class GetendpointsApiController implements GetendpointsApi {

    private final NativeWebRequest request;

    @Autowired
    private UaWebService service;

    @Autowired
    public GetendpointsApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }


    @Override
    public CompletableFuture<ResponseEntity<GetEndpointsResponse>> getEndpoints(
            String serverUri,
            GetEndpointsRequest getEndpointsRequest) {
        try {
            GetEndpointContext getEndpointContext = new GetEndpointContext(
                    getEndpointsRequest.getEndpointUrl(),
                    getEndpointsRequest.getLocaleIds(),
                    getEndpointsRequest.getProfileUris(),
                    serverUri,
                    UaPayloadMapper.requestHeaderFromWebApi(getEndpointsRequest.getRequestHeader()),
                    (getRequest().isPresent()) ? getRequest().get() : null);

            return service.getEndpoints(getEndpointContext)
                    .thenApply(results -> getEndpointsComplete(getEndpointsRequest, results))
                    .exceptionally(ex -> getErrorResponse(getEndpointsRequest.getRequestHeader(), ex.getCause()));

        } catch (Exception e) {
            return CompletableFuture.completedFuture(getErrorResponse(getEndpointsRequest.getRequestHeader(), e));
        }
    }

    public ResponseEntity<GetEndpointsResponse> getEndpointsComplete(
            GetEndpointsRequest getEndpointsRequest,
            List<org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription> results)
    {
        StatusCode statusCode = StatusCode.GOOD;
        GetEndpointsResponse response = new GetEndpointsResponse();
        response.setEndpoints(UaPayloadMapper.endpointDescriptionsFromMilo(results));

        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                getEndpointsRequest.getRequestHeader(),
                statusCode);

        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<GetEndpointsResponse> getErrorResponse(
            @Nullable RequestHeader requestHeader,
            Throwable exception)
    {
        StatusCode errorCode = StatusCode.of(StatusCodes.Bad_UnexpectedError);

        if (exception instanceof UaRuntimeException)
        {
            errorCode = ((UaRuntimeException)exception).getStatusCode();
        }

        GetEndpointsResponse response = new GetEndpointsResponse();
        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                requestHeader,
                errorCode);

        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }
}
