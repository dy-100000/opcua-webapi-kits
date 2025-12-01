package org.opcfoundation.webserver.addressspace.nodes;

public class UaReference {
    private final UaNode linkedNode;
    private final UaReferenceType reference;
    private final boolean isForward;

    public UaReference(
            UaNode linkedNode,
            UaReferenceType reference,
            boolean isForward)
    {
        this.linkedNode = linkedNode;
        this.reference = reference;
        this.isForward = isForward;
    }

    public UaNode linkedNode() {
        return linkedNode;
    }

    public UaReferenceType reference() {
        return reference;
    }

    public boolean isForward() {
        return isForward;
    }

    @Override
    public String toString() {
        String ret = "{";
        ret += "Linked Node: " + linkedNode.nodeId() + " " + linkedNode.browseName();
        ret += " Reference " + reference().browseName();
        ret += " IsForward " + isForward;
        ret += " }";
        return ret;
    }
}
