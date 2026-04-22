package org.opcfoundation.webserver.addressspace.nodemanager;

import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.opcfoundation.webserver.addressspace.nodes.UaNode;
import org.opcfoundation.webserver.addressspace.nodes.UaVariable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.*;
import org.opcfoundation.webserver.service.transactions.base.*;
import org.opcfoundation.webserver.types.common.UaBrowseAdditionalInfo;
import org.opcfoundation.webapi.service.types.ReadContext;
import org.opcfoundation.webapi.service.types.ServiceContext;

import java.util.ArrayList;
import java.util.List;

public class NodeManagerNs0 extends NodeManager {

    public NodeManagerNs0()
    {
        super("http://opcfoundation.org/UA/");
    }

    @Override
    public void onStartUp() throws UaRuntimeException
    {
        buildReferenceTypes();
        buildObjectTypes();
        buildVariableTypes();
        buildDataTypes();

        buildObjects();
        buildVariables();
    }

    public void updateServerArray(String[] serverUris)
    {
        UaNode node = getNode(NodeIds.Server_ServerArray);
        if (null == node || NodeClass.Variable != node.nodeClass()) return;

        ((UaVariable)node).setValue(new Variant(serverUris));
    }

    public void updateNamespaceArray(String[] namespaceUris)
    {
        UaNode node = getNode(NodeIds.Server_NamespaceArray);
        if (null == node || NodeClass.Variable != node.nodeClass()) return;

        ((UaVariable)node).setValue(new Variant(namespaceUris));
    }

    private void buildReferenceTypes()
    {
        addNode(UaReferenceTypes.References);

        addNode(UaReferenceTypes.HierarchicalReferences);
        UaReferenceTypes.HierarchicalReferences.setParentType(UaReferenceTypes.References);

        addNode(UaReferenceTypes.Organizes);
        UaReferenceTypes.Organizes.setParentType(UaReferenceTypes.HierarchicalReferences);

        addNode(UaReferenceTypes.HasChild);
        UaReferenceTypes.HasChild.setParentType(UaReferenceTypes.HierarchicalReferences);

        addNode(UaReferenceTypes.HasSubType);
        UaReferenceTypes.HasSubType.setParentType(UaReferenceTypes.HasChild);

        addNode(UaReferenceTypes.Aggregates);
        UaReferenceTypes.Aggregates.setParentType(UaReferenceTypes.HasChild);

        addNode(UaReferenceTypes.HasComponent);
        UaReferenceTypes.HasComponent.setParentType(UaReferenceTypes.Aggregates);

        addNode(UaReferenceTypes.HasProperty);
        UaReferenceTypes.HasProperty.setParentType(UaReferenceTypes.Aggregates);

        addNode(UaReferenceTypes.NonHierarchicalReferences);
        UaReferenceTypes.NonHierarchicalReferences.setParentType(UaReferenceTypes.References);

        addNode(UaReferenceTypes.HasTypeDefinition);
        UaReferenceTypes.HasTypeDefinition.setParentType(UaReferenceTypes.NonHierarchicalReferences);

        addNode(UaReferenceTypes.HasModellingRule);
        UaReferenceTypes.HasModellingRule.setParentType(UaReferenceTypes.NonHierarchicalReferences);

        addNode(UaReferenceTypes.GeneratesEvent);
        UaReferenceTypes.GeneratesEvent.setParentType(UaReferenceTypes.NonHierarchicalReferences);
    }

    private void buildObjectTypes()
    {
        addNode(UaObjectTypes.BaseObjectType);

        addNode(UaObjectTypes.FolderType);
        UaObjectTypes.FolderType.setParentType(UaObjectTypes.BaseObjectType);

        addNode(UaObjectTypes.ServerType);
        UaObjectTypes.ServerType.setParentType(UaObjectTypes.BaseObjectType);

        addNode(UaObjectTypes.BaseEventType);
        UaObjectTypes.BaseEventType.setParentType(UaObjectTypes.BaseObjectType);
    }

    private void buildVariableTypes()
    {
        addNode(UaVariableTypes.BaseVariableType);

        addNode(UaVariableTypes.BaseDataVariableType);
        UaVariableTypes.BaseDataVariableType.setParentType(UaVariableTypes.BaseVariableType);

        addNode(UaVariableTypes.PropertyType);
        UaVariableTypes.PropertyType.setParentType(UaVariableTypes.BaseVariableType);

        addNode(UaVariableTypes.DataItemType);
        UaVariableTypes.DataItemType.setParentType(UaVariableTypes.BaseDataVariableType);

        addNode(UaVariableTypes.BaseAnalogType);
        UaVariableTypes.BaseAnalogType.setParentType(UaVariableTypes.DataItemType);
    }

    private void buildDataTypes()
    {
        addNode(UaDataTypes.BaseDataType);

        addNode(UaDataTypes.Boolean);
        UaDataTypes.Boolean.setParentType(UaDataTypes.BaseDataType);

        addNode(UaDataTypes.Number);
        UaDataTypes.Number.setParentType(UaDataTypes.BaseDataType);

        addNode(UaDataTypes.Integer);
        UaDataTypes.Integer.setParentType(UaDataTypes.Number);

        addNode(UaDataTypes.SByte);
        UaDataTypes.SByte.setParentType(UaDataTypes.Integer);

        addNode(UaDataTypes.Int16);
        UaDataTypes.Int16.setParentType(UaDataTypes.Integer);

        addNode(UaDataTypes.Int32);
        UaDataTypes.Int32.setParentType(UaDataTypes.Integer);

        addNode(UaDataTypes.Int64);
        UaDataTypes.Int64.setParentType(UaDataTypes.Integer);

        addNode(UaDataTypes.UInteger);
        UaDataTypes.UInteger.setParentType(UaDataTypes.Number);

        addNode(UaDataTypes.Byte);
        UaDataTypes.Byte.setParentType(UaDataTypes.UInteger);

        addNode(UaDataTypes.UInt16);
        UaDataTypes.UInt16.setParentType(UaDataTypes.UInteger);

        addNode(UaDataTypes.UInt32);
        UaDataTypes.UInt32.setParentType(UaDataTypes.UInteger);

        addNode(UaDataTypes.UInt64);
        UaDataTypes.UInt64.setParentType(UaDataTypes.UInteger);

        addNode(UaDataTypes.Float);
        UaDataTypes.Float.setParentType(UaDataTypes.Number);

        addNode(UaDataTypes.Double);
        UaDataTypes.Double.setParentType(UaDataTypes.Number);

        addNode(UaDataTypes.Duration);
        UaDataTypes.Duration.setParentType(UaDataTypes.Double);

        addNode(UaDataTypes.Decimal);
        UaDataTypes.Decimal.setParentType(UaDataTypes.Number);

        addNode(UaDataTypes.String);
        UaDataTypes.String.setParentType(UaDataTypes.BaseDataType);

        addNode(UaDataTypes.DateString);
        UaDataTypes.DateString.setParentType(UaDataTypes.String);

        addNode(UaDataTypes.NormalizedString);
        UaDataTypes.NormalizedString.setParentType(UaDataTypes.String);

        addNode(UaDataTypes.NumericRange);
        UaDataTypes.NumericRange.setParentType(UaDataTypes.String);

        addNode(UaDataTypes.UriString);
        UaDataTypes.UriString.setParentType(UaDataTypes.String);

        addNode(UaDataTypes.ByteString);
        UaDataTypes.ByteString.setParentType(UaDataTypes.BaseDataType);

        addNode(UaDataTypes.Image);
        UaDataTypes.Image.setParentType(UaDataTypes.ByteString);

        addNode(UaDataTypes.ImageBMP);
        UaDataTypes.ImageBMP.setParentType(UaDataTypes.Image);

        addNode(UaDataTypes.ImageGIF);
        UaDataTypes.ImageGIF.setParentType(UaDataTypes.Image);

        addNode(UaDataTypes.ImageJPG);
        UaDataTypes.ImageJPG.setParentType(UaDataTypes.Image);

        addNode(UaDataTypes.ImagePNG);
        UaDataTypes.ImagePNG.setParentType(UaDataTypes.Image);

        addNode(UaDataTypes.AudioDataType);
        UaDataTypes.AudioDataType.setParentType(UaDataTypes.ByteString);

        addNode(UaDataTypes.Enumeration);
        UaDataTypes.Enumeration.setParentType(UaDataTypes.BaseDataType);

        addNode(UaDataTypes.DateTime);
        UaDataTypes.DateTime.setParentType(UaDataTypes.BaseDataType);

        addNode(UaDataTypes.UtcTime);
        UaDataTypes.UtcTime.setParentType(UaDataTypes.DateTime);

        addNode(UaDataTypes.NodeId);
        UaDataTypes.NodeId.setParentType(UaDataTypes.BaseDataType);

        addNode(UaDataTypes.ExpandedNodeId);
        UaDataTypes.ExpandedNodeId.setParentType(UaDataTypes.BaseDataType);

        addNode(UaDataTypes.Guid);
        UaDataTypes.Guid.setParentType(UaDataTypes.BaseDataType);

        addNode(UaDataTypes.LocalizedText);
        UaDataTypes.LocalizedText.setParentType(UaDataTypes.BaseDataType);

        addNode(UaDataTypes.QualifiedName);
        UaDataTypes.QualifiedName.setParentType(UaDataTypes.BaseDataType);

        addNode(UaDataTypes.StatusCode);
        UaDataTypes.StatusCode.setParentType(UaDataTypes.BaseDataType);

        addNode(UaDataTypes.Structure);
        UaDataTypes.Structure.setParentType(UaDataTypes.BaseDataType);

        addNode(UaDataTypes.Range);
        UaDataTypes.Range.setParentType(UaDataTypes.Structure);

        addNode(UaDataTypes.EUInformation);
        UaDataTypes.EUInformation.setParentType(UaDataTypes.Structure);

        addNode(UaDataTypes.EnumValueType);
        UaDataTypes.EnumValueType.setParentType(UaDataTypes.Structure);
    }

    private void buildObjects()
    {
        addNode(UaObjects.RootFolder);
        addNode(UaObjects.ObjectsFolder);
        addNode(UaObjects.TypesFolder);
        addNode(UaObjects.ViewsFolder);
        addNode(UaObjects.Server);
        addNode(UaObjects.ObjectTypesFolder);
        addNode(UaObjects.VariableTypesFolder);
        addNode(UaObjects.EventTypesFolder);
        addNode(UaObjects.DataTypesFolder);
        addNode(UaObjects.ReferenceTypesFolder);

        addNode(UaObjects.ModellingRule_Mandatory);
        addNode(UaObjects.ModellingRule_Optional);
        addNode(UaObjects.ModellingRule_MandatoryPlaceHolder);
        addNode(UaObjects.ModellingRule_OptionalPlaceHolder);

        UaObjects.RootFolder.organizes(UaObjects.ObjectsFolder);
        UaObjects.RootFolder.organizes(UaObjects.TypesFolder);
        UaObjects.RootFolder.organizes(UaObjects.ViewsFolder);

        UaObjects.ObjectsFolder.organizes(UaObjects.Server);

        UaObjects.TypesFolder.organizes(UaObjects.ObjectTypesFolder);
        UaObjects.TypesFolder.organizes(UaObjects.VariableTypesFolder);
        UaObjects.TypesFolder.organizes(UaObjects.EventTypesFolder);
        UaObjects.TypesFolder.organizes(UaObjects.DataTypesFolder);
        UaObjects.TypesFolder.organizes(UaObjects.ReferenceTypesFolder);

        UaObjects.EventTypesFolder.organizes(UaObjectTypes.BaseEventType);

        UaObjects.ObjectTypesFolder.organizes(UaObjectTypes.BaseObjectType);
        UaObjects.VariableTypesFolder.organizes(UaVariableTypes.BaseVariableType);
        UaObjects.DataTypesFolder.organizes(UaDataTypes.BaseDataType);
        UaObjects.ReferenceTypesFolder.organizes(UaReferenceTypes.References);
    }

    private void buildVariables()
    {
        // ServerType
        UaVariable ServerType_ServerArray =  new UaVariable(NodeIds.ServerType_ServerArray, "ServerArray", new LocalizedText("ServerArray"),
                NodeIds.String, ValueRank.OneDimension.getValue(), AccessLevel.CurrentRead.getValue(), UaVariableTypes.PropertyType);
        UaObjectTypes.ServerType.addMember(ServerType_ServerArray);
        this.addNode(ServerType_ServerArray);

        UaVariable ServerType_NamespaceArray =  new UaVariable(NodeIds.ServerType_NamespaceArray, "NamespaceArray", new LocalizedText("NamespaceArray"),
                NodeIds.String, ValueRank.OneDimension.getValue(), AccessLevel.CurrentRead.getValue(), UaVariableTypes.PropertyType);
        UaObjectTypes.ServerType.addMember(ServerType_NamespaceArray);
        this.addNode(ServerType_NamespaceArray);

        // Server
        UaVariable Server_ServerArray = new UaVariable(NodeIds.Server_ServerArray, "ServerArray", new LocalizedText("ServerArray"),
                NodeIds.String, ValueRank.OneDimension.getValue(), AccessLevel.CurrentRead.getValue(), UaVariableTypes.PropertyType);
        UaObjects.Server.addMember(Server_ServerArray);
        this.addNode(Server_ServerArray);

        UaVariable Server_NamespaceArray =  new UaVariable(NodeIds.Server_NamespaceArray, "NamespaceArray", new LocalizedText("NamespaceArray"),
                NodeIds.String, ValueRank.OneDimension.getValue(), AccessLevel.CurrentRead.getValue(), UaVariableTypes.PropertyType);
        UaObjects.Server.addMember(Server_NamespaceArray);
        this.addNode(Server_NamespaceArray);

        // DataItemType
        UaVariable DataItemType_Definition = new UaVariable(NodeIds.DataItemType_Definition, "Definition", new LocalizedText("Definition"), NodeIds.String, -1,1, UaVariableTypes.PropertyType);
        addNode(DataItemType_Definition);
        UaVariableTypes.DataItemType.addMember(DataItemType_Definition);

        UaVariable DataItemType_ValuePrecision = new UaVariable(NodeIds.DataItemType_ValuePrecision, "ValuePrecision", new LocalizedText("ValuePrecision"), NodeIds.Double, -1,1, UaVariableTypes.PropertyType);
        addNode(DataItemType_ValuePrecision);
        UaVariableTypes.DataItemType.addMember(DataItemType_ValuePrecision);

        // BaseAnalogType
        UaVariable BaseAnalogType_EURange = new UaVariable(NodeIds.BaseAnalogType_EURange, "EURange", new LocalizedText("EURange"), NodeIds.Range, -1,1, UaVariableTypes.PropertyType);
        addNode(BaseAnalogType_EURange);
        UaVariableTypes.BaseAnalogType.addMember(BaseAnalogType_EURange);

        UaVariable BaseAnalogType_EngineeringUnits = new UaVariable(NodeIds.BaseAnalogType_EngineeringUnits, "EngineeringUnits", new LocalizedText("EngineeringUnits"), NodeIds.EUInformation, -1,1, UaVariableTypes.PropertyType);
        addNode(BaseAnalogType_EngineeringUnits);
        UaVariableTypes.BaseAnalogType.addMember(BaseAnalogType_EngineeringUnits);

        // EventType
        UaVariable BaseEventType_EventId = new UaVariable(NodeIds.BaseEventType_EventId, "EventId", new LocalizedText("EventId"),
                NodeIds.ByteString, ValueRank.Scalar.getValue(), AccessLevel.CurrentRead.getValue(), UaVariableTypes.PropertyType);
        UaObjectTypes.BaseEventType.addMember(BaseEventType_EventId);
        this.addNode(BaseEventType_EventId);

        UaVariable BaseEventType_EventType = new UaVariable(NodeIds.BaseEventType_EventType, "EventType", new LocalizedText("EventType"),
                NodeIds.NodeId, ValueRank.Scalar.getValue(), AccessLevel.CurrentRead.getValue(), UaVariableTypes.PropertyType);
        UaObjectTypes.BaseEventType.addMember(BaseEventType_EventType);
        this.addNode(BaseEventType_EventType);

        UaVariable BaseEventType_Time = new UaVariable(NodeIds.BaseEventType_Time, "Time", new LocalizedText("Time"),
                NodeIds.UtcTime, ValueRank.Scalar.getValue(), AccessLevel.CurrentRead.getValue(), UaVariableTypes.PropertyType);
        UaObjectTypes.BaseEventType.addMember(BaseEventType_Time);
        this.addNode(BaseEventType_Time);

        UaVariable BaseEventType_Message = new UaVariable(NodeIds.BaseEventType_Message, "Message", new LocalizedText("Message"),
                NodeIds.LocalizedText, ValueRank.Scalar.getValue(), AccessLevel.CurrentRead.getValue(), UaVariableTypes.PropertyType);
        UaObjectTypes.BaseEventType.addMember(BaseEventType_Message);
        this.addNode(BaseEventType_Message);
    }

    @Override
    public UaBrowseTransaction getBrowseTransaction(
            ServiceContext context,
            BrowseDescription nodeToBrowse,
            UaBrowseAdditionalInfo additionalInfo,
            int handleId)
    {
        return new UaBrowseNodeTransaction(
                context,
                nodeToBrowse,
                additionalInfo,
                handleId,
                this);
    }

    @Override
    public List<UaReadTransaction> getReadTransactions(
            ReadContext context,
            List<Integer> handleIds)
    {
        UaReadNodeTransaction transaction = new UaReadNodeTransaction(
                context,
                handleIds,
                this);

        ArrayList<UaReadTransaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        return transactions;
    }
}
