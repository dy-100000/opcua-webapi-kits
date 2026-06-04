import { UaLocalizedText } from "opcua-webapi-ts";
import { DigitalTwinSpace } from "opcua-webservice-node";
import { DynamicSubmodelType } from "opcua-webservice-node";
import { GetObjectElementListRequest, GetObjectElementListResponse } from "opcua-webservice-node";
import { ObjectElementDescriptor } from "opcua-webservice-node";
import { DigitalTwinSpaceTest } from "./DigitalTwinSpaceTest";

export class DynamicSubmodelTestType extends DynamicSubmodelType {
    constructor(space: DigitalTwinSpace) {
        super("DynamicSubmodelTestType", new UaLocalizedText("DynamicSubmodelTestType"), space);
        this.description = new UaLocalizedText("DynamicSubmodelTestType");
    }

    override async onGetObjectElementList(request: GetObjectElementListRequest): Promise<GetObjectElementListResponse> {
        const response = new GetObjectElementListResponse();

        for (let index = request.offset; index < request.offset + 5; ++index) {
            response.add(
                new ObjectElementDescriptor(
                    `${index}`,
                    new UaLocalizedText(`${index}`),
                    DigitalTwinSpaceTest.elementCollectionTestAType,
                ),
            );
        }

        response.containsMoreData = request.offset === 0;
        return response;
    }
}