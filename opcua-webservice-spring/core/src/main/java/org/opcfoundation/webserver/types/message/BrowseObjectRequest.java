package org.opcfoundation.webserver.types.message;

import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.opcfoundation.webserver.types.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.UaObjectId;

public class BrowseObjectRequest {
    private final UaObjectId objectId;
    private final UaBrowseAdditionalInfo additionalInfo;
    private final BrowseDescription browseDescription;

    public BrowseObjectRequest(
            UaObjectId objectId,
            UaBrowseAdditionalInfo additionalInfo,
            BrowseDescription browseDescription)
    {
        this.objectId = objectId;
        this.additionalInfo = additionalInfo;
        this.browseDescription = browseDescription;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public UaBrowseAdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public BrowseDescription getBrowseDescription() { return browseDescription; }
}
