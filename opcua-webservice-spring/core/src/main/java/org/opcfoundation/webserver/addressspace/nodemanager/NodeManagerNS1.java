package org.opcfoundation.webserver.addressspace.nodemanager;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.opcfoundation.webapi.service.types.ReadContext;
import org.opcfoundation.webapi.service.types.ServiceContext;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaReferenceTypes;
import org.opcfoundation.webserver.service.transactions.reactiveobject.UaBrowseNodeTransaction;
import org.opcfoundation.webserver.service.transactions.reactiveobject.UaReadNodeTransaction;
import org.opcfoundation.webserver.service.transactions.base.UaBrowseTransaction;
import org.opcfoundation.webserver.service.transactions.base.UaReadTransaction;
import org.opcfoundation.webserver.types.common.UaBrowseAdditionalInfo;

import java.util.ArrayList;
import java.util.List;

public class NodeManagerNS1 extends NodeManager {

    public NodeManagerNS1()
    {
        super("http://opcfoundation.org/UA/DigitalTwin");
    }

    @Override
    public void onStartUp() throws UaRuntimeException
    {
        buildReferenceTypes();
        buildObjectTypes();
    }

    private void buildObjectTypes() {
        addNode(UaObjectTypes.DigitalTwinRepositoryType);
        addNode(UaObjectTypes.DigitalTwinType);
        addNode(UaObjectTypes.SubmodelType);
        addNode(UaObjectTypes.ElementType);
        addNode(UaObjectTypes.ReferenceElementType);
        addNode(UaObjectTypes.ElementCollectionType);
        addNode(UaObjectTypes.ElementListType);
    }

    private void buildReferenceTypes() {
        addNode(UaReferenceTypes.HasLink);
        UaReferenceTypes.HasLink.setParentReferenceType(UaReferenceTypes.NonHierarchicalReferences);
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
