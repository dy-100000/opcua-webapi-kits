package org.opcfoundation.uawebservicedemo.models;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.opcfoundation.uawebservicedemo.models.skill.SkillDirectoryType;
import org.opcfoundation.uawebservicedemo.models.skill.SkillType;
import org.opcfoundation.uawebservicedemo.models.skill.enumeration.SkillCategoryEnumType;
import org.opcfoundation.uawebservicedemo.models.skill.enumeration.SkillLevelEnumType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerWebService;

public class EmployeeDataManager extends NodeManagerWebService {
    // Skill definitions
    public SkillLevelEnumType skillLevelEnumType;
    public SkillCategoryEnumType skillCategoryEnumType;
    public SkillType skillType;
    public SkillDirectoryType skillDirectoryType;

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

        addRootObject(skillDirectoryType.RootSkillDirectoryId, skillDirectoryType);
    }
}
