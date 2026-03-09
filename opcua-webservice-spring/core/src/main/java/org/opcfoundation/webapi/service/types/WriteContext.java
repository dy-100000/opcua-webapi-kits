package org.opcfoundation.webapi.service.types;

import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

public class WriteContext extends ServiceContext {
    final private List<WriteValue> nodesToWrite;

    public WriteContext(
            List<WriteValue> nodesToWrite,
            @Nullable String serverUri,
            @Nullable RequestHeader header,
            @Nullable NativeWebRequest webRequest)
    {
        super(serverUri,header,webRequest);
        this.nodesToWrite = nodesToWrite;
    }

    public List<WriteValue> getNodesToWrite() {
        return nodesToWrite;
    }
}
