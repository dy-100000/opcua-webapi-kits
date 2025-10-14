package org.opcfoundation.webserver.addressspace.nodemanager;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class NodeManagerList {
    private final List<NodeManagerBase> nodeManagers;
    private static int globalNsIndex = 0;

    public final static NodeManagerList nodeManagerList = new NodeManagerList();

    public static int getGlobalNsIndex()
    {
        int ret = globalNsIndex;
        globalNsIndex++;
        return ret;
    }

    public NodeManagerList()
    {
        nodeManagers = new ArrayList<>();
    }

    public List<Integer> getNsIndexes()
    {
        List<Integer> ret = new ArrayList<>();
        for (NodeManagerBase item: nodeManagers)
        {
            ret.add(item.nsIndex());
        }
        return ret;
    }

    public @Nullable NodeManagerBase getNodeManager(int nsIndex)
    {
        if (nodeManagers.size() <= nsIndex) return null;
        return nodeManagers.get(nsIndex);
    }

    public void addNodeManager(NodeManagerBase nodeManager) throws UaRuntimeException
    {
        if (nodeManager.nsIndex() != nodeManagers.size()) throw new UaRuntimeException(StatusCodes.Bad_InternalError);
        nodeManagers.add(nodeManager.nsIndex(),nodeManager);
    }
}
