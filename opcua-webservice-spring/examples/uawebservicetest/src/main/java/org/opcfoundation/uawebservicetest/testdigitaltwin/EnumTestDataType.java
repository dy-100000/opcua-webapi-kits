package org.opcfoundation.uawebservicetest.testdigitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.models.UaEnumDataType;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;

import java.util.List;

public class EnumTestDataType extends UaEnumDataType {
    EnumTestDataType(DigitalTwinSpace space)
    {
        super(
                "EnumTest",
                new LocalizedText("EnumTest"),
                List.of("A","B","C","D"),
                space);
    }
}
