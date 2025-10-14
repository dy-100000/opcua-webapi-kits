package org.opcfoundation.webapi.service.types;

import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.jspecify.annotations.Nullable;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

public class WriteContext extends ServiceContext {
    final private List<WriteValue> nodesToWrite;

    public WriteContext(
            List<WriteValue> nodesToWrite,
            RequestHeader header,
            @Nullable String serverUri,
            NativeWebRequest webRequest)
    {
        super(header,serverUri,webRequest);
        this.nodesToWrite = nodesToWrite;
    }

    public List<WriteValue> getNodesToWrite() {
        return nodesToWrite;
    }
}
