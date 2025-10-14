package org.opcfoundation.webapi.service.types;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.jspecify.annotations.Nullable;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

public class BrowseContext extends ServiceContext {
    private final List<BrowseDescription> nodesToBrowse;
    private final ViewDescription view;
    private final UInteger requestedMaxReferencesPerNode;

    public BrowseContext(
            List<BrowseDescription> nodesToBrowse,
            ViewDescription view,
            UInteger requestedMaxReferencesPerNode,
            RequestHeader header,
            @Nullable String serverUri,
            NativeWebRequest webRequest)
    {
        super(header,serverUri,webRequest);
        this.nodesToBrowse = nodesToBrowse;
        this.view = view;
        this.requestedMaxReferencesPerNode = requestedMaxReferencesPerNode;
    }

    public List<BrowseDescription> getNodesToBrowse() {
        return nodesToBrowse;
    }

    public ViewDescription getView() {
        return view;
    }

    public UInteger getRequestedMaxReferencesPerNode() {
        return requestedMaxReferencesPerNode;
    }
}
