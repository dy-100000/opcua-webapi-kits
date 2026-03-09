package org.opcfoundation.webapi.service.types;

import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

public class CallContext extends ServiceContext {
    private final List<CallMethodRequest> methodsToCall;

    public CallContext(
            List<CallMethodRequest> methodsToCall,
            @Nullable String serverUri,
            @Nullable RequestHeader header,
            @Nullable NativeWebRequest webRequest)
    {
        super(serverUri,header,webRequest);
        this.methodsToCall = methodsToCall;
    }

    public List<CallMethodRequest> getMethodsToCall() {
        return methodsToCall;
    }
}
