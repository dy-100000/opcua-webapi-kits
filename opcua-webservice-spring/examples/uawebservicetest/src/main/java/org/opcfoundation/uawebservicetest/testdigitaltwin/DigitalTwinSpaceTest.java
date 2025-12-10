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
    public static ElementListTestType elementListTestType;

    public static EnumTestDataType enumTestDataType;

    public DigitalTwinSpaceTest()
    {
        super("opcfoundation/DigitalTwinSpaceTest");
    }

    @Override
    public void onStartUp()
    {
        System.out.println("DigitalTwinSpaceTest.onStartUp");

        enumTestDataType = new EnumTestDataType(this);
        addDefinition(enumTestDataType);

        elementListTestType = new ElementListTestType(this);
        addDefinition(elementListTestType);

        referenceElementTestType = new ReferenceElementTestType(this);
        addDefinition(referenceElementTestType);

        elementCollectionTestAType = new ElementCollectionTestAType(enumTestDataType,this);
        addDefinition(elementCollectionTestAType);

        elementCollectionTestBType = new ElementCollectionTestBType(referenceElementTestType, elementListTestType, this);
        addDefinition(elementCollectionTestBType);

        submodelTestType = new SubmodelTestType(elementCollectionTestAType, elementCollectionTestBType, referenceElementTestType, elementListTestType, this);
        addDefinition(submodelTestType);

        dynamicSubmodelTestType = new DynamicSubmodelTestType(this);
        addDefinition(dynamicSubmodelTestType);

        digitalTwinTestType = new DigitalTwinTestType(submodelTestType, dynamicSubmodelTestType, this);
        addDefinition(digitalTwinTestType);

        digitalTwinDirectoryTestType = new DigitalTwinDirectoryTestType(this);
        addDefinition(digitalTwinDirectoryTestType);
        addEntryPoint(digitalTwinDirectoryTestType, "Entry", new LocalizedText("入口"), new LocalizedText("测试用入口节点"));
    }
}
