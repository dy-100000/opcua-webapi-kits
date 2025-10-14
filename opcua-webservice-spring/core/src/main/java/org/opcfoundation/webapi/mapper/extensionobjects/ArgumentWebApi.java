package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.Argument;

import java.util.ArrayList;
import java.util.List;

public class ArgumentWebApi extends Argument implements ExtensionObjectWebApi {
    public ArgumentWebApi(org.eclipse.milo.opcua.stack.core.types.structured.Argument argument)
    {
        super();
        setName(argument.getName());
        setDataType(argument.getDataType().toParseableString());
        setValueRank(argument.getValueRank());

        if (null != argument.getArrayDimensions())
        {
            List<Long> arrayDimensions = new ArrayList<>();
            for (UInteger item : argument.getArrayDimensions())
            {
                arrayDimensions.add(item.longValue());
            }
            setArrayDimensions(arrayDimensions);
        }

        if (argument.getDescription().isNotNull())
        {
            setDescription(UaTypeMapper.localizedTextFromMilo(argument.getDescription()));
        }
    }

    @Override
    public String getUaTypeId()
    {
        return "i=296";
    }
}
