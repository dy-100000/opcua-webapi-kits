package org.opcfoundation.webapi.mapper.extensionobjects;

import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.EUInformation;

public class EUInformationWebApi extends EUInformation implements ExtensionObjectWebApi {
    public EUInformationWebApi(org.eclipse.milo.opcua.stack.core.types.structured.EUInformation euInformation)
    {
        super();
        setUnitId(euInformation.getUnitId());
        setDisplayName(UaTypeMapper.localizedTextFromMilo(euInformation.getDisplayName()));
        if (euInformation.getDescription().isNotNull()) setDescription(UaTypeMapper.localizedTextFromMilo(euInformation.getDescription()));
        if (null != euInformation.getNamespaceUri() && !euInformation.getNamespaceUri().isEmpty()) setNamespaceUri(euInformation.getNamespaceUri());
    }

    @Override
    public String getUaTypeId()
    {
        return "i=887";
    }
}
