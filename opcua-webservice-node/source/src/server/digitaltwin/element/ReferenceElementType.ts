import { NodeClass } from "opcua-webapi";
import { ReferenceTypeIds, UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaObjectTypes } from "../../addressspace/nodes/builtin";
import { BrowseObjectRequest, BrowseObjectResponse, GetLinkRequest, GetLinkResponse } from "../../service/message";
import { UaBrowseAdditionalInfo, UaReferenceDescriptor } from "../../types";
import { ObjectServiceContext } from "../../types/digitaltwin/ObjectServiceContext";
import { ElementType } from "./ElementType";
import { DigitalTwinSpace } from "../DigitalTwinSpace";

export abstract class ReferenceElementType extends ElementType {
    constructor(
        typeId: string,
        displayName: UaLocalizedText,
        twinSpace: DigitalTwinSpace) {
        super(typeId, displayName, UaObjectTypes.ReferenceElementType, twinSpace);
    }

    /**
     * Override in subclasses to return linked objects.
     */
    abstract onGetLinks(request: GetLinkRequest): Promise<GetLinkResponse>;   

    /**
     * Internal framework callback used by the base type to get the reference type id for this repository.
     * Do not call or override this method directly.
     */
    supportedReferenceType(): UaNodeId {
        return UaNodeId.from(ReferenceTypeIds.Organizes);
    }

    /**
     * Internal framework callback used by the base type to browse linked objects.
     * Do not call or override this method directly.
     */
    override async onBrowseObject(request: BrowseObjectRequest): Promise<BrowseObjectResponse> {
        if (!request.additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_RELATED_OBJECT_TASK)) {
            return new BrowseObjectResponse([]);
        }

        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetLinks(
            new GetLinkRequest(
                context,
                request.additionalInfo.maxReferencesPerNode,
                request.additionalInfo.referenceOffset,
            ),
        );

        return this.processBrowseObjectResponse(response,request.additionalInfo);
    }

    private processBrowseObjectResponse(
        response: GetLinkResponse,
        additionalInfo: UaBrowseAdditionalInfo): BrowseObjectResponse {
        const linkDescriptors: Array<UaReferenceDescriptor> = [];
        const referenceType = this.supportedReferenceType();

        for (const item of response.targets) {
            const descriptor = new UaReferenceDescriptor(
                    item.id,
                    NodeClass.Object,
                    item.id,
                    item.displayName,
                    item.typeId,
                    referenceType,
                    item.instanceDeclaration === null ? UaNodeId.nullNodeId : item.instanceDeclaration.nodeId);

            linkDescriptors.push(descriptor);
        }

        let taskMask = (response.containsMoreData) ? UaBrowseAdditionalInfo.GET_RELATED_OBJECT_TASK : 0;
        return new BrowseObjectResponse(linkDescriptors, taskMask, additionalInfo.referenceOffset + response.targets.length);
    }
}