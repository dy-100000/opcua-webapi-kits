package org.opcfoundation.webserver.addressspace.models;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.addressspace.nodes.UaReferenceType;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaReferenceTypes;

public class UaCustomizedReferenceType extends UaReferenceType {
    UaCustomizedReferenceType(
            String referenceTypeId,
            LocalizedText displayName,
            LocalizedText inverseName,
            boolean isHierarchical,
            NodeManager nodeManager)
    {
        super(new NodeId(nodeManager.nsIndex(), referenceTypeId),
                referenceTypeId,
                displayName,
                false,
                inverseName,
                !isHierarchical && inverseName.isNull());

        if (isHierarchical)
        {
            setParentType(UaReferenceTypes.Organizes);
        } else {
            setParentType(UaReferenceTypes.NonHierarchicalReferences);
        }
    }
}
