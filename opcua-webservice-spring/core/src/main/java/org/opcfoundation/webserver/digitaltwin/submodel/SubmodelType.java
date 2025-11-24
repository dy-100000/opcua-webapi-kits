package org.opcfoundation.webserver.digitaltwin.submodel;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.models.UaObjectType;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.types.message.*;

import java.util.concurrent.CompletableFuture;

public abstract class SubmodelType extends UaObjectType {
    public SubmodelType(
            String typeId,
            LocalizedText displayName,
            DigitalTwinSpace namespace)
    {
        super(
                typeId,
                displayName,
                UaObjectTypes.SubmodelType,
                namespace);
    }

    @Override
    public final CompletableFuture<ReadObjectAttributeResponse> onReadObjectAttributes(ReadObjectAttributeRequest request) {
        UaObject instanceDeclaration = request.getObjectId().getInstance();

        if (null == instanceDeclaration) {
            return CompletableFuture.completedFuture(new ReadObjectAttributeResponse(
                    new LocalizedText(request.getObjectId().getId()),
                    LocalizedText.NULL_VALUE));
        } else {
            return CompletableFuture.completedFuture(new ReadObjectAttributeResponse(
                        instanceDeclaration.displayName(),
                        instanceDeclaration.description()));
        }
    }
}
