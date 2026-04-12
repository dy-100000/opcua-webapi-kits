package org.opcfoundation.webapi.serverapi;

import jakarta.annotation.Nullable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.opcfoundation.webapi.mapper.UaPayloadMapper;
import org.opcfoundation.webapi.model.BrowseRequest;
import org.opcfoundation.webapi.model.BrowseResponse;
import org.opcfoundation.webapi.model.RequestHeader;
import org.opcfoundation.webapi.model.ResponseHeader;

import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.webapi.service.UaWebService;
import org.opcfoundation.webapi.service.types.BrowseContext;
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
public class BrowseApiController implements BrowseApi {

    private final NativeWebRequest request;

    @Autowired
    private UaWebService service;

    @Autowired
    public BrowseApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public CompletableFuture<ResponseEntity<BrowseResponse>> browse(
            String serverUri,
            BrowseRequest browseRequest)
    {
        try
        {
            UaServerConfigure serverConfig = service.getServerConfigure();

            if (browseRequest.getNodesToBrowse().isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_NothingToDo);
            if (serverConfig.getBrowseRequestMaxSize() != 0 && browseRequest.getNodesToBrowse().size() > serverConfig.getBrowseRequestMaxSize())
                throw new UaRuntimeException(StatusCodes.Bad_RequestTooLarge);

            if (browseRequest.getRequestedMaxReferencesPerNode() < 0 || UInteger.MAX_VALUE < browseRequest.getRequestedMaxReferencesPerNode())
                throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

            List<BrowseDescription> nodesToBrowse = UaPayloadMapper.nodesToBrowseFromWebApi(browseRequest.getNodesToBrowse());
            org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription viewDescription = UaPayloadMapper.viewDescriptionFromWebApi(browseRequest.getView());

            BrowseContext browseContext = new BrowseContext(
                    nodesToBrowse,
                    viewDescription,
                    UInteger.valueOf(browseRequest.getRequestedMaxReferencesPerNode()),
                    serverUri,
                    UaPayloadMapper.requestHeaderFromWebApi(browseRequest.getRequestHeader()),
                    (getRequest().isPresent()) ? getRequest().get() : null);

            return service.browse(browseContext)
                    .thenApply(results -> browseComplete(browseRequest, results))
                    .exceptionally(ex-> getErrorResponse(browseRequest.getRequestHeader(), ex.getCause()));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(getErrorResponse(browseRequest.getRequestHeader(), e));
        }
    }

    public ResponseEntity<BrowseResponse> browseComplete(
            BrowseRequest browseRequest,
            List<org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult> results)
    {
        StatusCode statusCode = StatusCode.GOOD;
        BrowseResponse response = new BrowseResponse();

        if (results.size() == browseRequest.getNodesToBrowse().size())
        {
            response.setResults(UaPayloadMapper.browseResultsFromMilo(results));
        } else {
            statusCode = new StatusCode(StatusCodes.Bad_InternalError);
        }

        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                    browseRequest.getRequestHeader(),
                    statusCode);

        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<BrowseResponse> getErrorResponse(
            @Nullable RequestHeader requestHeader,
            Throwable exception)
    {
        StatusCode errorCode = StatusCode.of(StatusCodes.Bad_UnexpectedError);

        if (exception instanceof UaRuntimeException)
        {
            errorCode = ((UaRuntimeException)exception).getStatusCode();
        }

        BrowseResponse response = new BrowseResponse();
        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                requestHeader,
                errorCode);
        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }
}
