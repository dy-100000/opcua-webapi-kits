package org.opcfoundation.webapi.serverapi;


import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.opcfoundation.webapi.mapper.UaPayloadMapper;
import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.webapi.service.UaWebService;
import org.opcfoundation.webapi.service.types.ReadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import org.opcfoundation.webapi.model.HistoryReadRequest;
import org.opcfoundation.webapi.model.HistoryReadResponse;
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

    /*
    @Override
    public CompletableFuture<ResponseEntity<HistoryReadResponse>> historyRead(
            String serverUri,
            HistoryReadRequest historyReadRequest)
    {
        try
        {
            UaServerConfigure serverConfig = service.getServerConfigure();
            if (serverConfig.isServerUriPathSupported() && null != serverUri) throw new UaRuntimeException(StatusCodes.Bad_ServerUriInvalid);

            org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn timestampsToReturn = org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn.Neither;
            if (null != historyReadRequest.getTimestampsToReturn())
            {
                timestampsToReturn = TimestampsToReturn.from(historyReadRequest.getTimestampsToReturn());
            }

            if (null == timestampsToReturn) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

            if (historyReadRequest.getNodesToRead().isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_NothingToDo);
            if (serverConfig.getReadRequestMaxSize() != 0 && historyReadRequest.getNodesToRead().size() > serverConfig.getReadRequestMaxSize())
                throw new UaRuntimeException(StatusCodes.Bad_RequestTooLarge);

            List<ReadValueId> nodesToRead = UaPayloadMapper.nodesToReadFromWebApi(readRequest.getNodesToRead());

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
    }*/
}
