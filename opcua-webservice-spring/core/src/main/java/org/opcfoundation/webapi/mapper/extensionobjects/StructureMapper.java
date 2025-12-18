package org.opcfoundation.webapi.mapper.extensionobjects;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public interface StructureMapper {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    NodeId dataTypeId();

    UaStructuredType toStructure(byte[] json) throws Exception;

    byte[] toJson(UaStructuredType struct) throws Exception;
}
