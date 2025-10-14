package org.opcfoundation.uawebservicedemo.models;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.opcfoundation.uawebservicedemo.models.department.DepartmentDirectoryType;
import org.opcfoundation.uawebservicedemo.models.department.DepartmentType;
import org.opcfoundation.uawebservicedemo.models.project.ProjectDirectoryType;
import org.opcfoundation.uawebservicedemo.models.project.ProjectType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerWebService;

public class EmployeeDataManager extends NodeManagerWebService {
    // Project definitions
    public ProjectType projectType;
    public ProjectDirectoryType projectDirectoryType;

    // Department definitions
    public DepartmentType departmentType;
    public DepartmentDirectoryType departmentDirectoryType;

    public EmployeeDataManager()
    {
        super("EmployeeDataManager");
    }

    @Override
    public void onStartUp() throws UaRuntimeException {
        // Create all types
        projectDirectoryType = new ProjectDirectoryType(this);
        addObjectType(projectDirectoryType);

        projectType = new ProjectType(this);
        addObjectType(projectType);

        departmentDirectoryType = new DepartmentDirectoryType(this);
        addObjectType(departmentDirectoryType);

        departmentType = new DepartmentType(
                departmentDirectoryType,
                projectDirectoryType,
                this);
        addObjectType(departmentType);

        addRootObject(departmentDirectoryType.RootDepartmentDirectoryId, departmentDirectoryType);
    }
}
