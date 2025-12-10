package org.opcfoundation.uawebservicedemo.models.company.skillclassification.enumeration;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.webserver.addressspace.nodes.UaEnumDataType;

public class SkillLevelEnumType extends UaEnumDataType {
    public SkillLevelEnumType(EmployeeTwinSpace twinSpace)
    {
        super(
                "SkillLevelEnumType",
                new LocalizedText("SkillLevel"),
                SkillLevel.getDescriptions(),
                twinSpace);
    }
}
