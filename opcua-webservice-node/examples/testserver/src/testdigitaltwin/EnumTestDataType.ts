import { UaLocalizedText } from "opcua-webapi-ts";
import { UaEnumDataType } from "opcua-webservice-node";
import { DigitalTwinSpace } from "opcua-webservice-node";

export class EnumTestDataType extends UaEnumDataType {
    constructor(space: DigitalTwinSpace) {
        super(
            "EnumTest",
            new UaLocalizedText("EnumTest"),
            ["A", "B", "C", "D"],
            space,
        );

        this.description = new UaLocalizedText("EnumTestDataType");
    }
}