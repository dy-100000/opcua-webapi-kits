package org.opcfoundation.uawebservicedemo.models.skill;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.Skill;
import org.opcfoundation.uawebservicedemo.database.mapper.SkillMapper;
import org.opcfoundation.uawebservicedemo.models.EmployeeDataManager;
import org.opcfoundation.webserver.addressspace.models.UaObjectDirectoryType;
import org.opcfoundation.webserver.types.UaChildObjectDescriptor;
import org.opcfoundation.webserver.types.message.GetObjectDirectoryChildRequest;
import org.opcfoundation.webserver.types.message.GetObjectDirectoryChildResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SkillDirectoryType extends UaObjectDirectoryType {
    public final String RootSkillDirectoryId = "Skills";

    public SkillDirectoryType(EmployeeDataManager nodeManager) {
        super("SkillDirectoryType", new LocalizedText("技能目录类定义"), nodeManager);
    }

    // Get skill information of employee
    @Override
    public CompletableFuture<GetObjectDirectoryChildResponse> getChildren(GetObjectDirectoryChildRequest request)
    {
        try
        {
            // Database query
            SkillMapper skillMapper = EmployeeDatabase.getSkillMapper();
            List<Skill> skills;

            if (request.getObjectId().getId().equals(RootSkillDirectoryId))
            {
                // If is root skill directory, return all skills
                skills = skillMapper.getAllSkillInfo();
            } else {
                // Get employee id from request
                Integer employeeID = Integer.parseInt(request.getObjectId().getId());
                skills = skillMapper.getSkillInfoOfEmployee(employeeID);
            }

            // Return general information of skill
            List<UaChildObjectDescriptor> children = new ArrayList<>();
            SkillType skillType = ((EmployeeDataManager)nodeManager).skillType;

            for (Skill item : skills)
            {
                UaChildObjectDescriptor descriptor = new UaChildObjectDescriptor(
                        item.getId().toString(), // SkillId
                        new LocalizedText(item.getName()), // SkillName
                        skillType); // SkillType

                children.add(descriptor);
            }

            GetObjectDirectoryChildResponse response = new GetObjectDirectoryChildResponse(children, false);
            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);
        }
    }
}
