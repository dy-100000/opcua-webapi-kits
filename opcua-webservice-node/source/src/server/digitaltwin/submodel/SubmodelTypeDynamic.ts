import { NodeClass } from "opcua-webapi";
import { ReferenceTypeIds, UaLocalizedText, UaNodeId, UaModellingRule } from "opcua-webapi-ts";
import {
    BrowseObjectRequest,
    BrowseObjectResponse,
    GetObjectElementListRequest,
    GetObjectElementListResponse
} from "../../service/message";
import { UaBrowseAdditionalInfo, UaReferenceDescriptor } from "../../types";
import { ObjectServiceContext } from "../../types/digitaltwin/ObjectServiceContext";
import { SubmodelTypeBase } from "./SubmodelTypeBase";
import { DigitalTwinSpace } from "../DigitalTwinSpace";
import { ElementType } from "../../..";

export abstract class SubmodelTypeDynamic extends SubmodelTypeBase {
    constructor(
        typeId: string,
        displayName: UaLocalizedText,
        twinSpace: DigitalTwinSpace) {
        super(typeId, displayName, twinSpace);
    }

    /**
     * Set the element type can be added to this submodel.
     */
    mayAdd(type: ElementType): void {
        const newObject = this.addObjectNode(type.name, type.displayName, type);       
        newObject.setModellingRule(UaModellingRule.PlaceHolder);
    }

    /**
     * Override in subclasses to return dynamic child objects.
     */
    abstract onGetObjectElementList(request: GetObjectElementListRequest): Promise<GetObjectElementListResponse>;

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
