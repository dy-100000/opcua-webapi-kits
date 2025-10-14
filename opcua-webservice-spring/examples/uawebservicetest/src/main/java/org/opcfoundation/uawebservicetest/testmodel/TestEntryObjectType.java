package org.opcfoundation.uawebservicetest.testmodel;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.models.UaMasterObjectType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;

public class TestEntryObjectType extends UaMasterObjectType {
    public TestEntryObjectType(
            TestObjectDirectoryType testObjectDirectoryType,
            TestVariableDirectoryType testVariableDirectoryType,
            TestDataObjectType testDataObjectType,
            NodeManager nodeManager)
    {
        super("TestEntryObjectType", new LocalizedText("TestEntryObjectType"), null,nodeManager);

        addSubmodel(
                "TestObjectFolder",
                true,
                new LocalizedText("TestObjectFolder"),
                new LocalizedText("Folder contains data objects"),
                testObjectDirectoryType);

        addSubmodel(
                "TestVariableFolder",
                true,
                new LocalizedText("TestVariableFolder"),
                new LocalizedText("Folder contains variables"),
                testVariableDirectoryType);

        addSubmodel(
                "TestDataObject",
                true,
                new LocalizedText("TestDataObject"),
                new LocalizedText("Data object"),
                testDataObjectType);
    }
}
