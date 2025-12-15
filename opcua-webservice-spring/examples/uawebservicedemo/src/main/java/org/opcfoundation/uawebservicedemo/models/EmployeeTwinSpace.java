package org.opcfoundation.uawebservicedemo.models;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.models.company.CompanyDigitalTwinType;
import org.opcfoundation.uawebservicedemo.models.company.CompanyRepositoryType;
import org.opcfoundation.uawebservicedemo.models.company.department.DepartmentEmployeeReferenceType;
import org.opcfoundation.uawebservicedemo.models.company.department.DepartmentSubmodelType;
import org.opcfoundation.uawebservicedemo.models.company.department.DepartmentType;
import org.opcfoundation.uawebservicedemo.models.company.skillclassification.SkillClassificationSubmodelType;
import org.opcfoundation.uawebservicedemo.models.employee.employeedata.EmployeeDataSubmodelType;
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
    public EmployeeDataSubmodelType employeeDataSubmodelType;
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
        // Employee definitions
        sexEnumType = new SexEnumType(this);
        addDefinition(sexEnumType);

        employeeSkillReferenceType = new EmployeeSkillReferenceType(this);
        addDefinition(employeeSkillReferenceType);

        employeeSkillSubmodelType = new org.opcfoundation.uawebservicedemo.models.employee.employeeskill.EmployeeSkillSubmodelType(employeeSkillReferenceType,this);
        addDefinition(employeeSkillSubmodelType);

        personalDataSubmodelType = new PersonalDataSubmodelType(sexEnumType,this);
        addDefinition(personalDataSubmodelType);

        employeeDataSubmodelType = new EmployeeDataSubmodelType(this);
        addDefinition(employeeDataSubmodelType);

        employeeDigitalTwinType = new EmployeeDigitalTwinType(personalDataSubmodelType, employeeDataSubmodelType, employeeSkillSubmodelType, this);
        addDefinition(employeeDigitalTwinType);

        employeeRepositoryType = new EmployeeRepositoryType(this);
        addDefinition(employeeRepositoryType);

        // Department
        departmentEmployeeReferenceType = new DepartmentEmployeeReferenceType(this);
        addDefinition(departmentEmployeeReferenceType);

        departmentType = new DepartmentType(departmentEmployeeReferenceType, this);
        addDefinition(departmentType);

        departmentSubmodelType = new DepartmentSubmodelType(this);
        addDefinition(departmentSubmodelType);

        // Person skill
        skillLevelEnumType = new SkillLevelEnumType(this);
        addDefinition(skillLevelEnumType);

        skillCategoryEnumType = new SkillCategoryEnumType(this);
        addDefinition(skillCategoryEnumType);

        skillClassType = new SkillClassType(skillLevelEnumType, skillCategoryEnumType, this);
        addDefinition(skillClassType);

        skillClassificationSubmodelType = new SkillClassificationSubmodelType(this);
        addDefinition(skillClassificationSubmodelType);

        // Company resource
        companyDigitalTwinType = new CompanyDigitalTwinType(departmentSubmodelType, skillClassificationSubmodelType,this);
        addDefinition(companyDigitalTwinType);

        companyRepositoryType = new CompanyRepositoryType(this);
        addDefinition(companyRepositoryType);

        // Repository
        addRepository(employeeRepositoryType, "Employees", new LocalizedText("Employees"), new LocalizedText("Provides employee information"));
        addRepository(companyRepositoryType, "Companies", new LocalizedText("Companies"), new LocalizedText("Provides company information"));
    }
}
