package org.opcfoundation.webserver.addressspace.nodes;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjects;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaReferenceTypes;

public abstract class UaInstanceNode extends UaNode {
    private UaModellingRule modellingRule;

    public UaInstanceNode(
            NodeId nodeId,
            String browseName,
            LocalizedText displayName)
    {
        super(nodeId, browseName, displayName);
        modellingRule = UaModellingRule.None;
    }

    public UaModellingRule modellingRule() {
        return modellingRule;
    }

    public void setModellingRule(UaModellingRule modellingRule) throws UaRuntimeException {
        if (UaModellingRule.None != this.modellingRule) throw new UaRuntimeException(StatusCodes.Bad_AlreadyExists);
        if (UaModellingRule.None == modellingRule) return;

        this.modellingRule = modellingRule;

        if (UaModellingRule.Mandatory == modellingRule)
        {
            addReference(new UaReference(UaObjects.ModellingRule_Mandatory, UaReferenceTypes.HasModellingRule, true));
        }
    }
}
