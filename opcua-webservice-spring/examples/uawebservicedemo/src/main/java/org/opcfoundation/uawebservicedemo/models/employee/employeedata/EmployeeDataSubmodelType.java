package org.opcfoundation.uawebservicedemo.models.employee.employeedata;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.EmployeeData;
import org.opcfoundation.uawebservicedemo.database.mapper.EmployeeMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.webserver.addressspace.nodes.UaMethod;
import org.opcfoundation.webserver.addressspace.nodes.UaVariable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;
import org.opcfoundation.webserver.digitaltwin.submodel.SubmodelType;
import org.opcfoundation.webserver.service.message.digitaltwin.InvokeOperationRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.InvokeOperationResponse;
import org.opcfoundation.webserver.service.message.digitaltwin.ReadPropertyValuesRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.ReadPropertyValuesResponse;
import org.opcfoundation.webserver.types.common.UaStructureUtilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EmployeeDataSubmodelType extends SubmodelType {
    private final UaVariable startTime;
    private final UaVariable salary;

    private UaMethod getCumulatedSalary;

    public EmployeeDataSubmodelType(EmployeeTwinSpace twinSpace) {
        super("EmployeeDataSubmodelType", new LocalizedText("EmployeeDataModel"), twinSpace);

        // Add start time
        startTime = addPropertyElement(
                "StartTime",
                new LocalizedText("StartTime"),
                new LocalizedText("The date this employee start to work in this company"),
                UaDataTypes.DateTime,
                false);

        // Add salary field
        salary = addPropertyElement(
                "Salary",
                new LocalizedText("Salary"),
                new LocalizedText("Employee salary"),
                UaDataTypes.Float,
                false,
                false,
                null,
                UaVariableTypes.BaseAnalogType,
                true);

        addSubElementOfProperty(
                salary,
                "EngineeringUnits",
                UaStructureUtilities.toVariant(new EUInformation(null,-1, new LocalizedText("RMB/M"), new LocalizedText("RMB per month"))));

        addCumulatedSalaryOperation();
    }

    @Override
    public CompletableFuture<ReadPropertyValuesResponse> onReadPropertyValues(ReadPropertyValuesRequest request)
    {
        try
        {
            // Get employee id from request
            Integer employeeId = Integer.parseInt(request.getId());

            // Database query
            EmployeeMapper employeeMapper = EmployeeDatabase.getEmployeeMapper();
            EmployeeData employee = employeeMapper.getEmployeeData(employeeId);
            if (null == employee) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            // Return field data
            ReadPropertyValuesResponse response = new ReadPropertyValuesResponse();
            ZoneId zoneId = ZoneId.systemDefault();

            for (String item: request.getPropertyNames())
            {
                // Return start time if required
                if (startTime.browseName().equals(item)) {
                    DateTime time = new DateTime(employee.getStartTime().atZone(zoneId).toInstant());
                    response.setValue(item,Variant.ofDateTime(time));
                }

                // Return salary if required
                if (salary.browseName().equals(item)) {
                    response.setValue(item,Variant.ofInt32(employee.getSalary()));
                }
            }

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }

    public CompletableFuture<InvokeOperationResponse> onInvokeOperation(InvokeOperationRequest request) {
        try {
            // Get employee id from request
            Integer employeeId = Integer.parseInt(request.getId());
            List<Variant> outputArguments = new ArrayList<>();

            // Database query
            EmployeeMapper employeeMapper = EmployeeDatabase.getEmployeeMapper();
            EmployeeData employeeData = employeeMapper.getEmployeeData(employeeId);

            if (null == employeeData) throw new UaRuntimeException(StatusCodes.Bad_MethodInvalid);

            if (request.getOperationName().equals(getCumulatedSalary.browseName()))
            {
                Integer result = getCumulatedSalary(employeeData);
                outputArguments.add(Variant.ofInt32(result));
                return CompletableFuture.completedFuture(new InvokeOperationResponse(outputArguments));
            }

            throw new UaRuntimeException(StatusCodes.Bad_MethodInvalid);
        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_MethodInvalid);
        }
    }

    private void addCumulatedSalaryOperation()
    {
        List<Argument> outputArguments = new ArrayList<>();
        Argument cumulatedSalary = new Argument(
                "CumulatedSalary",
                UaDataTypes.Int32.nodeId(),
                -1,
                null,
                new LocalizedText("CumulatedSalary"));

        outputArguments.add(cumulatedSalary);

        getCumulatedSalary = addOperationElement(
                "GetCumulatedSalary",
                new LocalizedText("GetCumulatedSalary"),
                new LocalizedText("Calculate the cumulated salary the company gives to employee"),
                null,
                outputArguments,
                true);
    }

    private Integer getCumulatedSalary(EmployeeData employeeData)
    {
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

        // 2. LocalDateTime转LocalDate（Period仅处理日期，忽略时分秒）
        LocalDate startDate = employeeData.getStartTime().toLocalDate();
        LocalDate nowDate = now.toLocalDate();

        // 3. 计算年、月差值
        Period period = Period.between(startDate,nowDate);
        int years = period.getYears();
        int months = period.getMonths();

        // 4. 总月份 = 年×12 + 月（Period的差值已处理自然月，如2024-01-30到2024-02-28算1个月）
        int totalMonth = years * 12 + months;

        return employeeData.getSalary() * totalMonth;
    }
}
