package org.opcfoundation.uawebservicedemo.models.employee.enumeration;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.uawebservicedemo.models.EmployeeDataManager;
import org.opcfoundation.webserver.addressspace.models.UaEnumDataType;

public class SexEnumType extends UaEnumDataType {
    public SexEnumType(EmployeeDataManager nodeManager)
    {
        super(
                "Sex",
                new LocalizedText("Sex"),
                Sex.getDescriptions(),
                nodeManager);
    }
}
