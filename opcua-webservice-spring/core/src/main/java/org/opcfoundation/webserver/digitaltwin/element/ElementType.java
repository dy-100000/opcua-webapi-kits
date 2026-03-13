package org.opcfoundation.webserver.digitaltwin.element;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.reactiveobject.UaReactiveObjectType;
import org.opcfoundation.webserver.addressspace.nodes.UaObjectType;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.springframework.lang.Nullable;

public abstract class ElementType extends UaReactiveObjectType {
    public ElementType(
            String typeId,
            LocalizedText displayName,
            @Nullable UaObjectType elementType,
            DigitalTwinSpace twinSpace)
    {
        super(
                typeId,
                displayName,
                elementType,
                twinSpace);
    }
}
