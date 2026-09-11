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
     * Internal framework callback used by the base type to get the reference type id for this repository.
     * Do not call or override this method directly.
     */
    supportedReferenceType(): UaNodeId {
        return UaNodeId.from(ReferenceTypeIds.HasComponent);
    }

    /**
     * Internal framework callback used by the base type to browse child nodes.
     * Do not call or override this method directly.
     */
    override async onBrowseObject(request: BrowseObjectRequest): Promise<BrowseObjectResponse> {
        if (!request.additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_RELATED_OBJECT_TASK)) {
            return new BrowseObjectResponse([]);
        }

        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetObjectElementList(
            new GetObjectElementListRequest(
                context,
                request.additionalInfo.maxReferencesPerNode,
                request.additionalInfo.referenceOffset
            ),
        );

        return this.processBrowseObjectResponse(response,request.additionalInfo);
    }

    private processBrowseObjectResponse(
        response: GetObjectElementListResponse,
        additionalInfo: UaBrowseAdditionalInfo): BrowseObjectResponse {
        const childDescriptors: Array<UaReferenceDescriptor> = [];
        const referenceType = this.supportedReferenceType();

        for (const item of response.elements) {
            childDescriptors.push(
                new UaReferenceDescriptor(
                    item.id,
                    NodeClass.Object,
                    item.id,
                    item.displayName,
                    item.typeId,
                    referenceType
                )
            );
        }

        let taskMask = (response.containsMoreData) ? UaBrowseAdditionalInfo.GET_RELATED_OBJECT_TASK : 0;
        return new BrowseObjectResponse(childDescriptors, taskMask, additionalInfo.referenceOffset + response.elements.length);
    }
}
