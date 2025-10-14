package org.opcfoundation.uawebservicedemo.models.employee;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.EmployeeInfo;
import org.opcfoundation.uawebservicedemo.database.mapper.EmployeeMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeDataManager;
import org.opcfoundation.uawebservicedemo.models.employee.enumeration.Sex;
import org.opcfoundation.uawebservicedemo.models.employee.enumeration.SexEnumType;
import org.opcfoundation.webserver.addressspace.models.UaDataObjectType;
import org.opcfoundation.webserver.addressspace.nodes.UaVariable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.types.UaChildId;
import org.opcfoundation.webserver.types.message.ReadVariableValueRequest;
import org.opcfoundation.webserver.types.message.ReadVariableValueResponse;

import java.time.ZoneId;
import java.util.concurrent.CompletableFuture;

public class EmployeeInfoType extends UaDataObjectType {
    private final UaVariable sex;
    private final UaVariable birthday;
    private final UaVariable phoneNumber;
    private final UaVariable address;

    public EmployeeInfoType(
            SexEnumType sexEnumType,
            EmployeeDataManager nodeManager) {
        super("EmployeeInfoType", new LocalizedText("员工信息类定义"), null,nodeManager);

        // Add sex field
        sex = addVariable(
                "Sex",
                true,
                new LocalizedText("Sex"),
                new LocalizedText("Sex of person"),
                sexEnumType,
                false,
                false,
                null,
                null);

        // Add birthday field
        birthday = addVariable(
                "Birthday",
                true,
                new LocalizedText("Birthday"),
                new LocalizedText("Birthday of person"),
                UaDataTypes.DateTime,
                false,
                false,
                null,
                null);

        // Add PhoneNumber field
        phoneNumber = addVariable(
                "PhoneNumber",
                true,
                new LocalizedText("PhoneNumber"),
                new LocalizedText("PhoneNumber of person"),
                UaDataTypes.String,
                false,
                false,
                null,
                null);

        // Add Address field
        address = addVariable(
                "Address",
                true,
                new LocalizedText("Address"),
                new LocalizedText("Address of person"),
                UaDataTypes.String,
                false,
                false,
                null,
                null);
    }

    @Override
    public CompletableFuture<ReadVariableValueResponse> getVariableValues(ReadVariableValueRequest request)
    {
        try
        {
            // Get employee id from request
            Integer employeeId = Integer.parseInt(request.getObjectId().getId());

            // Database query
            EmployeeMapper employeeMapper = EmployeeDatabase.getEmployeeMapper();
            EmployeeInfo employee = employeeMapper.getEmployeeInfo(employeeId);
            if (null == employee) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            // Return field data
            ReadVariableValueResponse response = new ReadVariableValueResponse();
            ZoneId zoneId = ZoneId.systemDefault();

            for (UaChildId item: request.getVariableIds())
            {
                // Return sex if required
                if (sex.browseName().equals(item.getPathId()))
                {
                    Sex sex = Sex.fromInt(employee.getSex());
                    response.setValue(item, Variant.ofInt32(sex.getCode()));
                }

                // Return birthday if required
                if (birthday.browseName().equals(item.getPathId())) {
                    DateTime time = new DateTime(employee.getBirthday().atZone(zoneId).toInstant());
                    response.setValue(item, Variant.ofDateTime(time));
                }

                // Return phoneNumber if required
                if (phoneNumber.browseName().equals(item.getPathId())) {
                    response.setValue(item,Variant.ofString(employee.getPhoneNumber()));
                }

                // Return address if required
                if (address.browseName().equals(item.getPathId())) {
                    response.setValue(item,Variant.ofString(employee.getAddress()));
                }
            }

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
