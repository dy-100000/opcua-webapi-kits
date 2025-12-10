package org.opcfoundation.webserver.service.message.reactiveobject;

import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.opcfoundation.webserver.types.common.UaObjectId;

public class BrowseMemberRequest {
    private final UaObjectId objectId;
    private final String childId;
    private final boolean isMethod;
    private final BrowseDescription browseDescription;

    public BrowseMemberRequest(
            UaObjectId objectId,
            String childId,
            boolean isMethod,
            BrowseDescription browseDescription)
    {
        this.objectId = objectId;
        this.childId = childId;
        this.isMethod = isMethod;
        this.browseDescription = browseDescription;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public String getChildId() {
        return childId;
    }

    public boolean isMethod() {
        return isMethod;
    }

    public BrowseDescription getBrowseDescription() { return browseDescription; }
}
