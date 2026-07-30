import { StatusCodes } from "opcua-webapi";
import { UaError, UaModellingRule,UaLocalizedText, UaNodeId, makeUaStatusCode } from "opcua-webapi-ts";
import { UaNode } from "./UaNode";
import { UaReference } from "./UaReference";
import { UaReferenceTypes } from "./builtin/UaReferenceTypes";

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

        // require UaObjects at runtime to avoid circular import during module initialization
        const { UaObjects } = require("./builtin");
        if (modellingRule === UaModellingRule.Mandatory) {
            this.addReference(new UaReference(UaObjects.ModellingRule_Mandatory, UaReferenceTypes.HasModellingRule, true));
        } else if (modellingRule === UaModellingRule.Optional) {
            this.addReference(new UaReference(UaObjects.ModellingRule_Optional, UaReferenceTypes.HasModellingRule, true));
        } else if (modellingRule === UaModellingRule.PlaceHolder) {
            this.addReference(new UaReference(UaObjects.ModellingRule_OptionalPlaceHolder, UaReferenceTypes.HasModellingRule, true));
        }
    }
}