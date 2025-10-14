package org.opcfoundation.webserver.addressspace.transactions;

import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerWebService;
import org.opcfoundation.webserver.addressspace.nodes.UaNode;
import org.opcfoundation.webserver.service.transactions.UaBrowseTransaction;
import org.opcfoundation.webserver.types.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.UaInstanceIdentifier;
import org.opcfoundation.webserver.types.UaMemberIdentifier;
import org.opcfoundation.webapi.service.types.ServiceContext;

import java.util.concurrent.CompletableFuture;

public class UaBrowseMemberTransaction extends UaBrowseTransaction {
    private final UaInstanceIdentifier identifier;
    private final NodeManagerWebService nodeManager;

    public UaBrowseMemberTransaction(
            ServiceContext serviceContext,
            BrowseDescription nodeToBrowse,
            UaBrowseAdditionalInfo additionalInfo,
            int handleId,
            UaInstanceIdentifier identifier,
            NodeManagerWebService nodeManager)
    {
        super(serviceContext, nodeToBrowse, additionalInfo, handleId);
        this.identifier = identifier;
        this.nodeManager = nodeManager;
    }

    public CompletableFuture<Void> execute()
    {
        return CompletableFuture.supplyAsync(()-> {
            try {
                BrowseDescription browseDescription = getItem();
                if ((browseDescription.getNodeClassMask().intValue() & NodeClass.Variable.getValue()) == 0) return null;

                UaNode memberNode = nodeManager.findMember(identifier);
                if (null == memberNode || null == identifier.getMemberId()) {
                    statusCode = StatusCode.of(StatusCodes.Bad_NodeIdUnknown);
                    return null;
                }

                boolean isAggregatesReference = false;
                if (browseDescription.getIncludeSubtypes()) {
                    if (browseDescription.getReferenceTypeId().equals(NodeIds.Aggregates) ||
                            browseDescription.getReferenceTypeId().equals(NodeIds.HasChild) ||
                            browseDescription.getReferenceTypeId().equals(NodeIds.HierarchicalReferences) ||
                            browseDescription.getReferenceTypeId().equals(NodeIds.References))
                        isAggregatesReference = true;
                } else {
                    if (browseDescription.getReferenceTypeId().equals(NodeIds.HasComponent) ||
                            browseDescription.getReferenceTypeId().equals(NodeIds.HasProperty))
                        isAggregatesReference = true;
                }

                if (browseDescription.getBrowseDirection() == BrowseDirection.Inverse || !isAggregatesReference)
                    return null;

                BrowseDescription newBrowseDescription = new BrowseDescription(
                        memberNode.nodeId(),
                        BrowseDirection.Forward,
                        NodeIds.Aggregates,
                        true,
                        UInteger.valueOf(NodeClass.Variable.getValue()),
                        UInteger.valueOf(browseDescription.getResultMask().intValue() | BrowseResultMask.NodeClass.getValue() | BrowseResultMask.BrowseName.getValue()));

                BrowseResult result = nodeManager.browseNode(newBrowseDescription, new UaBrowseAdditionalInfo(0, 0, 0));
                if (!result.getStatusCode().isGood() || null == result.getReferences()) {
                    statusCode = result.getStatusCode();
                    return null;
                }

                for (ReferenceDescription item : result.getReferences()) {
                    if (item.getNodeClass() != NodeClass.Variable ||
                            item.getBrowseName().isNull()) continue;

                    UaMemberIdentifier newMemberId = new UaMemberIdentifier(
                            identifier.getMemberId().getPath(),
                            item.getBrowseName().getName(),
                            identifier.getMemberId().getVariableTypeId());

                    UaInstanceIdentifier newIdentifier = new UaInstanceIdentifier(
                            identifier.getObjectId(),
                            newMemberId);

                    //System.out.println("Result: " + newIdentifier);

                    references.add(new ReferenceDescription(
                            item.getReferenceTypeId(),
                            item.getIsForward(),
                            new NodeId(nodeManager.nsIndex(), newIdentifier.toByteString()).expanded(),
                            item.getBrowseName(),
                            item.getDisplayName(),
                            item.getNodeClass(),
                            item.getTypeDefinition()));
                }
            } catch (Exception e) {
                buildErrorResponse(e);
            }

            return null;
        });
    }
}
