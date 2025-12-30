package org.opcfoundation.webapi.mapper.extensionobjects;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.springframework.lang.Nullable;
import org.opcfoundation.webapi.mapper.UaTypeMapper;
import org.opcfoundation.webapi.model.EUInformation;

public class EUInformationMapper implements StructureMapper {
    public NodeId dataTypeId()
    {
        return NodeIds.EUInformation;
    }

    @Override
    public UaStructuredType toStructure(byte[] json) throws Exception
    {
        EUInformation euInformation = OBJECT_MAPPER.readValue(json, EUInformation.class);
        return new org.eclipse.milo.opcua.stack.core.types.structured.EUInformation(
                    euInformation.getNamespaceUri(),
                    euInformation.getUnitId(),
                    (null == euInformation.getDisplayName()) ? LocalizedText.NULL_VALUE : new LocalizedText(euInformation.getDisplayName().getLocale(), euInformation.getDisplayName().getText()),
                    (null == euInformation.getDescription()) ? LocalizedText.NULL_VALUE : new LocalizedText(euInformation.getDescription().getLocale(), euInformation.getDescription().getText()));
    }

    @Override
    public byte[] toJson(UaStructuredType struct) throws Exception
    {
        org.eclipse.milo.opcua.stack.core.types.structured.EUInformation euInformation = (org.eclipse.milo.opcua.stack.core.types.structured.EUInformation) struct;

        EUInformation euInformationWebApi = new EUInformation();
        euInformationWebApi.setNamespaceUri(euInformationWebApi.getNamespaceUri());
        euInformationWebApi.setUnitId(euInformationWebApi.getUnitId());
        euInformationWebApi.setDisplayName(UaTypeMapper.localizedTextFromMilo(euInformation.getDisplayName()));
        euInformationWebApi.setDescription(UaTypeMapper.localizedTextFromMilo(euInformation.getDescription()));

        return OBJECT_MAPPER.writeValueAsBytes(euInformationWebApi);
    }
}
