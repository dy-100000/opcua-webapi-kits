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
import { SubmodelTypeBase } from "./SubmodelTypeBase";
import { DigitalTwinSpace } from "../DigitalTwinSpace";

export abstract class DynamicSubmodelType extends SubmodelTypeBase {
    constructor(
        typeId: string,
        displayName: UaLocalizedText,
        twinSpace: DigitalTwinSpace) {
        super(typeId, displayName, twinSpace);
    }

    /**
     * Override in subclasses to return dynamic child objects.
     */
    abstract onGetObjectElementList(request: GetObjectElementListRequest): Promise<GetObjectElementListResponse>;

    /**
     * Optional override point to provide a custom descriptor for this instance.
     */
    async onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse>
    {
        const instance = request.context.objectId.instance;

        if (instance === null) {
            return new GetDescriptorResponse("NotImplemented");
        }

        return new GetDescriptorResponse(instance.displayName, instance.description);
    }

    /**
     * Internal framework callback used by the base type to read object attributes.
     * Do not call or override this method directly.
     */
    override async onReadObjectAttributes(request: ReadObjectAttributeRequest): Promise<ReadObjectAttributeResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetDescriptor(new GetDescriptorRequest(context));

        return new ReadObjectAttributeResponse(
            request.objectId.id,
            response.displayName,
            response.description,
        );
    }

    /**
     * Internal framework callback used by the base type to browse child nodes.
     * Do not call or override this method directly.
     */
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
