import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId, UaReference } from "../../common/types";
import { UaNode } from "../../common/nodes/UaNode";
import { UaObject } from "../../common/nodes/UaObject";
import { UaMethod } from "../../common/nodes/UaMethod";
import { UaObjectType } from "../../common/nodes/UaObjectType";
import { UaDataType } from "../../common/nodes/UaDataType";
import { UaVariableType } from "../../common/nodes/UaVariableType";
import { UaReferenceType } from "../../common/nodes/UaReferenceType";
import { OpcUaVariableAttributes, UaVariable } from "../..";

export class ReferenceToNode {
    static fromReference(reference: UaReference): UaNode | null {
        if (reference.nodeClass === NodeClass.Object) 
        {
            return new UaObject(reference.nodeId, reference.browseName, reference.displayName, 0, reference.typeDefinitionId);
        } else if (reference.nodeClass === NodeClass.Method) {
            return new UaMethod(reference.nodeId, reference.browseName, reference.displayName);
        } else if (reference.nodeClass === NodeClass.ObjectType) {
            return new UaObjectType(reference.nodeId, reference.browseName, reference.displayName, false);
        } else if (reference.nodeClass === NodeClass.DataType) {
            return new UaDataType(reference.nodeId, reference.browseName, reference.displayName, false);
        } else if (reference.nodeClass === NodeClass.VariableType) {
            return new UaVariableType(reference.nodeId, reference.browseName, reference.displayName, false, UaNodeId.nullNodeId, -1);
        } else if (reference.nodeClass === NodeClass.ReferenceType) {
            return new UaReferenceType(reference.nodeId, reference.browseName, reference.displayName, false, UaLocalizedText.nullText, false);
        }

        return null;
    }

    static fromVariableAttribute(reference: UaReference, variableAttributes: OpcUaVariableAttributes): UaVariable | null {
        if (reference.nodeClass !== NodeClass.Variable) return null;
        return new UaVariable(
            reference.nodeId, 
            reference.browseName, 
            reference.displayName, 
            variableAttributes.dataType, 
            variableAttributes.valueRank, 
            variableAttributes.accessLevel, 
            variableAttributes.userAccessLevel, 
            variableAttributes.historizing,
            reference.typeDefinitionId);
    }
}