import { UaLocalizedText } from "opcua-webapi-ts";
import { UaReactiveObjectType } from "../../addressspace/reactiveobject/UaReactiveObjectType";
import { UaObjectTypes } from "../../addressspace/nodes/builtin";
import { DigitalTwinSpace } from "../DigitalTwinSpace";
import { GetDescriptorRequest, GetDescriptorResponse } from "../../service/message";

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

    /**
     * Optional override point to provide a custom descriptor for this instance.
     */
    async onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse>
    {
        const instance = request.context.objectId.instance;

        if (instance === null) {
            return new GetDescriptorResponse("NotImplemented");
        }

        return new GetDescriptorResponse(instance.displayName, instance.description);
    }
}