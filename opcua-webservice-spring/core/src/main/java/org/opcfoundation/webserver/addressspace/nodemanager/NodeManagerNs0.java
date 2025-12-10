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
import org.opcfoundation.webserver.service.transactions.reactiveobject.UaBrowseNodeTransaction;
import org.opcfoundation.webserver.service.transactions.reactiveobject.UaReadNodeTransaction;
import org.opcfoundation.webserver.service.transactions.base.UaBrowseTransaction;
import org.opcfoundation.webserver.service.transactions.base.UaReadTransaction;
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
        UaReferenceTypes.HierarchicalReferences.setParentReferenceType(UaReferenceTypes.References);

        addNode(UaReferenceTypes.Organizes);
        UaReferenceTypes.Organizes.setParentReferenceType(UaReferenceTypes.HierarchicalReferences);

        addNode(UaReferenceTypes.HasChild);
        UaReferenceTypes.HasChild.setParentReferenceType(UaReferenceTypes.HierarchicalReferences);

        addNode(UaReferenceTypes.HasSubType);
        UaReferenceTypes.HasSubType.setParentReferenceType(UaReferenceTypes.HasChild);

        addNode(UaReferenceTypes.Aggregates);
        UaReferenceTypes.Aggregates.setParentReferenceType(UaReferenceTypes.HasChild);

        addNode(UaReferenceTypes.HasComponent);
        UaReferenceTypes.HasComponent.setParentReferenceType(UaReferenceTypes.Aggregates);

        addNode(UaReferenceTypes.HasProperty);
        UaReferenceTypes.HasProperty.setParentReferenceType(UaReferenceTypes.Aggregates);

        addNode(UaReferenceTypes.NonHierarchicalReferences);
        UaReferenceTypes.NonHierarchicalReferences.setParentReferenceType(UaReferenceTypes.References);

        addNode(UaReferenceTypes.HasTypeDefinition);
        UaReferenceTypes.HasTypeDefinition.setParentReferenceType(UaReferenceTypes.NonHierarchicalReferences);

        addNode(UaReferenceTypes.HasModellingRule);
        UaReferenceTypes.HasModellingRule.setParentReferenceType(UaReferenceTypes.NonHierarchicalReferences);
    }

    private void buildObjectTypes()
    {
        addNode(UaObjectTypes.BaseObjectType);
        addNode(UaObjectTypes.FolderType);
        addNode(UaObjectTypes.ServerType);
        addNode(UaObjectTypes.BaseEventType);
    }

    private void buildVariableTypes()
    {
        addNode(UaVariableTypes.BaseVariableType);
        addNode(UaVariableTypes.BaseDataVariableType);
        addNode(UaVariableTypes.PropertyType);

        // DataItemType
        addNode(UaVariableTypes.DataItemType);

        UaVariable DataItemType_Definition = new UaVariable(NodeIds.DataItemType_Definition, "Definition", new LocalizedText("Definition"), NodeIds.String, -1,1, UaVariableTypes.PropertyType);
        addNode(DataItemType_Definition);
        UaVariableTypes.DataItemType.addMember(DataItemType_Definition);

        UaVariable DataItemType_ValuePrecision = new UaVariable(NodeIds.DataItemType_ValuePrecision, "ValuePrecision", new LocalizedText("ValuePrecision"), NodeIds.Double, -1,1, UaVariableTypes.PropertyType);
        addNode(DataItemType_ValuePrecision);
        UaVariableTypes.DataItemType.addMember(DataItemType_ValuePrecision);

        // BaseAnalogType
        addNode(UaVariableTypes.BaseAnalogType);

        UaVariable BaseAnalogType_EURange = new UaVariable(NodeIds.BaseAnalogType_EURange, "EURange", new LocalizedText("EURange"), NodeIds.Range, -1,1, UaVariableTypes.PropertyType);
        addNode(BaseAnalogType_EURange);
        UaVariableTypes.BaseAnalogType.addMember(BaseAnalogType_EURange);

        UaVariable BaseAnalogType_EngineeringUnits = new UaVariable(NodeIds.BaseAnalogType_EngineeringUnits, "EngineeringUnits", new LocalizedText("EngineeringUnits"), NodeIds.EUInformation, -1,1, UaVariableTypes.PropertyType);
        addNode(BaseAnalogType_EngineeringUnits);
        UaVariableTypes.BaseAnalogType.addMember(BaseAnalogType_EngineeringUnits);
    }

    private void buildDataTypes()
    {
        addNode(UaDataTypes.BaseDataType);
        addNode(UaDataTypes.Boolean);

        addNode(UaDataTypes.Number);
        addNode(UaDataTypes.Integer);
        addNode(UaDataTypes.SByte);
        addNode(UaDataTypes.Int16);
        addNode(UaDataTypes.Int32);
        addNode(UaDataTypes.Int64);
        addNode(UaDataTypes.UInteger);
        addNode(UaDataTypes.Byte);
        addNode(UaDataTypes.UInt16);
        addNode(UaDataTypes.UInt32);
        addNode(UaDataTypes.UInt64);
        addNode(UaDataTypes.Float);
        addNode(UaDataTypes.Double);
        addNode(UaDataTypes.Duration);
        addNode(UaDataTypes.Decimal);

        addNode(UaDataTypes.String);
        addNode(UaDataTypes.DateString);
        addNode(UaDataTypes.NormalizedString);
        addNode(UaDataTypes.NumericRange);
        addNode(UaDataTypes.UriString);

        addNode(UaDataTypes.ByteString);
        addNode(UaDataTypes.Image);
        addNode(UaDataTypes.ImageBMP);
        addNode(UaDataTypes.ImageGIF);
        addNode(UaDataTypes.ImageJPG);
        addNode(UaDataTypes.ImagePNG);
        addNode(UaDataTypes.AudioDataType);

        addNode(UaDataTypes.Enumeration);
        addNode(UaDataTypes.DateTime);
        addNode(UaDataTypes.NodeId);
        addNode(UaDataTypes.ExpandedNodeId);
        addNode(UaDataTypes.Guid);
        addNode(UaDataTypes.LocalizedText);
        addNode(UaDataTypes.QualifiedName);
        addNode(UaDataTypes.StatusCode);

        addNode(UaDataTypes.Structure);
        addNode(UaDataTypes.Range);
        addNode(UaDataTypes.EUInformation);
        addNode(UaDataTypes.EnumValueType);
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
