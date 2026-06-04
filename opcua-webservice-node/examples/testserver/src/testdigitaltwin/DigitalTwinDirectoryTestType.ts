import { UaLocalizedText } from "opcua-webapi-ts";
import { DigitalTwinSpace } from "opcua-webservice-node";
import { DigitalTwinRepositoryType } from "opcua-webservice-node";
import { GetDigitalTwinListRequest, GetDigitalTwinListResponse } from "opcua-webservice-node";
import { DigitalTwinDescriptor } from "opcua-webservice-node";
import { DigitalTwinSpaceTest } from "./DigitalTwinSpaceTest";

export class DigitalTwinDirectoryTestType extends DigitalTwinRepositoryType {
    constructor(space: DigitalTwinSpace) {
        super("TestDigitalTwinDirectory", new UaLocalizedText("TestDigitalTwinDirectory"), space);
        this.description = new UaLocalizedText("TestDigitalTwinDirectory");
    }

    override async onGetDigitalTwinList(request: GetDigitalTwinListRequest): Promise<GetDigitalTwinListResponse> {
        const response = new GetDigitalTwinListResponse();

        for (let index = request.offset; index < request.offset + 5; ++index) {
            response.add(
                new DigitalTwinDescriptor(
                    `${index}`,
                    new UaLocalizedText(`DT ${index}`),
                    DigitalTwinSpaceTest.digitalTwinTestType,
                ),
            );
        }

        response.containsMoreData = request.offset === 0;
        return response;
    }
}