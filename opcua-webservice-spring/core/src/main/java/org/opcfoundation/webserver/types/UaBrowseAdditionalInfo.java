package org.opcfoundation.webserver.types;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;

public class UaBrowseAdditionalInfo {
    private final int maxReferencesPerNode;
    private final int referenceOffset;
    private final int taskCheckListMasks;

    public static final int GET_PARENT_TASK = 1;
    public static final int GET_CHILD_TASK = 2;
    public static final int GET_DEFINITION_TASK = 4;
    public static final int GET_LINK_TASK = 8;

    public UaBrowseAdditionalInfo(
            int maxReferencesPerNode,
            int referenceOffset,
            int taskCheckListMasks)
    {
        this.maxReferencesPerNode = Math.max(maxReferencesPerNode, 0);
        this.referenceOffset = Math.max(referenceOffset, 0);
        this.taskCheckListMasks = taskCheckListMasks;
    }

    public int getMaxReferencesPerNode() {
        return maxReferencesPerNode;
    }

    public int getReferenceOffset() {
        return referenceOffset;
    }

    public int getTaskCheckListMasks() {
        return taskCheckListMasks;
    }

    public UaBrowseAdditionalInfo updateOffset(int offsetToAdd)
    {
        return new UaBrowseAdditionalInfo(maxReferencesPerNode, referenceOffset + offsetToAdd, taskCheckListMasks);
    }

    public UaBrowseAdditionalInfo updateTasks(BrowseDescription description)
    {
        if (taskCheckListMasks != 0) return this;

        int browseInfoMask = 0;

        if (description.getIncludeSubtypes())
        {
            if ((description.getReferenceTypeId().equals(NodeIds.References) ||
                    description.getReferenceTypeId().equals(NodeIds.HierarchicalReferences) ||
                    description.getReferenceTypeId().equals(NodeIds.Aggregates)))
            {
                if (description.getBrowseDirection() == BrowseDirection.Inverse ||
                        description.getBrowseDirection() == BrowseDirection.Both)
                {
                    if ((description.getNodeClassMask().longValue() & NodeClass.Object.getValue()) != 0) {
                        browseInfoMask = browseInfoMask | GET_PARENT_TASK; // Get parent
                    }
                }

                if (description.getBrowseDirection() == BrowseDirection.Forward ||
                        description.getBrowseDirection() == BrowseDirection.Both) {
                    browseInfoMask = browseInfoMask | GET_CHILD_TASK; // Get Child
                }
            }

            if (description.getReferenceTypeId().equals(NodeIds.References) ||
                    description.getReferenceTypeId().equals(NodeIds.NonHierarchicalReferences))
            {
                if ((description.getNodeClassMask().longValue() & NodeClass.ObjectType.getValue()) != 0)
                {
                    browseInfoMask = browseInfoMask | GET_DEFINITION_TASK; // Get type definition
                }

                if ((description.getNodeClassMask().longValue() & NodeClass.Object.getValue()) != 0)
                {
                    browseInfoMask = browseInfoMask | GET_LINK_TASK; // Get links
                }
            }
        } else {
            if (description.getBrowseDirection() == BrowseDirection.Forward ||
                    description.getBrowseDirection() == BrowseDirection.Both)
            {
                if (description.getReferenceTypeId().equals(NodeIds.Organizes) ||
                            description.getReferenceTypeId().equals(NodeIds.HasComponent) ||
                            description.getReferenceTypeId().equals(NodeIds.HasProperty))
                {
                    browseInfoMask = browseInfoMask | GET_CHILD_TASK; // Get Child
                }

                if ((description.getNodeClassMask().longValue() & NodeClass.ObjectType.getValue()) != 0 &&
                        description.getReferenceTypeId().equals(NodeIds.HasTypeDefinition))
                {
                    browseInfoMask = browseInfoMask | GET_DEFINITION_TASK; // Get type definition
                }
            }
        }

        return new UaBrowseAdditionalInfo(maxReferencesPerNode, referenceOffset, browseInfoMask);
    }

    public boolean isTaskRequired(int taskMask)
    {
        return (taskCheckListMasks & taskMask) != 0;
    }

    public UaBrowseAdditionalInfo taskComplete(int taskMask)
    {
        return new UaBrowseAdditionalInfo(
                maxReferencesPerNode,
                referenceOffset,
                isTaskRequired(taskMask) ? taskCheckListMasks^taskMask : taskCheckListMasks);
    }

    public boolean isAllTaskComplete()
    {
        return (0 == taskCheckListMasks);
    }

    @Override
    public String toString()
    {
        String ret = "maxReferencesPerNode: " + maxReferencesPerNode;
        ret += " referenceOffset: " + referenceOffset;
        ret += " taskCheckListMasks: " + taskCheckListMasks;
        return ret;
    }
}
