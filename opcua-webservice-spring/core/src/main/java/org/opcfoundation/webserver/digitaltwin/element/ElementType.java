package org.opcfoundation.webserver.digitaltwin.element;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.models.UaObjectType;
import org.opcfoundation.webserver.addressspace.nodes.BaseUaObjectType;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.springframework.lang.Nullable;

public abstract class ElementType extends UaObjectType {
    public ElementType(
            String typeId,
            LocalizedText displayName,
            @Nullable BaseUaObjectType elementType,
            DigitalTwinSpace namespace)
    {
        super(
                typeId,
                displayName,
                elementType,
                namespace);
    }
}
