package org.opcfoundation.uawebservicedemo.models.skill;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Skill;
import org.opcfoundation.uawebservicedemo.models.skill.enumeration.SkillCategory;
import org.opcfoundation.uawebservicedemo.models.skill.enumeration.SkillLevel;
import org.opcfoundation.uawebservicedemo.database.mapper.SkillMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeDataManager;
import org.opcfoundation.uawebservicedemo.models.skill.enumeration.SkillCategoryEnumType;
import org.opcfoundation.uawebservicedemo.models.skill.enumeration.SkillLevelEnumType;
import org.opcfoundation.webserver.addressspace.models.UaDataObjectType;
import org.opcfoundation.webserver.addressspace.nodes.UaVariable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.types.UaChildId;
import org.opcfoundation.webserver.types.message.ReadObjectAttributeRequest;
import org.opcfoundation.webserver.types.message.ReadObjectAttributeResponse;
import org.opcfoundation.webserver.types.message.ReadVariableValueRequest;
import org.opcfoundation.webserver.types.message.ReadVariableValueResponse;

import java.util.concurrent.CompletableFuture;

public class SkillType extends UaDataObjectType {
    private final UaVariable level;
    private final UaVariable category;
    private final UaVariable yearOfExperience;

    public SkillType(
            SkillLevelEnumType skillLevelEnumType,
            SkillCategoryEnumType skillCategoryEnumType,
            EmployeeDataManager nodeManager) {
        super("SkillType", new LocalizedText("技能类定义"), null,nodeManager);
        setDescription(new LocalizedText("To present the data of employee skill"));

        // Add start time data field
        level = addVariable(
                "Level",
                true,
                new LocalizedText("Level"),
                new LocalizedText("The skill level of employee"),
                skillLevelEnumType,
                false,
                false,
                null,
                null);

        // Add end time data field
        category = addVariable(
                "Category",
                true,
                new LocalizedText("Category"),
                new LocalizedText("The category of skill"),
                skillCategoryEnumType,
                false,
                false,
                null,
                null);

        // Add budget data field
        yearOfExperience = addVariable(
                "YearOfExperience",
                true,
                new LocalizedText("YearOfExperience"),
                new LocalizedText("The year of experience required to be qualified"),
                UaDataTypes.Int32,
                false,
                false,
                null,
                null);
    }

    // Return general information such as name, description
    public CompletableFuture<ReadObjectAttributeResponse> getObjectAttribute(ReadObjectAttributeRequest request) {
        try
        {
            // Get department id from request
            Integer skillId = Integer.parseInt(request.getObjectId().getId());

            // Database query
            SkillMapper skillMapper = EmployeeDatabase.getSkillMapper();
            Skill skill = skillMapper.getSkillInfo(skillId);
            if (null == skill) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            // Return department general information
            ReadObjectAttributeResponse response = new ReadObjectAttributeResponse(
                    new LocalizedText(skill.getName()), // Object name
                    new LocalizedText(skill.getDescription())); // Object description

            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }

    @Override
    public CompletableFuture<ReadVariableValueResponse> getVariableValues(ReadVariableValueRequest request)
    {
        try
        {
            // Get skill id from request
            Integer skillId = Integer.parseInt(request.getObjectId().getId());

            // Database query
            SkillMapper skillMapper = EmployeeDatabase.getSkillMapper();
            Skill skill = skillMapper.getSkillData(skillId);
            if (null == skill) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            // Return field data
            ReadVariableValueResponse response = new ReadVariableValueResponse();

            for (UaChildId item: request.getVariableIds())
            {
                // Return level if required
                if (level.browseName().equals(item.getPathId()))
                {
                    SkillLevel level = SkillLevel.fromInt(skill.getLevel());
                    response.setValue(item, Variant.ofInt32(level.getCode()));
                }

                // Return category if required
                if (category.browseName().equals(item.getPathId())) {
                    SkillCategory category = SkillCategory.fromString(skill.getCategory());
                    response.setValue(item, Variant.ofInt32(category.getCode()));
                }

                // Return yearOfExperience if required
                if (yearOfExperience.browseName().equals(item.getPathId())) {
                    response.setValue(item,Variant.ofInt32(skill.getYearOfExperience()));
                }
            }

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
