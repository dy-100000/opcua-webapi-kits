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

export class CompanyRepositoryType extends DigitalTwinRepositoryType {
    constructor(twinSpace: EmployeeTwinSpace) {
        super("CompanyRepositoryType", new UaLocalizedText("CompanyRepository"), twinSpace);
    }

    public async onGetDigitalTwinList(request: GetDigitalTwinListRequest): Promise<GetDigitalTwinListResponse> {
        // Database query
        const departments = await prisma.department.findMany({
            where: { ParentDepartmentId: null } // The department without parent id is a company
        });

        // Return department general information
        const response = new GetDigitalTwinListResponse();
        const companyDigitalTwinType = EmployeeTwinSpace.CompanyDigitalTwinType;

        for (const item of departments) {
            const descriptor = new DigitalTwinDescriptor(
                item.ID.toString(), // CompanyId
                new UaLocalizedText(item.Name), // CompanyName
                companyDigitalTwinType // CompanyDigitalTwinType
            );

            response.add(descriptor);
        }

        return response;
    }
}