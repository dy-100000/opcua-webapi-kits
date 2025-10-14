package org.opcfoundation.webserver.types.message;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.webserver.types.UaObjectId;

public class BrowseChildrenRequest {
    private final UaObjectId objectId;
    private final NodeId referenceId;
    private final Integer nodeClassMask;
    private final int limit;
    private final int offset;

    public BrowseChildrenRequest(
            UaObjectId objectId,
            NodeId referenceId,
            Integer nodeClassMask,
            int limit,
            int offset)
    {
        this.objectId = objectId;
        this.referenceId = referenceId;
        this.nodeClassMask = nodeClassMask;
        this.limit = limit;
        this.offset = offset;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public NodeId getReferenceId() {
        return referenceId;
    }

    public Integer getNodeClassMask() {
        return nodeClassMask;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }
}
