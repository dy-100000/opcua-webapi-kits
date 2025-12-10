package org.opcfoundation.webserver.service.message.reactiveobject;

import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.util.List;

public class MethodCallResponse {
    private final List<Variant> outputArguments;

    public MethodCallResponse(List<Variant> outputArguments)
    {
        this.outputArguments = outputArguments;
    }

    public List<Variant> getOutputArguments() {
        return outputArguments;
    }
}
