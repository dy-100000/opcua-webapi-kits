package org.opcfoundation.uawebservicedemo.models.employee;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Department;
import org.opcfoundation.uawebservicedemo.database.entity.Employee;
import org.opcfoundation.uawebservicedemo.database.mapper.DepartmentMapper;
import org.opcfoundation.uawebservicedemo.database.mapper.EmployeeMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeDataManager;
import org.opcfoundation.uawebservicedemo.models.department.DepartmentType;
import org.opcfoundation.uawebservicedemo.models.skill.SkillDirectoryType;
import org.opcfoundation.webserver.addressspace.models.UaMasterObjectType;
import org.opcfoundation.webserver.types.message.ReadObjectAttributeRequest;
import org.opcfoundation.webserver.types.message.ReadObjectAttributeResponse;
import org.opcfoundation.webserver.types.message.GetParentObjectRequest;
import org.opcfoundation.webserver.types.message.GetParentObjectResponse;

import java.util.concurrent.CompletableFuture;

public class EmployeeType extends UaMasterObjectType {
    public EmployeeType(
            EmployeeInfoType employeeInfoType,
            SkillDirectoryType skillDirectoryType,
            EmployeeCompetenceType employeeCompetenceType,
            EmployeeDataManager nodeManager)
    {
        super("EmployeeType", new LocalizedText("员工类定义"), null,nodeManager);

        // Add employee o to get child department
        addSubmodel(
                "Information",
                true,
                new LocalizedText("基本信息"),
                new LocalizedText("提供员工的基本信息"),
                employeeInfoType);

        // Add skill directory to get child department
        addSubmodel(
                "Skill",
                true,
                new LocalizedText("技能"),
                new LocalizedText("员工技能列表"),
                skillDirectoryType);

        // Add EmployeeCompetenceType to get employee competence
        addSubmodel(
                "Competence",
                true,
                new LocalizedText("能力"),
                new LocalizedText("判断员工是否具备相应的技术能力"),
                employeeCompetenceType);
    }

    @Override
    public CompletableFuture<ReadObjectAttributeResponse> getObjectAttribute(ReadObjectAttributeRequest request) {
        try
        {
            Integer employeeId = Integer.parseInt(request.getObjectId().getId());

            EmployeeMapper employeeMapper = EmployeeDatabase.getEmployeeMapper();
            Employee employee = employeeMapper.getEmployee(employeeId);
            if (null == employee) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            ReadObjectAttributeResponse response = new ReadObjectAttributeResponse(
                    new LocalizedText(employee.getName()),
                    LocalizedText.NULL_VALUE);

            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }

    @Override
    public boolean isGetParentSupported()
    {
        return true;
    }

    @Override
    public CompletableFuture<GetParentObjectResponse> getParent(GetParentObjectRequest request) {
        try
        {
            Integer employeeId = Integer.parseInt(request.getObjectId().getId());

            DepartmentMapper departmentMapper = EmployeeDatabase.getDepartmentMapper();
            Department department = departmentMapper.getDepartmentByEmployee(employeeId);

            DepartmentType departmentType = ((EmployeeDataManager)nodeManager).departmentType;

            if (null == department) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            GetParentObjectResponse response = new GetParentObjectResponse(
                    department.getId().toString(),
                    new LocalizedText(department.getName()),
                    departmentType);

            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            return CompletableFuture.completedFuture(new GetParentObjectResponse()); // Return response with empty parent
        }
    }
}
