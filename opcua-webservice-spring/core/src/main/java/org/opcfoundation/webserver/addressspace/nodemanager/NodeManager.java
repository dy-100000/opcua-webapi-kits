package org.opcfoundation.webserver.addressspace.nodemanager;

import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.springframework.lang.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.UaNode;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.addressspace.nodes.UaReference;
import org.opcfoundation.webserver.addressspace.nodes.UaVariable;
import org.opcfoundation.webserver.types.common.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.common.UaBrowseContinuationPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class NodeManager implements NodeManagerBase {

    private final int nsIndex;
    private final String namespaceUri;
    private final HashMap<NodeId, UaNode> nodes;

    public NodeManager(String namespaceUri)
    {
        this.nsIndex = NodeManagerList.nodeManagerList.getNewNsIndex();
        this.namespaceUri = namespaceUri;
        this.nodes = new HashMap<>();
    }

    @Override
    public int nsIndex() {
        return nsIndex;
    }

    @Override
    public String namespaceUri() {
        return namespaceUri;
    }

    @Override
    public @Nullable UaNode getNode(NodeId nodeId)
    {
        if (nsIndex() == nodeId.getNamespaceIndex().intValue())
        {
            return nodes.get(nodeId);
        } else {
            NodeManagerBase nodeManager = NodeManagerList.nodeManagerList.getNodeManager(nodeId.getNamespaceIndex().intValue());
            if (null == nodeManager) return null;
            return nodeManager.getNode(nodeId);
        }
    }

    public void addNode(UaNode node) throws UaRuntimeException
    {
        if (nodes.containsKey(node.nodeId())) throw new UaRuntimeException(StatusCodes.Bad_NodeIdExists);
        if (node.nodeId().getNamespaceIndex().intValue() != nsIndex()) throw new UaRuntimeException(StatusCodes.Bad_NodeIdInvalid);
        nodes.put(node.nodeId(), node);
    }

    public @Nullable List<UaReference> browseNode(
            NodeId nodeId,
            BrowseDirection browseDirection,
            NodeId referenceTypeId,
            Boolean includeSubtypes,
            int nodeClassMask)
    {
        UaNode nodeToBrowse = getNode(nodeId);
        if (null == nodeToBrowse) return null;

        List<UaReference> references = nodeToBrowse.getReferences(browseDirection);

        ArrayList<UaReference> refsToReturn = new ArrayList<>();

        for (UaReference item : references)
        {
            if ((item.linkedNode().nodeClass().getValue() & nodeClassMask) == 0) continue;

            if (includeSubtypes)
            {
                if (!item.reference().isSubtypeOf(referenceTypeId)) continue;
            } else {
                if (!item.reference().nodeId().equals(referenceTypeId)) continue;
            }

            refsToReturn.add(item);
        }

        return refsToReturn;
    }

    public BrowseResult browse(
            BrowseDescription browseDescription,
            UaBrowseAdditionalInfo additionalInfo)
    {
        List<UaReference> referencesOfNode = browseNode(
                browseDescription.getNodeId(),
                browseDescription.getBrowseDirection(),
                browseDescription.getReferenceTypeId(),
                browseDescription.getIncludeSubtypes(),
                browseDescription.getNodeClassMask().intValue());

        if (null == referencesOfNode) return new BrowseResult(StatusCode.of(StatusCodes.Bad_NodeIdUnknown), ByteString.NULL_VALUE, null);

        // Determinate the range
        boolean isCPRequired = false;
        int fromIndex = additionalInfo.getReferenceOffset();
        int toIndex = referencesOfNode.size();

        if (referencesOfNode.size() <= fromIndex) return new BrowseResult(StatusCode.of(StatusCodes.Good), ByteString.NULL_VALUE, null);

        if (0 < additionalInfo.getMaxReferencesPerNode())
        {
            toIndex = fromIndex + additionalInfo.getMaxReferencesPerNode();
            if (toIndex >= referencesOfNode.size())
            {
                toIndex = referencesOfNode.size();
            } else {
                isCPRequired = true;
            }
        }

        List<UaReference> referencesToReturn = referencesOfNode.subList(fromIndex, toIndex);
        ArrayList<ReferenceDescription> references = new ArrayList<>();

        for (UaReference item: referencesToReturn)
        {
            NodeId referenceTypeId = NodeId.NULL_VALUE;
            QualifiedName browseName = QualifiedName.NULL_VALUE;
            LocalizedText displayName = LocalizedText.NULL_VALUE;
            NodeClass nodeClass = NodeClass.Unspecified;
            NodeId typeDefinition = NodeId.NULL_VALUE;

            if (0 != (browseDescription.getResultMask().intValue() & BrowseResultMask.ReferenceTypeId.getValue()))
            {
                referenceTypeId = item.reference().nodeId();
            }

            if (0 != (browseDescription.getResultMask().intValue() & BrowseResultMask.BrowseName.getValue()))
            {
                browseName = new QualifiedName(0,item.linkedNode().browseName());
            }

            if (0 != (browseDescription.getResultMask().intValue() & BrowseResultMask.DisplayName.getValue()))
            {
                displayName =  item.linkedNode().displayName();
            }

            if (0 != (browseDescription.getResultMask().intValue() & BrowseResultMask.NodeClass.getValue()))
            {
                nodeClass = item.linkedNode().nodeClass();
            }

            if (0 != (browseDescription.getResultMask().intValue() & BrowseResultMask.TypeDefinition.getValue()))
            {
                if (item.linkedNode().nodeClass() == NodeClass.Object)
                {
                    typeDefinition = ((UaObject)item.linkedNode()).typeDefinition().nodeId();
                }

                if (item.linkedNode().nodeClass() == NodeClass.Variable)
                {
                    typeDefinition = ((UaVariable)item.linkedNode()).typeDefinition().nodeId();
                }
            }

            references.add(new ReferenceDescription(
                    referenceTypeId,
                    item.isForward(),
                    item.linkedNode().nodeId().expanded(),
                    browseName,
                    displayName,
                    nodeClass,
                    typeDefinition.expanded()));
        }

        ByteString continuationPoint = ByteString.NULL_VALUE;
        if (isCPRequired)
        {
            UaBrowseContinuationPoint cp = new UaBrowseContinuationPoint(browseDescription, new UaBrowseAdditionalInfo(additionalInfo.getMaxReferencesPerNode(), toIndex, 0));
            continuationPoint = cp.toByteString();
        }

        return new BrowseResult(StatusCode.of(StatusCodes.Good), continuationPoint, references.toArray(new ReferenceDescription[0]));
    }

    public DataValue read(
            NodeId nodeId,
            int attributeId,
            DateTime now,
            TimestampsToReturn timestampsToReturn)
    {
        UaNode nodeToRead = getNode(nodeId);

        Variant value = Variant.NULL_VALUE;
        StatusCode statusCode = StatusCode.GOOD;
        DateTime sourceTimestamp = null;
        DateTime serverTimestamp = null;

        if (null != nodeToRead)
        {
            value = nodeToRead.getAttribute(attributeId);
            if (value.isNull() && AttributeId.Value.id() != attributeId)
            {
                statusCode =  StatusCode.of(StatusCodes.Bad_AttributeIdInvalid);
            }
        } else {
            statusCode = StatusCode.of(StatusCodes.Bad_NodeIdUnknown);
        }

        if (statusCode.isGood() && AttributeId.Value.id() == attributeId)
        {
            if (TimestampsToReturn.Both == timestampsToReturn ||
                    TimestampsToReturn.Source == timestampsToReturn)
            {
                sourceTimestamp = now;
            }

            if (TimestampsToReturn.Both == timestampsToReturn ||
                    TimestampsToReturn.Server == timestampsToReturn)
            {
                serverTimestamp = now;
            }
        }

        return new DataValue(value, statusCode, sourceTimestamp, serverTimestamp);
    }
}
