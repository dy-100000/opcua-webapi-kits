import { StatusCodes } from "opcua-webapi";
import { UaError, UaLocalizedText } from "opcua-webapi-ts";
import {
    GetLinkRequest,
    GetLinkResponse,
    ReferenceElementType,
    ReferenceTargetDescriptor
} from "opcua-webservice-node";

import { prisma } from "../../../connectors/prismaClient";
import { EmployeeTwinSpace } from "../../EmployeeTwinSpace";

export class DepartmentEmployeeReferenceType extends ReferenceElementType {
    constructor(twinSpace: EmployeeTwinSpace) {
        super("DepartmentEmployeeReferenceType", new UaLocalizedText("DepartmentEmployee"), twinSpace);
    }

    override async onGetLinks(request: GetLinkRequest): Promise<GetLinkResponse> {
        // Get department id from request
        const departmentId = Number.parseInt(request.id, 10);
        if (Number.isNaN(departmentId)) {
            throw UaError.from(StatusCodes.BadNodeIdUnknown);
        }

        // Database query
        let department = await prisma.department.findFirst({
            where: { ID: departmentId },
            select: {                    
                Name: true,
                employeedepartment: { 
                    select: { employee: { select: { ID: true, Name: true } } } }
                }
        });

        if (department === null) {
            throw UaError.from(StatusCodes.BadNodeIdUnknown);
        }

        console.log(department.employeedepartment);

        let response = new GetLinkResponse();

        for (const item of department.employeedepartment) {
            const descriptor = new ReferenceTargetDescriptor(
                item.employee.ID.toString(), 
                new UaLocalizedText(item.employee.Name), 
                EmployeeTwinSpace.EmployeeDigitalTwinType);
            response.add(descriptor);
        }

        return response;   
    }
}