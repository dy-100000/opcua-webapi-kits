package org.opcfoundation.webserver.types;

import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.UaVariableType;

import java.util.Objects;

public class UaChildId {
    private final String pathId;
    private final @Nullable String subElementId;

    public UaChildId(String pathId)
    {
        this.pathId = pathId;
        subElementId = null;
    }

    public UaChildId(
            String pathId,
            @Nullable String subElementId)
    {
        this.pathId = pathId;
        this.subElementId = (null == subElementId || subElementId.isEmpty()) ? null : subElementId;
    }

    public String getPathId() {
        return pathId;
    }

    public @Nullable String getSubElementId() {
        return subElementId;
    }

    @Override
    public String toString()
    {
        String ret = "PathId: ";
        ret += pathId;

        if (null != subElementId)
        {
            ret += " SubElementId: ";
            ret += subElementId;
        }

        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UaChildId childId = (UaChildId) obj;

        return Objects.equals(pathId,childId.pathId) &&
                Objects.equals(subElementId,childId.subElementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathId, subElementId);
    }
}
