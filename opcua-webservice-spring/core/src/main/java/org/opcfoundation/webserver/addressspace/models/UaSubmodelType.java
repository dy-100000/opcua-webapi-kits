package org.opcfoundation.webserver.addressspace.models;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.addressspace.nodes.BaseUaObjectType;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.types.message.ReadObjectAttributeRequest;
import org.opcfoundation.webserver.types.message.ReadObjectAttributeResponse;
import java.util.concurrent.CompletableFuture;

public class UaSubmodelType extends UaObjectType {
    public UaSubmodelType(
            String objectTypeId,
            LocalizedText displayName,
            BaseUaObjectType parentType,
            NodeManager nodeManager) {
        super(
                objectTypeId,
                displayName,
                parentType,
                nodeManager);
    }

    // Can be overridden
    public CompletableFuture<ReadObjectAttributeResponse> getObjectAttribute(ReadObjectAttributeRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            return new ReadObjectAttributeResponse(
                    new LocalizedText(request.getObjectId().getId()),
                    LocalizedText.NULL_VALUE);
        });
    }

    @Override
    public final CompletableFuture<ReadObjectAttributeResponse> onReadObjectAttributes(ReadObjectAttributeRequest request) {
        UaObject instanceDeclaration = request.getObjectId().getInstanceDeclaration();

        if (null == instanceDeclaration) {
            return getObjectAttribute(request);
        } else {
            return CompletableFuture.supplyAsync(() -> {
                return new ReadObjectAttributeResponse(
                        instanceDeclaration.displayName(),
                        instanceDeclaration.description());
            });
        }
    }
}


