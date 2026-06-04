import { StatusCodes } from "opcua-webapi";
import { UaError, UaLocalizedText } from "opcua-webapi-ts";
import {
    DigitalTwinType,
    GetDescriptorRequest,
    GetDescriptorResponse,
} from "opcua-webservice-node";

import { prisma } from "../../connectors/prismaClient";
import { EmployeeTwinSpace } from "../EmployeeTwinSpace";
import type { DepartmentSubmodelType } from "./department";
import type { SkillClassificationSubmodelType } from "./skill";

export class CompanyDigitalTwinType extends DigitalTwinType {
    constructor(
        departmentSubmodelType: DepartmentSubmodelType,
        skillClassificationSubmodelType: SkillClassificationSubmodelType,
        twinSpace: EmployeeTwinSpace) {
        super("CompanyDigitalTwinType", new UaLocalizedText("CompanyDigitalTwin"), twinSpace); 
        this.addSubmodel(
            departmentSubmodelType, 
            "Departments", 
            new UaLocalizedText("Departments"), 
            new UaLocalizedText("Provides department information"));       
        this.addSubmodel(
            skillClassificationSubmodelType,
            "SkillClassifications",
            new UaLocalizedText("SkillClassifications"),
            new UaLocalizedText("Provides skill classification information"));
    }

    public async onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse> {
        try {
            const companyId = Number.parseInt(request.id, 10);

            if (Number.isNaN(companyId)) {
                throw UaError.from(StatusCodes.BadNodeIdUnknown);
            }

            const company = await prisma.department.findUnique({
                where: { ID: companyId },
                select: { Name: true },
            });

            if (company === null) {
                throw UaError.from(StatusCodes.BadNodeIdUnknown);
            }

            return new GetDescriptorResponse(
                new UaLocalizedText(company.Name),
                UaLocalizedText.nullText,
            );
        } catch (error) {
            if (error instanceof UaError) {
                throw error;
            }

            throw UaError.from(StatusCodes.BadNodeIdUnknown);
        }
    }
}