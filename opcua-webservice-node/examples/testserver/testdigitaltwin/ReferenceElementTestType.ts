import { UaLocalizedText } from "opcua-webapi-ts";
import { DigitalTwinSpace } from "../../../src/server/digitaltwin";
import { ReferenceElementType } from "../../../src/server/digitaltwin/element";
import { GetDescriptorRequest, GetDescriptorResponse, GetLinkRequest, GetLinkResponse } from "../../../src/server/service/message";
import { ReferenceTargetDescriptor } from "../../../src/server/types/digitaltwin/ReferenceTargetDescriptor";
import { DigitalTwinSpaceTest } from "./DigitalTwinSpaceTest";

export class ReferenceElementTestType extends ReferenceElementType {
    constructor(space: DigitalTwinSpace) {
        super("ReferenceElementTestType", new UaLocalizedText("ReferenceElementTestType"), space);
        this.description = new UaLocalizedText("ReferenceElementTestType");
    }

    override async onGetLinks(request: GetLinkRequest): Promise<GetLinkResponse> {
        const response = new GetLinkResponse();

        for (let index = request.offset; index < request.offset + 5; ++index) {
            response.add(
                new ReferenceTargetDescriptor(
                    `${index}`,
                    new UaLocalizedText(`DT ${index}`),
                    DigitalTwinSpaceTest.digitalTwinTestType,
                ),
            );
        }

        response.containsMoreData = request.offset === 0;
        return response;
    }

    override async onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse> {
        return new GetDescriptorResponse(
            new UaLocalizedText(`ReferenceElement-${request.id}`),
            new UaLocalizedText(`ReferenceElement with id ${request.id}`),
        );
    }
}