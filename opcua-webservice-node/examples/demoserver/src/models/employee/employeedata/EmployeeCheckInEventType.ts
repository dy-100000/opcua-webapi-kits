import { UaLocalizedText, UaVariant } from "opcua-webapi-ts";
import { EventData, EventType, UaDataTypes } from "opcua-webservice-node";

import type { employeecheckin } from "../../../connectors/prisma/client";
import { EmployeeTwinSpace } from "../../EmployeeTwinSpace";

export class EmployeeCheckInEventType extends EventType {
    static readonly CheckIn = "CheckIn";
    static readonly Location = "Location";

    constructor(space: EmployeeTwinSpace) {
        super("EmployeeCheckInEventType", new UaLocalizedText("EmployeeCheckInEventType"), space);

        this.addField(
            EmployeeCheckInEventType.CheckIn,
            new UaLocalizedText(EmployeeCheckInEventType.CheckIn),
            new UaLocalizedText("Is check in"),
            UaDataTypes.Boolean,
        );

        this.addField(
            EmployeeCheckInEventType.Location,
            new UaLocalizedText(EmployeeCheckInEventType.Location),
            new UaLocalizedText("Location"),
            UaDataTypes.String,
        );
    }

    static generateEventData(record: employeecheckin): EventData {
        const data = new EventData(record.ID.toString());
        data.time = record.Time;
        data.message = record.Remark ?? "";
        data.setFieldData(EmployeeCheckInEventType.CheckIn, UaVariant.boolean(record.CheckIn));
        data.setFieldData(EmployeeCheckInEventType.Location, UaVariant.string(record.Location));

        return data;
    }
}