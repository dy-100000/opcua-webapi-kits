import { NodeClass } from "opcua-webapi";
import { UaLocalizedText } from "opcua-webapi-ts";
import { UaObject } from "../../../src/server/addressspace/nodes";
import { DigitalTwinSpace } from "../../../src/server/digitaltwin";
import { DigitalTwinType } from "../../../src/server/digitaltwin/digitaltwin/DigitalTwinType";
import { GetDescriptorRequest, GetDescriptorResponse, GetSubmodelsRequest, GetSubmodelsResponse } from "../../../src/server/service/message";
import { SubmodelDescriptor } from "../../../src/server/types/digitaltwin/SubmodelDescriptor";
import { DigitalTwinSpaceTest } from "./DigitalTwinSpaceTest";
import { DynamicSubmodelTestType } from "./DynamicSubmodelTestType";
import { SubmodelTestType } from "./SubmodelTestType";

export class DigitalTwinTestType extends DigitalTwinType {
    constructor(
        submodel: SubmodelTestType,
        elementListSubmodel: DynamicSubmodelTestType,
        space: DigitalTwinSpace,
    ) {
        super("TestDigitalTwin", new UaLocalizedText("TestDigitalTwin"), space);
        this.description = new UaLocalizedText("TestDigitalTwin");

        this.addSubmodel(
            submodel,
            "Submodel",
            new UaLocalizedText("Submodel"),
            new UaLocalizedText("Test Submodel"),
        );

        this.addSubmodel(
            elementListSubmodel,
            "ElementListSubmodel",
            UaLocalizedText.from("ElementListSubmodel"),
            UaLocalizedText.from("Test ElementListSubmodel"),
        );
    }

    override async onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse> {
        return new GetDescriptorResponse(
            new UaLocalizedText(`DT ${request.id}`),
            new UaLocalizedText(`Test Digital Twin number ${request.id}`),
        );
    }

    override async onGetSubmodels(request: GetSubmodelsRequest): Promise<GetSubmodelsResponse> {
        const response = new GetSubmodelsResponse();
        const submodels = this.getMembers().filter((item) => item.nodeClass === NodeClass.Object) as Array<UaObject>;

        let id = request.id;
        if (id === "5") {
            id = `${id}-16`;
        }

        for (const item of submodels) {
            if (id === "4" && item.browseName === "ElementListSubmodel") {
                continue;
            }

            response.add(new SubmodelDescriptor(id, item));
        }

        if (id === "3") {
            response.add(new SubmodelDescriptor(id, new UaLocalizedText(`Submodel-${id}`), DigitalTwinSpaceTest.submodelTestType));
        }

        return response;
    }
}