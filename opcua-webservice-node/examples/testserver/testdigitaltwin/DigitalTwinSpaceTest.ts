import { UaLocalizedText } from "opcua-webapi-ts";
import { DigitalTwinSpace } from "../../../src/server/digitaltwin";
import { DigitalTwinDirectoryTestType } from "./DigitalTwinDirectoryTestType";
import { DigitalTwinTestType } from "./DigitalTwinTestType";
import { DynamicSubmodelTestType } from "./DynamicSubmodelTestType";
import { ElementCollectionTestAType } from "./ElementCollectionTestAType";
import { ElementCollectionTestBType } from "./ElementCollectionTestBType";
import { ElementListTestType } from "./ElementListTestType";
import { EnumTestDataType } from "./EnumTestDataType";
import { EventElementTestType } from "./EventElementTestType";
import { EventTestType } from "./EventTestType";
import { ReferenceElementTestType } from "./ReferenceElementTestType";
import { SubmodelTestType } from "./SubmodelTestType";

export class DigitalTwinSpaceTest extends DigitalTwinSpace {
    static digitalTwinDirectoryTestType: DigitalTwinDirectoryTestType;
    static digitalTwinTestType: DigitalTwinTestType;
    static submodelTestType: SubmodelTestType;
    static dynamicSubmodelTestType: DynamicSubmodelTestType;
    static elementCollectionTestAType: ElementCollectionTestAType;
    static elementCollectionTestBType: ElementCollectionTestBType;
    static referenceElementTestType: ReferenceElementTestType;
    static eventElementTestType: EventElementTestType;
    static elementListTestType: ElementListTestType;
    static enumTestDataType: EnumTestDataType;
    static eventTestType: EventTestType;

    constructor() {
        super("opcfoundation/DigitalTwinSpaceTest");
    }

    override async onStartUp(): Promise<void> {
        console.log("DigitalTwinSpaceTest.onStartUp");

        DigitalTwinSpaceTest.enumTestDataType = new EnumTestDataType(this);
        DigitalTwinSpaceTest.eventTestType = new EventTestType(this);

        DigitalTwinSpaceTest.elementListTestType = new ElementListTestType(this);
        DigitalTwinSpaceTest.referenceElementTestType = new ReferenceElementTestType(this);

        DigitalTwinSpaceTest.eventElementTestType = new EventElementTestType(DigitalTwinSpaceTest.eventTestType, this);
        DigitalTwinSpaceTest.elementCollectionTestAType = new ElementCollectionTestAType(DigitalTwinSpaceTest.enumTestDataType, this);
        DigitalTwinSpaceTest.elementCollectionTestBType = new ElementCollectionTestBType(
            DigitalTwinSpaceTest.referenceElementTestType,
            DigitalTwinSpaceTest.elementListTestType,
            this,
        );
        DigitalTwinSpaceTest.submodelTestType = new SubmodelTestType(
            DigitalTwinSpaceTest.elementCollectionTestAType,
            DigitalTwinSpaceTest.elementCollectionTestBType,
            DigitalTwinSpaceTest.referenceElementTestType,
            DigitalTwinSpaceTest.elementListTestType,
            DigitalTwinSpaceTest.eventElementTestType,
            this,
        );
        DigitalTwinSpaceTest.dynamicSubmodelTestType = new DynamicSubmodelTestType(this);
        DigitalTwinSpaceTest.digitalTwinTestType = new DigitalTwinTestType(
            DigitalTwinSpaceTest.submodelTestType,
            DigitalTwinSpaceTest.dynamicSubmodelTestType,
            this,
        );
        DigitalTwinSpaceTest.digitalTwinDirectoryTestType = new DigitalTwinDirectoryTestType(this);

        this.addRepository(
            DigitalTwinSpaceTest.digitalTwinDirectoryTestType,
            "Entry",
            new UaLocalizedText("入口"),
            new UaLocalizedText("测试用入口节点"),
        );
    }
}