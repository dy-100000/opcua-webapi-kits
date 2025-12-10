package org.opcfoundation.uawebservicedemo.models.employee;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Employee;
import org.opcfoundation.uawebservicedemo.database.mapper.EmployeeMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.uawebservicedemo.models.employee.employeedata.EmployeeDataSubmodelType;
import org.opcfoundation.uawebservicedemo.models.employee.personaldata.PersonalDataSubmodelType;
import org.opcfoundation.uawebservicedemo.models.employee.employeeskill.EmployeeSkillSubmodelType;
import org.opcfoundation.webserver.digitaltwin.digitaltwin.DigitalTwinType;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorResponse;

import java.util.concurrent.CompletableFuture;

// Defining employee digital twin including employee information and employee skill
public class EmployeeDigitalTwinType extends DigitalTwinType {
    public EmployeeDigitalTwinType(
            PersonalDataSubmodelType personalDataSubmodelType,
            EmployeeDataSubmodelType employeeDataSubmodelType,
            EmployeeSkillSubmodelType employeeSkillSubmodelType,
            EmployeeTwinSpace twinSpace)
    {
        super("EmployeeDigitalTwinType", new LocalizedText("Employee"),twinSpace);

        // Add personal data submodel
        addSubmodel(
                personalDataSubmodelType,
                "PersonalData",
                new LocalizedText("PersonalData"),
                new LocalizedText("Provides personal data"));

        // Add employee data submodel
        addSubmodel(
                employeeDataSubmodelType,
                "EmployeeData",
                new LocalizedText("EmployeeData"),
                new LocalizedText("Provides employee data"));

        // Add skill submodel
        addSubmodel(
                employeeSkillSubmodelType,
                "SkillInfo",
                new LocalizedText("SkillInfo"),
                new LocalizedText("Provides skill information"));
    }

    @Override
    public CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request) {
        try
        {
            Integer employeeId = Integer.parseInt(request.getId());

            EmployeeMapper employeeMapper = EmployeeDatabase.getEmployeeMapper();
            Employee employee = employeeMapper.getEmployee(employeeId);
            if (null == employee) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            GetDescriptorResponse response = new GetDescriptorResponse(
                    new LocalizedText(employee.getName()),
                    LocalizedText.NULL_VALUE);

            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
