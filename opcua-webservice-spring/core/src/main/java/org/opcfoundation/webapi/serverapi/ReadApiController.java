package org.opcfoundation.webapi.serverapi;

import jakarta.annotation.Nullable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.opcfoundation.webapi.mapper.UaPayloadMapper;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.ReadRequest;
import org.opcfoundation.webapi.model.ReadResponse;
import org.opcfoundation.webapi.model.RequestHeader;
import org.opcfoundation.webapi.model.ResponseHeader;

import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.webapi.service.UaWebService;
import org.opcfoundation.webapi.service.types.ReadContext;
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
public class ReadApiController implements ReadApi {

    private final NativeWebRequest request;

    @Autowired
    private UaWebService service;

    @Autowired
    public ReadApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public CompletableFuture<ResponseEntity<ReadResponse>> read(
            String serverUri,
            ReadRequest readRequest)
    {
        try
        {
            UaServerConfigure serverConfig = service.getServerConfigure();
            if (serverConfig.isServerUriPathSupported() && null != serverUri) throw new UaRuntimeException(StatusCodes.Bad_ServerUriInvalid);

            org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn timestampsToReturn = org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn.Neither;
            if (null != readRequest.getTimestampsToReturn())
            {
                timestampsToReturn = TimestampsToReturn.from(readRequest.getTimestampsToReturn());
            }

            if (null == timestampsToReturn) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

            if (readRequest.getNodesToRead().isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_NothingToDo);
            if (serverConfig.getReadRequestMaxSize() != 0 && readRequest.getNodesToRead().size() > serverConfig.getReadRequestMaxSize())
                throw new UaRuntimeException(StatusCodes.Bad_RequestTooLarge);

            List<org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId> nodesToRead = UaPayloadMapper.nodesToReadFromWebApi(readRequest.getNodesToRead());

            ReadContext readContext = new ReadContext(
                    nodesToRead,
                    readRequest.getMaxAge(),
                    timestampsToReturn,
                    UaPayloadMapper.requestHeaderFromWebApi(readRequest.getRequestHeader()),
                    serverUri,
                    (getRequest().isPresent()) ? getRequest().get() : null);

            return service.read(readContext)
                    .thenApply(results -> readComplete(readRequest, results))
                    .exceptionally(ex-> getErrorResponse(readRequest.getRequestHeader(), ex.getCause()));

        } catch (Exception e) {
            return CompletableFuture.completedFuture(getErrorResponse(readRequest.getRequestHeader(), e));
        }
    }

    public ResponseEntity<ReadResponse> readComplete(
            ReadRequest readRequest,
            List<DataValue> results)
    {
        StatusCode statusCode = StatusCode.GOOD;
        ReadResponse response = new ReadResponse();

        if (results.size() == readRequest.getNodesToRead().size())
        {
            response.setResults(UaTypeMapper.dataValuesFromMilo(results));
        } else {
            statusCode = new StatusCode(StatusCodes.Bad_InternalError);
        }

        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                readRequest.getRequestHeader(),
                statusCode);

        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ReadResponse> getErrorResponse(
            @Nullable RequestHeader requestHeader,
            Throwable exception)
    {
        StatusCode errorCode = StatusCode.of(StatusCodes.Bad_UnexpectedError);

        if (exception instanceof UaRuntimeException)
        {
            errorCode = ((UaRuntimeException)exception).getStatusCode();
        }

        ReadResponse response = new ReadResponse();
        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                requestHeader,
                errorCode);
        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }
}
