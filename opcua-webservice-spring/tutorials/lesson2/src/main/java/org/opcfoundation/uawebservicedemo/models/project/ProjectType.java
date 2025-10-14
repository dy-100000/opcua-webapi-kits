package org.opcfoundation.uawebservicedemo.models.project;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Project;
import org.opcfoundation.uawebservicedemo.database.mapper.ProjectMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeDataManager;
import org.opcfoundation.webserver.addressspace.models.UaDataObjectType;
import org.opcfoundation.webserver.addressspace.nodes.UaVariable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.types.UaChildId;
import org.opcfoundation.webserver.types.message.ReadObjectAttributeRequest;
import org.opcfoundation.webserver.types.message.ReadObjectAttributeResponse;
import org.opcfoundation.webserver.types.message.ReadVariableValueRequest;
import org.opcfoundation.webserver.types.message.ReadVariableValueResponse;

import java.time.ZoneId;
import java.util.concurrent.CompletableFuture;

// Defining a project data object which include
// start time, end time and budget
public class ProjectType extends UaDataObjectType {
    private final UaVariable startTime;
    private final UaVariable endTime;
    private final UaVariable budget;

    public ProjectType(EmployeeDataManager nodeManager)
    {
        super("ProjectType", new LocalizedText("项目类定义"), null,nodeManager);

        // Add start time data field
        startTime = addVariable(
                "StartTime",
                true,
                new LocalizedText("StartTime"),
                new LocalizedText("The start time of project"),
                UaDataTypes.DateTime,
                false,
                false,
                null,
                null);

        // Add end time data field
        endTime = addVariable(
                "EndTime",
                false,
                new LocalizedText("EndTime"),
                new LocalizedText("The end time of project"),
                UaDataTypes.DateTime,
                false,
                false,
                null,
                null);

        // Add budget data field
        budget = addVariable(
                "Budget",
                false,
                new LocalizedText("Budget"),
                new LocalizedText("The total project budget in thousand dollar"),
                UaDataTypes.Float,
                false,
                false,
                null,
                null);
    }

    // Return project general information such as name, description
    @Override
    public CompletableFuture<ReadObjectAttributeResponse> getObjectAttribute(ReadObjectAttributeRequest request)
    {
        try
        {
            // Get project id from request
            Integer projectId = Integer.parseInt(request.getObjectId().getId());

            // Database query
            ProjectMapper projectMapper = EmployeeDatabase.getProjectMapper();
            Project project = projectMapper.getProjectInfo(projectId);
            if (null == project) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            // Return general information
            ReadObjectAttributeResponse response = new ReadObjectAttributeResponse(
                    new LocalizedText(project.getName()),
                    new LocalizedText(project.getDescription()));

            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }

    // Return project data such as start time, end time and budget
    @Override
    public CompletableFuture<ReadVariableValueResponse> getVariableValues(ReadVariableValueRequest request)
    {
        try
        {
            // Get project id from request
            Integer projectId = Integer.parseInt(request.getObjectId().getId());

            // Database query
            ProjectMapper projectMapper = EmployeeDatabase.getProjectMapper();
            Project project = projectMapper.getProjectData(projectId);
            if (null == project) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

            // Return field data
            ReadVariableValueResponse response = new ReadVariableValueResponse();
            ZoneId zoneId = ZoneId.systemDefault();

            for (UaChildId item: request.getVariableIds())
            {
                // Return start time if required
                if (startTime.browseName().equals(item.getPathId()))
                {
                    DateTime time = new DateTime(project.getStartTime().atZone(zoneId).toInstant());
                    response.setValue(item, Variant.ofDateTime(time));
                }

                // Return end time if required
                if (endTime.browseName().equals(item.getPathId())) {
                    DateTime time = new DateTime(project.getEndTime().atZone(zoneId).toInstant());
                    response.setValue(item, Variant.ofDateTime(time));
                }

                // Return budget if required
                if (budget.browseName().equals(item.getPathId())) {
                    response.setValue(item,Variant.ofFloat(project.getBudget()));
                }
            }

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
