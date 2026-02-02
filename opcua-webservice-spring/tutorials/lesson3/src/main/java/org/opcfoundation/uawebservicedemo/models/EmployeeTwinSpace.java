package org.opcfoundation.uawebservicedemo.models;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.models.employee.employeedata.EmployeeAttendanceElementType;
import org.opcfoundation.uawebservicedemo.models.employee.employeedata.EmployeeDataSubmodelType;
import org.opcfoundation.uawebservicedemo.models.employee.employeedata.event.EmployeeCheckInEventType;
import org.opcfoundation.uawebservicedemo.models.employee.EmployeeRepositoryType;
import org.opcfoundation.uawebservicedemo.models.employee.personaldata.PersonalDataSubmodelType;
import org.opcfoundation.uawebservicedemo.models.employee.EmployeeDigitalTwinType;
import org.opcfoundation.uawebservicedemo.models.employee.personaldata.enumeration.SexEnumType;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;

public class EmployeeTwinSpace extends DigitalTwinSpace {
    // Employee definitions
    public SexEnumType sexEnumType;
    public EmployeeRepositoryType employeeRepositoryType;
    public EmployeeDigitalTwinType employeeDigitalTwinType;
    public PersonalDataSubmodelType personalDataSubmodelType;

    public EmployeeCheckInEventType employeeCheckInEventType;
    public EmployeeDataSubmodelType employeeDataSubmodelType;
    public EmployeeAttendanceElementType employeeAttendanceElementType;

    public EmployeeTwinSpace()
    {
        super("EmployeeTwinSpace");
    }

    @Override
    public void onStartUp() throws UaRuntimeException {
        // Employee
        sexEnumType = new SexEnumType(this);
        personalDataSubmodelType = new PersonalDataSubmodelType(sexEnumType,this);

        employeeCheckInEventType = new EmployeeCheckInEventType(this);
        employeeAttendanceElementType = new EmployeeAttendanceElementType(employeeCheckInEventType, this);
        employeeDataSubmodelType = new EmployeeDataSubmodelType(employeeAttendanceElementType,this);
        employeeDigitalTwinType = new EmployeeDigitalTwinType(personalDataSubmodelType, employeeDataSubmodelType, this);
        employeeRepositoryType = new EmployeeRepositoryType(this);

        // Repository
        addRepository(employeeRepositoryType, "Employees", new LocalizedText("Employees"), new LocalizedText("Provides employee information"));
    }
}
