package org.opcfoundation.webapi.serverapi;

import jakarta.annotation.Nullable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult;
import org.opcfoundation.webapi.mapper.UaPayloadMapper;
import org.opcfoundation.webapi.mapper.extensionobjects.ExtensionObjectEncoder;
import org.opcfoundation.webapi.model.*;
import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.webapi.service.UaWebService;
import org.opcfoundation.webapi.service.types.HistoryReadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
@Controller
@RequestMapping("${openapi.oPCUAWeb.base-path:}")
public class HistoryreadApiController implements HistoryreadApi {

    private final NativeWebRequest request;

    @Autowired
    private UaWebService service;

    @Autowired
    public HistoryreadApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public CompletableFuture<ResponseEntity<HistoryReadResponse>> historyRead(
            String serverUri,
            HistoryReadRequest historyReadRequest)
    {
        try
        {
            UaServerConfigure serverConfig = service.getServerConfigure();
            if (!serverConfig.isServerUriPathSupported() && null != serverUri) throw new UaRuntimeException(StatusCodes.Bad_ServerUriInvalid);

            org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn timestampsToReturn = org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn.Neither;
            if (null != historyReadRequest.getTimestampsToReturn())
            {
                timestampsToReturn = TimestampsToReturn.from(historyReadRequest.getTimestampsToReturn());
            }

            if (null == timestampsToReturn) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

            if (historyReadRequest.getNodesToRead().isEmpty() || null == historyReadRequest.getHistoryReadDetails()) throw new UaRuntimeException(StatusCodes.Bad_NothingToDo);
            if (serverConfig.getHistoryReadRequestMaxSize() != 0 && historyReadRequest.getNodesToRead().size() > serverConfig.getHistoryReadRequestMaxSize())
                throw new UaRuntimeException(StatusCodes.Bad_RequestTooLarge);

            List<HistoryReadValueId> nodesToRead = UaPayloadMapper.historyReadValueIdsFromWebApi(historyReadRequest.getNodesToRead());
            ExtensionObject details = ExtensionObjectEncoder.Encoder.fromExtensionObjectWebApi(historyReadRequest.getHistoryReadDetails());

            if (null == details) throw new UaRuntimeException(StatusCodes.Bad_DecodingError);

            UaStructuredType detailsStruct = details.decode(ExtensionObjectEncoder.Encoder.getEncodingContext());
            if (!(detailsStruct instanceof org.eclipse.milo.opcua.stack.core.types.structured.ReadRawModifiedDetails) &&
                    !(detailsStruct instanceof org.eclipse.milo.opcua.stack.core.types.structured.ReadAtTimeDetails) &&
                    !(detailsStruct instanceof org.eclipse.milo.opcua.stack.core.types.structured.ReadProcessedDetails) &&
                    !(detailsStruct instanceof org.eclipse.milo.opcua.stack.core.types.structured.ReadEventDetails)) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

            HistoryReadContext historyReadContext = new HistoryReadContext(
                    nodesToRead,
                    detailsStruct,
                    historyReadRequest.getReleaseContinuationPoints(),
                    timestampsToReturn,
                    serverUri,
                    UaPayloadMapper.requestHeaderFromWebApi(historyReadRequest.getRequestHeader()),
                    (getRequest().isPresent()) ? getRequest().get() : null);

            return service.historyRead(historyReadContext)
                    .thenApply(results -> historyReadComplete(historyReadRequest, results))
                    .exceptionally(ex-> getErrorResponse(historyReadRequest.getRequestHeader(), ex.getCause()));

        } catch (Exception e) {
            return CompletableFuture.completedFuture(getErrorResponse(historyReadRequest.getRequestHeader(), e));
        }
    }

    public ResponseEntity<HistoryReadResponse> historyReadComplete(
            HistoryReadRequest historyReadRequest,
            List<HistoryReadResult> results)
    {
        StatusCode statusCode = StatusCode.GOOD;
        HistoryReadResponse response = new HistoryReadResponse();

        if (results.size() == historyReadRequest.getNodesToRead().size())
        {
            response.setResults(UaPayloadMapper.historyReadResultsFromMilo(results));
        } else {
            statusCode = new StatusCode(StatusCodes.Bad_InternalError);
        }

        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                historyReadRequest.getRequestHeader(),
                statusCode);

        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<HistoryReadResponse> getErrorResponse(
            @Nullable RequestHeader requestHeader,
            Throwable exception)
    {
        StatusCode errorCode = StatusCode.of(StatusCodes.Bad_UnexpectedError);

        if (exception instanceof UaRuntimeException)
        {
            errorCode = ((UaRuntimeException)exception).getStatusCode();
        }

        HistoryReadResponse response = new HistoryReadResponse();
        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                requestHeader,
                errorCode);
        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }
}
