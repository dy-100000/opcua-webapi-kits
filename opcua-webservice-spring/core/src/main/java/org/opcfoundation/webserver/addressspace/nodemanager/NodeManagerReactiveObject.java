package org.opcfoundation.webserver.addressspace.nodemanager;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.enumerated.*;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.springframework.lang.Nullable;
import org.opcfoundation.webserver.addressspace.reactiveobject.UaObjectTransactionManager;
import org.opcfoundation.webserver.addressspace.nodes.UaDataType;
import org.opcfoundation.webserver.addressspace.nodes.UaNode;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjects;
import org.opcfoundation.webserver.addressspace.reactiveobject.UaReactiveObjectType;
import org.opcfoundation.webserver.types.common.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.common.UaInstanceIdentifier;
import org.opcfoundation.webserver.types.common.UaObjectIdentifier;
import org.opcfoundation.webserver.service.transactions.base.UaBrowseTransaction;
import org.opcfoundation.webserver.service.transactions.base.UaMethodCallTransaction;
import org.opcfoundation.webserver.service.transactions.base.UaReadTransaction;
import org.opcfoundation.webserver.service.transactions.base.UaWriteTransaction;
import org.opcfoundation.webapi.service.types.CallContext;
import org.opcfoundation.webapi.service.types.ReadContext;
import org.opcfoundation.webapi.service.types.ServiceContext;
import org.opcfoundation.webapi.service.types.WriteContext;

import java.util.*;

public class NodeManagerReactiveObject extends NodeManager {

    public NodeManagerReactiveObject(String namespaceUri)
    {
        super(namespaceUri);
    }

    @Deprecated
    public final void addRootObject(String objectId, UaReactiveObjectType objectType) throws UaRuntimeException
    {
        UaInstanceIdentifier objectIdentifier = new UaInstanceIdentifier(
                new UaObjectIdentifier(objectType.nodeId().toParseableString(), objectId, null),
                null);

        NodeId objectNodeId = new NodeId(nsIndex(), objectIdentifier.toByteString());

        UaObject newObject = new UaObject(
                objectNodeId,
                objectId,
                new LocalizedText(objectId),
                objectType);

        this.addNode(newObject);

        UaObjects.ObjectsFolder.organizes(newObject);
    }

    public final void addObjectType(UaReactiveObjectType objectType) throws UaRuntimeException
    {
        super.addNode(objectType);
    }

    public final void addDataType(UaDataType dataType) throws UaRuntimeException
    {
        super.addNode(dataType);
    }

    @Override
    public final void addNode(UaNode node) throws UaRuntimeException {
        if (node.nodeClass() == NodeClass.ObjectType ||
                node.nodeClass() == NodeClass.DataType ||
                node.nodeClass() == NodeClass.VariableType ||
                node.nodeClass() == NodeClass.ReferenceType ||
                node.nodeClass() == NodeClass.View) throw new UaRuntimeException(StatusCodes.Bad_NodeClassInvalid);
        super.addNode(node);
    }

    public final @Nullable UaReactiveObjectType findObjectType(UaObjectIdentifier objectId)
    {
        NodeId objectTypeId = NodeId.parseOrNull(objectId.getTypeId());
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
        return new UaObjectTransactionManager(this).getBrowseTransaction(
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
        return new UaObjectTransactionManager(this).getReadTransactions(context, handleIds);
    }

    @Override
    public final List<UaWriteTransaction> getWriteTransactions(
            WriteContext context,
            List<Integer> handleIds)
    {
        return new UaObjectTransactionManager(this).getWriteTransactions(context, handleIds);
    }

    @Override
    public final UaMethodCallTransaction getMethodCallTransaction(
            CallContext context,
            int handleId)
    {
        return new UaObjectTransactionManager(this).getMethodCallTransaction(context, handleId);
    }
}
