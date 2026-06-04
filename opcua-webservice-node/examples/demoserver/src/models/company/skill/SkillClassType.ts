import { StatusCodes } from "opcua-webapi";
import {
    UaError,
    UaEUInformation,
    UaLocalizedText,
    UaValueRank,
    UaVariant,
} from "opcua-webapi-ts";
import {
    ElementCollectionType,
    GetDescriptorRequest,
    GetDescriptorResponse,
    ReadPropertyValuesRequest,
    ReadPropertyValuesResponse,
    UaDataTypes,
    UaVariable,
    UaVariableTypes
} from "opcua-webservice-node";

import { prisma } from "../../../connectors/prismaClient";
import { EmployeeTwinSpace } from "../../EmployeeTwinSpace";
import type { SkillCategoryEnumType } from "./SkillCategoryEnumType";
import type { SkillLevelEnumType } from "./SkillLevelEnumType";

export class SkillClassType extends ElementCollectionType {
    private readonly level: UaVariable;
    private readonly category: UaVariable;
    private readonly experience: UaVariable;

    constructor(
            skillLevelEnumType: SkillLevelEnumType,
            skillCategoryEnumType: SkillCategoryEnumType,
            twinSpace: EmployeeTwinSpace) {
        super("SkillClassType", new UaLocalizedText("SkillClass"), twinSpace);

        // Add start time data field
        this.level = this.addPropertyElement(
                "Level",
                new UaLocalizedText("Level"),
                new UaLocalizedText("The skill level of person"),
                skillLevelEnumType,
                false);

        // Add end time data field
        this.category = this.addPropertyElement(
                "Category",
                new UaLocalizedText("Category"),
                new UaLocalizedText("The category of skill"),
                skillCategoryEnumType,
                false);

        // Add experience data field
        this.experience = this.addPropertyElement(
                "Experience",
                new UaLocalizedText("Experience"),
                new UaLocalizedText("The year of experience required to be qualified"),
                UaDataTypes.Double,
                false,
                false, 
                UaValueRank.Scalar,     
                true,
                UaVariableTypes.BaseDataVariableType);
        
        this.addSubElementOfProperty(
                this.experience,
                "EngineeringUnits",
                UaVariant.extensionObject(
                new UaEUInformation(
                    1,
                    new UaLocalizedText("Y"),
                    new UaLocalizedText("Year"),
                    null,
                ).toExtensionObject())
            );
    }
        
    override async onGetDescriptor(request: GetDescriptorRequest) : Promise<GetDescriptorResponse> {
        // Get department id from request
        const skillId = parseInt(request.id);

        if (Number.isNaN(skillId)) throw UaError.from(StatusCodes.BadNodeIdUnknown);

        // Database query
        let skill = await prisma.skill.findUnique({
            where: {
                ID: skillId
            }
        });

        if (skill === null) throw UaError.from(StatusCodes.BadNodeIdUnknown); 

        // Return department general information
        const response = new GetDescriptorResponse(
                new UaLocalizedText(skill.SkillName),
                new UaLocalizedText(skill.Description));

        return Promise.resolve(response);
    }
        
    override async onReadPropertyValues(request: ReadPropertyValuesRequest) : Promise<ReadPropertyValuesResponse> {
            // Get skill id from request
            const skillId = parseInt(request.id);

            // Database query
            const skill = await prisma.skill.findUnique({
                where: { ID: skillId },
                select: {
                    Level: true,
                    Category: true,
                    YearOfExperience: true
                }
            });
            if (skill === null) throw UaError.from(StatusCodes.BadNodeIdUnknown);

            // Return field data
            const response = new ReadPropertyValuesResponse();
            response.setValue(this.level.name, UaVariant.integer(EmployeeTwinSpace.SkillLevelEnumType.parse(skill.Level)));
            response.setValue(this.category.name, UaVariant.integer(EmployeeTwinSpace.SkillCategoryEnumType.parse(skill.Category)));
            response.setValue(this.experience.name, UaVariant.integer(skill.YearOfExperience));

            return response;
    }
}