package org.opcfoundation.webserver.types.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.springframework.lang.Nullable;

public class UaHistoryReadContinuationPoint {
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private Integer offset;

    public UaHistoryReadContinuationPoint()
    {
        offset = 0;
    }

    public ByteString toByteString()
    {
        try
        {
            if (0 >= offset) return ByteString.NULL_VALUE;
            return ByteString.of(jsonMapper.writeValueAsBytes(this));
        } catch (Exception e) {
            return ByteString.NULL_VALUE;
        }
    }

    public static @Nullable UaHistoryReadContinuationPoint fromByteString(ByteString continuationPoint)
    {
        try {
            if (continuationPoint.isNull()) return null;
            return jsonMapper.readValue(continuationPoint.bytes(), UaHistoryReadContinuationPoint.class);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
