import { UaHistoryEventFieldList, UaLocalizedText, UaVariant } from "opcua-webapi-ts";
import { DigitalTwinSpace } from "../../../src/server/digitaltwin";
import { EventData } from "../../../src/server/digitaltwin/event";
import { EventElementType } from "../../../src/server/digitaltwin/element";
import { ReadEventsRequest, ReadEventsResponse } from "../../../src/server/service/message";
import { EventTestType } from "./EventTestType";

export class EventElementTestType extends EventElementType {
    constructor(testType: EventTestType, space: DigitalTwinSpace) {
        super("EventElementTestType", new UaLocalizedText("EventElementTestType"), testType, space);
        this.description = new UaLocalizedText("EventElementTestType");
    }

    override async onReadEvents(request: ReadEventsRequest): Promise<ReadEventsResponse> {
        console.log(
            `Id: ${request.id} StartTime: ${request.startTime.toISOString()} EndTime: ${request.endTime.toISOString()} Limit: ${request.limit} Offset: ${request.offset}`,
        );
        console.log("Select:", request.select);
        console.log("Where:", request.where);

        const now = new Date();
        const data1 = EventTestType.generateEventData("123", now, "abc", "Customized field");
        const data2 = EventTestType.generateEventData("456", new Date(now.getTime() + 60000), "def", "Customized field");

        const response = new ReadEventsResponse();
        response.addEventData(this.toHistoryEvent(data1, request.select));
        response.addEventData(this.toHistoryEvent(data2, request.select));
        response.containsMoreData = request.offset === 0;

        return response;
    }

    private toHistoryEvent(eventData: EventData, selectedFields: Array<string>): UaHistoryEventFieldList {
        const eventFields: Array<UaVariant> = [];

        for (const fieldName of selectedFields) {
            if (fieldName === "EventId" || fieldName === "eventId") {
                eventFields.push(UaVariant.string(eventData.eventId));
            } else if (fieldName === "Time" || fieldName === "time") {
                eventFields.push(eventData.time === null ? UaVariant.null() : UaVariant.dateTime(eventData.time));
            } else if (fieldName === "Message" || fieldName === "message") {
                eventFields.push(UaVariant.localizedText(new UaLocalizedText(eventData.message)));
            } else {
                eventFields.push(eventData.eventData.get(fieldName) ?? UaVariant.null());
            }
        }

        return new UaHistoryEventFieldList(eventFields);
    }
}