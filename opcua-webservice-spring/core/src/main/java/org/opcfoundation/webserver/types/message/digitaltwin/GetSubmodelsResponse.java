package org.opcfoundation.webserver.types.message.digitaltwin;

import org.opcfoundation.webserver.types.SubmodelDescriptor;

import java.util.ArrayList;
import java.util.List;

public class GetSubmodelsResponse {
    private final List<SubmodelDescriptor> submodels;

    public GetSubmodelsResponse()
    {
        this.submodels = new ArrayList<>();
    }

    public void add(SubmodelDescriptor descriptor)
    {
        submodels.add(descriptor);
    }

    public List<SubmodelDescriptor> getSubmodels() {
        return submodels;
    }
}
