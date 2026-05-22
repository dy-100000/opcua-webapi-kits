package org.opcfoundation.webserver.addressspace.reactiveobject;

import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.opcfoundation.webapi.service.types.*;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerReactiveObject;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.service.transactions.base.*;
import org.opcfoundation.webserver.service.transactions.reactiveobject.*;
import org.opcfoundation.webserver.types.common.*;

import java.util.*;

public class UaObjectTransactionManager {
    private final NodeManagerReactiveObject nodeManager;

    public UaObjectTransactionManager(NodeManagerReactiveObject nodeManager)
    {
        this.nodeManager = nodeManager;
    }

    public UaBrowseTransaction getBrowseTransaction(
            ServiceContext context,
            BrowseDescription nodeToBrowse,
            UaBrowseAdditionalInfo additionalInfo,
            int handleId)
    {
        UaBrowseTransaction transactionNothingToDo = new UaBrowseTransaction(
                context,
                nodeToBrowse,
                additionalInfo,
                handleId);

        if (IdType.Opaque != nodeToBrowse.getNodeId().getType())
            return new UaBrowseNodeTransaction(
                    context,
                    nodeToBrowse,
                    additionalInfo,
                    handleId,
                    nodeManager);

        final UaInstanceIdentifier identifier = UaInstanceIdentifier.fromByteString((ByteString) nodeToBrowse.getNodeId().getIdentifier());
        if (null == identifier)
        {
            transactionNothingToDo.setStatusCode(StatusCode.of(StatusCodes.Bad_NodeIdInvalid));
            return transactionNothingToDo;
        }

        // Browse object
        UaReactiveObjectType objectType = nodeManager.findObjectType(identifier.getObjectId());

        if (null == objectType)
        {
            transactionNothingToDo.setStatusCode(StatusCode.of(StatusCodes.Bad_NodeIdUnknown));
            return transactionNothingToDo;
        }

        UaObject instanceDeclaration = nodeManager.findInstanceDeclaration(identifier.getObjectId());
        UaObjectId objectId = new UaObjectId(identifier.getObjectId().getId(), instanceDeclaration);

        //System.out.println("NodeToBrowse: " + identifier);
        if (null == identifier.getChildId())
        {
            return new UaBrowseObjectTransaction(
                    context,
                    nodeToBrowse,
                    additionalInfo,
                    handleId,
                    objectType,
                    objectId,
                    nodeManager);
        } else {
            return new UaBrowseMemberTransaction(
                    context,
                    nodeToBrowse,
                    additionalInfo,
                    handleId,
                    objectType,
                    objectId,
                    identifier.getChildId(),
                    nodeManager);
        }
    }

    public List<UaReadTransaction> getReadTransactions(
            ReadContext context,
            List<Integer> handleIds)
    {
        List<Integer> handleIdsForNodes = new ArrayList<>();
        Map<Integer, UaInstanceIdentifier> handleIdsForObjectAttributes = new TreeMap<>();
        Map<Integer, UaInstanceIdentifier> handleIdsForObjectMemberAttributes = new TreeMap<>();
        Map<Integer, UaInstanceIdentifier> handleIdsForObjectVariableValues = new TreeMap<>();

        for (Integer handleId: handleIds)
        {
            ReadValueId nodeToRead = context.getNodesToRead().get(handleId);
            NodeId nodeId = nodeToRead.getNodeId();

            if (nodeId.getType() == IdType.Opaque)
            {
                UaInstanceIdentifier identifier = UaInstanceIdentifier.fromByteString((ByteString) nodeId.getIdentifier());
                UaReactiveObjectType objectType = null;
                if (null != identifier) objectType = nodeManager.findObjectType(identifier.getObjectId());

                if (null != identifier && null != objectType)
                {
                    if (nodeToRead.getAttributeId().intValue() == AttributeId.Value.id())
                    {
                        if (null != identifier.getChildId())
                        {
                            if (null == identifier.getChildId().getMethodNode() ||
                                    !identifier.getChildId().getMethodNode())
                            {
                                handleIdsForObjectVariableValues.put(handleId, identifier);
                                //System.out.println("Read value: " + identifier);
                            } else {
                                handleIdsForObjectMemberAttributes.put(handleId, identifier);
                                //System.out.println("Read arguments: " + identifier);
                            }
                        } else {
                            handleIdsForNodes.add(handleId);
                            //System.out.println("Read error, id invalid");
                        }
                    } else {
                        if (null != identifier.getChildId())
                        {
                            handleIdsForObjectMemberAttributes.put(handleId, identifier);
                            //System.out.println("Read object member attribute: " + identifier);
                        } else {
                            handleIdsForObjectAttributes.put(handleId, identifier);
                            //System.out.println("Read object attribute: " + identifier);
                        }
                    }
                } else {
                    handleIdsForNodes.add(handleId);
                    //System.out.println("Read error, id invalid");
                }
            } else {
                handleIdsForNodes.add(handleId);
                //System.out.println("Read: " + nodeId.toParseableString());
            }
        }

        List<UaReadTransaction> transactions = new ArrayList<>();

        if (!handleIdsForNodes.isEmpty())
        {
            UaReadNodeTransaction transaction = new UaReadNodeTransaction(
                    context,
                    handleIdsForNodes,
                    nodeManager);

            transactions.add(transaction);
        }

        if (!handleIdsForObjectAttributes.isEmpty())
        {
            Map<UaObjectIdentifier, List<Integer>> handleIdsByObjectId = new HashMap<>();

            for (Map.Entry<Integer, UaInstanceIdentifier> item: handleIdsForObjectAttributes.entrySet())
            {
                UaInstanceIdentifier identifier = item.getValue();
                handleIdsByObjectId.computeIfAbsent(identifier.getObjectId(), k-> new ArrayList<>()).add(item.getKey());
            }

            for (Map.Entry<UaObjectIdentifier, List<Integer>> item: handleIdsByObjectId.entrySet())
            {
                UaReadObjectAttributeTransaction transaction = new UaReadObjectAttributeTransaction(
                        context,
                        item.getValue(),
                        item.getKey(),
                        nodeManager);

                transactions.add(transaction);
            }
        }

        if (!handleIdsForObjectMemberAttributes.isEmpty())
        {
            Map<UaObjectIdentifier, List<Integer>> handleIdsByObjectId = new HashMap<>();

            for (Map.Entry<Integer, UaInstanceIdentifier> item: handleIdsForObjectMemberAttributes.entrySet())
            {
                UaInstanceIdentifier identifier = item.getValue();
                handleIdsByObjectId.computeIfAbsent(identifier.getObjectId(), k-> new ArrayList<>()).add(item.getKey());
            }

            for (Map.Entry<UaObjectIdentifier, List<Integer>> item: handleIdsByObjectId.entrySet())
            {
                Map<UaChildIdentifier, List<Integer>> handleIdsByChildId = new HashMap<>();

                for (Integer handleId: item.getValue())
                {
                    UaChildIdentifier memberId = handleIdsForObjectMemberAttributes.get(handleId).getChildId();
                    if (null == memberId) continue;

                    handleIdsByChildId.computeIfAbsent(memberId, k-> new ArrayList<>()).add(handleId);
                }

                for (Map.Entry<UaChildIdentifier, List<Integer>> child: handleIdsByChildId.entrySet())
                {
                    UaReadMemberAttributeTransaction transaction = new UaReadMemberAttributeTransaction(
                            context,
                            item.getKey(),
                            child.getKey(),
                            child.getValue(),
                            nodeManager);

                    transactions.add(transaction);
                }
            }
        }

        if (!handleIdsForObjectVariableValues.isEmpty())
        {
            Map<UaObjectIdentifier, List<Integer>> handleIdsByObjectId = new HashMap<>();

            for (Map.Entry<Integer, UaInstanceIdentifier> item: handleIdsForObjectVariableValues.entrySet())
            {
                UaInstanceIdentifier identifier = item.getValue();
                handleIdsByObjectId.computeIfAbsent(identifier.getObjectId(), k-> new ArrayList<>()).add(item.getKey());
            }

            for (Map.Entry<UaObjectIdentifier, List<Integer>> item: handleIdsByObjectId.entrySet())
            {
                Map<Integer, UaChildId> handleIdsAndVariableIds = new HashMap<>();

                for (Integer handleId: item.getValue())
                {
                    UaChildIdentifier memberId = handleIdsForObjectVariableValues.get(handleId).getChildId();
                    if (null == memberId) continue;
                    handleIdsAndVariableIds.put(handleId,new UaChildId(memberId.getPath(), memberId.getPathL2()));
                }

                UaReadVariableValueTransaction transaction = new UaReadVariableValueTransaction(
                        context,
                        item.getKey(),
                        handleIdsAndVariableIds,
                        nodeManager);

                transactions.add(transaction);
            }
        }

        return transactions;
    }

    public List<UaWriteTransaction> getWriteTransactions(
            WriteContext context,
            List<Integer> handleIds)
    {
        List<Integer> handleIdsForNodes = new ArrayList<>();
        Map<Integer, UaInstanceIdentifier> handleIdsForObjectAttributes = new TreeMap<>();
        Map<Integer, UaInstanceIdentifier> handleIdsForWriteVariableValues = new TreeMap<>();

        for (Integer handleId: handleIds)
        {
            WriteValue nodeToWrite = context.getNodesToWrite().get(handleId);
            NodeId nodeId = nodeToWrite.getNodeId();

            if (nodeId.getType() == IdType.Opaque)
            {
                UaInstanceIdentifier identifier = UaInstanceIdentifier.fromByteString((ByteString) nodeId.getIdentifier());
                UaReactiveObjectType objectType = null;
                if (null != identifier) objectType = nodeManager.findObjectType(identifier.getObjectId());

                if (null != identifier && null != objectType)
                {
                    if (nodeToWrite.getAttributeId().intValue() == AttributeId.Value.id())
                    {
                        if (null != identifier.getChildId())
                        {
                            handleIdsForWriteVariableValues.put(handleId, identifier);
                            //System.out.println("Write value: " + identifier);
                        } else {
                            handleIdsForNodes.add(handleId);
                            //System.out.println("Write not writable: " + identifier);
                        }
                    } else {
                        if (null == identifier.getChildId() &&
                                null == identifier.getObjectId().getInstanceDeclId())
                        {
                            handleIdsForObjectAttributes.put(handleId, identifier);
                            //System.out.println("Write object attribute: " + identifier);
                        } else {
                            handleIdsForNodes.add(handleId);
                            //System.out.println("Write not writable: " + identifier);
                        }
                    }
                } else {
                    handleIdsForNodes.add(handleId);
                    //System.out.println("Write not writable: " + identifier);
                }
            } else {
                handleIdsForNodes.add(handleId);
                //System.out.println("Write not writable: " + nodeId.toParseableString());
            }
        }

        List<UaWriteTransaction> transactions = new ArrayList<>();

        if (!handleIdsForNodes.isEmpty())
        {
            UaWriteNodeTransaction transaction = new UaWriteNodeTransaction(
                    context,
                    handleIdsForNodes);

            transactions.add(transaction);
        }

        if (!handleIdsForObjectAttributes.isEmpty())
        {
            Map<UaObjectIdentifier, List<Integer>> handleIdsByObjectId = new HashMap<>();

            for (Map.Entry<Integer, UaInstanceIdentifier> item: handleIdsForObjectAttributes.entrySet())
            {
                UaInstanceIdentifier identifier = item.getValue();
                handleIdsByObjectId.computeIfAbsent(identifier.getObjectId(), k-> new ArrayList<>()).add(item.getKey());
            }

            for (Map.Entry<UaObjectIdentifier, List<Integer>> item: handleIdsByObjectId.entrySet())
            {
                UaWriteObjectAttributeTransaction transaction = new UaWriteObjectAttributeTransaction(
                        context,
                        item.getValue(),
                        item.getKey(),
                        nodeManager);

                transactions.add(transaction);
            }
        }

        if (!handleIdsForWriteVariableValues.isEmpty())
        {
            Map<UaObjectIdentifier, List<Integer>> handleIdsByObjectId = new HashMap<>();

            for (Map.Entry<Integer, UaInstanceIdentifier> item: handleIdsForWriteVariableValues.entrySet())
            {
                UaInstanceIdentifier identifier = item.getValue();
                handleIdsByObjectId.computeIfAbsent(identifier.getObjectId(), k-> new ArrayList<>()).add(item.getKey());
            }

            for (Map.Entry<UaObjectIdentifier, List<Integer>> item: handleIdsByObjectId.entrySet())
            {
                Map<Integer, WriteVariableValue> handleIdsAndVariableValues = new HashMap<>();

                for (Integer handleId: item.getValue())
                {
                    UaChildIdentifier memberId = handleIdsForWriteVariableValues.get(handleId).getChildId();
                    DataValue value = context.getNodesToWrite().get(handleId).getValue();
                    if (null == memberId) continue;

                    handleIdsAndVariableValues.put(
                            handleId,
                            new WriteVariableValue(
                                    new UaChildId(memberId.getPath(), memberId.getPathL2()),
                                    value));
                }

                UaWriteVariableValueTransaction transaction = new UaWriteVariableValueTransaction(
                        context,
                        item.getKey(),
                        handleIdsAndVariableValues,
                        nodeManager);

                transactions.add(transaction);
            }
        }

        return transactions;
    }

    public UaMethodCallTransaction getMethodCallTransaction(
            CallContext context,
            int handleId)
    {
        CallMethodRequest callRequest = context.getMethodsToCall().get(handleId);

        UaInstanceIdentifier objectIdentifier = null;
        UaInstanceIdentifier methodIdentifier = null;

        if (callRequest.getObjectId().getType() == IdType.Opaque)
        {
            objectIdentifier = UaInstanceIdentifier.fromByteString((ByteString) callRequest.getObjectId().getIdentifier());
        }

        if (callRequest.getMethodId().getType() == IdType.Opaque)
        {
            methodIdentifier = UaInstanceIdentifier.fromByteString((ByteString) callRequest.getMethodId().getIdentifier());
        }

        if (null == objectIdentifier || null == methodIdentifier)
        {
            return new UaMethodCallTransaction(context, handleId);
        }

        return new UaCallMethodTransaction(context, handleId, objectIdentifier, methodIdentifier, nodeManager);
    }

    public UaHistoryReadTransaction getHistoryReadTransaction(
            HistoryReadContext context,
            int handleId)
    {
        UaHistoryReadTransaction transactionNothingToDo = new UaHistoryReadTransaction(
                context,
                handleId);

        HistoryReadValueId nodeToRead = context.getNodesToRead().get(handleId);
        if (IdType.Opaque != nodeToRead.getNodeId().getType()) return transactionNothingToDo;

        final UaInstanceIdentifier identifier = UaInstanceIdentifier.fromByteString((ByteString) nodeToRead.getNodeId().getIdentifier());
        if (null == identifier)
        {
            transactionNothingToDo.setStatusCode(StatusCode.of(StatusCodes.Bad_NodeIdUnknown));
            return transactionNothingToDo;
        }

        // Object type
        UaReactiveObjectType objectType = nodeManager.findObjectType(identifier.getObjectId());

        if (null == objectType)
        {
            transactionNothingToDo.setStatusCode(StatusCode.of(StatusCodes.Bad_NodeIdUnknown));
            return transactionNothingToDo;
        }

        if (null == identifier.getChildId())
        {
            if (context.getHistoryReadDetails() instanceof ReadEventDetails)
            {
                return new UaReadEventHistoryTransaction(context, handleId, identifier, nodeManager);
            }
        } else {
            if (context.getHistoryReadDetails() instanceof ReadRawModifiedDetails ||
                    context.getHistoryReadDetails() instanceof ReadAtTimeDetails ||
                    context.getHistoryReadDetails() instanceof ReadProcessedDetails)
            {
                return new UaReadDataHistoryTransaction(context,handleId,identifier, nodeManager);
            }
        }

        transactionNothingToDo.setStatusCode(StatusCode.of(StatusCodes.Bad_HistoryOperationUnsupported));
        return transactionNothingToDo;
    }
}
