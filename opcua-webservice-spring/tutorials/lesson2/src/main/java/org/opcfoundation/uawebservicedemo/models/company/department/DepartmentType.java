package org.opcfoundation.uawebservicedemo.models.company.department;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Department;
import org.opcfoundation.uawebservicedemo.database.mapper.DepartmentMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.webserver.digitaltwin.element.ElementCollectionType;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorResponse;

import java.util.concurrent.CompletableFuture;

// Defining department including child department and employee
public class DepartmentType extends ElementCollectionType {
    public DepartmentType(
            DepartmentEmployeeReferenceType departmentEmployeeReferenceType,
            EmployeeTwinSpace twinSpace) {
        super("DepartmentType", new LocalizedText("Department"), twinSpace);

        addReferenceElement(
                departmentEmployeeReferenceType,
                "Employees",
                new LocalizedText("Employees"),
                new LocalizedText("Employees Of department"),
                true);
    }

    public CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request)
    {
        try
        {
            // Get department id from request
            Integer departmentId = Integer.parseInt(request.getId());

            // Database query
            DepartmentMapper departmentMapper = EmployeeDatabase.getDepartmentMapper();
            Department company = departmentMapper.getDepartment(departmentId);

            GetDescriptorResponse response = new GetDescriptorResponse(
                    new LocalizedText(company.getName()),
                    new LocalizedText(company.getComment()));

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
