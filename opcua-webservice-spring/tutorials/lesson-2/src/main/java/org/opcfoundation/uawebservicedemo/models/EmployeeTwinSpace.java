package org.opcfoundation.uawebservicedemo.models;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.models.company.CompanyDigitalTwinType;
import org.opcfoundation.uawebservicedemo.models.company.CompanyRepositoryType;
import org.opcfoundation.uawebservicedemo.models.company.department.DepartmentEmployeeReferenceType;
import org.opcfoundation.uawebservicedemo.models.company.department.DepartmentSubmodelType;
import org.opcfoundation.uawebservicedemo.models.company.department.DepartmentType;
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

    // Department definitions
    public DepartmentSubmodelType departmentSubmodelType;
    public DepartmentType departmentType;
    public DepartmentEmployeeReferenceType departmentEmployeeReferenceType;

    // Company definition
    public CompanyRepositoryType companyRepositoryType;
    public CompanyDigitalTwinType companyDigitalTwinType;

    public EmployeeTwinSpace()
    {
        super("EmployeeTwinSpace");
    }

    @Override
    public void onStartUp() throws UaRuntimeException {
        // Employee definitions
        sexEnumType = new SexEnumType(this);
        personalDataSubmodelType = new PersonalDataSubmodelType(sexEnumType,this);
        employeeDataSubmodelType = new EmployeeDataSubmodelType(this);
        employeeDigitalTwinType = new EmployeeDigitalTwinType(personalDataSubmodelType, employeeDataSubmodelType, this);
        employeeRepositoryType = new EmployeeRepositoryType(this);

        // Department
        departmentEmployeeReferenceType = new DepartmentEmployeeReferenceType(this);
        departmentType = new DepartmentType(departmentEmployeeReferenceType, this);
        departmentSubmodelType = new DepartmentSubmodelType(this);

        // Company resource
        companyDigitalTwinType = new CompanyDigitalTwinType(departmentSubmodelType, this);
        companyRepositoryType = new CompanyRepositoryType(this);

        // Entry points
        addRepository(employeeRepositoryType, "Employees", new LocalizedText("Employees"), new LocalizedText("Provides employee information"));
        addRepository(companyRepositoryType, "Companies", new LocalizedText("Companies"), new LocalizedText("Provides company information"));
    }
}
