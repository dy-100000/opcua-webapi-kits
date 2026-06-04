import { StatusCodes } from "opcua-webapi";
import {
    UaError,
    UaLocalizedText
} from "opcua-webapi-ts";
import {
    DynamicSubmodelType,
    GetObjectElementListRequest,
    GetObjectElementListResponse,
    ObjectElementDescriptor,
} from "opcua-webservice-node";

import { prisma } from "../../../connectors/prismaClient";
import { EmployeeTwinSpace } from "../../EmployeeTwinSpace";

export class DepartmentSubmodelType extends DynamicSubmodelType {
    constructor(twinSpace: EmployeeTwinSpace) {
        super("DepartmentSubmodelType", new UaLocalizedText("DepartmentSubmodel"), twinSpace);
    }
    
    async onGetObjectElementList(request: GetObjectElementListRequest) : Promise<GetObjectElementListResponse> {
        // Get company id from request
        const companyId = Number.parseInt(request.id, 10);

        const company = await prisma.department.findUnique({
            where: { ID: companyId },
            select: { other_department: { select: { ID: true, Name: true } } },
        });

        if (company === null) {
            throw UaError.from(StatusCodes.BadNodeIdUnknown);
        }

        let response = new GetObjectElementListResponse();
        for (let item of company.other_department) {
            response.add(
                new ObjectElementDescriptor(
                    item.ID.toString(), new UaLocalizedText(item.Name), EmployeeTwinSpace.DepartmentType
                )
            );
        }

        return response;
    }
}