import { UaLocalizedText } from "opcua-webapi-ts";
import { UaReactiveObjectType } from "../../addressspace/reactiveobject/UaReactiveObjectType";
import { UaObjectTypes } from "../../addressspace/nodes/builtin";
import { DigitalTwinSpace } from "../DigitalTwinSpace";

export abstract class SubmodelTypeBase extends UaReactiveObjectType {
    constructor(
        typeId: string,
        displayName: UaLocalizedText,
        twinSpace: DigitalTwinSpace) {
        super(typeId, displayName, UaObjectTypes.SubmodelType, twinSpace);
    }

    digitalTwinSpace(): DigitalTwinSpace {
        return this.nodeManager as DigitalTwinSpace;
    }
}