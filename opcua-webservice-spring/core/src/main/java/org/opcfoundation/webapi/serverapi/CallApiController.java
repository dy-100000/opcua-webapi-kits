package org.opcfoundation.webapi.serverapi;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.springframework.lang.Nullable;
import org.opcfoundation.webapi.mapper.UaPayloadMapper;
import org.opcfoundation.webapi.model.CallRequest;
import org.opcfoundation.webapi.model.CallResponse;


import org.opcfoundation.webapi.model.RequestHeader;
import org.opcfoundation.webapi.model.ResponseHeader;
import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.webapi.service.UaWebService;
import org.opcfoundation.webapi.service.types.CallContext;
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
public class CallApiController implements CallApi {

    private final NativeWebRequest request;

    @Autowired
    private UaWebService service;

    @Autowired
    public CallApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public CompletableFuture<ResponseEntity<CallResponse>> call(
            String serverUri,
            CallRequest callRequest)
    {
        try
        {
            UaServerConfigure serverConfig = service.getServerConfigure();

            if (!serverConfig.isServerUriPathSupported() && null != serverUri) throw new UaRuntimeException(StatusCodes.Bad_ServerUriInvalid);
            if (callRequest.getMethodsToCall().isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_NothingToDo);
            if (serverConfig.getCallRequestMaxSize() != 0 && callRequest.getMethodsToCall().size() > serverConfig.getCallRequestMaxSize())
                throw new UaRuntimeException(StatusCodes.Bad_RequestTooLarge);

            List<org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest> methodsToCall= UaPayloadMapper.callMethodRequestsFromWebApi(callRequest.getMethodsToCall());

            CallContext callContext = new CallContext(
                    methodsToCall,
                    serverUri,
                    UaPayloadMapper.requestHeaderFromWebApi(callRequest.getRequestHeader()),
                    (getRequest().isPresent()) ? getRequest().get() : null);

            return service.call(callContext)
                    .thenApply(results -> callComplete(callRequest, results))
                    .exceptionally(ex-> getErrorResponse(callRequest.getRequestHeader(), ex.getCause()) );

        } catch (Exception e) {
            return CompletableFuture.completedFuture(getErrorResponse(callRequest.getRequestHeader(), e));
        }
    }

    public ResponseEntity<CallResponse> callComplete(
            CallRequest callRequest,
            List<org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult> results)
    {
        StatusCode statusCode = StatusCode.GOOD;
        CallResponse response = new CallResponse();

        if (results.size() == callRequest.getMethodsToCall().size())
        {
            response.setResults(UaPayloadMapper.callMethodResultsFromMilo(results));
        } else {
            statusCode = new StatusCode(StatusCodes.Bad_InternalError);
        }

        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                callRequest.getRequestHeader(),
                statusCode);

        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<CallResponse> getErrorResponse(
            @Nullable RequestHeader requestHeader,
            Throwable exception)
    {
        StatusCode errorCode = StatusCode.of(StatusCodes.Bad_UnexpectedError);

        if (exception instanceof UaRuntimeException)
        {
            errorCode = ((UaRuntimeException)exception).getStatusCode();
        }

        CallResponse response = new CallResponse();
        ResponseHeader responseHeader = UaPayloadMapper.responseHeaderFromMilo(
                requestHeader,
                errorCode);

        response.setResponseHeader(responseHeader);

        return ResponseEntity.ok(response);
    }
}
