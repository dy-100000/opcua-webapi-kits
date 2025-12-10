package org.opcfoundation.uawebservicedemo.models;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.models.company.CompanyDigitalTwinType;
import org.opcfoundation.uawebservicedemo.models.company.CompanyDirectoryType;
import org.opcfoundation.uawebservicedemo.models.company.department.DepartmentEmployeeReferenceType;
import org.opcfoundation.uawebservicedemo.models.company.department.DepartmentSubmodelType;
import org.opcfoundation.uawebservicedemo.models.company.department.DepartmentType;
import org.opcfoundation.uawebservicedemo.models.employee.employeedata.EmployeeDataSubmodelType;
import org.opcfoundation.uawebservicedemo.models.employee.EmployeeDirectoryType;
import org.opcfoundation.uawebservicedemo.models.employee.personaldata.PersonalDataSubmodelType;
import org.opcfoundation.uawebservicedemo.models.employee.EmployeeDigitalTwinType;
import org.opcfoundation.uawebservicedemo.models.employee.personaldata.enumeration.SexEnumType;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;

public class EmployeeTwinSpace extends DigitalTwinSpace {
    // Employee definitions
    public SexEnumType sexEnumType;
    public EmployeeDirectoryType employeeDirectoryType;
    public EmployeeDigitalTwinType employeeDigitalTwinType;
    public PersonalDataSubmodelType personalDataSubmodelType;
    public EmployeeDataSubmodelType employeeDataSubmodelType;

    // Department definitions
    public DepartmentSubmodelType departmentSubmodelType;
    public DepartmentType departmentType;
    public DepartmentEmployeeReferenceType departmentEmployeeReferenceType;

    // Company definition
    public CompanyDirectoryType companyDirectoryType;
    public CompanyDigitalTwinType companyDigitalTwinType;

    public EmployeeTwinSpace()
    {
        super("EmployeeDataManager");
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

        employeeDirectoryType = new EmployeeDirectoryType(this);
        addDefinition(employeeDirectoryType);

        // Department
        departmentEmployeeReferenceType = new DepartmentEmployeeReferenceType(this);
        addDefinition(departmentEmployeeReferenceType);

        departmentType = new DepartmentType(departmentEmployeeReferenceType, this);
        addDefinition(departmentType);

        departmentSubmodelType = new DepartmentSubmodelType(this);
        addDefinition(departmentSubmodelType);

        // Company resource
        companyDigitalTwinType = new CompanyDigitalTwinType(departmentSubmodelType, this);
        addDefinition(companyDigitalTwinType);

        companyDirectoryType = new CompanyDirectoryType(this);
        addDefinition(companyDirectoryType);

        // Entry points
        addEntryPoint(employeeDirectoryType, "Employees", new LocalizedText("Employees"), new LocalizedText("Provides employee information"));
        addEntryPoint(companyDirectoryType, "Companies", new LocalizedText("Companies"), new LocalizedText("Provides company information"));
    }
}
