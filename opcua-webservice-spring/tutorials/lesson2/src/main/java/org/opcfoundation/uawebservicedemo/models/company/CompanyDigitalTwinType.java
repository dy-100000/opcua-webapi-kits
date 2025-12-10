package org.opcfoundation.uawebservicedemo.models.company;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Department;
import org.opcfoundation.uawebservicedemo.database.mapper.DepartmentMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.uawebservicedemo.models.company.department.DepartmentSubmodelType;
import org.opcfoundation.webserver.digitaltwin.digitaltwin.DigitalTwinType;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CompanyDigitalTwinType extends DigitalTwinType {
    public CompanyDigitalTwinType(
            DepartmentSubmodelType departmentSubmodel,
            EmployeeTwinSpace twinSpace) {
        super("CompanyDigitalTwinType", new LocalizedText("CompanyDigitalTwin"), twinSpace);

        addSubmodel(
                departmentSubmodel,
                "Departments",
                new LocalizedText("Departments"),
                new LocalizedText("Company departments"));
    }

    public CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request)
    {
        try
        {
            // Get company id from request
            Integer companyId = Integer.parseInt(request.getId());

            // Database query
            DepartmentMapper departmentMapper = EmployeeDatabase.getDepartmentMapper();
            Department company = departmentMapper.getDepartment(companyId);

            GetDescriptorResponse response = new GetDescriptorResponse(
                    new LocalizedText(company.getName()),
                    new LocalizedText(company.getComment()));

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
