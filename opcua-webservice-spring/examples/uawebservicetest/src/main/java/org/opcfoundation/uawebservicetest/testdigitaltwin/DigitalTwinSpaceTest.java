package org.opcfoundation.uawebservicetest.testdigitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;

public class DigitalTwinSpaceTest extends DigitalTwinSpace {
    public static DigitalTwinDirectoryTestType digitalTwinDirectoryTestType;
    public static DigitalTwinTestType digitalTwinTestType;

    public static SubmodelTestType submodelTestType;
    public static DynamicSubmodelTestType dynamicSubmodelTestType;

    public static ElementCollectionTestAType elementCollectionTestAType;
    public static ElementCollectionTestBType elementCollectionTestBType;
    public static ReferenceElementTestType referenceElementTestType;
    public static EventElementTestType eventElementTestType;
    public static ElementListTestType elementListTestType;

    public static EnumTestDataType enumTestDataType;
    public static EventTestType eventTestType;

    public DigitalTwinSpaceTest()
    {
        super("opcfoundation/DigitalTwinSpaceTest");
    }

    @Override
    public void onStartUp()
    {
        System.out.println("DigitalTwinSpaceTest.onStartUp");

        enumTestDataType = new EnumTestDataType(this);
        eventTestType = new EventTestType(this);

        elementListTestType = new ElementListTestType(this);
        referenceElementTestType = new ReferenceElementTestType(this);

        eventElementTestType = new EventElementTestType(eventTestType,this);
        elementCollectionTestAType = new ElementCollectionTestAType(enumTestDataType,this);
        elementCollectionTestBType = new ElementCollectionTestBType(referenceElementTestType, elementListTestType, this);
        submodelTestType = new SubmodelTestType(elementCollectionTestAType, elementCollectionTestBType, referenceElementTestType, elementListTestType, eventElementTestType,this);
        dynamicSubmodelTestType = new DynamicSubmodelTestType(this);
        digitalTwinTestType = new DigitalTwinTestType(submodelTestType, dynamicSubmodelTestType, this);
        digitalTwinDirectoryTestType = new DigitalTwinDirectoryTestType(this);

        addRepository(digitalTwinDirectoryTestType, "Entry", new LocalizedText("入口"), new LocalizedText("测试用入口节点"));
    }
}
