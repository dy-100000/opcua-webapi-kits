import { NodeClass } from "opcua-webapi";
import { ReferenceTypeIds, UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import {
    BrowseObjectRequest,
    BrowseObjectResponse,
    GetDescriptorRequest,
    GetDescriptorResponse,
    GetObjectElementListRequest,
    GetObjectElementListResponse,
    ReadObjectAttributeRequest,
    ReadObjectAttributeResponse,
} from "../../service/message";
import { UaBrowseAdditionalInfo, UaReferenceDescriptor } from "../../types";
import { ObjectServiceContext } from "../../types/digitaltwin/ObjectServiceContext";
import type { DynamicSubmodelCallback } from "../callback/DynamicSubmodelCallback";
import { SubmodelTypeBase } from "./SubmodelTypeBase";
import { DigitalTwinSpace } from "../DigitalTwinSpace";

export abstract class DynamicSubmodelType extends SubmodelTypeBase implements DynamicSubmodelCallback {
    constructor(
        typeId: string,
        displayName: UaLocalizedText,
        twinSpace: DigitalTwinSpace) {
        super(typeId, displayName, twinSpace);
    }

    // Override to return object elements
    abstract onGetObjectElementList(request: GetObjectElementListRequest): Promise<GetObjectElementListResponse>;

    // Can be override to provide customized descriptor
    async onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse>
    {
        const instance = request.context.objectId.instance;

        if (instance === null) {
            return new GetDescriptorResponse("NotImplemented");
        }

        return new GetDescriptorResponse(instance.displayName, instance.description);
    }

    // Implementation of parent type methods, don't override 
    override async onReadObjectAttributes(request: ReadObjectAttributeRequest): Promise<ReadObjectAttributeResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetDescriptor(new GetDescriptorRequest(context));

        return new ReadObjectAttributeResponse(
            request.objectId.id,
            response.displayName,
            response.description,
        );
    }

    // Implementation of parent type methods, don't override
    override async onBrowseObjectChildren(request: BrowseObjectRequest): Promise<BrowseObjectResponse> {
        if (!request.additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK)) {
            return new BrowseObjectResponse([], false);
        }

        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetObjectElementList(
            new GetObjectElementListRequest(
                context,
                request.additionalInfo.maxReferencesPerNode,
                request.additionalInfo.referenceOffset,
            ),
        );

        return this.processBrowseChildResponse(response);
    }

    private processBrowseChildResponse(response: GetObjectElementListResponse): BrowseObjectResponse {
        const childDescriptors: Array<UaReferenceDescriptor> = [];

        for (const item of response.elements) {
            childDescriptors.push(
                new UaReferenceDescriptor(
                    item.id,
                    NodeClass.Object,
                    item.id,
                    item.displayName,
                    item.typeId,
                    UaNodeId.from(ReferenceTypeIds.HasComponent),
                    true,
                ),
            );
        }

        return new BrowseObjectResponse(childDescriptors, response.containsMoreData);
    }
}
