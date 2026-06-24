import { UaLocalizedText } from "opcua-webapi-ts";
import { UaReactiveObjectType,UaObjectType } from "../../addressspace";
import { DigitalTwinSpace } from "../DigitalTwinSpace";
import { GetDescriptorRequest, GetDescriptorResponse } from "../../service/message";

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