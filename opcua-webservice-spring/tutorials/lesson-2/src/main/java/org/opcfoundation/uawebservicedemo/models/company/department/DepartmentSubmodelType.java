package org.opcfoundation.uawebservicedemo.models.company.department;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Department;
import org.opcfoundation.uawebservicedemo.database.mapper.DepartmentMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.webserver.digitaltwin.submodel.DynamicSubmodelType;
import org.opcfoundation.webserver.service.message.digitaltwin.GetObjectElementListRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetObjectElementListResponse;
import org.opcfoundation.webserver.types.digitaltwin.ObjectElementDescriptor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

// Defining department submodel including list of highest level department
public class DepartmentSubmodelType extends DynamicSubmodelType {

    public DepartmentSubmodelType(EmployeeTwinSpace twinSpace)
    {
        super("DepartmentSubmodelType", new LocalizedText("DepartmentModel"), twinSpace);
    }

    // Return department general information such as name, description
    public CompletableFuture<GetObjectElementListResponse> onGetObjectElementList(GetObjectElementListRequest request) {
        try
        {
            // Get department id from request
            Integer departmentId = Integer.parseInt(request.getId());

            // Database query
            DepartmentMapper departmentMapper = EmployeeDatabase.getDepartmentMapper();
            List<Department> departments = departmentMapper.getChildDepartment(departmentId);

            // Return department general information
            GetObjectElementListResponse response = new GetObjectElementListResponse();
            DepartmentType departmentType = ((EmployeeTwinSpace)nodeManager).departmentType;

            for (Department item : departments)
            {
                ObjectElementDescriptor descriptor = new ObjectElementDescriptor(
                        item.getId().toString(), // DepartmentId
                        new LocalizedText(item.getName()), // DepartmentName
                        departmentType); // DepartmentType

                response.add(descriptor);
            }

            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
