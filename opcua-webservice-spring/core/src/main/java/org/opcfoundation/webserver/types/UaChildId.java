package org.opcfoundation.webserver.types;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class UaChildId {
    private final String id;
    private final @Nullable String subElementName;

    public UaChildId(String id)
    {
        this.id = id;
        subElementName = null;
    }

    public UaChildId(
            String id,
            @Nullable String subElementName)
    {
        this.id = id;
        this.subElementName = (null == subElementName || subElementName.isEmpty()) ? null : subElementName;
    }

    public String getId() {
        return id;
    }

    public @Nullable String getSubElementName() {
        return subElementName;
    }

    @Override
    public String toString()
    {
        String ret = "Id: ";
        ret += id;

        if (null != subElementName)
        {
            ret += " SubElementName: ";
            ret += subElementName;
        }

        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UaChildId childId = (UaChildId) obj;

        return Objects.equals(id,childId.id) &&
                Objects.equals(subElementName,childId.subElementName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subElementName);
    }

    @Deprecated
    public String getPathId() {
        return id;
    }

    @Deprecated
    public @Nullable String getSubElementId() {
        return subElementName;
    }
}
