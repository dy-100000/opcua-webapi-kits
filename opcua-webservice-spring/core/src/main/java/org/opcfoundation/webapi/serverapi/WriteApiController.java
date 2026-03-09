package org.opcfoundation.webapi.serverapi;

import jakarta.annotation.Nullable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.opcfoundation.webapi.mapper.UaPayloadMapper;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.RequestHeader;
import org.opcfoundation.webapi.model.ResponseHeader;
import org.opcfoundation.webapi.model.WriteRequest;
import org.opcfoundation.webapi.model.WriteResponse;

import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.webapi.service.UaWebService;
import org.opcfoundation.webapi.service.types.WriteContext;
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
public class WriteApiController implements WriteApi {

    private final NativeWebRequest request;

    @Autowired
    private UaWebService service;

    @Autowired
    public WriteApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public CompletableFuture<ResponseEntity<WriteResponse>> write(
            String serverUri,
            WriteRequest writeRequest)
    {
        try {
            UaServerConfigure serverConfig = service.getServerConfigure();
            if (serverConfig.isServerUriPathSupported() && null != serverUri) throw new UaRuntimeException(StatusCodes.Bad_ServerUriInvalid);

            if (writeRequest.getNodesToWrite().isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_NothingToDo);
            if (serverConfig.getWriteRequestMaxSize() != 0 && writeRequest.getNodesToWrite().size() > serverConfig.getWriteRequestMaxSize())
                throw new UaRuntimeException(StatusCodes.Bad_RequestTooLarge);

            List<org.eclipse.milo.opcua.stack.core.types.structured.WriteValue> nodesToWrite= UaPayloadMapper.nodesToWriteFromWebApi(writeRequest.getNodesToWrite());

            WriteContext writeContext = new WriteContext(
                    nodesToWrite,
                    serverUri,
                    UaPayloadMapper.requestHeaderFromWebApi(writeRequest.getRequestHeader()),
                    (getRequest().isPresent()) ? getRequest().get() : null);

            return service.write(writeContext)
                    .thenApply(results -> writeComplete(writeRequest, results))
                    .exceptionally(ex-> getErrorResponse(writeRequest.getRequestHeader(), ex.getCause()));

        } catch (Exception e) {
            return CompletableFuture.completedFuture(getErrorResponse(writeRequest.getRequestHeader(), e));
        }
    }

    public ResponseEntity<WriteResponse> writeComplete(
            WriteRequest writeRequest,
            List<StatusCode> results)
    {
        StatusCode statusCode = StatusCode.GOOD;
        WriteResponse response = new WriteResponse();

        if (results.size() == writeRequest.getNodesToWrite().size())
        {
            response.setResults(UaTypeMapper.statusCodesFromMilo(results));
        } else {
            statusCode = new StatusCode(StatusCodes.Bad_InternalError);
        }

        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                writeRequest.getRequestHeader(),
                statusCode);

        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<WriteResponse> getErrorResponse(
            @Nullable RequestHeader requestHeader,
            Throwable exception)
    {
        StatusCode errorCode = StatusCode.of(StatusCodes.Bad_UnexpectedError);

        if (exception instanceof UaRuntimeException)
        {
            errorCode = ((UaRuntimeException)exception).getStatusCode();
        }

        WriteResponse response = new WriteResponse();
        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                requestHeader,
                errorCode);
        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }
}
