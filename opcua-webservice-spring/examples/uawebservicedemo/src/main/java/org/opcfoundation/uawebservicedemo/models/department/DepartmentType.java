package org.opcfoundation.uawebservicedemo.models.department;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Department;
import org.opcfoundation.uawebservicedemo.database.mapper.DepartmentMapper;
import org.opcfoundation.uawebservicedemo.models.employee.EmployeeDirectoryType;
import org.opcfoundation.uawebservicedemo.models.EmployeeDataManager;
import org.opcfoundation.uawebservicedemo.models.project.ProjectDirectoryType;
import org.opcfoundation.webserver.addressspace.models.UaMasterObjectType;
import org.opcfoundation.webserver.types.message.ReadObjectAttributeRequest;
import org.opcfoundation.webserver.types.message.ReadObjectAttributeResponse;

import java.util.concurrent.CompletableFuture;

// Defining a department master object which include child department
// employee information, and project data
public class DepartmentType extends UaMasterObjectType {

    public DepartmentType(
            DepartmentDirectoryType departmentDirectoryType,
            EmployeeDirectoryType employeeDirectoryType,
            ProjectDirectoryType projectDirectoryType,
            EmployeeDataManager nodeManager)
    {
        super("DepartmentType", new LocalizedText("部门类定义"), null,nodeManager);

        // Add department directory to get child department
        addSubmodel(
                "Departments",
                true,
                new LocalizedText("部门"),
                new LocalizedText("归属该部门管理的下级部门"),
                departmentDirectoryType);

        // Add employee directory to get employees of department
        addSubmodel(
                "Employees",
                true,
                new LocalizedText("员工"),
                new LocalizedText("归属该部门管理的员工"),
                employeeDirectoryType);

        // Add project directory to get project of department
        addSubmodel(
                "Projects",
                true,
                new LocalizedText("项目"),
                new LocalizedText("归属该部门的项目"),
                projectDirectoryType);
    }

    // Return department general information such as name, description
    public CompletableFuture<ReadObjectAttributeResponse> getObjectAttribute(ReadObjectAttributeRequest request) {
        try
        {
            // Get department id from request
            Integer departmentId = Integer.parseInt(request.getObjectId().getId());

            // Database query
            DepartmentMapper departmentMapper = EmployeeDatabase.getDepartmentMapper();
            Department department = departmentMapper.getDepartment(departmentId);
            if (null == department) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            // Return department general information
            ReadObjectAttributeResponse response = new ReadObjectAttributeResponse(
                    new LocalizedText(department.getName()), // Object name
                    new LocalizedText(department.getComment())); // Object description

            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
