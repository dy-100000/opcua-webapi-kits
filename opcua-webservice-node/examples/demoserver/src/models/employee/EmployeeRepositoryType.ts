import { StatusCodes } from "opcua-webapi";
import { UaError, UaLocalizedText, makeUaStatusCode } from "opcua-webapi-ts";
import {
    DigitalTwinDescriptor,
    DigitalTwinRepositoryType,
    GetDigitalTwinListRequest,
    GetDigitalTwinListResponse,
} from "opcua-webservice-node";

import { prisma } from "../../connectors/prismaClient";
import { EmployeeTwinSpace } from "../EmployeeTwinSpace";

export class EmployeeRepositoryType extends DigitalTwinRepositoryType {
    constructor(twinSpace: EmployeeTwinSpace) {
        super("EmployeeRepositoryType", new UaLocalizedText("EmployeeRepository"), twinSpace);
    }

    override async onGetDigitalTwinList(
        _request: GetDigitalTwinListRequest,
    ): Promise<GetDigitalTwinListResponse> {
        const employees = await prisma.employee.findMany({
            select: {
                ID: true,
                Name: true,
            },
            orderBy: {
                ID: "asc",
            },
        });

        const response = new GetDigitalTwinListResponse();

        for (const item of employees) {
            response.add(
                new DigitalTwinDescriptor(
                    item.ID.toString(),
                    new UaLocalizedText(item.Name),
                    EmployeeTwinSpace.EmployeeDigitalTwinType,
                ),
            );
        }

        return response;
    }
}