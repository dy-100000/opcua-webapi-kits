import { NodeClass, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, ReferenceTypeIds, UaError, UaLocalizedText, UaNodeId, UaNodeIdType, UaModellingRule } from "opcua-webapi-ts";
import { UaReactiveObjectType } from "../../addressspace/reactiveobject/UaReactiveObjectType";
import { UaObjectTypes } from "../../addressspace/nodes";
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
import { DigitalTwinType } from "../..";

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
     * Set the digital twin type can be added to this repository.
     */
    mayAdd(type: DigitalTwinType): void {
        const newObject = this.addObjectNode(type.name, type.displayName, type);
        newObject.setModellingRule(UaModellingRule.PlaceHolder);
    }

    /**
     * Override in subclasses to provide the digital twin list for this repository.
     */
    abstract onGetDigitalTwinList(request: GetDigitalTwinListRequest): Promise<GetDigitalTwinListResponse>;

    /**
     * Internal framework callback used by the base type to get the reference type id for this repository.
     * Do not call or override this method directly.
     */
    supportedReferenceType(): UaNodeId {
        return UaNodeId.from(ReferenceTypeIds.Organizes);
    }

    /**
     * Internal framework callback used by the base type to browse repository children.
     * Do not call or override this method directly.
     */
    override async onBrowseObject(request: BrowseObjectRequest): Promise<BrowseObjectResponse> {
        if (!request.additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_RELATED_OBJECT_TASK))
        {
            return new BrowseObjectResponse([]);
        }

        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetDigitalTwinList(
            new GetDigitalTwinListRequest(
                context,
                request.additionalInfo.maxReferencesPerNode,
                request.additionalInfo.referenceOffset
            ),
        );

        return this.processBrowseObjectResponse(response,request.additionalInfo);
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

    private processBrowseObjectResponse(
        response: GetDigitalTwinListResponse,
        additionalInfo: UaBrowseAdditionalInfo): BrowseObjectResponse {
        const childDescriptors: Array<UaReferenceDescriptor> = [];
        const referenceType = this.supportedReferenceType();

        for (const item of response.digitalTwins) {
            childDescriptors.push(
                new UaReferenceDescriptor(
                    item.id,
                    NodeClass.Object,
                    item.id,
                    item.displayName,
                    item.typeId,
                    referenceType
                ),
            );
        }

        let taskMask = (response.containsMoreData) ? UaBrowseAdditionalInfo.GET_RELATED_OBJECT_TASK : 0;
        return new BrowseObjectResponse(childDescriptors, taskMask, additionalInfo.referenceOffset + response.digitalTwins.length);
    }
}