package org.opcfoundation.webserver.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class UaMemberIdentifier {
    @JsonProperty("p")
    private String path;
    @JsonProperty("p2")
    private @Nullable String pathL2;
    @JsonProperty("vt")
    private @Nullable String variableTypeId;

    public UaMemberIdentifier()
    {
        path = "";
        pathL2 = null;
        variableTypeId = null;
    }

    public UaMemberIdentifier(
            String path,
            @Nullable String pathL2,
            @Nullable String variableTypeId)
    {
        this.path = path;
        this.pathL2 = pathL2;
        this.variableTypeId = variableTypeId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public @Nullable String getPathL2() {
        return pathL2;
    }

    public void setPathL2(@Nullable String pathL2) {
        this.pathL2 = pathL2;
    }

    public @Nullable String getVariableTypeId() {
        return variableTypeId;
    }

    public void setVariableTypeId(@Nullable String variableTypeId) {
        this.variableTypeId = variableTypeId;
    }

    @Override
    public String toString() {
        String ret = "{";
        ret += "Path: ";
        ret += path;

        if (null != pathL2)
        {
            ret += " PathL2: ";
            ret += pathL2;
        }

        if (null != variableTypeId)
        {
            ret += " VariableTypeId: ";
            ret += variableTypeId;
        }

        ret += "}";

        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UaMemberIdentifier memberId = (UaMemberIdentifier) obj;

        return Objects.equals(path, memberId.path) &&
                Objects.equals(pathL2, memberId.pathL2) &&
                Objects.equals(variableTypeId, memberId.variableTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, pathL2, variableTypeId);
    }
}
