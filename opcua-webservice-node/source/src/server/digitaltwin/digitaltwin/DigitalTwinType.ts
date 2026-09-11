import { Attributes, NodeClass, StatusCodes } from "opcua-webapi";
import { ReferenceTypeIds, UaError, UaLocalizedText, UaNodeId, UaStatusCode, UaWriteMask, UaModellingRule } from "opcua-webapi-ts";
import { UaReactiveObjectType } from "../../addressspace/reactiveobject/UaReactiveObjectType";
import { UaObject } from "../../addressspace/nodes";
import { UaObjectTypes } from "../../addressspace/nodes/builtin";
import {
    AddRequest,
    AddResponse,
    DeleteRequest,
    DeleteResponse,
    AddObjectRequest,
    AddObjectResponse,
    BrowseObjectRequest,
    BrowseObjectResponse,
    DeleteObjectRequest,
    DeleteObjectResponse,
    GetDescriptorRequest,
    GetDescriptorResponse,
    GetPermissionRequest,
    GetPermissionResponse,
    GetSubmodelsRequest,
    GetSubmodelsResponse,
    ReadObjectAttributeRequest,
    ReadObjectAttributeResponse,
    ModifyAttributeRequest,
    ModifyAttributeResponse,
    WriteObjectAttributeRequest,
    WriteObjectAttributeResponse,
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
     * Set the submodel type can be added to this repository.
     */
    mayAdd(type: SubmodelTypeBase): void {
        const newObject = this.addObjectNode(type.name, type.displayName, type);       
        newObject.setModellingRule(UaModellingRule.PlaceHolder);
    }

    /**
     * Add a submodel to the digital twin.
     */
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
     * Override in subclasses to provide the descriptor for a digital twin instance.
     */
    abstract onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse>;

    /**
     * Optional override point to provide a custom submodel list.
     */
    async onGetSubmodels(request: GetSubmodelsRequest): Promise<GetSubmodelsResponse> {
        const response = new GetSubmodelsResponse();
        for (const item of this.getMembers()) {
            if (item.nodeClass === NodeClass.Object) {
                response.add(new SubmodelDescriptor(request.id, item as UaObject));
            }
        }

        return response;
    }

    /**
     * Optional override point to get permissions
     */
    async onGetPermission(request: GetPermissionRequest): Promise<GetPermissionResponse> {
        return new GetPermissionResponse(false, false, false);
    }

    /**
     * Optional override point to add a digital twin.
     */
    async onAddDigitalTwin(request: AddRequest): Promise<AddResponse>
    {
        throw new UaError(UaStatusCode.from(StatusCodes.BadNotImplemented));
    }

    /**
     * Optional override point to delete a digital twin.
     */
    async onDeleteDigitalTwin(request: DeleteRequest): Promise<DeleteResponse>
    {
        throw new UaError(UaStatusCode.from(StatusCodes.BadNotImplemented));
    }

    /**
     * Optional override point to rename a digital twin instance.
     */
    async onRename(request: ModifyAttributeRequest): Promise<ModifyAttributeResponse>
    {
        throw new UaError(UaStatusCode.from(StatusCodes.BadNotImplemented));
    }

    /**
     * Optional override point to modify the descriptor of a digital twin instance.
     */
    async onSetDescriptor(request: ModifyAttributeRequest): Promise<ModifyAttributeResponse>
    {
        throw new UaError(UaStatusCode.from(StatusCodes.BadNotImplemented));
    }

    /**
     * Internal framework callback used by the base type to get the reference type id for this repository.
     * Do not call or override this method directly.
     */
    supportedReferenceType(): UaNodeId {
        return UaNodeId.from(ReferenceTypeIds.HasComponent);
    }

    /**
     * Internal framework callback used by the base type to read object attributes.
     * Do not call or override this method directly.
     */
    override async onReadObjectAttributes(request: ReadObjectAttributeRequest): Promise<ReadObjectAttributeResponse> {
        const context = new ObjectServiceContext(request.objectId);
        
        let displayName = UaLocalizedText.nullText;
        let description = UaLocalizedText.nullText;
        
        if (request.attributeIds.has(Attributes.DisplayName) || request.attributeIds.has(Attributes.Description)) {
            let response = await this.onGetDescriptor(new GetDescriptorRequest(context));
            displayName = response.displayName;
            description = response.description;
        }
        
        let writeMask = 0;
        if (request.attributeIds.has(Attributes.WriteMask) || request.attributeIds.has(Attributes.UserWriteMask)) {
            if (request.objectId.instance === null) {
                let response = await this.onGetPermission(new GetPermissionRequest(context));
                if (response.deletePermission) {
                    writeMask |= UaWriteMask.NodeId;
                }
                if (response.renamePermission) {
                    writeMask |= UaWriteMask.DisplayName;
                }
                if (response.modifyDescriptorPermission) {
                    writeMask |= UaWriteMask.Description;
                }
            }
        }

        return new ReadObjectAttributeResponse(
            request.objectId.id,
            displayName,
            description,
            0,
            writeMask);
    }

    /**
     * Internal framework callback used by the base type to write object attributes.
     * Do not call or override this method directly.
     */
    override async onWriteObjectAttributes(request: WriteObjectAttributeRequest): Promise<WriteObjectAttributeResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const permissionResponse = await this.onGetPermission(new GetPermissionRequest(context));

        let displayNameStatusCode = UaStatusCode.from(StatusCodes.BadNotWritable);
        let descriptionStatusCode = UaStatusCode.from(StatusCodes.BadNotWritable);

        if (request.displayName !== null) {
            if (permissionResponse.renamePermission)  
            {
                try {      
                    let response = await this.onRename(new ModifyAttributeRequest(context, request.displayName));
                    displayNameStatusCode = response.statusCode;
                } catch (error) {  
                    if (error instanceof UaError) displayNameStatusCode = error.statusCode;                
                }
            } else { displayNameStatusCode = UaStatusCode.from(StatusCodes.BadUserAccessDenied); }
        }   

        if (request.description !== null) {
            if (permissionResponse.modifyDescriptorPermission) {
                try {
                    let response = await this.onSetDescriptor(new ModifyAttributeRequest(context, request.description));
                    descriptionStatusCode = response.statusCode;
                } catch (error) {
                    if (error instanceof UaError) descriptionStatusCode = error.statusCode;
                }
            } else { descriptionStatusCode = UaStatusCode.from(StatusCodes.BadUserAccessDenied); }
        }

        return new WriteObjectAttributeResponse(displayNameStatusCode, descriptionStatusCode);
    }

    /**
     * Internal framework callback used by the base type to browse child submodels.
     * Do not call or override this method directly.
     */
    override async onBrowseObject(request: BrowseObjectRequest): Promise<BrowseObjectResponse> {
        if (!request.additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_RELATED_OBJECT_TASK)) {
            return new BrowseObjectResponse([]);
        }

        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetSubmodels(new GetSubmodelsRequest(context));
        return this.processBrowseObjectResponse(response);
    }

    override async onAddObject(request: AddObjectRequest): Promise<AddObjectResponse> {
        const context = new ObjectServiceContext(request.parentId);
        const addRequest = new AddRequest(
            context,
            request.displayName);
        
        let response = await this.onAddDigitalTwin(addRequest);
        return new AddObjectResponse(response.newId);
    }

    override async onDeleteObject(request: DeleteObjectRequest): Promise<DeleteObjectResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const permissionResponse = await this.onGetPermission(new GetPermissionRequest(context));
        if (!permissionResponse.deletePermission) {
            throw new UaError(UaStatusCode.from(StatusCodes.BadUserAccessDenied));
        }

        const deleteRequest = new DeleteRequest(context);
        let response = await this.onDeleteDigitalTwin(deleteRequest);
        return new DeleteObjectResponse(response.statusCode);
    }

    private processBrowseObjectResponse(response: GetSubmodelsResponse): BrowseObjectResponse {
        const childDescriptors: Array<UaReferenceDescriptor> = [];
        const referenceType = this.supportedReferenceType();

        for (const item of response.submodels) {
            const descriptor = (item.instance === null)
                ? new UaReferenceDescriptor(
                    item.id,
                    NodeClass.Object,
                    item.id,
                    item.displayName,
                    item.typeId,
                    referenceType)
                : UaReferenceDescriptor.fromInstanceDeclaration(
                    item.id,
                    item.instance);

            childDescriptors.push(descriptor);
        }

        return new BrowseObjectResponse(childDescriptors);
    }
}