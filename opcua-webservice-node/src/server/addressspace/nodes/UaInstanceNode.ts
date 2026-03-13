import { StatusCodes } from "opcua-webapi";
import { UaError, UaLocalizedText, UaNodeId, makeUaStatusCode } from "opcua-webapi-ts";
import { UaNode,UaReference,UaModellingRule, UaObjects, UaReferenceTypes } from ".";

export abstract class UaInstanceNode extends UaNode {
    private _modellingRule: UaModellingRule;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText
    ) {
        super(nodeId, browseName, displayName);
        this._modellingRule = UaModellingRule.None;
    }

    public get modellingRule(): UaModellingRule {
        return this._modellingRule;
    }

    public setModellingRule(modellingRule: UaModellingRule): void {
        if (this._modellingRule !== UaModellingRule.None) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadAlreadyExists));
        }
        if (modellingRule === UaModellingRule.None) {
            return;
        }

        this._modellingRule = modellingRule;

        if (modellingRule === UaModellingRule.Mandatory) {
            this.addReference(new UaReference(UaObjects.ModellingRule_Mandatory, UaReferenceTypes.HasModellingRule, true));
        }
    }
}