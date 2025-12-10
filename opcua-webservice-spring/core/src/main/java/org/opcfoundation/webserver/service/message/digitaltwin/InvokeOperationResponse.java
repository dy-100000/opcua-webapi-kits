package org.opcfoundation.webserver.service.message.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.util.List;

public class InvokeOperationResponse {
    private final List<Variant> outputArguments;

    public InvokeOperationResponse(List<Variant> outputArguments)
    {
        this.outputArguments = outputArguments;
    }

    public List<Variant> getOutputArguments() {
        return outputArguments;
    }
}
