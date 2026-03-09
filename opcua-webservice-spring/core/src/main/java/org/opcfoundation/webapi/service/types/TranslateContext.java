package org.opcfoundation.webapi.service.types;

import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

public class TranslateContext extends ServiceContext {
    private final List<BrowsePath> nodesToTranslate;

    public TranslateContext(
            List<BrowsePath> nodesToTranslate,
            @Nullable String serverUri,
            @Nullable RequestHeader header,
            @Nullable NativeWebRequest webRequest)
    {
        super(serverUri,header,webRequest);
        this.nodesToTranslate = nodesToTranslate;
    }

    public List<BrowsePath> getNodesToTranslate() {
        return nodesToTranslate;
    }
}
