package org.opcfoundation.uawebservicedemo.models.project;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Project;
import org.opcfoundation.uawebservicedemo.database.mapper.ProjectMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeDataManager;
import org.opcfoundation.webserver.addressspace.models.UaObjectDirectoryType;
import org.opcfoundation.webserver.types.UaChildObjectDescriptor;
import org.opcfoundation.webserver.types.message.GetObjectDirectoryChildRequest;
import org.opcfoundation.webserver.types.message.GetObjectDirectoryChildResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

// Defining project list of department
public class ProjectDirectoryType extends UaObjectDirectoryType {
    public ProjectDirectoryType(EmployeeDataManager nodeManager) {
        super("ProjectDirectoryType", new LocalizedText("项目目录类定义"), nodeManager);
    }

    // Get project information for giving department
    @Override
    public CompletableFuture<GetObjectDirectoryChildResponse> getChildren(GetObjectDirectoryChildRequest request)
    {
        try
        {
            // Get department id from request
            Integer departmentId = Integer.parseInt(request.getObjectId().getId());

            // Database query
            ProjectMapper projectMapper = EmployeeDatabase.getProjectMapper();
            List<Project> projects = projectMapper.getProjectOfDepartment(departmentId);

            // Return general information of project
            List<UaChildObjectDescriptor> children = new ArrayList<>();
            ProjectType projectType = ((EmployeeDataManager)nodeManager).projectType;

            for (Project item : projects)
            {
                UaChildObjectDescriptor descriptor = new UaChildObjectDescriptor(
                        item.getId().toString(),
                        new LocalizedText(item.getName()),
                        projectType);

                children.add(descriptor);
            }

            GetObjectDirectoryChildResponse response = new GetObjectDirectoryChildResponse(children, false);
            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
