package org.opcfoundation.uawebservicedemo.models;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.models.company.CompanyDigitalTwinType;
import org.opcfoundation.uawebservicedemo.models.company.CompanyRepositoryType;
import org.opcfoundation.uawebservicedemo.models.company.department.DepartmentEmployeeReferenceType;
import org.opcfoundation.uawebservicedemo.models.company.department.DepartmentSubmodelType;
import org.opcfoundation.uawebservicedemo.models.company.department.DepartmentType;
import org.opcfoundation.uawebservicedemo.models.company.skillclassification.SkillClassificationSubmodelType;
import org.opcfoundation.uawebservicedemo.models.employee.employeedata.EmployeeAttendanceEventType;
import org.opcfoundation.uawebservicedemo.models.employee.employeedata.EmployeeDataSubmodelType;
import org.opcfoundation.uawebservicedemo.models.employee.employeedata.event.EmployeeCheckInEventType;
import org.opcfoundation.uawebservicedemo.models.employee.employeeskill.EmployeeSkillReferenceType;
import org.opcfoundation.uawebservicedemo.models.employee.EmployeeRepositoryType;
import org.opcfoundation.uawebservicedemo.models.employee.employeeskill.EmployeeSkillSubmodelType;
import org.opcfoundation.uawebservicedemo.models.employee.personaldata.PersonalDataSubmodelType;
import org.opcfoundation.uawebservicedemo.models.employee.EmployeeDigitalTwinType;
import org.opcfoundation.uawebservicedemo.models.employee.personaldata.enumeration.SexEnumType;
import org.opcfoundation.uawebservicedemo.models.company.skillclassification.SkillClassType;
import org.opcfoundation.uawebservicedemo.models.company.skillclassification.enumeration.SkillCategoryEnumType;
import org.opcfoundation.uawebservicedemo.models.company.skillclassification.enumeration.SkillLevelEnumType;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;

public class EmployeeTwinSpace extends DigitalTwinSpace {
    // Employee definitions
    public SexEnumType sexEnumType;
    public EmployeeRepositoryType employeeRepositoryType;
    public EmployeeDigitalTwinType employeeDigitalTwinType;
    public PersonalDataSubmodelType personalDataSubmodelType;

    public EmployeeCheckInEventType employeeCheckInEventType;
    public EmployeeDataSubmodelType employeeDataSubmodelType;
    public EmployeeAttendanceEventType employeeAttendanceEventType;

    public EmployeeSkillSubmodelType employeeSkillSubmodelType;
    public EmployeeSkillReferenceType employeeSkillReferenceType;

    // Department definitions
    public DepartmentSubmodelType departmentSubmodelType;
    public DepartmentType departmentType;
    public DepartmentEmployeeReferenceType departmentEmployeeReferenceType;

    // Skill definitions
    public SkillLevelEnumType skillLevelEnumType;
    public SkillCategoryEnumType skillCategoryEnumType;
    public SkillClassificationSubmodelType skillClassificationSubmodelType;
    public SkillClassType skillClassType;

    // Company definition
    public CompanyRepositoryType companyRepositoryType;
    public CompanyDigitalTwinType companyDigitalTwinType;

    public EmployeeTwinSpace()
    {
        super("EmployeeTwinSpace");
    }

    @Override
    public void onStartUp() throws UaRuntimeException {
        // Employee
        sexEnumType = new SexEnumType(this);
        employeeSkillReferenceType = new EmployeeSkillReferenceType(this);
        employeeSkillSubmodelType = new org.opcfoundation.uawebservicedemo.models.employee.employeeskill.EmployeeSkillSubmodelType(employeeSkillReferenceType,this);

        personalDataSubmodelType = new PersonalDataSubmodelType(sexEnumType,this);

        employeeCheckInEventType = new EmployeeCheckInEventType(this);
        employeeAttendanceEventType = new EmployeeAttendanceEventType(employeeCheckInEventType, this);
        employeeDataSubmodelType = new EmployeeDataSubmodelType(employeeAttendanceEventType,this);
        employeeDigitalTwinType = new EmployeeDigitalTwinType(personalDataSubmodelType, employeeDataSubmodelType, employeeSkillSubmodelType, this);
        employeeRepositoryType = new EmployeeRepositoryType(this);

        // Department
        departmentEmployeeReferenceType = new DepartmentEmployeeReferenceType(this);
        departmentType = new DepartmentType(departmentEmployeeReferenceType, this);
        departmentSubmodelType = new DepartmentSubmodelType(this);

        // Person skill
        skillLevelEnumType = new SkillLevelEnumType(this);
        skillCategoryEnumType = new SkillCategoryEnumType(this);
        skillClassType = new SkillClassType(skillLevelEnumType, skillCategoryEnumType, this);
        skillClassificationSubmodelType = new SkillClassificationSubmodelType(this);

        // Company resource
        companyDigitalTwinType = new CompanyDigitalTwinType(departmentSubmodelType, skillClassificationSubmodelType,this);
        companyRepositoryType = new CompanyRepositoryType(this);

        // Repository
        addRepository(employeeRepositoryType, "Employees", new LocalizedText("Employees"), new LocalizedText("Provides employee information"));
        addRepository(companyRepositoryType, "Companies", new LocalizedText("Companies"), new LocalizedText("Provides company information"));
    }
}
