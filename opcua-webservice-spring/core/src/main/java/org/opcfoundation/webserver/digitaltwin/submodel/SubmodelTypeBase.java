package org.opcfoundation.webserver.digitaltwin.submodel;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.reactiveobject.UaReactiveObjectType;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;

public abstract class SubmodelTypeBase extends UaReactiveObjectType {
    public SubmodelTypeBase(
            String typeId,
            LocalizedText displayName,
            DigitalTwinSpace twinSpace)
    {
        super(
                typeId,
                displayName,
                UaObjectTypes.SubmodelType,
                twinSpace);
    }
}