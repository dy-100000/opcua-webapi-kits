package org.opcfoundation.uawebservicedemo.models;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.models.employee.employeedata.EmployeeDataSubmodelType;
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
    public EmployeeDataSubmodelType employeeDataSubmodelType;

    public EmployeeTwinSpace()
    {
        super("EmployeeTwinSpace");
    }

    @Override
    public void onStartUp() throws UaRuntimeException {
        // Employee definitions
        sexEnumType = new SexEnumType(this);
        addDefinition(sexEnumType);

        personalDataSubmodelType = new PersonalDataSubmodelType(sexEnumType,this);
        addDefinition(personalDataSubmodelType);

        employeeDataSubmodelType = new EmployeeDataSubmodelType(this);
        addDefinition(employeeDataSubmodelType);

        employeeDigitalTwinType = new EmployeeDigitalTwinType(personalDataSubmodelType, employeeDataSubmodelType, this);
        addDefinition(employeeDigitalTwinType);

        employeeRepositoryType = new EmployeeRepositoryType(this);
        addDefinition(employeeRepositoryType);

        // Repository
        addRepository(employeeRepositoryType, "Employees", new LocalizedText("Employees"), new LocalizedText("Provides employee information"));
    }
}
