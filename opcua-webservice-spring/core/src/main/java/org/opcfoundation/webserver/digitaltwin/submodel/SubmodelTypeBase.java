package org.opcfoundation.webserver.digitaltwin.submodel;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.models.UaObjectType;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;

public abstract class SubmodelTypeBase extends UaObjectType {
    public SubmodelTypeBase(
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
}