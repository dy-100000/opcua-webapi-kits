package org.opcfoundation.uawebservicedemo.models.employee.personaldata;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.EmployeeData;
import org.opcfoundation.uawebservicedemo.database.mapper.EmployeeMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.uawebservicedemo.models.employee.personaldata.enumeration.Sex;
import org.opcfoundation.uawebservicedemo.models.employee.personaldata.enumeration.SexEnumType;
import org.opcfoundation.webserver.addressspace.nodes.UaVariable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.digitaltwin.submodel.SubmodelType;
import org.opcfoundation.webserver.service.message.digitaltwin.ReadPropertyValuesRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.ReadPropertyValuesResponse;

import java.time.ZoneId;
import java.util.concurrent.CompletableFuture;

// Defining employee information include personal data and company related information
public class PersonalDataSubmodelType extends SubmodelType {
    private final UaVariable sex;
    private final UaVariable birthday;
    private final UaVariable phoneNumber;
    private final UaVariable address;

    public PersonalDataSubmodelType(
            SexEnumType sexEnumType,
            EmployeeTwinSpace twinSpace) {
        super("PersonalDataSubmodelType", new LocalizedText("PersonalDataModel"), twinSpace);

        // Add sex field
        sex = addPropertyElement(
                "Sex",
                new LocalizedText("Sex"),
                new LocalizedText("Sex of person"),
                sexEnumType,
                false);

        // Add birthday field
        birthday = addPropertyElement(
                "Birthday",
                new LocalizedText("Birthday"),
                new LocalizedText("Birthday of person"),
                UaDataTypes.DateTime,
                false);

        // Add PhoneNumber field
        phoneNumber = addPropertyElement(
                "PhoneNumber",
                new LocalizedText("PhoneNumber"),
                new LocalizedText("PhoneNumber of person"),
                UaDataTypes.String,
                false);

        // Add Address field
        address = addPropertyElement(
                "Address",
                new LocalizedText("Address"),
                new LocalizedText("Address of person"),
                UaDataTypes.String,
                false);
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
            Sex sexValue = Sex.fromInt(employee.getSex());
            DateTime time = new DateTime(employee.getBirthday().atZone(zoneId).toInstant());

            response.setValue(sex.name(), Variant.ofInt32(sexValue.getCode()));
            response.setValue(birthday.name(), Variant.ofDateTime(time));
            response.setValue(phoneNumber.name(),Variant.ofString(employee.getPhoneNumber()));
            response.setValue(address.name(),Variant.ofString(employee.getAddress()));

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
