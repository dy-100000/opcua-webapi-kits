package org.opcfoundation.uawebservicedemo.models.department;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Department;
import org.opcfoundation.uawebservicedemo.database.mapper.DepartmentMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeDataManager;
import org.opcfoundation.webserver.addressspace.models.UaObjectDirectoryType;
import org.opcfoundation.webserver.types.UaChildObjectDescriptor;
import org.opcfoundation.webserver.types.message.GetObjectDirectoryChildRequest;
import org.opcfoundation.webserver.types.message.GetObjectDirectoryChildResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

// Defining department list which presents the child department
public class DepartmentDirectoryType extends UaObjectDirectoryType {
    public final String RootDepartmentDirectoryId = "Departments";

    public DepartmentDirectoryType(EmployeeDataManager nodeManager) {
        super("DepartmentDirectoryType", new LocalizedText("部门目录类定义"), nodeManager);
    }

    // Get child departments
    @Override
    public CompletableFuture<GetObjectDirectoryChildResponse> getChildren(GetObjectDirectoryChildRequest request)
    {
        try
        {
            // Get parent department id from request
            Integer parentDepartmentId;

            if (request.getObjectId().getId().equals(RootDepartmentDirectoryId))
            {
                // If is root department, parent id is null
                parentDepartmentId = null;
            } else {
                // Provide parent department id
                parentDepartmentId = Integer.parseInt(request.getObjectId().getId());
            }

            // Database query
            DepartmentMapper departmentMapper = EmployeeDatabase.getDepartmentMapper();
            List<Department> departments = departmentMapper.getChildDepartment(parentDepartmentId);

            // Return general information of child department
            List<UaChildObjectDescriptor> children = new ArrayList<>();
            DepartmentType departmentType = ((EmployeeDataManager)nodeManager).departmentType;

            for (Department item : departments)
            {
                UaChildObjectDescriptor descriptor = new UaChildObjectDescriptor(
                        item.getId().toString(), // Child id
                        new LocalizedText(item.getName()), // Child name
                        departmentType); // Child object type

                children.add(descriptor);
            }

            GetObjectDirectoryChildResponse response = new GetObjectDirectoryChildResponse(children, false);
            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
