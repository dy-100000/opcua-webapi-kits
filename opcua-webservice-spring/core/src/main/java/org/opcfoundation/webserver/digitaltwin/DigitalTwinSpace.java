package org.opcfoundation.webserver.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webserver.addressspace.nodes.UaEnumDataType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerReactiveObject;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjects;
import org.opcfoundation.webserver.digitaltwin.digitaltwin.DigitalTwinRepositoryType;
import org.opcfoundation.webserver.digitaltwin.digitaltwin.DigitalTwinType;
import org.opcfoundation.webserver.digitaltwin.element.ElementType;
import org.opcfoundation.webserver.digitaltwin.submodel.SubmodelTypeBase;
import org.opcfoundation.webserver.types.common.UaInstanceIdentifier;
import org.opcfoundation.webserver.types.common.UaObjectIdentifier;

public class DigitalTwinSpace extends NodeManagerReactiveObject {
    public DigitalTwinSpace(String namespaceUri)
    {
        super(namespaceUri);
    }

    public final void addDefinition(DigitalTwinType type)
    {
        addObjectType(type);
    }

    public final void addDefinition(DigitalTwinRepositoryType type)
    {
        addObjectType(type);
    }

    public final void addDefinition(SubmodelTypeBase type)
    {
        addObjectType(type);
    }

    public final void addDefinition(ElementType type)
    {
        addObjectType(type);
    }

    public final void addDefinition(UaEnumDataType type)
    {
        addDataType(type);
    }

    public final void addRepository(
            DigitalTwinRepositoryType repositoryType,
            String id,
            LocalizedText displayName,
            LocalizedText description)
    {
        UaInstanceIdentifier objectIdentifier = new UaInstanceIdentifier(
                new UaObjectIdentifier(repositoryType.nodeId().toParseableString(), id, null),
                null);

        NodeId objectNodeId = new NodeId(nsIndex(), objectIdentifier.toByteString());

        UaObject newObject = new UaObject(
                objectNodeId,
                id,
                displayName,
                repositoryType);

        newObject.setDescription(description);

        this.addNode(newObject);

        UaObjects.ObjectsFolder.organizes(newObject);
    }
}
