import { UaError, UaLocalizedText, UaStatusCode, UaWriteMask } from "opcua-webapi-ts";
import { UaReactiveObjectType } from "../../addressspace/reactiveobject/UaReactiveObjectType";
import { UaObjectTypes } from "../../addressspace/nodes/builtin";
import { DigitalTwinSpace } from "../DigitalTwinSpace";
import { AddObjectRequest, AddObjectResponse, AddRequest, AddResponse, DeleteObjectRequest, DeleteObjectResponse, DeleteRequest, DeleteResponse, GetDescriptorRequest, GetDescriptorResponse, GetPermissionRequest, GetPermissionResponse, ModifyAttributeRequest, ModifyAttributeResponse, ReadObjectAttributeRequest, ReadObjectAttributeResponse, WriteObjectAttributeRequest, WriteObjectAttributeResponse } from "../../service/message";
import { Attributes, StatusCodes } from "opcua-webapi";
import { ObjectServiceContext } from "../..";

export abstract class SubmodelTypeBase extends UaReactiveObjectType {
    constructor(
        typeId: string,
        displayName: UaLocalizedText,
        twinSpace: DigitalTwinSpace) {
        super(typeId, displayName, UaObjectTypes.SubmodelType, twinSpace);
    }

    digitalTwinSpace(): DigitalTwinSpace {
        return this.nodeManager as DigitalTwinSpace;
    }

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
     * Optional override point to get permissions
     */
    async onGetPermission(request: GetPermissionRequest): Promise<GetPermissionResponse> {
        return new GetPermissionResponse(false, false, false);
    }

    /**
     * Optional override point to add a digital twin.
     */
    async onAddSubmodel(request: AddRequest): Promise<AddResponse>
    {
        throw new UaError(UaStatusCode.from(StatusCodes.BadNotImplemented));
    }

    /**
     * Optional override point to delete a digital twin.
     */
    async onDeleteSubmodel(request: DeleteRequest): Promise<DeleteResponse>
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

    override async onAddObject(request: AddObjectRequest): Promise<AddObjectResponse> {
        const context = new ObjectServiceContext(request.parentId);
        const addRequest = new AddRequest(
            context,
            request.displayName);
        
        let response = await this.onAddSubmodel(addRequest);
        return new AddObjectResponse(response.newId);
    }

    override async onDeleteObject(request: DeleteObjectRequest): Promise<DeleteObjectResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const permissionResponse = await this.onGetPermission(new GetPermissionRequest(context));
        if (!permissionResponse.deletePermission) {
            throw new UaError(UaStatusCode.from(StatusCodes.BadUserAccessDenied));
        }

        const deleteRequest = new DeleteRequest(context);
        let response = await this.onDeleteSubmodel(deleteRequest);
        return new DeleteObjectResponse(response.statusCode);
    }
}