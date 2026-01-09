package org.opcfoundation.webserver.types.digitaltwin;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.ElementOperand;
import org.opcfoundation.webapi.mapper.extensionobjects.ExtensionObjectEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventQuery {
    private final List<EventQueryElement> elements;
    private final boolean OrLogic;

    public EventQuery(
            List<EventQueryElement> elements,
            boolean OrLogic)
    {
        this.elements = elements;
        this.OrLogic = OrLogic;
    }

    public Boolean isEmpty()
    {
        return elements.isEmpty();
    }

    public List<EventQueryElement> getElements() {
        return elements;
    }

    public boolean getOrLogic() {
        return OrLogic;
    }

    public static EventQuery getQuery(ContentFilter filter) throws UaRuntimeException
    {
        Map<Integer,EventQueryElement> elements = new HashMap<>();
        Map<Integer, Integer> notElements = new HashMap<>();
        boolean orLogic = false;

        if (null != filter.getElements())
        {
            for (int i=0; i<filter.getElements().length; ++i)
            {
                ContentFilterElement currentElement = filter.getElements()[i];

                if (null == currentElement.getFilterOperands()) throw new UaRuntimeException(StatusCodes.Bad_ContentFilterInvalid);

                if (currentElement.getFilterOperator() == FilterOperator.Not)
                {
                    if (currentElement.getFilterOperands().length != 1) throw new UaRuntimeException(StatusCodes.Bad_ContentFilterInvalid);

                    UaStructuredType struct = currentElement.getFilterOperands()[0].decode(ExtensionObjectEncoder.Encoder.getEncodingContext());
                    if (!(struct instanceof ElementOperand)) throw new UaRuntimeException(StatusCodes.Bad_ContentFilterInvalid);
                    notElements.put(i, ((ElementOperand)struct).getIndex().intValue());

                } else if (currentElement.getFilterOperator() == FilterOperator.Or) {
                    if (0 != currentElement.getFilterOperands().length) throw new UaRuntimeException(StatusCodes.Bad_ContentFilterInvalid);
                    orLogic = true;
                } else if (currentElement.getFilterOperator().getValue() <= FilterOperator.InList.getValue()) {
                    EventQueryElement element = EventQueryElement.getQueryElement(currentElement);
                    elements.put(i, element);
                } else {
                    throw new UaRuntimeException(StatusCodes.Bad_ContentFilterInvalid);
                }
            }
        }

        for (Integer item: notElements.values())
        {
            EventQueryElement element = elements.get(item);
            if (null == element) throw new UaRuntimeException(StatusCodes.Bad_ContentFilterInvalid);
            element.setNot(true);
        }

        return new EventQuery(new ArrayList<>(elements.values()), orLogic);
    }
}
