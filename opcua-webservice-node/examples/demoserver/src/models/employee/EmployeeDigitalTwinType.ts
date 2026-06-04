import { StatusCodes } from "opcua-webapi";
import { UaError, UaLocalizedText } from "opcua-webapi-ts";
import {
    DigitalTwinType,
    GetDescriptorRequest,
    GetDescriptorResponse,
} from "opcua-webservice-node";

import { prisma } from "../../connectors/prismaClient";
import { EmployeeTwinSpace } from "../EmployeeTwinSpace";
import type { EmployeeDataSubmodelType, EmployeeSkillSubmodelType, PersonalDataSubmodelType } from "../..";

export class EmployeeDigitalTwinType extends DigitalTwinType {
    constructor(
        personalDataSubmodelType: PersonalDataSubmodelType,
        employeeDataSubmodelType: EmployeeDataSubmodelType,
        employeeSkillSubmodelType: EmployeeSkillSubmodelType,
        twinSpace: EmployeeTwinSpace) {
        super("EmployeeDigitalTwinType", new UaLocalizedText("Employee"), twinSpace);
    
        this.addSubmodel(
            personalDataSubmodelType,
            "PersonalData",
            new UaLocalizedText("PersonalData"),
            new UaLocalizedText("Provides personal data")
        );

        this.addSubmodel(
            employeeDataSubmodelType,
            "EmployeeData",
            new UaLocalizedText("EmployeeData"),
            new UaLocalizedText("Provides employee data")
        );

        this.addSubmodel(
            employeeSkillSubmodelType,
            "EmployeeSkill",
            new UaLocalizedText("EmployeeSkill"),
            new UaLocalizedText("Provides employee skill data")
        );
    }

    override async onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse> {
        const employeeId = Number.parseInt(request.id, 10);

        if (Number.isNaN(employeeId)) {
            throw UaError.from(StatusCodes.BadNodeIdUnknown);
        }

        const employee = await prisma.employee.findUnique({
            where: { ID: employeeId },
            select: { Name: true },
        });

        if (employee === null) {
            throw UaError.from(StatusCodes.BadNodeIdUnknown);
        }

        return new GetDescriptorResponse(
            new UaLocalizedText(employee.Name),
            UaLocalizedText.nullText,
        );
    }
}