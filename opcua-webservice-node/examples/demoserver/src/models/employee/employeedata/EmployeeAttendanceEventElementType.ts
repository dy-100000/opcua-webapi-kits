import { StatusCodes } from "opcua-webapi";
import { UaError, type UaHistoryEventFieldList, UaLocalizedText, UaQueryFilterType } from "opcua-webapi-ts";
import {
    EventElementType,
    ReadEventsRequest,
    ReadEventsResponse,
} from "opcua-webservice-node";

import { prisma } from "../../../connectors/prismaClient";
import { EmployeeTwinSpace } from "../../EmployeeTwinSpace";
import { EmployeeCheckInEventType } from "./EmployeeCheckInEventType";

export class EmployeeAttendanceEventElementType extends EventElementType {
    constructor(eventType: EmployeeCheckInEventType, space: EmployeeTwinSpace) {
        super("EmployeeAttendanceEventElementType", new UaLocalizedText("EmployeeAttendanceEventElementType"), eventType, space);
    }

    override async onReadEvents(request: ReadEventsRequest): Promise<ReadEventsResponse> {
        const employeeId = Number.parseInt(request.id, 10);

        if (Number.isNaN(employeeId)) {
            throw UaError.from(StatusCodes.BadNodeIdUnknown);
        }

        const checkIn = this.getCheckInFilter(request);
        const take = request.limit > 0 ? request.limit + 1 : undefined;
        const results = await prisma.employeecheckin.findMany({
            where: {
                EmployeeId: employeeId,
                Time: {
                    gte: request.startTime,
                    lte: request.endTime,
                },
                ...(checkIn === null ? {} : { CheckIn: checkIn }),
            },
            orderBy: {
                Time: request.ascendingOrder ? "asc" : "desc",
            },
            skip: request.offset,
            take,
        });

        const response = new ReadEventsResponse();
        const containsMoreData = request.limit > 0 && results.length > request.limit;
        const events = containsMoreData ? results.slice(0, request.limit) : results;

        response.containsMoreData = containsMoreData;

        for (const item of events) {
            response.addEventData(EmployeeCheckInEventType.generateEventData(item));
        }

        return response;
    }

    private getCheckInFilter(request: ReadEventsRequest): boolean | null {
        const filters = request.where?.filters ?? [];

        for (const filter of filters) {
            if (
                filter.fieldName !== EmployeeCheckInEventType.CheckIn
                || filter.operator !== UaQueryFilterType.Equals
            ) {
                continue;
            }

            const value = filter.value.toBoolean();

            if (value === null) {
                throw UaError.from(StatusCodes.BadEventFilterInvalid);
            }

            return filter.isNot ? !value : value;
        }

        return null;
    }
}