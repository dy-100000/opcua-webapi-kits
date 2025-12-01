package org.opcfoundation.uawebservicetest.testdigitaltwin;

import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;

public class DigitalTwinSpaceTest extends DigitalTwinSpace {
    public static DigitalTwinDirectoryTestType digitalTwinDirectoryTestType;
    public static DigitalTwinTestType digitalTwinTestType;

    public static SubmodelTestType submodelTestType;
    public static ElementListSubmodelTestType elementListSubmodelTestType;

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
        addEnumerationType(enumTestDataType);

        elementListTestType = new ElementListTestType(this);
        addElementType(elementListTestType);

        referenceElementTestType = new ReferenceElementTestType(this);
        addElementType(referenceElementTestType);

        elementCollectionTestAType = new ElementCollectionTestAType(enumTestDataType,this);
        addElementType(elementCollectionTestAType);

        elementCollectionTestBType = new ElementCollectionTestBType(referenceElementTestType, elementListTestType, this);
        addElementType(elementCollectionTestBType);

        submodelTestType = new SubmodelTestType(elementCollectionTestAType, elementCollectionTestBType, referenceElementTestType, elementListTestType, this);
        addSubmodelType(submodelTestType);

        elementListSubmodelTestType = new ElementListSubmodelTestType(this);
        addSubmodelType(elementListSubmodelTestType);

        digitalTwinTestType = new DigitalTwinTestType(submodelTestType, elementListSubmodelTestType, this);
        addDigitalTwinType(digitalTwinTestType);

        digitalTwinDirectoryTestType = new DigitalTwinDirectoryTestType(this);
        addDigitalTwinDirectoryType(digitalTwinDirectoryTestType, "Entry");
    }
}
