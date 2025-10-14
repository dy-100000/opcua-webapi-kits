package org.opcfoundation.webserver.types;

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
    @JsonProperty("mi")
    private @Nullable UaMemberIdentifier memberId;

    public UaInstanceIdentifier()
    {
        objectId = new UaObjectIdentifier();
        memberId = null;
    }

    public UaInstanceIdentifier(
            UaObjectIdentifier objectId,
            @Nullable UaMemberIdentifier memberId)
    {
        this.objectId = objectId;
        this.memberId = memberId;
    }

    public UaObjectIdentifier getObjectId() {
        return objectId;
    }

    public void setObjectId(UaObjectIdentifier objectId) {
        this.objectId = objectId;
    }

    public @Nullable UaMemberIdentifier getMemberId() {
        return memberId;
    }

    public void setMemberId(@Nullable UaMemberIdentifier memberId) {
        this.memberId = memberId;
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

        if (null != memberId) ret += " MemberId: " + memberId;
        return ret;
    }
}
