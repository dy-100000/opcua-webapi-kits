package org.opcfoundation.uawebservicedemo.models.company;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Department;
import org.opcfoundation.uawebservicedemo.database.mapper.DepartmentMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.webserver.digitaltwin.digitaltwin.DigitalTwinRepositoryType;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDigitalTwinListRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDigitalTwinListResponse;
import org.opcfoundation.webserver.types.digitaltwin.DigitalTwinDescriptor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

// Defining entry of company resource
public class CompanyRepositoryType extends DigitalTwinRepositoryType {
    public CompanyRepositoryType(EmployeeTwinSpace twinSpace) {
        super("CompanyRepositoryType", new LocalizedText("CompanyRepository"), twinSpace);
    }

    @Override
    public CompletableFuture<GetDigitalTwinListResponse> onGetDigitalTwinList(GetDigitalTwinListRequest request)
    {
        try
        {
            // Database query
            DepartmentMapper departmentMapper = EmployeeDatabase.getDepartmentMapper();
            List<Department> departments = departmentMapper.getChildDepartment(null); // The department without parent id is a company

            // Return department general information
            GetDigitalTwinListResponse response = new GetDigitalTwinListResponse();
            CompanyDigitalTwinType companyDigitalTwinType = ((EmployeeTwinSpace)nodeManager).companyDigitalTwinType;

            for (Department item : departments)
            {
                DigitalTwinDescriptor descriptor = new DigitalTwinDescriptor(
                        item.getId().toString(), // CompanyId
                        new LocalizedText(item.getName()), // CompanyName
                        companyDigitalTwinType); // CompanyDigitalTwinType

                response.add(descriptor);
            }

            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
