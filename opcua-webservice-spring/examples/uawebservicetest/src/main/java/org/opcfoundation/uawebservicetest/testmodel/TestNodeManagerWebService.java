package org.opcfoundation.uawebservicetest.testmodel;

import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerWebService;

public class TestNodeManagerWebService extends NodeManagerWebService {
    public static TestDataObjectType testDataObjectType;
    public static TestDataObject2Type testDataObject2Type;
    public static TestObjectDirectoryType testObjectDirectoryType;
    public static TestEntryObjectType testEntryObjectType;

    public TestNodeManagerWebService()
    {
        super("TestNodeManager");
    }

    @Override
    public void onStartUp()
    {
        System.out.println("TestNodeManager.onStartUp");

        testDataObject2Type = new TestDataObject2Type(this);
        testDataObjectType = new TestDataObjectType(this, testDataObject2Type);
        testObjectDirectoryType = new TestObjectDirectoryType(testDataObjectType, this);
        testEntryObjectType = new TestEntryObjectType(testObjectDirectoryType, testDataObjectType, this);

        addObjectType(testDataObject2Type);
        addObjectType(testDataObjectType);
        addObjectType(testObjectDirectoryType);
        addObjectType(testEntryObjectType);

        addRootObject("Entry", testEntryObjectType);
    }
}
