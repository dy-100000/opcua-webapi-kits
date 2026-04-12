package org.opcfoundation.webserver.types.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UaObjectIdentifier {
    @JsonProperty("i")
    private String id;
    @JsonProperty("t")
    private @Nullable String typeId;
    @JsonProperty("id")
    private @Nullable String instanceDeclId;

    public UaObjectIdentifier()
    {
        id = "";
        typeId = null;
        instanceDeclId = null;
    }

    public UaObjectIdentifier(
            String typeId,
            String id,
            @Nullable String instanceDeclId)
    {
        if (null == instanceDeclId) this.typeId = typeId;
        this.id = id;
        this.instanceDeclId = instanceDeclId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @Nullable String getTypeId() {
        return typeId;
    }

    public void setTypeId(@Nullable String typeId) {
        this.typeId = typeId;
    }

    public @Nullable String getInstanceDeclId() {
        return instanceDeclId;
    }

    public void setInstanceDeclId(@Nullable String instanceDeclId) {
        this.instanceDeclId = instanceDeclId;
    }

    @Override
    public String toString() {
        String ret = "{";
        ret += "TypeId: ";
        ret += typeId;
        ret += " Id: ";
        ret += id;

        if (null != instanceDeclId)
        {
            ret += " InstanceDecl: ";
            ret += instanceDeclId;
        }

        ret += "}";

        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UaObjectIdentifier objectId = (UaObjectIdentifier) obj;

        return Objects.equals(typeId, objectId.typeId) &&
                Objects.equals(id, objectId.id) &&
                Objects.equals(instanceDeclId, objectId.instanceDeclId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeId, id, instanceDeclId);
    }
}
