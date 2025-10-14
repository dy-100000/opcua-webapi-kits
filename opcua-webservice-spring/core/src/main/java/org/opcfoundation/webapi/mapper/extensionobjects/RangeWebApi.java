package org.opcfoundation.webapi.mapper.extensionobjects;

import org.opcfoundation.webapi.model.Range;

public class RangeWebApi extends Range implements ExtensionObjectWebApi {
    public RangeWebApi(org.eclipse.milo.opcua.stack.core.types.structured.Range range)
    {
        super();
        setHigh(range.getHigh());
        setLow(range.getLow());
    }

    @Override
    public String getUaTypeId()
    {
        return "i=884";
    }
}
