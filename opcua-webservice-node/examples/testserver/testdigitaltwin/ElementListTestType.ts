import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaLocalizedText, UaVariant } from "opcua-webapi-ts";
import { UaDataTypes, UaVariableTypes } from "../../../src/server/addressspace/nodes";
import { DigitalTwinSpace } from "../../../src/server/digitaltwin";
import { ElementListType } from "../../../src/server/digitaltwin/element";
import {
    GetObjectElementListRequest,
    GetObjectElementListResponse,
    GetPropertyDescriptorRequest,
    GetPropertyDescriptorResponse,
    GetPropertyElementListRequest,
    GetPropertyElementListResponse,
    GetPropertySubElementsRequest,
    GetPropertySubElementsResponse,
    ReadPropertyListValueRequest,
    ReadPropertyListValueResponse,
    WritePropertyListValuesRequest,
    WritePropertyListValuesResponse,
} from "../../../src/server/service/message";
import { ObjectElementDescriptor } from "../../../src/server/types/digitaltwin/ObjectElementDescriptor";
import { PropertyElementDescriptor } from "../../../src/server/types/digitaltwin/PropertyElementDescriptor";
import { DigitalTwinSpaceTest } from "./DigitalTwinSpaceTest";

export class ElementListTestType extends ElementListType {
    constructor(space: DigitalTwinSpace) {
        super("ElementListTestType", new UaLocalizedText("ElementListTestType"), space);
        this.description = new UaLocalizedText("ElementListTestType");
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

    override supportObjectElementList(): boolean {
        return true;
    }

    override async onGetPropertyElementList(request: GetPropertyElementListRequest): Promise<GetPropertyElementListResponse> {
        const response = new GetPropertyElementListResponse();

        for (let index = request.offset; index < request.offset + 5; ++index) {
            response.add(
                new PropertyElementDescriptor(
                    `${index}`,
                    new UaLocalizedText(`Var-${index}`),
                    UaVariableTypes.DataItemType,
                ),
            );
        }

        response.containsMoreData = request.offset === 0;
        return response;
    }

    override supportPropertyElementList(): boolean {
        return true;
    }

    override async onGetPropertyDescriptor(request: GetPropertyDescriptorRequest): Promise<GetPropertyDescriptorResponse> {
        if (request.subElementName === null) {
            return new GetPropertyDescriptorResponse(
                new UaLocalizedText(`Var-${request.propertyId}`),
                new UaLocalizedText(`Variable number ${request.propertyId}`),
                UaDataTypes.String,
                true,
            );
        }

        return new GetPropertyDescriptorResponse(
            new UaLocalizedText(`Element-${request.subElementName}`),
            new UaLocalizedText(`Sub-element ${request.subElementName}`),
            UaDataTypes.String,
            false,
        );
    }

    override async onReadPropertyValues(request: ReadPropertyListValueRequest): Promise<ReadPropertyListValueResponse> {
        const response = new ReadPropertyListValueResponse();

        for (const propertyId of request.propertyIds) {
            response.setValue(propertyId, UaVariant.string(propertyId));
        }

        for (const subPropertyId of request.subPropertyIds) {
            if (subPropertyId.subElementName === null) {
                continue;
            }

            response.setValue(subPropertyId, UaVariant.string(subPropertyId.subElementName));
        }

        return response;
    }

    override async onWritePropertyValues(request: WritePropertyListValuesRequest): Promise<WritePropertyListValuesResponse> {
        console.log(`Write object, ${request.id}`);

        const response = new WritePropertyListValuesResponse();

        for (const [key, value] of request.propertyIdsAndValues) {
            console.log(key, value.value);
            response.setWriteValueResult(key, makeUaStatusCode(StatusCodes.Good));
        }

        for (const [key, value] of request.subPropertyIdsAndValues) {
            console.log(key.toString(), value.value);
            response.setWriteValueResult(key, makeUaStatusCode(StatusCodes.Good));
        }

        return response;
    }

    override async onGetPropertySubElements(request: GetPropertySubElementsRequest): Promise<GetPropertySubElementsResponse> {
        const response = new GetPropertySubElementsResponse();
        response.add(`Sub-${request.propertyId}`);
        return response;
    }
}