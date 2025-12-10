package org.opcfoundation.uawebservicedemo.models.company.skillclassification;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Skill;
import org.opcfoundation.uawebservicedemo.database.mapper.SkillMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.webserver.digitaltwin.submodel.DynamicSubmodelType;
import org.opcfoundation.webserver.service.message.digitaltwin.GetObjectElementListRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetObjectElementListResponse;
import org.opcfoundation.webserver.types.digitaltwin.ObjectElementDescriptor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SkillClassificationSubmodelType extends DynamicSubmodelType {
    public SkillClassificationSubmodelType(EmployeeTwinSpace twinSpace) {
        super("SkillClassificationSubmodelType", new LocalizedText("SkillClassificationModel"), twinSpace);
    }

    // Get skill information of employee
    @Override
    public CompletableFuture<GetObjectElementListResponse> onGetObjectElementList(GetObjectElementListRequest request)
    {
        try
        {
            // Database query
            SkillMapper skillMapper = EmployeeDatabase.getSkillMapper();
            List<Skill> skills = skillMapper.getAllSkill();

            // Return general information of skill
            SkillClassType skillClassType = ((EmployeeTwinSpace)nodeManager).skillClassType;
            GetObjectElementListResponse response = new GetObjectElementListResponse();

            for (Skill item : skills)
            {
                ObjectElementDescriptor descriptor = new ObjectElementDescriptor(
                        item.getId().toString(), // SkillId
                        new LocalizedText(item.getName()), // SkillName
                        skillClassType); // SkillType

                response.add(descriptor);
            }

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_ResourceUnavailable);
        }
    }
}
