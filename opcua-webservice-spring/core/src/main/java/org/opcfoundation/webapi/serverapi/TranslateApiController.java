package org.opcfoundation.webapi.serverapi;

import jakarta.annotation.Nullable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.opcfoundation.webapi.mapper.UaPayloadMapper;
import org.opcfoundation.webapi.model.RequestHeader;
import org.opcfoundation.webapi.model.ResponseHeader;
import org.opcfoundation.webapi.model.TranslateBrowsePathsToNodeIdsRequest;
import org.opcfoundation.webapi.model.TranslateBrowsePathsToNodeIdsResponse;

import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.webapi.service.UaWebService;
import org.opcfoundation.webapi.service.types.TranslateContext;
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
public class TranslateApiController implements TranslateApi {

    private final NativeWebRequest request;

    @Autowired
    private UaWebService service;

    @Autowired
    public TranslateApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public CompletableFuture<ResponseEntity<TranslateBrowsePathsToNodeIdsResponse>> translateBrowsePathsToNodeIds(
            String serverUri,
            TranslateBrowsePathsToNodeIdsRequest translateRequest)
    {
        try
        {
            UaServerConfigure serverConfig = service.getServerConfigure();
            if (serverConfig.isServerUriPathSupported() && null != serverUri) throw new UaRuntimeException(StatusCodes.Bad_ServerUriInvalid);

            if (translateRequest.getBrowsePaths().isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_NothingToDo);
            if (serverConfig.getTranslateRequestMaxSize() != 0 && translateRequest.getBrowsePaths().size() > serverConfig.getTranslateRequestMaxSize())
                throw new UaRuntimeException(StatusCodes.Bad_RequestTooLarge);

            List<org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath> nodesToTranslate = UaPayloadMapper.browsePathsFromWebApi(translateRequest.getBrowsePaths());

            TranslateContext translateContext = new TranslateContext(
                    nodesToTranslate,
                    serverUri,
                    UaPayloadMapper.requestHeaderFromWebApi(translateRequest.getRequestHeader()),
                    (getRequest().isPresent()) ? getRequest().get() : null);

            return service.translate(translateContext)
                    .thenApply(results -> translateComplete(translateRequest, results))
                    .exceptionally(ex-> getErrorResponse(translateRequest.getRequestHeader(),ex.getCause()));

        } catch (Exception e) {
            return CompletableFuture.completedFuture(getErrorResponse(translateRequest.getRequestHeader(), e));
        }
    }

    public ResponseEntity<TranslateBrowsePathsToNodeIdsResponse> translateComplete(
            TranslateBrowsePathsToNodeIdsRequest translateRequest,
             List<org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathResult> results)
    {
        StatusCode statusCode = StatusCode.GOOD;
        TranslateBrowsePathsToNodeIdsResponse response = new TranslateBrowsePathsToNodeIdsResponse();

        if (results.size() == translateRequest.getBrowsePaths().size())
        {
            response.setResults(UaPayloadMapper.browsePathResultsFromMilo(results));
        } else {
            statusCode = new StatusCode(StatusCodes.Bad_InternalError);
        }

        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                translateRequest.getRequestHeader(),
                statusCode);

        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<TranslateBrowsePathsToNodeIdsResponse> getErrorResponse(
            @Nullable RequestHeader requestHeader,
            Throwable exception)
    {
        StatusCode errorCode = StatusCode.of(StatusCodes.Bad_UnexpectedError);

        if (exception instanceof UaRuntimeException)
        {
            errorCode = ((UaRuntimeException)exception).getStatusCode();
        }

        TranslateBrowsePathsToNodeIdsResponse response = new TranslateBrowsePathsToNodeIdsResponse();
        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                requestHeader,
                errorCode);
        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }
}
