package org.opcfoundation.uawebservicedemo.models.employee.employeeskill;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Skill;
import org.opcfoundation.uawebservicedemo.database.mapper.SkillMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.uawebservicedemo.models.company.skillclassification.SkillClassType;
import org.opcfoundation.webserver.digitaltwin.element.ReferenceElementType;
import org.opcfoundation.webserver.service.message.digitaltwin.GetLinkRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetLinkResponse;
import org.opcfoundation.webserver.types.digitaltwin.ReferenceTargetDescriptor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EmployeeSkillReferenceType extends ReferenceElementType {
    public EmployeeSkillReferenceType(EmployeeTwinSpace twinSpace) {
        super("EmployeeSkillReferenceType", new LocalizedText("EmployeeSkillReference"), twinSpace);
    }

    public CompletableFuture<GetLinkResponse> onGetLinks(GetLinkRequest request)
    {
        try
        {
            // Get employee id from request
            Integer employeeId = Integer.parseInt(request.getId());

            // Database query
            SkillMapper skillMapper = EmployeeDatabase.getSkillMapper();
            List<Skill> skills = skillMapper.getSkillOfEmployee(employeeId);

            // Return general information of child department
            GetLinkResponse response = new GetLinkResponse();
            SkillClassType skillClassType = ((EmployeeTwinSpace)nodeManager).skillClassType;

            for (Skill item : skills)
            {
                ReferenceTargetDescriptor descriptor = new ReferenceTargetDescriptor(
                        item.getId().toString(), // Child id
                        new LocalizedText(item.getName()), // Child name
                        skillClassType); // Child object type

                response.add(descriptor);
            }

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
