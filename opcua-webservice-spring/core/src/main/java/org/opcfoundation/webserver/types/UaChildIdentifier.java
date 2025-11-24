package org.opcfoundation.webserver.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UaChildIdentifier {
    @JsonProperty("p")
    private String path;
    @JsonProperty("p2")
    private @Nullable String pathL2;
    @JsonProperty("mn")
    private @Nullable Boolean methodNode;

    public UaChildIdentifier()
    {
        path = "";
        pathL2 = null;
        methodNode = null;
    }

    public UaChildIdentifier(
            String path,
            @Nullable String pathL2,
            boolean methodNode)
    {
        this.path = path;
        this.pathL2 = pathL2;
        this.methodNode = (methodNode) ? true : null;
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

    public @Nullable Boolean getMethodNode() {
        return methodNode;
    }

    public void setMethodNode(@Nullable Boolean methodNode) {
        this.methodNode = methodNode;
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

        if (null != methodNode)
        {
            ret += " MethodNode: ";
            ret += methodNode;
        }

        ret += "}";

        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UaChildIdentifier childId = (UaChildIdentifier) obj;

        return Objects.equals(path, childId.path) &&
                Objects.equals(pathL2, childId.pathL2) &&
                    Objects.equals(methodNode, childId.methodNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, pathL2, methodNode);
    }
}
