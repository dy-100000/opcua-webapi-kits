import { StatusCodes } from "opcua-webapi";
import { UaLocalizedText, UaStatusCode } from "opcua-webapi-ts";
import { UaObject,DigitalTwinSpace, DigitalTwinType,SubmodelDescriptor,GetDescriptorRequest, GetDescriptorResponse, GetSubmodelsRequest, GetSubmodelsResponse, AddRequest, AddResponse, DeleteResponse, DeleteRequest, GetPermissionRequest, GetPermissionResponse, ModifyAttributeRequest, ModifyAttributeResponse } from "../../src";
import { DigitalTwinSpaceTest } from "./DigitalTwinSpaceTest";
import { DynamicSubmodelTestType } from "./DynamicSubmodelTestType";
import { SubmodelTestType } from "./SubmodelTestType";
import { SubmodelTestInteralClientType } from "./SubmodelTestInternalClient";

export class DigitalTwinTestType extends DigitalTwinType {

    private readonly submodel: UaObject;
    private readonly elementListSubmodel: UaObject;
    private readonly internalClientSubmodel: UaObject;

    constructor(
        submodel: SubmodelTestType,
        elementListSubmodel: DynamicSubmodelTestType,
        internalClientSubmodel: SubmodelTestInteralClientType,
        space: DigitalTwinSpace) {
        super("TestDigitalTwin", new UaLocalizedText("TestDigitalTwin"), space);
        this.description = new UaLocalizedText("TestDigitalTwin");

        this.submodel = this.addSubmodel(
            submodel,
            "Submodel",
            new UaLocalizedText("Submodel"),
            new UaLocalizedText("Test Submodel"),
        );

        this.elementListSubmodel = this.addSubmodel(
            elementListSubmodel,
            "ElementListSubmodel",
            UaLocalizedText.from("ElementListSubmodel"),
            UaLocalizedText.from("Test ElementListSubmodel"),
        );

        this.internalClientSubmodel = this.addSubmodel(
            internalClientSubmodel,
            "InternalClientSubmodel",
            UaLocalizedText.from("InternalClientSubmodel"),
            UaLocalizedText.from("Test InternalClientSubmodel"),
        );

        this.mayAdd(submodel);
    }

    override async onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse> {      
        return new GetDescriptorResponse(
            new UaLocalizedText(`DT ${request.id}`),
            new UaLocalizedText(`Test Digital Twin number ${request.id}`),
        );
    }

    override async onGetSubmodels(request: GetSubmodelsRequest): Promise<GetSubmodelsResponse> {
        const response = new GetSubmodelsResponse();

        let id = request.id;

        if (id !== "1") {
            response.add(new SubmodelDescriptor(id, this.submodel));
        }
        
        if (id !== "2") {
            response.add(new SubmodelDescriptor(id, this.elementListSubmodel));
        }

        if (id === "3") {
            response.add(new SubmodelDescriptor(id, new UaLocalizedText(`Submodel-${id}`), DigitalTwinSpaceTest.submodelTestType));
        }

        response.add(new SubmodelDescriptor(id, this.internalClientSubmodel));

        return response;
    }

    override async onAddDigitalTwin(request: AddRequest): Promise<AddResponse>
    {
        console.log("Add DigitalTwinTestType, parent: " + request.parentId + " DisplayName: " + request.displayName?.text);
        let response = new AddResponse(request.parentId);
        return response;
    }

    override async onDeleteDigitalTwin(request: DeleteRequest): Promise<DeleteResponse>
    {
        console.log("Delete DigitalTwinTestType, id: " + request.id);
        let response = new DeleteResponse();
        return response;
    }

    override async onGetPermission(request: GetPermissionRequest): Promise<GetPermissionResponse> {
        console.log("GetPermission DigitalTwinTestType, id: " + request.id);
        return new GetPermissionResponse(true, true, true);
    }

    override async onRename(request: ModifyAttributeRequest): Promise<ModifyAttributeResponse> {
        console.log("Rename DigitalTwinTestType, id: " + request.id + " DisplayName: " + request.text.text);
        return new ModifyAttributeResponse(UaStatusCode.from(StatusCodes.Good));
    }

    override async onSetDescriptor(request: ModifyAttributeRequest): Promise<ModifyAttributeResponse> {
        console.log("ModifyDescriptor DigitalTwinTestType, id: " + request.id + " Description: " + request.text.text);
        return new ModifyAttributeResponse(UaStatusCode.from(StatusCodes.Good));
    }
}