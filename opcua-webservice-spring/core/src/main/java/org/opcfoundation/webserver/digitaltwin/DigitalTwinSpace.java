package org.opcfoundation.webserver.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webserver.addressspace.models.UaEnumDataType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerWebService;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjects;
import org.opcfoundation.webserver.digitaltwin.digitaltwin.DigitalTwinDirectoryType;
import org.opcfoundation.webserver.digitaltwin.digitaltwin.DigitalTwinType;
import org.opcfoundation.webserver.digitaltwin.element.ElementType;
import org.opcfoundation.webserver.digitaltwin.submodel.SubmodelTypeBase;
import org.opcfoundation.webserver.types.UaInstanceIdentifier;
import org.opcfoundation.webserver.types.UaObjectIdentifier;

import java.util.HashSet;
import java.util.Set;

public class DigitalTwinSpace extends NodeManagerWebService {
    public DigitalTwinSpace(String namespaceUri)
    {
        super(namespaceUri);
    }

    public final void addDigitalTwinType(DigitalTwinType type)
    {
        addObjectType(type);
    }

    public final void addSubmodelType(SubmodelTypeBase type)
    {
        addObjectType(type);
    }

    public final void addElementType(ElementType type)
    {
        addObjectType(type);
    }

    public final void addEnumerationType(UaEnumDataType dataType)
    {
        addDataType(dataType);
    }

    public final void addDigitalTwinDirectoryType(
            DigitalTwinDirectoryType type,
            String entryId)
    {
        HashSet<String> entryIds = new HashSet<>();
        entryIds.add(entryId);
        addDigitalTwinDirectoryType(type, entryIds);
    }

    public final void addDigitalTwinDirectoryType(
            DigitalTwinDirectoryType type,
            Set<String> entryIds)
    {
        addObjectType(type);

        for (String item: entryIds)
        {
            UaInstanceIdentifier objectIdentifier = new UaInstanceIdentifier(
                    new UaObjectIdentifier(type.nodeId().toParseableString(), item, null),
                    null);

            NodeId objectNodeId = new NodeId(nsIndex(), objectIdentifier.toByteString());

            UaObject newObject = new UaObject(
                    objectNodeId,
                    item,
                    new LocalizedText(item),
                    type);

            this.addNode(newObject);

            UaObjects.ObjectsFolder.organizes(newObject);
        }

    }
}
