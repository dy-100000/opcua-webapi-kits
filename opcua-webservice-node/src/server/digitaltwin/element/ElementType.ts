import { UaLocalizedText } from "opcua-webapi-ts";
import { UaReactiveObjectType,UaObjectType } from "../../addressspace";
import { DigitalTwinSpace } from "../DigitalTwinSpace";

export abstract class ElementType extends UaReactiveObjectType {
    constructor(
        typeId: string,
        displayName: UaLocalizedText,
        elementType: UaObjectType | null,
        twinSpace: DigitalTwinSpace,
    ) {
        super(typeId, displayName, elementType, twinSpace);
    }

    digitalTwinSpace(): DigitalTwinSpace {
        return this.nodeManager as DigitalTwinSpace;
    }
}