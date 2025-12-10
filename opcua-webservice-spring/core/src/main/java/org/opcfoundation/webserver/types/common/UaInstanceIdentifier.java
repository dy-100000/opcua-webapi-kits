package org.opcfoundation.webserver.types.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.jspecify.annotations.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UaInstanceIdentifier {
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    @JsonProperty("oi")
    private UaObjectIdentifier objectId;
    @JsonProperty("ci")
    private @Nullable UaChildIdentifier childId;

    public UaInstanceIdentifier()
    {
        objectId = new UaObjectIdentifier();
        childId = null;
    }

    public UaInstanceIdentifier(
            UaObjectIdentifier objectId,
            @Nullable UaChildIdentifier memberId)
    {
        this.objectId = objectId;
        this.childId = memberId;
    }

    public UaObjectIdentifier getObjectId() {
        return objectId;
    }

    public void setObjectId(UaObjectIdentifier objectId) {
        this.objectId = objectId;
    }

    public @Nullable UaChildIdentifier getChildId() {
        return childId;
    }

    public void setChildId(@Nullable UaChildIdentifier childId) {
        this.childId = childId;
    }

    public static @Nullable UaInstanceIdentifier fromByteString(ByteString identifier)
    {
        try {
            return jsonMapper.readValue(identifier.bytes(), UaInstanceIdentifier.class);
        } catch (Exception e) {
            return null;
        }
    }

    public ByteString toByteString()
    {
        try
        {
            return ByteString.of(jsonMapper.writeValueAsBytes(this));
        } catch (Exception e) {
            return ByteString.NULL_VALUE;
        }
    }

    @Override
    public String toString()
    {
        String ret = "ObjectId: ";
        ret += objectId.toString();

        if (null != childId) ret += " ChildId: " + childId;
        return ret;
    }
}
