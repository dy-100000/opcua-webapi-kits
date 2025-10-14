package org.opcfoundation.uawebservicedemo.models.employee;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Skill;
import org.opcfoundation.uawebservicedemo.database.mapper.SkillMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeDataManager;
import org.opcfoundation.uawebservicedemo.models.skill.enumeration.SkillCategory;
import org.opcfoundation.uawebservicedemo.models.skill.enumeration.SkillLevel;
import org.opcfoundation.webserver.addressspace.models.UaDataObjectType;
import org.opcfoundation.webserver.addressspace.nodes.UaMethod;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.types.message.MethodCallRequest;
import org.opcfoundation.webserver.types.message.MethodCallResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EmployeeCompetenceType extends UaDataObjectType {
    private UaMethod fullStackDeveloper;
    private UaMethod expertTester;

    public EmployeeCompetenceType(EmployeeDataManager nodeManager) {
        super("EmployeeCompetenceType", new LocalizedText("员工能力类定义"), null,nodeManager);

        addFullStackDeveloperMethod();
        addHighLevelTesterMethod();
    }

    public CompletableFuture<MethodCallResponse> methodCall(MethodCallRequest request) {
        try {
            // Get employee id from request
            Integer employeeId = Integer.parseInt(request.getObjectId().getId());
            List<Variant> outputArguments = new ArrayList<>();

            // Database query
            SkillMapper shillMapper = EmployeeDatabase.getSkillMapper();
            List<Skill> skills = shillMapper.getSkillDataOfEmployee(employeeId);

            if (request.getMethodName().equals(fullStackDeveloper.browseName()))
            {
                Boolean result = isFullStackDeveloper(skills);
                outputArguments.add(Variant.ofBoolean(result));
                return CompletableFuture.completedFuture(new MethodCallResponse(outputArguments));

            } else if (request.getMethodName().equals(expertTester.browseName())) {
                Boolean result = isExpertTester(skills);
                outputArguments.add(Variant.ofBoolean(result));
                return CompletableFuture.completedFuture(new MethodCallResponse(outputArguments));

            } else {
                throw new UaRuntimeException(StatusCodes.Bad_MethodInvalid);
            }
        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_MethodInvalid);
        }
    }

    private void addFullStackDeveloperMethod()
    {
        List<Argument> outputArguments = new ArrayList<>();
        Argument isCompetent = new Argument(
                "IsCompetent",
                UaDataTypes.Boolean.nodeId(),
                -1,
                null,
                new LocalizedText("IsCompetent"));

        outputArguments.add(isCompetent);

        fullStackDeveloper = addMethod(
                "FullStackDeveloper",
                true,
                new LocalizedText("全栈开发者"),
                new LocalizedText("If this person possess full stack developer skills"),
                null,
                outputArguments);
    }

    private void addHighLevelTesterMethod()
    {
        List<Argument> outputArguments = new ArrayList<>();
        Argument isCompetent = new Argument(
                "IsCompetent",
                UaDataTypes.Boolean.nodeId(),
                -1,
                null,
                new LocalizedText("IsCompetent"));

        outputArguments.add(isCompetent);

        expertTester = addMethod(
                "ExpertTester",
                true,
                new LocalizedText("资深测试"),
                new LocalizedText("If this person possess expert tester skills"),
                null,
                outputArguments);
    }

    private Boolean isFullStackDeveloper(List<Skill> skills)
    {
        boolean hasBackendDeveloperSkill = false;
        boolean hasUIDeveloperSkill = false;

        for (Skill item : skills)
        {
            SkillCategory category = SkillCategory.fromString(item.getCategory());
            SkillLevel level = SkillLevel.fromInt(item.getLevel());

            if (SkillCategory.BACKEND_DEVELOPER == category &&
                    level.getCode() >= SkillLevel.MIDDLE.getCode()) hasBackendDeveloperSkill = true;

            if (SkillCategory.UI_DEVELOPER == category &&
                    level.getCode() >= SkillLevel.MIDDLE.getCode()) hasUIDeveloperSkill = true;
        }

        return hasBackendDeveloperSkill && hasUIDeveloperSkill;
    }

    private Boolean isExpertTester(List<Skill> skills)
    {
        boolean hasDeveloperSkill = false;
        boolean hasSeniorTesterSkill = false;

        for (Skill item : skills)
        {
            SkillCategory category = SkillCategory.fromString(item.getCategory());
            SkillLevel level = SkillLevel.fromInt(item.getLevel());

            if (SkillCategory.BACKEND_DEVELOPER == category ||
                    SkillCategory.UI_DEVELOPER == category) hasDeveloperSkill = true;

            if (SkillCategory.TESTER == category &&
                    level.getCode() >= SkillLevel.SENIOR.getCode()) hasSeniorTesterSkill = true;
        }

        return hasDeveloperSkill && hasSeniorTesterSkill;
    }
}
