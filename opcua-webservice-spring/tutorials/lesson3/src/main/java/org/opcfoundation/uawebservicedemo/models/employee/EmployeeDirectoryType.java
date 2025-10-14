package org.opcfoundation.uawebservicedemo.models.employee;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Employee;
import org.opcfoundation.uawebservicedemo.database.mapper.EmployeeMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeDataManager;
import org.opcfoundation.webserver.addressspace.models.UaObjectDirectoryType;
import org.opcfoundation.webserver.types.UaChildObjectDescriptor;
import org.opcfoundation.webserver.types.message.GetObjectDirectoryChildRequest;
import org.opcfoundation.webserver.types.message.GetObjectDirectoryChildResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

// Defining employ of a department
public class EmployeeDirectoryType extends UaObjectDirectoryType {
    public EmployeeDirectoryType(EmployeeDataManager nodeManager) {
        super("EmployeeDirectoryType", new LocalizedText("员工目录类定义"), nodeManager);
    }

    // Get employee information for giving department
    @Override
    public CompletableFuture<GetObjectDirectoryChildResponse> getChildren(GetObjectDirectoryChildRequest request)
    {
        try
        {
            // Get department id from request
            Integer departmentId = Integer.parseInt(request.getObjectId().getId());

            // Database query
            EmployeeMapper employeeMapper = EmployeeDatabase.getEmployeeMapper();
            List<Employee> employees = employeeMapper.getEmployeeOfDepartment(departmentId);

            // Return general information of employee
            List<UaChildObjectDescriptor> children = new ArrayList<>();
            EmployeeType employeeType = ((EmployeeDataManager)nodeManager).employeeType;

            for (Employee item : employees)
            {
                UaChildObjectDescriptor descriptor = new UaChildObjectDescriptor(
                        item.getId().toString(),
                        new LocalizedText(item.getName()),
                        employeeType);

                children.add(descriptor);
            }

            GetObjectDirectoryChildResponse response = new GetObjectDirectoryChildResponse(children, false);
            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
