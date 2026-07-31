import { StatusCodes } from "opcua-webapi";
import {
    UaError,
    UaEUInformation,
    UaLocalizedText,
    UaStatusCode,
    UaValueRank,
    UaVariant,
} from "opcua-webapi-ts";
import {
    AddRequest,
    AddResponse,
    DeleteRequest,
    DeleteResponse,
    ElementCollectionType,
    GetDescriptorRequest,
    GetDescriptorResponse,
    GetPermissionRequest,
    GetPermissionResponse,
    ModifyAttributeRequest,
    ModifyAttributeResponse,
    ReadPropertyValuesRequest,
    ReadPropertyValuesResponse,
    UaDataTypes,
    UaVariable,
    UaVariableTypes,
    WritePropertyValuesRequest,
    WritePropertyValuesResponse
} from "opcua-webservice-node";

import { prisma } from "../../../connectors/prismaClient";
import { EmployeeTwinSpace } from "../../EmployeeTwinSpace";
import { SkillCategoryEnumType } from "./SkillCategoryEnumType";
import { SkillLevelEnumType } from "./SkillLevelEnumType";

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
                true);

        // Add end time data field
        this.category = this.addPropertyElement(
                "Category",
                new UaLocalizedText("Category"),
                new UaLocalizedText("The category of skill"),
                skillCategoryEnumType,
                true);

        // Add experience data field
        this.experience = this.addPropertyElement(
                "Experience",
                new UaLocalizedText("Experience"),
                new UaLocalizedText("The year of experience required to be qualified"),
                UaDataTypes.Double,
                true,
                false, 
                UaValueRank.Scalar,     
                true,
                UaVariableTypes.BaseAnalogItemType);
        
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
    
    override async onWritePropertyValues(request: WritePropertyValuesRequest): Promise<WritePropertyValuesResponse>
    {
        const skillId = parseInt(request.id);
        if (Number.isNaN(skillId)) throw UaError.from(StatusCodes.BadNodeIdUnknown);

        let response = new WritePropertyValuesResponse();
        
        let levelValue = request.propertyNamesAndValues.get(this.level.name)?.toNumber();
        let categoryValue = request.propertyNamesAndValues.get(this.category.name)?.toNumber();
        let experienceValue = request.propertyNamesAndValues.get(this.experience.name)?.toNumber();
        
        let level = undefined;
        let category = undefined;
        let experience = undefined;
        
        if (levelValue !== null && levelValue !== undefined)
        {            
            level = SkillLevelEnumType.fromNumber(levelValue);
        }
            
        if (categoryValue !== null && categoryValue !== undefined)
        {
            
            category = SkillCategoryEnumType.fromNumber(categoryValue);
        }

        if (experienceValue !== null && experienceValue !== undefined)
        {
            if (experienceValue >= 0 && experienceValue <= 50) 
            {
                experience = experienceValue;
            } else {
                response.setWriteValueResult(this.experience.name, UaStatusCode.from(StatusCodes.BadInvalidArgument));
            }
        }

        if (level === undefined && category === undefined && experience === undefined)
        {
            throw UaError.from(StatusCodes.BadInvalidArgument);
        }

        try {
            await prisma.skill.update({
                where: { ID: skillId },
                data: {
                    Level: level,
                    Category: category,
                    YearOfExperience: experience }
                });
        } catch (error) {
            throw UaError.from(StatusCodes.BadDataUnavailable);
        }

        return response;
    } 

    override async onGetPermission(request: GetPermissionRequest): Promise<GetPermissionResponse>
    {
        return new GetPermissionResponse(true, true, true);
    }

    override async onAddElement(request: AddRequest): Promise<AddResponse>
    {        
        if (!request.displayName.text) throw UaError.from(StatusCodes.BadInvalidArgument);
        
        let skill = await prisma.skill.create({
            data: {
                SkillName: request.displayName.text,
                Description: null,
                Level: 3,
                Category: "",
                YearOfExperience: 0
            }
        });

        return new AddResponse(skill.ID.toString());
    }

    override async onDeleteElement(request: DeleteRequest): Promise<DeleteResponse> {        
        const skillId = parseInt(request.id);
        if (Number.isNaN(skillId)) throw UaError.from(StatusCodes.BadNodeIdUnknown);

        let statusCode = UaStatusCode.from(StatusCodes.Good);
        try {
            await prisma.skill.delete({
                where: { ID: skillId }
            });
        } catch (error) {
            statusCode = UaStatusCode.from(StatusCodes.BadNodeIdUnknown);
        }

        return new DeleteResponse(statusCode);
    }

    override async onRename(request: ModifyAttributeRequest): Promise<ModifyAttributeResponse>
    {
        if (!request.text.text) throw UaError.from(StatusCodes.BadInvalidArgument);

        const skillId = parseInt(request.id);
        if (Number.isNaN(skillId)) throw UaError.from(StatusCodes.BadNodeIdUnknown);

        let statusCode = UaStatusCode.from(StatusCodes.Good);
        try {
            await prisma.skill.update({
                where: { ID: skillId },
                data: {
                    SkillName: request.text.text
                }
            });
        } catch (error) {
            statusCode = UaStatusCode.from(StatusCodes.BadNodeIdUnknown);
        }

        return new ModifyAttributeResponse(statusCode);
    }

    override async onSetDescriptor(request: ModifyAttributeRequest): Promise<ModifyAttributeResponse>
    {
        console.log("onSetDescriptor");
        if (!request.text.text) throw UaError.from(StatusCodes.BadInvalidArgument);

        const skillId = parseInt(request.id);
        if (Number.isNaN(skillId)) throw UaError.from(StatusCodes.BadNodeIdUnknown);

        let statusCode = UaStatusCode.from(StatusCodes.Good);
        try {
            await prisma.skill.update({
                where: { ID: skillId },
                data: {
                    Description: request.text.text
                }
            });
        } catch (error) {
            statusCode = UaStatusCode.from(StatusCodes.BadNodeIdUnknown);
        }

        return new ModifyAttributeResponse(statusCode);
    }
}