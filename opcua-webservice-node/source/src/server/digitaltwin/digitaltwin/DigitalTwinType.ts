import { NodeClass } from "opcua-webapi";
import { ReferenceTypeIds, UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaReactiveObjectType } from "../../addressspace/reactiveobject/UaReactiveObjectType";
import { NodeManager } from "../../addressspace/nodemanager/NodeManager";
import { UaModellingRule } from "../../addressspace/nodes/UaModellingRule";
import { UaObject } from "../../addressspace/nodes/UaObject";
import { UaObjectTypes } from "../../addressspace/nodes/builtin";
import {
    BrowseObjectRequest,
    BrowseObjectResponse,
    GetDescriptorRequest,
    GetDescriptorResponse,
    GetSubmodelsRequest,
    GetSubmodelsResponse,
    ReadObjectAttributeRequest,
    ReadObjectAttributeResponse,
} from "../../service/message";
import { UaBrowseAdditionalInfo, UaReferenceDescriptor } from "../../types";
import { ObjectServiceContext } from "../../types/digitaltwin/ObjectServiceContext";
import { SubmodelDescriptor } from "../../types/digitaltwin/SubmodelDescriptor";
import { DigitalTwinSpace } from "../DigitalTwinSpace";
import { SubmodelTypeBase } from "../submodel";

export abstract class DigitalTwinType extends UaReactiveObjectType {
    constructor(
        typeId: string,
        displayName: UaLocalizedText,
        twinSpace: DigitalTwinSpace) {
        super(typeId, displayName, UaObjectTypes.DigitalTwinType, twinSpace);
    }

    digitalTwinSpace(): DigitalTwinSpace {
        return this.nodeManager as DigitalTwinSpace;
    }

    /**
     * Override in subclasses to provide the descriptor for a digital twin instance.
     */
    abstract onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse>;

    /**
     * Optional override point to provide a custom submodel list.
     */
    async onGetSubmodels(request: GetSubmodelsRequest): Promise<GetSubmodelsResponse> {
        const response = new GetSubmodelsResponse();

        let submodels: Array<UaObject> = [];

        for (const item of this.getMembers()) {
            if (item.nodeClass === NodeClass.Object) {
                response.add(new SubmodelDescriptor(request.id, item as UaObject));
            }
        }

        return response;
    }    

    addSubmodel(
        type: SubmodelTypeBase,
        name: string,
        displayName: UaLocalizedText,
        description: UaLocalizedText): UaObject {
        const newObject = this.addObjectNode(name, displayName, type);

        if (description.text.length > 0) {
            newObject.description = description;
        }

        newObject.setModellingRule(UaModellingRule.Optional);
        return newObject;
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
            response.description
        );
    }

    /**
     * Internal framework callback used by the base type to browse child submodels.
     * Do not call or override this method directly.
     */
    override async onBrowseObjectChildren(request: BrowseObjectRequest): Promise<BrowseObjectResponse> {
        if (!request.additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK)) {
            return new BrowseObjectResponse([], false);
        }

        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetSubmodels(new GetSubmodelsRequest(context));
        return this.processBrowseObjectChildrenResponse(response);
    }

    private processBrowseObjectChildrenResponse(response: GetSubmodelsResponse): BrowseObjectResponse {
        const childDescriptors: Array<UaReferenceDescriptor> = [];

        for (const item of response.submodels) {
            const descriptor = (item.instance === null)
                ? new UaReferenceDescriptor(
                    item.id,
                    NodeClass.Object,
                    item.id,
                    item.displayName,
                    item.typeId,                    
                    UaNodeId.from(ReferenceTypeIds.HasComponent),
                    true)
                : UaReferenceDescriptor.fromInstanceDeclaration(
                    item.id,
                    item.instance,
                    UaNodeId.from(ReferenceTypeIds.HasComponent),
                    true);

            childDescriptors.push(descriptor);
        }

        return new BrowseObjectResponse(childDescriptors, false);
    }
}