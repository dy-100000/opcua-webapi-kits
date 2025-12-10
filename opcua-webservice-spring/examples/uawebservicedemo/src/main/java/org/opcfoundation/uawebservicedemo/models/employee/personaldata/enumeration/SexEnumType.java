package org.opcfoundation.uawebservicedemo.models.employee.personaldata.enumeration;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.models.EmployeeTwinSpace;
import org.opcfoundation.webserver.addressspace.nodes.UaEnumDataType;

public class SexEnumType extends UaEnumDataType {
    public SexEnumType(EmployeeTwinSpace twinSpace)
    {
        super(
                "Sex",
                new LocalizedText("Sex"),
                Sex.getDescriptions(),
                twinSpace);
    }
}
