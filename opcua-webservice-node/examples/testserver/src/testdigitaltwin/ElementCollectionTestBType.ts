import { UaLocalizedText } from "opcua-webapi-ts";
import { UaObject } from "opcua-webservice-node";
import { DigitalTwinSpace } from "opcua-webservice-node";
import { ElementCollectionType } from "opcua-webservice-node";
import { ReadPropertyValuesRequest, ReadPropertyValuesResponse } from "opcua-webservice-node";
import { ElementListTestType } from "./ElementListTestType";
import { ReferenceElementTestType } from "./ReferenceElementTestType";

export class ElementCollectionTestBType extends ElementCollectionType {
        private readonly referenceElement: UaObject;
        private readonly elementList: UaObject;

        constructor(
                referenceElementTestType: ReferenceElementTestType,
                elementListTestType: ElementListTestType,
                space: DigitalTwinSpace,
        ) {
                super("ElementCollectionTestBType", new UaLocalizedText("ElementCollectionTestBType"), space);
                this.description = new UaLocalizedText("ElementCollectionTestBType");

                this.referenceElement = this.addReferenceElement(
                        referenceElementTestType,
                        "Reference",
                        new UaLocalizedText("Reference"),
                        new UaLocalizedText("Reference member"),
                        true,
                );

                this.elementList = this.addElementList(
                        elementListTestType,
                        "ElementList",
                        new UaLocalizedText("ElementList"),
                        new UaLocalizedText("Element list member"),
                        true,
                );
        }

        override async onReadPropertyValues(_request: ReadPropertyValuesRequest): Promise<ReadPropertyValuesResponse> {
                return new ReadPropertyValuesResponse();
        }
}