package org.opcfoundation.uawebservicedemo.models;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.opcfoundation.uawebservicedemo.models.department.DepartmentDirectoryType;
import org.opcfoundation.uawebservicedemo.models.department.DepartmentType;
import org.opcfoundation.uawebservicedemo.models.employee.EmployeeCompetenceType;
import org.opcfoundation.uawebservicedemo.models.employee.EmployeeDirectoryType;
import org.opcfoundation.uawebservicedemo.models.employee.EmployeeInfoType;
import org.opcfoundation.uawebservicedemo.models.employee.EmployeeType;
import org.opcfoundation.uawebservicedemo.models.employee.enumeration.SexEnumType;
import org.opcfoundation.uawebservicedemo.models.project.ProjectDirectoryType;
import org.opcfoundation.uawebservicedemo.models.project.ProjectType;
import org.opcfoundation.uawebservicedemo.models.skill.SkillDirectoryType;
import org.opcfoundation.uawebservicedemo.models.skill.SkillType;
import org.opcfoundation.uawebservicedemo.models.skill.enumeration.SkillCategoryEnumType;
import org.opcfoundation.uawebservicedemo.models.skill.enumeration.SkillLevelEnumType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerWebService;

public class EmployeeDataManager extends NodeManagerWebService {
    // Project definitions
    public ProjectType projectType;
    public ProjectDirectoryType projectDirectoryType;

    // Department definitions
    public DepartmentType departmentType;
    public DepartmentDirectoryType departmentDirectoryType;

    // Skill definitions
    public SkillLevelEnumType skillLevelEnumType;
    public SkillCategoryEnumType skillCategoryEnumType;
    public SkillType skillType;
    public SkillDirectoryType skillDirectoryType;

    // Employee definitions
    public SexEnumType sexEnumType;
    public EmployeeType employeeType;
    public EmployeeInfoType employeeInfoType;
    public EmployeeDirectoryType employeeDirectoryType;
    public EmployeeCompetenceType employeeCompetenceType;

    public EmployeeDataManager()
    {
        super("EmployeeDataManager");
    }

    @Override
    public void onStartUp() throws UaRuntimeException {
        // Create all types
        skillLevelEnumType = new SkillLevelEnumType(this);
        addDataType(skillLevelEnumType);

        skillCategoryEnumType = new SkillCategoryEnumType(this);
        addDataType(skillCategoryEnumType);

        skillType = new SkillType(skillLevelEnumType, skillCategoryEnumType, this);
        addObjectType(skillType);

        skillDirectoryType = new SkillDirectoryType(this);
        addObjectType(skillDirectoryType);

        sexEnumType = new SexEnumType(this);
        addDataType(sexEnumType);

        employeeInfoType = new EmployeeInfoType(sexEnumType, this);
        addObjectType(employeeInfoType);

        employeeDirectoryType = new EmployeeDirectoryType(this);
        addObjectType(employeeDirectoryType);

        employeeCompetenceType = new EmployeeCompetenceType(this);
        addObjectType(employeeCompetenceType);

        employeeType = new EmployeeType(employeeInfoType, skillDirectoryType,employeeCompetenceType,this);
        addObjectType(employeeType);

        projectDirectoryType = new ProjectDirectoryType(this);
        addObjectType(projectDirectoryType);

        projectType = new ProjectType(this);
        addObjectType(projectType);

        departmentDirectoryType = new DepartmentDirectoryType(this);
        addObjectType(departmentDirectoryType);

        departmentType = new DepartmentType(
                departmentDirectoryType,
                employeeDirectoryType,
                projectDirectoryType,
                this);
        addObjectType(departmentType);

        addRootObject(departmentDirectoryType.RootDepartmentDirectoryId, departmentDirectoryType);
        addRootObject(skillDirectoryType.RootSkillDirectoryId, skillDirectoryType);
    }
}
