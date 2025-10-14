package org.opcfoundation.webserver.types;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;

public class UaBrowseAdditionalInfo {
    private final int maxReferencesPerNode;
    private final int referenceOffset;
    private final int taskCheckListMasks;

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
                        browseInfoMask = browseInfoMask | 1; // Get parent
                    }
                }

                if (description.getBrowseDirection() == BrowseDirection.Forward ||
                        description.getBrowseDirection() == BrowseDirection.Both) {
                    browseInfoMask = browseInfoMask | 2; // Get Child
                }
            }

            if (description.getReferenceTypeId().equals(NodeIds.References) ||
                    description.getReferenceTypeId().equals(NodeIds.NonHierarchicalReferences))
            {
                if ((description.getNodeClassMask().longValue() & NodeClass.ObjectType.getValue()) != 0)
                {
                    browseInfoMask = browseInfoMask | 16; // Get type definition
                }

                if ((description.getNodeClassMask().longValue() & NodeClass.Object.getValue()) != 0)
                {
                    if (description.getBrowseDirection() == BrowseDirection.Forward ||
                            description.getBrowseDirection() == BrowseDirection.Both)
                    {
                        browseInfoMask = browseInfoMask | 32; // Get forward links
                    }

                    if (description.getBrowseDirection() == BrowseDirection.Inverse)
                    {
                        browseInfoMask = browseInfoMask | 64; // Get forward links
                    }
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
                    browseInfoMask = browseInfoMask | 2; // Get Child
                }

                if ((description.getNodeClassMask().longValue() & NodeClass.ObjectType.getValue()) != 0 &&
                        description.getReferenceTypeId().equals(NodeIds.HasTypeDefinition))
                {
                    browseInfoMask = browseInfoMask | 16; // Get type definition
                }
            }
        }

        return new UaBrowseAdditionalInfo(maxReferencesPerNode, referenceOffset, browseInfoMask);
    }

    public boolean isBrowseParentRequired()
    {
        return (taskCheckListMasks & 1) != 0;
    }

    public UaBrowseAdditionalInfo browseParentComplete()
    {
        return new UaBrowseAdditionalInfo(
                maxReferencesPerNode,
                referenceOffset,
                isBrowseParentRequired() ? taskCheckListMasks^1 : taskCheckListMasks);
    }

    public boolean isBrowseChildRequired()
    {
        return (taskCheckListMasks & 2) != 0;
    }

    public UaBrowseAdditionalInfo browseChildComplete()
    {
        return new UaBrowseAdditionalInfo(
                maxReferencesPerNode,
                referenceOffset,
                isBrowseChildRequired() ? taskCheckListMasks^2 : taskCheckListMasks);
    }

    public boolean isBrowseTypeDefinitionRequired()
    {
        return (taskCheckListMasks & 16) != 0;
    }

    public UaBrowseAdditionalInfo browseTypeDefinitionComplete()
    {
        return new UaBrowseAdditionalInfo(
                maxReferencesPerNode,
                referenceOffset,
                isBrowseTypeDefinitionRequired() ? taskCheckListMasks^16 : taskCheckListMasks);
    }

    public boolean isBrowseLinkRequired()
    {
        return (taskCheckListMasks & 32) != 0;
    }

    public UaBrowseAdditionalInfo browseLinkComplete()
    {
        return new UaBrowseAdditionalInfo(
                maxReferencesPerNode,
                referenceOffset,
                isBrowseLinkRequired() ? taskCheckListMasks^32 : taskCheckListMasks);
    }

    public boolean isBrowseInverseLinkRequired()
    {
        return (taskCheckListMasks & 64) != 0;
    }

    public UaBrowseAdditionalInfo browseInverseLinkComplete()
    {
        return new UaBrowseAdditionalInfo(
                maxReferencesPerNode,
                referenceOffset,
                isBrowseInverseLinkRequired() ? taskCheckListMasks^64 : taskCheckListMasks);
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
