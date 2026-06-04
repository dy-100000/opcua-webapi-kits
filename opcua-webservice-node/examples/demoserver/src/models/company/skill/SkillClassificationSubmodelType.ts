import { UaLocalizedText } from "opcua-webapi-ts";
import { DynamicSubmodelType, GetObjectElementListRequest, GetObjectElementListResponse, ObjectElementDescriptor } from "opcua-webservice-node";

import { prisma } from "../../../connectors/prismaClient";
import { EmployeeTwinSpace } from "../../EmployeeTwinSpace";

export class SkillClassificationSubmodelType extends DynamicSubmodelType {
    constructor(twinSpace: EmployeeTwinSpace) {
        super("SkillClassificationSubmodelType", new UaLocalizedText("SkillClassificationModel"), twinSpace);
    }
        
    override async onGetObjectElementList(request: GetObjectElementListRequest): Promise<GetObjectElementListResponse> {
        // Database query
        const skills = await prisma.skill.findMany({
            select: {
                ID: true,
                SkillName: true
            }
        });

        // Return general information of skill        
        let response = new GetObjectElementListResponse();

        for (const item of skills) {
            const descriptor = new ObjectElementDescriptor(
                    item.ID.toString(), // SkillId
                    new UaLocalizedText(item.SkillName), // SkillName
                    EmployeeTwinSpace.SkillClassType); // SkillType

            response.add(descriptor);
        }

        return response;       
    }
}