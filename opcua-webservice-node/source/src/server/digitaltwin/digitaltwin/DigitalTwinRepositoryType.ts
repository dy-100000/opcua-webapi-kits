import { NodeClass, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, ReferenceTypeIds, UaError, UaLocalizedText, UaNodeId, UaNodeIdType } from "opcua-webapi-ts";
import { UaReactiveObjectType } from "../../addressspace/reactiveobject/UaReactiveObjectType";
import { UaObjectTypes } from "../../addressspace/nodes/builtin";
import {
    BrowseObjectRequest,
    BrowseObjectResponse,
    GetDigitalTwinListRequest,
    GetDigitalTwinListResponse,
    ReadObjectAttributeRequest,
    ReadObjectAttributeResponse,
} from "../../service/message";
import { UaBrowseAdditionalInfo, UaInstanceIdentifier, UaObjectIdentifier, UaReferenceDescriptor } from "../../types";
import { ObjectServiceContext } from "../../types/digitaltwin/ObjectServiceContext";
import { DigitalTwinSpace } from "../DigitalTwinSpace";

export abstract class DigitalTwinRepositoryType extends UaReactiveObjectType {
    constructor(
        typeId: string,
        displayName: UaLocalizedText,
        twinSpace: DigitalTwinSpace) {
        super(typeId, displayName, UaObjectTypes.DigitalTwinRepositoryType, twinSpace);
    }

    digitalTwinSpace(): DigitalTwinSpace {
        return this.nodeManager as DigitalTwinSpace;
    }

    /**
     * Override in subclasses to provide the digital twin list for this repository.
     */
    abstract onGetDigitalTwinList(request: GetDigitalTwinListRequest): Promise<GetDigitalTwinListResponse>;

    /**
     * Internal framework callback used by the base type to browse repository children.
     * Do not call or override this method directly.
     */
    override async onBrowseObjectChildren(request: BrowseObjectRequest): Promise<BrowseObjectResponse> {
        const referenceTypeId = request.browseDescription.referenceTypeId;

        if (!request.additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK) ||            
            (!referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HierarchicalReferences)) &&
             !referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.Organizes))))
        {
            return new BrowseObjectResponse([], false);
        }

        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetDigitalTwinList(
            new GetDigitalTwinListRequest(
                context,
                request.additionalInfo.maxReferencesPerNode,
                request.additionalInfo.referenceOffset,
            ),
        );

        return this.processBrowseObjectChildrenResponse(response);
    }

    /**
     * Internal framework callback used by the base type to read repository attributes.
     * Do not call or override this method directly.
     */
    override async onReadObjectAttributes(request: ReadObjectAttributeRequest): Promise<ReadObjectAttributeResponse> {
        const objectIdentifier = new UaInstanceIdentifier(
            new UaObjectIdentifier(this.nodeId.toString(), request.objectId.id, null),
            null,
        );
        const directoryId = new UaNodeId(objectIdentifier.toByteString(), this.nodeManager.nsIndex(), UaNodeIdType.BYTESTRING);
        const directoryNode = this.nodeManager.getNode(directoryId);

        if (directoryNode === null || directoryNode.nodeClass !== NodeClass.Object) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
        }

        return new ReadObjectAttributeResponse(
            request.objectId.id,
            directoryNode.displayName,
            directoryNode.description,
        );
    }

    private processBrowseObjectChildrenResponse(response: GetDigitalTwinListResponse): BrowseObjectResponse {
        const childDescriptors: Array<UaReferenceDescriptor> = [];

        for (const item of response.digitalTwins) {
            childDescriptors.push(
                new UaReferenceDescriptor(
                    item.id,
                    NodeClass.Object,
                    item.id,
                    item.displayName,
                    item.typeId,
                    UaNodeId.from(ReferenceTypeIds.Organizes),
                    false,
                ),
            );
        }

        return new BrowseObjectResponse(childDescriptors, response.containsMoreData);
    }
}