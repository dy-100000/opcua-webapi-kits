import { NodeClass } from "opcua-webapi";
import { ReferenceTypeIds, UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaObjectTypes } from "../../addressspace/nodes/builtin";
import { UaReferenceTypes } from "../../addressspace/nodes/builtin/UaReferenceTypes";
import { BrowseObjectRequest, BrowseObjectResponse, GetDescriptorRequest, GetDescriptorResponse, GetLinkRequest, GetLinkResponse, ReadObjectAttributeRequest, ReadObjectAttributeResponse } from "../../service/message";
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
     * Internal framework callback used by the base type to advertise link support.
     * Do not call or override this method directly.
     */
    override isGetLinkSupported(): boolean {
        return true;
    }

    /**
     * Internal framework callback used by the base type to browse linked objects.
     * Do not call or override this method directly.
     */
    override async onBrowseObjectLinks(request: BrowseObjectRequest): Promise<BrowseObjectResponse> {
        const referenceTypeId = request.browseDescription.referenceTypeId;
        if (!referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.References)) &&
            !referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.NonHierarchicalReferences))) {
            return new BrowseObjectResponse([], false);
        }

        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetLinks(
            new GetLinkRequest(
                context,
                request.additionalInfo.maxReferencesPerNode,
                request.additionalInfo.referenceOffset,
            ),
        );

        return this.processBrowseLinkResponse(response);
    }

    private processBrowseLinkResponse(response: GetLinkResponse): BrowseObjectResponse {
        const linkDescriptors: Array<UaReferenceDescriptor> = [];

        for (const item of response.targets) {
            const descriptor = item.instanceDeclaration === null
                ? new UaReferenceDescriptor(
                    item.id,
                    NodeClass.Object,
                    item.id,
                    item.displayName,
                    item.typeId,
                    UaReferenceTypes.HasLink.nodeId,
                    true,
                )
                : UaReferenceDescriptor.fromInstanceDeclaration(
                    item.id,
                    item.instanceDeclaration,
                    UaReferenceTypes.HasLink.nodeId,
                    true,
                );

            linkDescriptors.push(descriptor);
        }

        return new BrowseObjectResponse(linkDescriptors, response.containsMoreData, UaBrowseAdditionalInfo.GET_LINK_TASK);
    }
}