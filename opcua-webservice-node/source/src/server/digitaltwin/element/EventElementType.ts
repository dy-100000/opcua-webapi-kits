import { StatusCodes} from "opcua-webapi";
import { UaLocalizedText, UaVariant, UaReadEventDetails, UaError, makeUaStatusCode, UaHistoryEventFieldList, UaNodeId, ReferenceTypeIds } from "opcua-webapi-ts";
import { ElementType } from "./ElementType";
import { EventType } from "../event/EventType";
import { DigitalTwinSpace } from "../DigitalTwinSpace";
import { UaObjectTypes } from "../../addressspace/nodes/builtin/UaObjectTypes";
import { UaObjectType } from "../../addressspace/nodes/UaObjectType";
import { UaReference } from "../../addressspace/nodes/UaReference";
import { UaReferenceTypes } from "../../addressspace/nodes/builtin/UaReferenceTypes";
import { ObjectServiceContext } from "../../types/digitaltwin/ObjectServiceContext";
import { EventData } from "../event/EventData";
import { ReadHistoryEventRequest,
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
     * Internal framework callback used by the base type to get the reference type id for this repository.
     * Do not call or override this method directly.
     */
    supportedReferenceType(): UaNodeId {
        return UaNodeId.from(ReferenceTypeIds.HasComponent);
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