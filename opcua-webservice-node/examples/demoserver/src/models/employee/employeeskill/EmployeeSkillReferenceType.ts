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

export class EmployeeSkillReferenceType extends ReferenceElementType {
    constructor(twinSpace: EmployeeTwinSpace) {
        super("EmployeeSkillReferenceType", new UaLocalizedText("EmployeeSkillReference"), twinSpace);
    }

    override async onGetLinks(request: GetLinkRequest): Promise<GetLinkResponse>
    {
        // Get employee id from request
        const employeeId = parseInt(request.id);
        if (isNaN(employeeId)) throw UaError.from(StatusCodes.BadInvalidArgument);
        
        // Database query
        const employee = await prisma.employee.findUnique({
            where: { ID: employeeId },
            select: {
                employeeskill: {
                    select: {
                        skill: { select: { ID: true, SkillName: true } }
                    }                    
                }
                }
            });

        if (!employee) throw UaError.from(StatusCodes.BadNodeIdUnknown);

        // Return general information of child department
        let response = new GetLinkResponse();

        for (const item of employee.employeeskill) {
            let current = item.skill;

            const descriptor = new ReferenceTargetDescriptor(
                current.ID.toString(), // Child id
                new UaLocalizedText(current.SkillName), // Child name
                EmployeeTwinSpace.SkillClassType);

            response.add(descriptor);
        }

        return response;
    }
}