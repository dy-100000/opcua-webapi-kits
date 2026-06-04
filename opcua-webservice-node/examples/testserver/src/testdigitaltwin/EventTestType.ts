import { UaLocalizedText, UaVariant } from "opcua-webapi-ts";
import { UaDataTypes } from "opcua-webservice-node";
import { DigitalTwinSpace } from "opcua-webservice-node";
import { EventData, EventType } from "opcua-webservice-node";

export class EventTestType extends EventType {
    static readonly Customized = "Customized";

    constructor(space: DigitalTwinSpace) {
        super("EventTestType", new UaLocalizedText("EventTestType"), space);
        this.description = new UaLocalizedText("EventTestType");

        this.addField(
            EventTestType.Customized,
            new UaLocalizedText(EventTestType.Customized),
            new UaLocalizedText("Customized field"),
            UaDataTypes.String,
        );
    }

    static generateEventData(
        eventId: string,
        time: Date,
        message: string,
        customized: string,
    ): EventData {
        const data = new EventData(eventId);
        data.time = time;
        data.message = message;
        data.setFieldData(EventTestType.Customized, UaVariant.string(customized));
        return data;
    }
}