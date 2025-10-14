package org.opcfoundation.uawebservicedemo.models.skill.enumeration;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.models.EmployeeDataManager;
import org.opcfoundation.webserver.addressspace.models.UaEnumDataType;

public class SkillCategoryEnumType extends UaEnumDataType {
    public SkillCategoryEnumType(EmployeeDataManager nodeManager)
    {
        super(
                "SkillCategory",
                new LocalizedText("SkillCategory"),
                SkillCategory.getDescriptions(),
                nodeManager);
    }
}
