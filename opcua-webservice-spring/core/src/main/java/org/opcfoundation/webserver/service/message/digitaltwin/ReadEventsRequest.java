package org.opcfoundation.webserver.service.message.digitaltwin;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadEventDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.opcfoundation.webserver.types.digitaltwin.EventQuery;
import org.opcfoundation.webserver.types.digitaltwin.ObjectServiceContext;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class ReadEventsRequest {
    private final String id;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    private final List<String> select;
    private final EventQuery where;
    private final boolean ascendingOrder;
    private final int limit;
    private final int offset;

    private final ObjectServiceContext context;

    public ReadEventsRequest(
            ObjectServiceContext context,
            LocalDateTime startTime,
            LocalDateTime endTime,
            List<String> select,
            EventQuery where,
            boolean ascendingOrder,
            int limit,
            int offset)
    {
        this.id = context.getObjectId().getId();
        this.context = context;
        this.startTime = startTime;
        this.endTime = endTime;
        this.select = select;
        this.where = where;
        this.ascendingOrder = ascendingOrder;
        this.limit = limit;
        this.offset = offset;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public List<String> getSelect() {
        return select;
    }

    public EventQuery getWhere() {
        return where;
    }

    public Boolean getAscendingOrder() {
        return ascendingOrder;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public ObjectServiceContext getContext() {
        return context;
    }

    public static ReadEventsRequest getRequest(ObjectServiceContext context, ReadEventDetails details, int offset) throws UaRuntimeException
    {
        LocalDateTime startTime = details.getStartTime().getJavaInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endTime = details.getEndTime().getJavaInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
        boolean ascendingOrder = true;

        List<String> select = new ArrayList<>();
        EventQuery where;

        if (endTime.isBefore(startTime))
        {
            ascendingOrder = false;
            LocalDateTime tmp = endTime;
            endTime = startTime;
            startTime = tmp;
        }

        EventFilter eventFilter = details.getFilter();

        // Select
        if (null == eventFilter.getSelectClauses() || 0 == eventFilter.getSelectClauses().length) throw new UaRuntimeException(StatusCodes.Bad_ContentFilterInvalid);

        for (SimpleAttributeOperand item: eventFilter.getSelectClauses())
        {
            if (null == item.getBrowsePath() || 0 == item.getBrowsePath().length || 2 < item.getBrowsePath().length) throw new UaRuntimeException(StatusCodes.Bad_ContentFilterInvalid);

            String fieldName = item.getBrowsePath()[0].getName();
            if (null == fieldName || fieldName.isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_ContentFilterInvalid);

            if (2 == item.getBrowsePath().length)
            {
                String subFieldName = item.getBrowsePath()[1].getName();
                if (null == subFieldName || subFieldName.isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_ContentFilterInvalid);
                fieldName += "/";
                fieldName += subFieldName;
            }

            select.add(fieldName);
        }

        if (select.size() != eventFilter.getSelectClauses().length) throw new UaRuntimeException(StatusCodes.Bad_ContentFilterInvalid);

        // Where
        where = EventQuery.getQuery(eventFilter.getWhereClause());

        return new ReadEventsRequest(
                context,
                startTime,
                endTime,
                select,
                where,
                ascendingOrder,
                details.getNumValuesPerNode().intValue(),
                offset);
    }
}
