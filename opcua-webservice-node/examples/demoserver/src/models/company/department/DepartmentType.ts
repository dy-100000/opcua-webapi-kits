import { StatusCodes } from "opcua-webapi";
import {
    UaError,
    UaLocalizedText,
} from "opcua-webapi-ts";
import {
    ElementCollectionType,
    GetDescriptorRequest,
    GetDescriptorResponse,
} from "opcua-webservice-node";

import { prisma } from "../../../connectors/prismaClient";
import { EmployeeTwinSpace } from "../../EmployeeTwinSpace";
import type { DepartmentEmployeeReferenceType } from "./DepartmentEmployeeReferenceType";

export class DepartmentType extends ElementCollectionType {
    constructor(
            departmentEmployeeReferenceType: DepartmentEmployeeReferenceType,
            twinSpace: EmployeeTwinSpace) {
        super("DepartmentType", new UaLocalizedText("Department"), twinSpace);

        this.addReferenceElement(
                departmentEmployeeReferenceType,
                "Employees",
                new UaLocalizedText("Employees"),
                new UaLocalizedText("Employees Of department"),
                true);
    }

    async onGetDescriptor(request : GetDescriptorRequest) : Promise<GetDescriptorResponse>
    {
        // Get department id from request
        const departmentId = Number.parseInt(request.id, 10);
        if (Number.isNaN(departmentId)) {
            throw UaError.from(StatusCodes.BadNodeIdUnknown);
        }

        // Database query
        const department = await prisma.department.findUnique({
            where: { ID: departmentId },
            select: { Name: true, Comment: true },
        });

        if (department === null) {
            throw UaError.from(StatusCodes.BadNodeIdUnknown);
        }

        return new GetDescriptorResponse(
            new UaLocalizedText(department.Name),
            new UaLocalizedText(department.Comment ?? ""));
    }
}