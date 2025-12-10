package org.opcfoundation.uawebservicedemo.models.employee.employeeskill;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Skill;
import org.opcfoundation.uawebservicedemo.database.mapper.SkillMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.uawebservicedemo.models.company.skillclassification.enumeration.SkillCategory;
import org.opcfoundation.uawebservicedemo.models.company.skillclassification.enumeration.SkillLevel;
import org.opcfoundation.webserver.addressspace.nodes.UaMethod;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.digitaltwin.submodel.SubmodelType;
import org.opcfoundation.webserver.service.message.digitaltwin.InvokeOperationRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.InvokeOperationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EmployeeSkillSubmodelType extends SubmodelType {
    private UaMethod fullStackDeveloper;
    private UaMethod expertTester;

    public EmployeeSkillSubmodelType(EmployeeSkillReferenceType employeeSkillReferenceType,  EmployeeTwinSpace twinSpace) {
        super("EmployeeSkillSubmodelType", new LocalizedText("EmployeeSkillModel"), twinSpace);

        addReferenceElement(
                employeeSkillReferenceType,
                "Skills",
                new LocalizedText("Skills"),
                new LocalizedText("Skills of employee"),
                true);

        addFullStackDeveloperMethod();
        addHighLevelTesterMethod();
    }

    public CompletableFuture<InvokeOperationResponse> onInvokeOperation(InvokeOperationRequest request) {
        try {
            // Get employee id from request
            Integer employeeId = Integer.parseInt(request.getId());
            List<Variant> outputArguments = new ArrayList<>();

            // Database query
            SkillMapper shillMapper = EmployeeDatabase.getSkillMapper();
            List<Skill> skills = shillMapper.getSkillDataOfEmployee(employeeId);

            if (request.getOperationName().equals(fullStackDeveloper.browseName()))
            {
                Boolean result = isFullStackDeveloper(skills);
                outputArguments.add(Variant.ofBoolean(result));
                return CompletableFuture.completedFuture(new InvokeOperationResponse(outputArguments));

            } else if (request.getOperationName().equals(expertTester.browseName())) {
                Boolean result = isExpertTester(skills);
                outputArguments.add(Variant.ofBoolean(result));
                return CompletableFuture.completedFuture(new InvokeOperationResponse(outputArguments));
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

        fullStackDeveloper = addOperationElement(
                "FullStackDeveloper",
                 new LocalizedText("IsFullStackDeveloper"),
                new LocalizedText("If this person possess full stack developer skills"),
                null,
                outputArguments,
                true);
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

        expertTester = addOperationElement(
                "ExpertTester",
                new LocalizedText("IsExpertTester"),
                new LocalizedText("If this person possess expert tester skills"),
                null,
                outputArguments,
                true);
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
