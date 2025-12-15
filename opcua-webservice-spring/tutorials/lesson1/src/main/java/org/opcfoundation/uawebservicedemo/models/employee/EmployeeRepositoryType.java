package org.opcfoundation.uawebservicedemo.models.employee;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Employee;
import org.opcfoundation.uawebservicedemo.database.mapper.EmployeeMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.webserver.digitaltwin.digitaltwin.DigitalTwinRepositoryType;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDigitalTwinListRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDigitalTwinListResponse;
import org.opcfoundation.webserver.types.digitaltwin.DigitalTwinDescriptor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

// Defining entry of employee list
public class EmployeeRepositoryType extends DigitalTwinRepositoryType {
    public EmployeeRepositoryType(EmployeeTwinSpace twinSpace) {
        super("EmployeeRepositoryType", new LocalizedText("EmployeeRepository"), twinSpace);
    }

    @Override
    public CompletableFuture<GetDigitalTwinListResponse> onGetDigitalTwinList(GetDigitalTwinListRequest request)
    {
        try
        {
            // Database query
            EmployeeMapper employeeMapper = EmployeeDatabase.getEmployeeMapper();
            List<Employee> employees = employeeMapper.getAllEmployee();

            // Return general information of employee
            GetDigitalTwinListResponse response = new GetDigitalTwinListResponse();
            EmployeeDigitalTwinType employeeDigitalTwinType = ((EmployeeTwinSpace)nodeManager).employeeDigitalTwinType;

            for (Employee item : employees)
            {
                DigitalTwinDescriptor descriptor = new DigitalTwinDescriptor(
                        item.getId().toString(),
                        new LocalizedText(item.getName()),
                        employeeDigitalTwinType);

                response.add(descriptor);
            }

            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_ResourceUnavailable);
        }
    }
}
