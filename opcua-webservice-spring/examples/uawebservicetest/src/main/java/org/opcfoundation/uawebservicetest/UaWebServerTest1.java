package org.opcfoundation.uawebservicetest;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.opcfoundation.webapi.mapper.extensionobjects.ExtensionObjectEncoder;
import org.opcfoundation.webserver.service.UaWebServerBase;
import org.opcfoundation.webapi.service.UaServerConfigure;
import org.opcfoundation.webapi.service.types.*;
import org.opcfoundation.webserver.types.digitaltwin.EventQuery;
import org.opcfoundation.webserver.types.digitaltwin.EventQueryElement;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

//@Service
public class UaWebServerTest1 extends UaWebServerBase
{
    public UaWebServerTest1()
    {
        super();
    }

    @Override
    public void onStartUp() {
        System.out.println("Server start up");

        UaServerConfigure configure = new UaServerConfigure();
        configure.setApplicationUri("test");
        configure.setApplicationName(LocalizedText.english("test"));
        configure.setProductUri("test");
        configure.setSupportServerUriPath(true);

        setServerConfigure(configure);
    }

    @Override
    public void onShutDown()
    {
        System.out.println("Server shut down");
    }

    @Override
    public CompletableFuture<List<BrowseResult>> browse(BrowseContext context) throws UaRuntimeException
    {
        ArrayList<BrowseResult> results = new ArrayList<>();
        for (BrowseDescription item: context.getNodesToBrowse())
        {
            ReferenceDescription[] references = new ReferenceDescription[1];
            references[0] = new ReferenceDescription(
                    NodeIds.HasComponent,
                    Boolean.TRUE,
                    NodeIds.ObjectsFolder.expanded(),
                    new QualifiedName(0, "Objects"),
                    new LocalizedText("Objects"),
                    NodeClass.Object,
                    NodeIds.FolderType.expanded()
            );

            results.add(new BrowseResult(StatusCode.GOOD, ByteString.NULL_VALUE, references));
        }

        return CompletableFuture.completedFuture(results);
    }

    @Override
    public CompletableFuture<List<BrowseResult>> browseNext(BrowseNextContext context) throws UaRuntimeException
    {
        ArrayList<BrowseResult> results = new ArrayList<>();
        for (ByteString item: context.getContinuationPoints())
        {
            ReferenceDescription[] references = new ReferenceDescription[1];
            references[0] = new ReferenceDescription(
                    NodeIds.HasComponent,
                    Boolean.TRUE,
                    NodeIds.TypesFolder.expanded(),
                    new QualifiedName(0, "Types"),
                    new LocalizedText("Types"),
                    NodeClass.Object,
                    NodeIds.FolderType.expanded()
            );

            results.add(new BrowseResult(StatusCode.GOOD, ByteString.NULL_VALUE, references));
        }

        return CompletableFuture.completedFuture(results);
    }

    @Override
    public CompletableFuture<List<BrowsePathResult>> translate(TranslateContext context) throws UaRuntimeException
    {
        ArrayList<BrowsePathResult> results = new ArrayList<>();
        for (BrowsePath item : context.getNodesToTranslate())
        {
            RelativePath path = item.getRelativePath();
            int remainingPathIndex = (null == path.getElements()) ? 0 : path.getElements().length;
            BrowsePathTarget target = new BrowsePathTarget(NodeIds.ObjectsFolder.expanded(), UInteger.valueOf(remainingPathIndex));
            BrowsePathTarget[] targets = { target };
            BrowsePathResult result = new BrowsePathResult(StatusCode.GOOD, targets);
            results.add(result);
        }

        return CompletableFuture.completedFuture(results);
    }

    @Override
    public CompletableFuture<List<DataValue>> read(ReadContext context) throws UaRuntimeException {
        DateTime now = DateTime.now();
        List<DataValue> results = new ArrayList<>();
        for (ReadValueId item: context.getNodesToRead())
        {
            results.add(new DataValue(new Variant(1), StatusCode.GOOD, now, DateTime.NULL_VALUE));
        }

        return CompletableFuture.completedFuture(results);
    }

    @Override
    public CompletableFuture<List<StatusCode>> write(WriteContext context) throws UaRuntimeException
    {
        ArrayList<StatusCode> results = new ArrayList<>();
        for (WriteValue item: context.getNodesToWrite())
        {
            results.add(StatusCode.GOOD);
        }

        return CompletableFuture.completedFuture(results);
    }

    @Override
    public CompletableFuture<List<CallMethodResult>> call(CallContext context) throws UaRuntimeException
    {
        ArrayList<CallMethodResult> results = new ArrayList<>();
        for (CallMethodRequest item: context.getMethodsToCall())
        {
            Variant[] outputArguments = new Variant[2];
            outputArguments[0] = new Variant("abc");
            outputArguments[1] = new Variant(false);

            results.add(new CallMethodResult(StatusCode.GOOD, null, null,outputArguments));
        }

        return CompletableFuture.completedFuture(results);
    }

    @Override
    public CompletableFuture<List<HistoryReadResult>> historyRead(HistoryReadContext context) throws UaRuntimeException
    {
        List<HistoryReadResult> results = new ArrayList<>();
        ByteString cp = new ByteString("abc".getBytes());

        for (HistoryReadValueId item : context.getNodesToRead())
        {
            System.out.println(
                    "NodeId: " + item.getNodeId().toParseableString() +
                    " CP: " + item.getContinuationPoint().toString());

            if (context.getHistoryReadDetails() instanceof ReadEventDetails details) {
                System.out.println(
                        "StartTime: " + details.getStartTime().toIso8601String() +
                                " EndTime: " + details.getEndTime().toIso8601String() +
                                " NumberPerNode: " + details.getNumValuesPerNode());

                System.out.println("Select");

                if (null != details.getFilter().getSelectClauses()) {
                    for (SimpleAttributeOperand item2 : details.getFilter().getSelectClauses()) {
                        System.out.println(Arrays.toString(item2.getBrowsePath()));
                    }
                }

                EventQuery query = EventQuery.getQuery(details.getFilter().getWhereClause());
                for (EventQueryElement item2 : query.getElements())
                {
                    System.out.println(item2);
                }

                System.out.println("OrLogic: " + query.getOrLogic());

                List<Variant> eventFieldsValue = new ArrayList<>();
                eventFieldsValue.add(Variant.ofString("abc"));
                eventFieldsValue.add(Variant.ofInt32(12));

                HistoryEventFieldList[] eventsData = new HistoryEventFieldList[1];
                eventsData[0] = new HistoryEventFieldList(eventFieldsValue.toArray(new Variant[0]));

                HistoryEvent event = new HistoryEvent(eventsData);
                ExtensionObject historyData = ExtensionObject.encode(ExtensionObjectEncoder.Encoder.getEncodingContext(), event);

                results.add(new HistoryReadResult(
                        StatusCode.GOOD,
                        cp,
                        historyData));
            } else if (context.getHistoryReadDetails() instanceof ReadRawModifiedDetails details) {
                System.out.println(
                        "StartTime: " + details.getStartTime().toIso8601String() +
                                " EndTime: " + details.getEndTime().toIso8601String() +
                                " NumberPerNode: " + details.getNumValuesPerNode() +
                                " ReturnBound: " + details.getReturnBounds() +
                                " IsModified: " + details.getIsReadModified());

                List<DataValue> dataValues = new ArrayList<>();
                dataValues.add(new DataValue(
                        Variant.ofInt32(15),
                        StatusCode.GOOD,
                        DateTime.now(),
                        null));

                dataValues.add(new DataValue(
                        Variant.ofString("abc"),
                        StatusCode.GOOD,
                        DateTime.now(),
                        null));

                HistoryData data = new HistoryData(dataValues.toArray(new DataValue[0]));
                ExtensionObject historyData = ExtensionObject.encode(ExtensionObjectEncoder.Encoder.getEncodingContext(), data);

                results.add(new HistoryReadResult(
                        StatusCode.GOOD,
                        cp,
                        historyData));
            }
        }

        return CompletableFuture.completedFuture(results);
    }

    @Override
    public CompletableFuture<List<ApplicationDescription>> findServers(FindServersContext context) throws UaRuntimeException
    {
        ArrayList<ApplicationDescription> results = new ArrayList<>();

        String[] discoveryUrls = {"def","efg"};

        ApplicationDescription description = new ApplicationDescription(
                "abc",
                "bcd",
                null,
                ApplicationType.Server,
                null,
                null,
                null);

        results.add(description);

        return CompletableFuture.completedFuture(results);
    }
}
