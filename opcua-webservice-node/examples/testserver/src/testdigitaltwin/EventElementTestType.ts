import { UaLocalizedText } from "opcua-webapi-ts";
import { DigitalTwinSpace } from "opcua-webservice-node";
import { EventElementType } from "opcua-webservice-node";
import { ReadEventsRequest, ReadEventsResponse } from "opcua-webservice-node";
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
                
        const data1 = EventTestType.generateEventData(
            "123", 
            new Date("2026-01-02T00:00:00Z"),
            "abc", 
            "Customized field");

        const data2 = EventTestType.generateEventData(
            "456", 
            new Date("2026-01-02T00:01:00Z"), 
            "def", 
            "Customized field");

        const response = new ReadEventsResponse();
        response.addEventData(data1);
        response.addEventData(data2);
        response.containsMoreData = request.offset === 0;

        return response;
    }
}