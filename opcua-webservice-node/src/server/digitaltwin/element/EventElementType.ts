import { UaEventNotifier, UaHistoryEventFieldList, UaLocalizedText, UaReadEventDetails } from "opcua-webapi-ts";
import { UaReference } from "../../addressspace/nodes/UaReference";
import type { UaObjectType } from "../../addressspace/nodes/UaObjectType";
import { UaObjectTypes } from "../../addressspace/nodes/builtin";
import { UaReferenceTypes } from "../../addressspace/nodes/builtin/UaReferenceTypes";
import { EventElementCallback } from "../callback/EventElementCallback";
import {
    GetDescriptorRequest,
    GetDescriptorResponse,
    ReadEventsRequest,
    ReadEventsResponse,
    ReadHistoryEventRequest,
    ReadHistoryEventResponse,
    ReadObjectAttributeRequest,
    ReadObjectAttributeResponse,
} from "../../service/message";
import { ObjectServiceContext } from "../../types/digitaltwin";
import { ElementType } from "./ElementType";
import { DigitalTwinSpace } from "../DigitalTwinSpace";

export abstract class EventElementType extends ElementType implements EventElementCallback {
    private readonly eventType: UaObjectType;

    constructor(
        typeId: string,
        displayName: UaLocalizedText,
        generateEventType: UaObjectType | null,
        twinSpace: DigitalTwinSpace,
    ) {
        super(typeId, displayName, UaObjectTypes.EventElementType, twinSpace);

        this.eventType = generateEventType ?? UaObjectTypes.BaseEventType;
        this.addReference(new UaReference(this.eventType, UaReferenceTypes.GeneratesEvent, true));
    }

    // Override to read event history data
    abstract onReadEvents(request: ReadEventsRequest): Promise<ReadEventsResponse>;
    
    // Can be override to provide customized descriptor
    async onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse>
    {
        const instance = request.context.objectId.instance;

        if (instance === null) {
            return new GetDescriptorResponse("NotImplemented");
        }

        return new GetDescriptorResponse(instance.displayName, instance.description);
    }

    // Implementation of parent type methods, don't override
    override async onReadObjectAttributes(request: ReadObjectAttributeRequest): Promise<ReadObjectAttributeResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetDescriptor(new GetDescriptorRequest(context));

        return new ReadObjectAttributeResponse(
            request.objectId.id,
            response.displayName,
            response.description,
            UaEventNotifier.HistoryRead);

    }

    // Implementation of parent type methods, don't override
    override async onReadHistoryEvent(request: ReadHistoryEventRequest): Promise<ReadHistoryEventResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const details = UaReadEventDetails.fromExtensionObject(request.details);
        if (details === null) {
            return new ReadHistoryEventResponse([], false);
        }

        const readEventsRequest = ReadEventsRequest.getRequest(context, details, request.offset);
        const response = await this.onReadEvents(readEventsRequest);

        return this.processReadHistoryEventResponse(response);
    }

    private processReadHistoryEventResponse(response: ReadEventsResponse): ReadHistoryEventResponse {
        return new ReadHistoryEventResponse(response.eventsData as Array<UaHistoryEventFieldList>, response.containsMoreData);
    }
}