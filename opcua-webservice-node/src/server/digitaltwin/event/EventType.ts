import { UaAccessLevel, UaError, UaLocalizedText, UaNodeId, UaValueRank, makeUaStatusCode } from "opcua-webapi-ts";
import { UaDataType,UaObjectType,UaVariable,UaObjectTypes,UaVariableTypes } from "../../addressspace/nodes";
import { DigitalTwinSpace } from "../DigitalTwinSpace";

export class EventType extends UaObjectType {
    protected readonly nodeManager: DigitalTwinSpace;

    constructor(
        typeId: string,
        displayName: UaLocalizedText,
        twinSpace: DigitalTwinSpace,
        parentType: EventType | null = null) {    
        super(
            new UaNodeId(typeId, twinSpace.nsIndex()), 
            typeId,
            displayName,             
            false);

        this.setParentType(parentType ?? UaObjectTypes.BaseEventType);
        this.nodeManager = twinSpace;
        this.nodeManager.addNode(this);
    }

    addField(
        name: string,
        displayName: UaLocalizedText,
        description: UaLocalizedText,
        dataType: UaDataType,
    ): UaVariable {
        const variableId = `${this.browseName}-${name}`;
        const newVariable = new UaVariable(
            new UaNodeId(variableId, this.nodeManager.nsIndex()),
            name,
            displayName,
            dataType.nodeId,
            UaValueRank.Scalar,
            UaAccessLevel.CurrentRead,
            UaVariableTypes.PropertyType,
        );

        if (description.text.length > 0) {
            newVariable.description = description;
        }

        this.addMember(newVariable);
        this.nodeManager.addNode(newVariable);

        return newVariable;
    }
}
