package org.opcfoundation.uawebservicetest.testdigitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.element.EventElementType;
import org.opcfoundation.webserver.digitaltwin.event.EventData;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorResponse;
import org.opcfoundation.webserver.service.message.digitaltwin.GetEventRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetEventResponse;
import org.opcfoundation.webserver.types.digitaltwin.EventQueryElement;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class EventElementTestType extends EventElementType {
    public EventElementTestType(EventTestType testType, DigitalTwinSpace space) {
        super("EventElementTestType",new LocalizedText("EventElementTestType"), testType, space);
    }

    @Override
    public CompletableFuture<GetEventResponse> onGetEvent(GetEventRequest request) {
        System.out.println(
                "Id: " + request.getId() +
                        " StartTime: " + request.getStartTime() +
                        " EndTime: " + request.getEndTime() +
                        " Limit: " + request.getLimit() +
                        " Offset: " + request.getOffset());

        System.out.println("Select:");
        for (String item : request.getSelect())
        {
            System.out.println(item);
        }

        System.out.println("Where:");
        for (EventQueryElement item : request.getWhere().getElements())
        {
            System.out.println(item);
        }

        EventData data1 = EventTestType.generateEventData(
                "123",
                LocalDateTime.now(),
                "abc",
                "Customized field");

        EventData data2 = EventTestType.generateEventData(
                "456",
                LocalDateTime.now().plusMinutes(1),
                "def",
                "Customized field");

        GetEventResponse response = new GetEventResponse();
        response.addEventData(data1);
        response.addEventData(data2);
        response.setContainsMoreData((0 == request.getOffset()));

        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            return new GetDescriptorResponse(
                    new LocalizedText("EventElement-" + request.getId()),
                    new LocalizedText("EventElement with id " + request.getId()));
        });
    }
}
