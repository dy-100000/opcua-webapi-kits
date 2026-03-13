package org.opcfoundation.webserver.digitaltwin.element;

import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.structured.EventNotifierType;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadEventDetails;
import org.opcfoundation.webserver.addressspace.nodes.UaObjectType;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.addressspace.nodes.UaReference;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaReferenceTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.callback.EventElementCallback;
import org.opcfoundation.webserver.digitaltwin.event.EventData;
import org.opcfoundation.webserver.digitaltwin.event.EventType;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetEventRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetEventResponse;
import org.opcfoundation.webserver.service.message.reactiveobject.*;
import org.opcfoundation.webserver.types.digitaltwin.ObjectServiceContext;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class EventElementType extends ElementType implements EventElementCallback {
    private final static Set<String> DefaultEventField = new HashSet<>();

    private final UaObjectType eventType;

    public EventElementType(
            String typeId,
            LocalizedText displayName,
            @Nullable EventType generateEventType,
            DigitalTwinSpace twinSpace)
    {
        super(
                typeId,
                displayName,
                UaObjectTypes.EventElementType,
                twinSpace);

        if (DefaultEventField.isEmpty())
        {
            DefaultEventField.add("EventId");
            DefaultEventField.add("EventType");
            DefaultEventField.add("Time");
            DefaultEventField.add("Message");
        }

        eventType = (null == generateEventType) ? UaObjectTypes.BaseEventType : generateEventType;
        this.addReference(new UaReference(eventType, UaReferenceTypes.GeneratesEvent, true));
    }

    @Override
    public final CompletableFuture<ReadObjectAttributeResponse> onReadObjectAttributes(ReadObjectAttributeRequest request)
    {
        UaObject instanceDeclaration = request.getObjectId().getInstance();
        UByte historyReadEventNotifier = UByte.valueOf(EventNotifierType.Field.HistoryRead.getBitIndex());

        if (null == instanceDeclaration)
        {
            ObjectServiceContext context = new ObjectServiceContext(request.getObjectId());
            GetDescriptorRequest getDescriptorRequest = new GetDescriptorRequest(context);

            return onGetDescriptor(getDescriptorRequest).thenApply(response -> {
                return new ReadObjectAttributeResponse(request.getObjectId().getId(), response.getDisplayName(), response.getDescription(), historyReadEventNotifier);
            });
        } else {
            return CompletableFuture.completedFuture(
                    new ReadObjectAttributeResponse(
                            instanceDeclaration.browseName(),
                            instanceDeclaration.displayName(),
                            instanceDeclaration.description(),
                            historyReadEventNotifier));
        }
    }

    @Override
    public final CompletableFuture<ReadHistoryEventResponse> onReadHistoryEvent(ReadHistoryEventRequest request)
    {
        ObjectServiceContext context = new ObjectServiceContext(request.getObjectId());
        final GetEventRequest getEventRequest = GetEventRequest.getRequest(context, (ReadEventDetails) request.getDetails(), request.getOffset());

        return onGetEvent(getEventRequest).
                thenApply(getEventResponse -> {
                    return processReadHistoryEventResponse(getEventRequest, getEventResponse);
                });
    }

    private ReadHistoryEventResponse processReadHistoryEventResponse(GetEventRequest request, GetEventResponse response) {
        List<String> select = request.getSelect();
        List<HistoryEventFieldList> eventFieldLists = new ArrayList<>();

        for (EventData item: response.getEventsData())
        {
            List<Variant> eventFieldsValue = new ArrayList<>();

            for (String field: select)
            {
                Variant value = getFieldValue(field, item);
                eventFieldsValue.add(value);
            }

            eventFieldLists.add(new HistoryEventFieldList(eventFieldsValue.toArray(new Variant[0])));
        }

        return new ReadHistoryEventResponse(eventFieldLists, response.containsMoreData());
    }

    private Variant getFieldValue(String fieldName, EventData eventData)
    {
        if (DefaultEventField.contains(fieldName))
        {
            return switch (fieldName) {
                case "EventId" -> Variant.ofByteString(ByteString.of(eventData.getEventId().getBytes()));
                case "EventType" -> Variant.ofNodeId(eventType.nodeId());
                case "Time" -> Variant.ofDateTime(eventData.getTime());
                case "Message" -> Variant.ofLocalizedText(new LocalizedText(eventData.getMessage()));
                default -> Variant.NULL_VALUE;
            };
        } else {
            Variant value = eventData.getEventData().get(fieldName);
            return (null == value) ? Variant.NULL_VALUE : value;
        }
    }
}
