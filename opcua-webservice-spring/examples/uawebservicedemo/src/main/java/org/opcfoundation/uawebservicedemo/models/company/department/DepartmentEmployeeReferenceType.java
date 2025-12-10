package org.opcfoundation.uawebservicedemo.models.company.department;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Department;
import org.opcfoundation.uawebservicedemo.database.entity.Employee;
import org.opcfoundation.uawebservicedemo.database.mapper.DepartmentMapper;
import org.opcfoundation.uawebservicedemo.database.mapper.EmployeeMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.uawebservicedemo.models.employee.EmployeeDigitalTwinType;
import org.opcfoundation.webserver.digitaltwin.element.ReferenceElementType;
import org.opcfoundation.webserver.service.message.digitaltwin.GetLinkRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetLinkResponse;
import org.opcfoundation.webserver.types.digitaltwin.ReferenceTargetDescriptor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

// Defining employee list of department
public class DepartmentEmployeeReferenceType extends ReferenceElementType {
    public DepartmentEmployeeReferenceType(EmployeeTwinSpace twinSpace) {
        super("DepartmentEmployeeReferenceType", new LocalizedText("DepartmentEmployee"), twinSpace);
    }

    // Get employee of department
    @Override
    public CompletableFuture<GetLinkResponse> onGetLinks(GetLinkRequest request)
    {
        try
        {
            // Get department id from request
            Integer departmentId = Integer.parseInt(request.getId());

            // Database query
            EmployeeMapper employeeMapper = EmployeeDatabase.getEmployeeMapper();
            List<Employee> employees = employeeMapper.getEmployeeOfDepartment(departmentId);

            // Return department general information
            GetLinkResponse response = new GetLinkResponse();
            EmployeeDigitalTwinType employeeDigitalTwinType = ((EmployeeTwinSpace)nodeManager).employeeDigitalTwinType;

            for (Employee item : employees)
            {
                ReferenceTargetDescriptor descriptor = new ReferenceTargetDescriptor(
                        item.getId().toString(), // EmployeeId
                        new LocalizedText(item.getName()), // EmployeeName
                        employeeDigitalTwinType); // EmployeeDigitalTwinType

                response.add(descriptor);
            }

            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
