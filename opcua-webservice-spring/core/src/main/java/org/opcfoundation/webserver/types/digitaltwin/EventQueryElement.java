package org.opcfoundation.webserver.types.digitaltwin;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.opcfoundation.webapi.mapper.extensionobjects.ExtensionObjectEncoder;

public class EventQueryElement {
    private final String fieldName;
    private final FilterOperator operator;
    private final Variant value;
    private Boolean isNot;

    public EventQueryElement(
            String fieldName,
            FilterOperator operator,
            Variant value)
    {
        this.fieldName = fieldName;
        this.operator = operator;
        this.value = value;
        isNot = false;
    }

    public String getFieldName() {
        return fieldName;
    }

    public FilterOperator getOperator() {
        return operator;
    }

    public Variant getValue() {
        return value;
    }

    public Boolean getNot() {
        return isNot;
    }

    public void setNot(Boolean not) {
        isNot = not;
    }

    public static EventQueryElement getQueryElement(ContentFilterElement element) throws UaRuntimeException
    {
        String fieldName = null;
        Variant value = null;

        if (element.getFilterOperator() == FilterOperator.Not ||
                element.getFilterOperator().getValue() >= FilterOperator.And.getValue()) throw new UaRuntimeException(StatusCodes.Bad_ContentFilterInvalid);

        if (null != element.getFilterOperands())
        {
            for (ExtensionObject item : element.getFilterOperands())
            {
                UaStructuredType struct = item.decode(ExtensionObjectEncoder.Encoder.getEncodingContext());

                if (struct instanceof SimpleAttributeOperand operand)
                {
                    if (null != operand.getBrowsePath())
                    {
                        if (operand.getBrowsePath().length == 1)
                        {
                            fieldName = operand.getBrowsePath()[0].getName();
                        }
                    }
                }

                if (struct instanceof LiteralOperand operand)
                {
                    value = operand.getValue();
                }
            }
        }

        if (null == fieldName || fieldName.isEmpty() || null == value) throw new UaRuntimeException(StatusCodes.Bad_ContentFilterInvalid);

        return new EventQueryElement(fieldName, element.getFilterOperator(), value);
    }

    @Override
    public String toString()
    {
        String ret = "";
        ret += "Field: ";
        ret += fieldName;

        ret += " Operator: ";
        if (isNot) ret += "Not ";
        ret += operator;

        ret += " Value: ";
        ret += value;

        return ret;
    }
}
