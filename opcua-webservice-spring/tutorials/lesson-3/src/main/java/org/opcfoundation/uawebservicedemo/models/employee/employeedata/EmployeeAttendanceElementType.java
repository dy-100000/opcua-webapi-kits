package org.opcfoundation.uawebservicedemo.models.employee.employeedata;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.opcfoundation.uawebservicedemo.database.EmployeeDatabase;
import org.opcfoundation.uawebservicedemo.database.entity.EmployeeCheckIn;
import org.opcfoundation.uawebservicedemo.database.mapper.EmployeeMapper;
import org.opcfoundation.uawebservicedemo.models.employee.employeedata.event.EmployeeCheckInEventType;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.element.EventElementType;
import org.opcfoundation.webserver.service.message.digitaltwin.GetEventRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetEventResponse;
import org.opcfoundation.webserver.types.digitaltwin.EventQueryElement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EmployeeAttendanceElementType extends EventElementType {
    public EmployeeAttendanceElementType(EmployeeCheckInEventType eventType, DigitalTwinSpace space) {
        super("EmployeeAttendanceElementType",new LocalizedText("EmployeeAttendanceElementType"), eventType, space);
    }

    @Override
    public CompletableFuture<GetEventResponse> onGetEvent(GetEventRequest request) {


        EmployeeMapper employeeMapper = EmployeeDatabase.getEmployeeMapper();

        Integer employeeId = Integer.parseInt(request.getId());
        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = request.getEndTime();
        Boolean checkIn = null;

        for (EventQueryElement item: request.getWhere().getElements())
        {
            if (item.getFieldName().equals(EmployeeCheckInEventType.CheckIn) &&
                    item.getOperator() == FilterOperator.Equals)
            {
                if (!(item.getValue().getValue() instanceof Boolean)) throw new UaRuntimeException(StatusCodes.Bad_EventFilterInvalid);

                checkIn = (Boolean) item.getValue().getValue();
                if (item.getNot()) checkIn = !checkIn;
            }
        }

        List<EmployeeCheckIn> results = employeeMapper.getCheckInData(employeeId, startTime, endTime, checkIn);

        GetEventResponse response = new GetEventResponse();
        for (EmployeeCheckIn item : results)
        {
            response.addEventData(EmployeeCheckInEventType.generateEventData(item));
        }

        return CompletableFuture.completedFuture(response);
    }
}
