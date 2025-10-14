package org.opcfoundation.uawebservicedemo.models.skill.enumeration;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.models.EmployeeDataManager;
import org.opcfoundation.webserver.addressspace.models.UaEnumDataType;

public class SkillLevelEnumType extends UaEnumDataType {
    public SkillLevelEnumType(EmployeeDataManager nodeManager)
    {
        super(
                "SkillLevel",
                new LocalizedText("SkillLevel"),
                SkillLevel.getDescriptions(),
                nodeManager);
    }
}
