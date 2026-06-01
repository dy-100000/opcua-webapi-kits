package org.opcfoundation.webserver.addressspace.nodemanager;

import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.enumerated.*;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.opcfoundation.webapi.service.types.*;
import org.opcfoundation.webserver.service.transactions.base.*;
import org.springframework.lang.Nullable;
import org.opcfoundation.webserver.addressspace.reactiveobject.UaReactiveObjectTransactionManager;
import org.opcfoundation.webserver.addressspace.nodes.UaNode;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.addressspace.reactiveobject.UaReactiveObjectType;
import org.opcfoundation.webserver.types.common.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.common.UaObjectIdentifier;

import java.util.*;

public class NodeManagerReactiveObject extends NodeManager {

    public NodeManagerReactiveObject(String namespaceUri)
    {
        super(namespaceUri);
    }

    public final @Nullable UaReactiveObjectType findObjectType(UaObjectIdentifier objectId)
    {
        NodeId objectTypeId = null;

        if (null != objectId.getTypeId())
        {
            objectTypeId = NodeId.parseOrNull(objectId.getTypeId());
        } else {
            if (null == objectId.getInstanceDeclId()) return null;

            NodeId instanceDeclId = NodeId.parseOrNull(objectId.getInstanceDeclId());
            if (null == instanceDeclId) return null;

            UaNode instanceDeclNode = getNode(instanceDeclId);
            if (null == instanceDeclNode || NodeClass.Object != instanceDeclNode.nodeClass()) return null;

            objectTypeId = ((UaObject)instanceDeclNode).typeDefinition().nodeId();
        }

        if (null == objectTypeId || objectTypeId.getType() != IdType.String) return null;

        UaNode node = getNode(objectTypeId);
        if (null == node || NodeClass.ObjectType != node.nodeClass()) return null;

        return (UaReactiveObjectType)node;
    }

    public final @Nullable UaObject findInstanceDeclaration(UaObjectIdentifier objectId)
    {
        if (null == objectId.getInstanceDeclId()) return null;
        NodeId instanceDeclarationId = NodeId.parseOrNull(objectId.getInstanceDeclId());
        if (null == instanceDeclarationId) return null;

        UaNode instanceDeclaration = getNode(instanceDeclarationId);
        if (null == instanceDeclaration || instanceDeclaration.nodeClass() != NodeClass.Object) return null;
        return (UaObject) instanceDeclaration;
    }

    @Override
    public final UaBrowseTransaction getBrowseTransaction(
            ServiceContext context,
            BrowseDescription nodeToBrowse,
            UaBrowseAdditionalInfo additionalInfo,
            int handleId)
    {
        return new UaReactiveObjectTransactionManager(this).getBrowseTransaction(
                context,
                nodeToBrowse,
                additionalInfo,
                handleId);
    }

    @Override
    public final List<UaReadTransaction> getReadTransactions(
            ReadContext context,
            List<Integer> handleIds)
    {
        return new UaReactiveObjectTransactionManager(this).getReadTransactions(context, handleIds);
    }

    @Override
    public final List<UaWriteTransaction> getWriteTransactions(
            WriteContext context,
            List<Integer> handleIds)
    {
        return new UaReactiveObjectTransactionManager(this).getWriteTransactions(context, handleIds);
    }

    @Override
    public final UaMethodCallTransaction getMethodCallTransaction(
            CallContext context,
            int handleId)
    {
        return new UaReactiveObjectTransactionManager(this).getMethodCallTransaction(context, handleId);
    }

    @Override
    public final UaHistoryReadTransaction getHistoryReadTransaction(
            HistoryReadContext context,
            int handleId)
    {
        return new UaReactiveObjectTransactionManager(this).getHistoryReadTransaction(context, handleId);
    }
}
