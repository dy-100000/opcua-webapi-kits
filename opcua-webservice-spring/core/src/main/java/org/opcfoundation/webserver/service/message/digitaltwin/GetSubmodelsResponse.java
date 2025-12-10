package org.opcfoundation.webserver.service.message.digitaltwin;

import org.opcfoundation.webserver.types.digitaltwin.SubmodelDescriptor;

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
