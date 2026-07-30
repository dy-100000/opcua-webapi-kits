import { UaLocalizedText } from "opcua-webapi-ts";
import { 
    DigitalTwinSpace,
    DigitalTwinRepositoryType,
    DigitalTwinDescriptor,
    GetDigitalTwinListRequest, 
    GetDigitalTwinListResponse } from "../../src";
import { DigitalTwinSpaceTest,DigitalTwinTestType } from ".";

export class DigitalTwinDirectoryTestType extends DigitalTwinRepositoryType {
    constructor(digitalTwinTestType: DigitalTwinTestType, space: DigitalTwinSpace) {
        super("TestDigitalTwinDirectory", new UaLocalizedText("TestDigitalTwinDirectory"), space);
        this.description = new UaLocalizedText("TestDigitalTwinDirectory");
        this.mayAdd(digitalTwinTestType);
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