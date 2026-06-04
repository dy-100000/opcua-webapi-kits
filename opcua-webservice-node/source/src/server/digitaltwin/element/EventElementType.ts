import { StatusCodes} from "opcua-webapi";
import { UaLocalizedText, UaVariant, UaEventNotifier, UaReadEventDetails, UaError, makeUaStatusCode, UaHistoryEventFieldList } from "opcua-webapi-ts";
import { ElementType } from "./ElementType";
import { EventType } from "../event/EventType";
import { DigitalTwinSpace } from "../DigitalTwinSpace";
import { UaObjectTypes } from "../../addressspace/nodes/builtin/UaObjectTypes";
import { UaObjectType } from "../../addressspace/nodes/UaObjectType";
import { UaReference } from "../../addressspace/nodes/UaReference";
import { UaReferenceTypes } from "../../addressspace/nodes/builtin/UaReferenceTypes";
import { ObjectServiceContext } from "../../types/digitaltwin/ObjectServiceContext";
import { EventData } from "../event/EventData";
import { ReadObjectAttributeRequest, 
    ReadObjectAttributeResponse, 
    GetDescriptorRequest, 
    GetDescriptorResponse,
    ReadHistoryEventRequest,
    ReadHistoryEventResponse,
    ReadEventsRequest, 
    ReadEventsResponse } from "../../service/message";

export abstract class EventElementType extends ElementType {
    private static readonly DefaultEventField: Set<string> = new Set<string>();
    private readonly eventType: UaObjectType;

    constructor(
        typeId: string,
        displayName: UaLocalizedText,
        generateEventType: EventType | null,
        twinSpace: DigitalTwinSpace) {
        super(
            typeId,
            displayName,
            UaObjectTypes.EventElementType,
            twinSpace,
        );

        if (EventElementType.DefaultEventField.size === 0) {
            EventElementType.DefaultEventField.add("EventId");
            EventElementType.DefaultEventField.add("EventType");
            EventElementType.DefaultEventField.add("Time");
            EventElementType.DefaultEventField.add("Message");
        }

        this.eventType = generateEventType ?? UaObjectTypes.BaseEventType;
        this.addReference(new UaReference(this.eventType, UaReferenceTypes.GeneratesEvent, true));
    }

    /**
     * Override in subclasses to read historical events.
     */
    abstract onReadEvents(request: ReadEventsRequest): Promise<ReadEventsResponse>;

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

    /**
     * Internal framework callback used by the base type to read object attributes.
     * Do not call or override this method directly.
     */
    override async onReadObjectAttributes(request: ReadObjectAttributeRequest): Promise<ReadObjectAttributeResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetDescriptor(new GetDescriptorRequest(context));
        return new ReadObjectAttributeResponse(
            request.objectId.id, 
            response.displayName, 
            response.description, 
            UaEventNotifier.SubscribeToEvents);
    }

    /**
     * Internal framework callback used by the base type to read history events.
     * Do not call or override this method directly.
     */
    async onReadHistoryEvent(request: ReadHistoryEventRequest): Promise<ReadHistoryEventResponse> {
        const context = new ObjectServiceContext(request.objectId);
        let details = UaReadEventDetails.fromExtensionObject(request.details);
        if (!details) { throw new UaError(makeUaStatusCode(StatusCodes.BadHistoryOperationUnsupported) ); }

        const readEventsRequest = ReadEventsRequest.getRequest(context, details, request.offset);
        const readEventResponse = await this.onReadEvents(readEventsRequest);
        return this.processReadHistoryEventResponse(readEventsRequest, readEventResponse);
    }

    private processReadHistoryEventResponse(request: ReadEventsRequest, response: ReadEventsResponse): ReadHistoryEventResponse {
        const select = request.select;
        const eventFieldLists: UaHistoryEventFieldList[] = [];

        for (const item of response.eventsData) {
            const eventFieldsValue: UaVariant[] = [];

            for (const field of select) {
                const value = this.getFieldValue(field, item);
                eventFieldsValue.push(value);
            }

            eventFieldLists.push(new UaHistoryEventFieldList(eventFieldsValue));
        }

        return new ReadHistoryEventResponse(eventFieldLists, response.containsMoreData);
    }

    private getFieldValue(fieldName: string, eventData: EventData): UaVariant {
        if (EventElementType.DefaultEventField.has(fieldName)) {
            switch (fieldName) {
                case "EventId":
                    return UaVariant.byteString(eventData.eventId);
                case "EventType":
                    return UaVariant.nodeId(this.eventType.nodeId);
                case "Time":
                    return UaVariant.dateTime(eventData.time ?? new Date());
                case "Message":
                    return UaVariant.localizedText(new UaLocalizedText(eventData.message));
                default:
                    return UaVariant.null();
            }
        } else {
            const value = eventData.eventData.get(fieldName);
            return value ?? UaVariant.null();
        }
    }
}