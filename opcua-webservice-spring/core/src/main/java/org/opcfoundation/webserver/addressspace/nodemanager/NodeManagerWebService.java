package org.opcfoundation.webserver.addressspace.nodemanager;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.enumerated.*;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.UaDataType;
import org.opcfoundation.webserver.addressspace.nodes.UaInstanceNode;
import org.opcfoundation.webserver.addressspace.nodes.UaNode;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjects;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;
import org.opcfoundation.webserver.addressspace.models.UaVariableDirectoryType;
import org.opcfoundation.webserver.addressspace.models.UaObjectType;
import org.opcfoundation.webserver.types.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.UaInstanceIdentifier;
import org.opcfoundation.webserver.types.UaMemberIdentifier;
import org.opcfoundation.webserver.types.UaObjectIdentifier;
import org.opcfoundation.webserver.service.transactions.UaBrowseTransaction;
import org.opcfoundation.webserver.service.transactions.UaMethodCallTransaction;
import org.opcfoundation.webserver.service.transactions.UaReadTransaction;
import org.opcfoundation.webserver.service.transactions.UaWriteTransaction;
import org.opcfoundation.webapi.service.types.CallContext;
import org.opcfoundation.webapi.service.types.ReadContext;
import org.opcfoundation.webapi.service.types.ServiceContext;
import org.opcfoundation.webapi.service.types.WriteContext;

import java.util.*;

public class NodeManagerWebService extends NodeManager {

    public NodeManagerWebService(String namespaceUri)
    {
        super(namespaceUri);
    }

    public void addRootObject(String objectId, UaObjectType objectType) throws UaRuntimeException
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

    public void addObjectType(UaObjectType objectType) throws UaRuntimeException
    {
        super.addNode(objectType);
    }

    public void addDataType(UaDataType dataType) throws UaRuntimeException
    {
        super.addNode(dataType);
    }

    @Override
    public void addNode(UaNode node) throws UaRuntimeException {
        if (node.nodeClass() == NodeClass.ObjectType ||
                node.nodeClass() == NodeClass.DataType ||
                node.nodeClass() == NodeClass.VariableType ||
                node.nodeClass() == NodeClass.ReferenceType ||
                node.nodeClass() == NodeClass.View) throw new UaRuntimeException(StatusCodes.Bad_NodeClassInvalid);
        super.addNode(node);
    }

    public @Nullable UaObjectType findObjectType(UaObjectIdentifier objectId)
    {
        NodeId objectTypeId = NodeId.parseOrNull(objectId.getTypeId());
        if (null == objectTypeId || objectTypeId.getType() != IdType.String) return null;

        UaNode node = getNode(objectTypeId);
        if (null == node || NodeClass.ObjectType != node.nodeClass()) return null;

        return (UaObjectType)node;
    }

    public @Nullable UaObject findInstanceDeclaration(UaObjectIdentifier objectId)
    {
        if (null == objectId.getInstanceDeclId()) return null;
        NodeId instanceDeclarationId = NodeId.parseOrNull(objectId.getInstanceDeclId());
        if (null == instanceDeclarationId) return null;

        UaNode instanceDeclaration = getNode(instanceDeclarationId);
        if (null == instanceDeclaration || instanceDeclaration.nodeClass() != NodeClass.Object) return null;
        return (UaObject) instanceDeclaration;
    }

    public @Nullable UaNode findMember(UaInstanceIdentifier identifier)
    {
        if (null == identifier.getMemberId()) return null;

        UaMemberIdentifier memberId = identifier.getMemberId();
        UaObjectType objectType = findObjectType(identifier.getObjectId());

        if (null == memberId || null == objectType) return null;

        UaNode memberNode = null;
        if (null == memberId.getVariableTypeId())
        {
            memberNode = objectType.getMember(memberId.getPath());
        } else {
            if (memberId.getVariableTypeId().isEmpty())
            {
                memberNode = UaVariableTypes.BaseDataVariableType;
            } else {
                NodeId variableTypeId = NodeId.parseOrNull(memberId.getVariableTypeId());
                if (null != variableTypeId) memberNode = getNode(variableTypeId);
            }
        }

        if (null == memberNode || null == memberId.getPathL2()) return memberNode;

        return memberNode.getMember(memberId.getPathL2());
    }

    @Override
    public UaBrowseTransaction getBrowseTransaction(
            ServiceContext context,
            BrowseDescription nodeToBrowse,
            UaBrowseAdditionalInfo additionalInfo,
            int handleId)
    {
        return new NodeManagerTransactionBuilder(this).getBrowseTransaction(
                context,
                nodeToBrowse,
                false,
                additionalInfo,
                handleId);
    }

    @Override
    public List<UaReadTransaction> getReadTransactions(
            ReadContext context,
            List<Integer> handleIds)
    {
        return new NodeManagerTransactionBuilder(this).getReadTransactions(context, handleIds);
    }

    @Override
    public List<UaWriteTransaction> getWriteTransactions(
            WriteContext context,
            List<Integer> handleIds)
    {
        return new NodeManagerTransactionBuilder(this).getWriteTransactions(context, handleIds);
    }

    @Override
    public UaMethodCallTransaction getMethodCallTransaction(
            CallContext context,
            int handleId)
    {
        return new NodeManagerTransactionBuilder(this).getMethodCallTransaction(context, handleId);
    }
}
