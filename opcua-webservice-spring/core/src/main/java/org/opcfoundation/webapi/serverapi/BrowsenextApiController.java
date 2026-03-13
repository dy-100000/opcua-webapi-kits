package org.opcfoundation.webapi.serverapi;

import jakarta.annotation.Nullable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.opcfoundation.webapi.mapper.UaPayloadMapper;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.BrowseNextRequest;
import org.opcfoundation.webapi.model.BrowseNextResponse;
import org.opcfoundation.webapi.model.RequestHeader;
import org.opcfoundation.webapi.model.ResponseHeader;

import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.webapi.service.UaWebService;
import org.opcfoundation.webapi.service.types.BrowseNextContext;
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
public class BrowsenextApiController implements BrowsenextApi {

    private final NativeWebRequest request;

    @Autowired
    private UaWebService service;

    @Autowired
    public BrowsenextApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public CompletableFuture<ResponseEntity<BrowseNextResponse>> browseNext(
            String serverUri,
            BrowseNextRequest browseNextRequest)
    {
        try
        {
            UaServerConfigure serverConfig = service.getServerConfigure();

            if (!serverConfig.isServerUriPathSupported() && null != serverUri) throw new UaRuntimeException(StatusCodes.Bad_ServerUriInvalid);
            if (browseNextRequest.getContinuationPoints().isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_NothingToDo);
            if (serverConfig.getBrowseRequestMaxSize() != 0 && browseNextRequest.getContinuationPoints().size() > serverConfig.getBrowseRequestMaxSize())
                throw new UaRuntimeException(StatusCodes.Bad_RequestTooLarge);

            BrowseNextContext browseNextContext = new BrowseNextContext(
                    UaTypeMapper.byteStringsFromWebApi(browseNextRequest.getContinuationPoints()),
                    browseNextRequest.getReleaseContinuationPoints(),
                    serverUri,
                    UaPayloadMapper.requestHeaderFromWebApi(browseNextRequest.getRequestHeader()),
                    (getRequest().isPresent()) ? getRequest().get() : null);

            return service.browseNext(browseNextContext)
                    .thenApply(results -> browseNextComplete(browseNextRequest, results))
                    .exceptionally(ex-> getErrorResponse(browseNextRequest.getRequestHeader(), ex.getCause()) );

        } catch (Exception e) {
            return CompletableFuture.completedFuture(getErrorResponse(browseNextRequest.getRequestHeader(), e));
        }
    }

    public ResponseEntity<BrowseNextResponse> browseNextComplete(
            BrowseNextRequest browseNextRequest,
            List<org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult> results)
    {
        StatusCode statusCode = StatusCode.GOOD;
        BrowseNextResponse response = new BrowseNextResponse();

        if (results.size() == browseNextRequest.getContinuationPoints().size())
        {
            response.setResults(UaPayloadMapper.browseResultsFromMilo(results));
        } else {
            statusCode = new StatusCode(StatusCodes.Bad_InternalError);
        }

        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                browseNextRequest.getRequestHeader(),
                statusCode);

        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<BrowseNextResponse> getErrorResponse(
            @Nullable RequestHeader requestHeader,
            Throwable exception)
    {
        StatusCode errorCode = StatusCode.of(StatusCodes.Bad_UnexpectedError);

        if (exception instanceof UaRuntimeException)
        {
            errorCode = ((UaRuntimeException)exception).getStatusCode();
        }

        BrowseNextResponse response = new BrowseNextResponse();
        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                requestHeader,
                errorCode);
        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }
}
