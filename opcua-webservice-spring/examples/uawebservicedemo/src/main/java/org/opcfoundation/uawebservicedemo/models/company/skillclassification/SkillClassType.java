package org.opcfoundation.uawebservicedemo.models.company.skillclassification;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.EmployeeData;
import org.opcfoundation.uawebservicedemo.database.entity.Skill;
import org.opcfoundation.uawebservicedemo.models.company.skillclassification.enumeration.SkillCategory;
import org.opcfoundation.uawebservicedemo.models.company.skillclassification.enumeration.SkillLevel;
import org.opcfoundation.uawebservicedemo.database.mapper.SkillMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.uawebservicedemo.models.company.skillclassification.enumeration.SkillCategoryEnumType;
import org.opcfoundation.uawebservicedemo.models.company.skillclassification.enumeration.SkillLevelEnumType;
import org.opcfoundation.uawebservicedemo.models.employee.personaldata.enumeration.Sex;
import org.opcfoundation.webserver.addressspace.nodes.UaVariable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;
import org.opcfoundation.webserver.digitaltwin.element.ElementCollectionType;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorResponse;
import org.opcfoundation.webserver.service.message.digitaltwin.ReadPropertyValuesRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.ReadPropertyValuesResponse;
import org.opcfoundation.webserver.types.common.UaStructureUtilities;

import java.time.ZoneId;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SkillClassType extends ElementCollectionType {
    private final UaVariable level;
    private final UaVariable category;
    private final UaVariable experience;

    public SkillClassType(
            SkillLevelEnumType skillLevelEnumType,
            SkillCategoryEnumType skillCategoryEnumType,
            EmployeeTwinSpace twinSpace) {
        super("SkillClassType", new LocalizedText("SkillClass"), twinSpace);

        // Add start time data field
        level = addPropertyElement(
                "Level",
                new LocalizedText("Level"),
                new LocalizedText("The skill level of person"),
                skillLevelEnumType,
                false);

        // Add end time data field
        category = addPropertyElement(
                "Category",
                new LocalizedText("Category"),
                new LocalizedText("The category of skill"),
                skillCategoryEnumType,
                false);

        // Add experience data field
        experience = addPropertyElement(
                "Experience",
                new LocalizedText("Experience"),
                new LocalizedText("The year of experience required to be qualified"),
                UaDataTypes.Double,
                false,
                false,
                null,
                UaVariableTypes.BaseAnalogType,
                true);

        addSubElementOfProperty(
                experience,
                "EngineeringUnits",
                UaStructureUtilities.toVariant(new EUInformation(null,-1, new LocalizedText("Y"), new LocalizedText("Year"))));
    }

    // Return general information such as name, description
    @Override
    public CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request) {
        try
        {
            // Get department id from request
            Integer skillId = Integer.parseInt(request.getId());

            // Database query
            SkillMapper skillMapper = EmployeeDatabase.getSkillMapper();
            Skill skill = skillMapper.getSkill(skillId);
            if (null == skill) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            // Return department general information
            GetDescriptorResponse response = new GetDescriptorResponse(
                    new LocalizedText(skill.getName()), // Object name
                    new LocalizedText(skill.getDescription())); // Object description

            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }

    @Override
    public CompletableFuture<ReadPropertyValuesResponse> onReadPropertyValues(ReadPropertyValuesRequest request) {
        try
        {
            // Get skill id from request
            Integer skillId = Integer.parseInt(request.getId());

            // Database query
            SkillMapper skillMapper = EmployeeDatabase.getSkillMapper();
            Skill skill = skillMapper.getSkillData(skillId);
            if (null == skill) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            // Return field data
            ReadPropertyValuesResponse response = new ReadPropertyValuesResponse();

            SkillLevel levelValue = SkillLevel.fromInt(skill.getLevel());
            SkillCategory categoryValue = SkillCategory.fromString(skill.getCategory());

            response.setValue(level.name(), Variant.ofInt32(levelValue.getCode()));
            response.setValue(category.name(), Variant.ofInt32(categoryValue.getCode()));
            response.setValue(experience.name(),Variant.ofInt32(skill.getYearOfExperience()));

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
