package org.opcfoundation.webapi.mapper;

import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.ServerTable;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingManager;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingManager;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;

public class UaEncoder implements EncodingContext {
    private final EncodingLimits encodingLimits;
    private final NamespaceTable namespaceTable;
    private final ServerTable serverTable;
    private final EncodingManager encodingManager;
    private final DataTypeManager dataTypeManager;

    public static final UaEncoder defaultEncoder = new UaEncoder();

    public UaEncoder()
    {
        encodingLimits = new EncodingLimits();
        namespaceTable = new NamespaceTable();
        serverTable = new ServerTable();
        encodingManager = DefaultEncodingManager.createAndInitialize();
        dataTypeManager = DefaultDataTypeManager.createAndInitialize(namespaceTable);
    }

    public DataTypeManager getDataTypeManager() {
        return dataTypeManager;
    }

    public EncodingManager getEncodingManager() {
        return encodingManager;
    }

    public EncodingLimits getEncodingLimits() {
        return encodingLimits;
    }

    public NamespaceTable getNamespaceTable() {
        return namespaceTable;
    }

    public ServerTable getServerTable() {
        return serverTable;
    }
}
