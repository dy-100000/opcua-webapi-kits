package org.opcfoundation.webserver.types.message;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.opcfoundation.webserver.types.UaObjectId;

public class BrowseObjectRequest {
    private final UaObjectId objectId;
    private final int limit;
    private final int offset;
    private final BrowseDescription browseDescription;

    public BrowseObjectRequest(
            UaObjectId objectId,
            int limit,
            int offset,
            BrowseDescription browseDescription)
    {
        this.objectId = objectId;
        this.limit = limit;
        this.offset = offset;
        this.browseDescription = browseDescription;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public BrowseDescription getBrowseDescription() { return browseDescription; }
}
