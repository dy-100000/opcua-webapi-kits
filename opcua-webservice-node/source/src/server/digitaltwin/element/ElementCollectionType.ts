import { NodeClass, StatusCodes } from "opcua-webapi";
import { ReferenceTypeIds, UaAccessLevel, UaArgument, UaDataValue, UaError, UaLocalizedText, UaNodeId, UaVariant, makeUaStatusCode } from "opcua-webapi-ts";
import { UaDataType } from "../../addressspace/nodes/UaDataType";
import { UaInstanceNode } from "../../addressspace/nodes/UaInstanceNode";
import { UaMethod } from "../../addressspace/nodes/UaMethod";
import { UaModellingRule } from "../../addressspace/nodes/UaModellingRule";
import { UaObject } from "../../addressspace/nodes/UaObject";
import { UaVariable } from "../../addressspace/nodes/UaVariable";
import type { UaVariableType } from "../../addressspace/nodes/UaVariableType";
import { UaObjectTypes, UaVariableTypes } from "../../addressspace/nodes/builtin";
import { ElementCollectionCallback } from "../callback/ElementCollectionCallback";

import {
    BrowseMemberRequest,
    BrowseMemberResponse,
    BrowseObjectRequest,
    BrowseObjectResponse,
    GetDescriptorRequest,
    GetDescriptorResponse,
    GetElementsRequest,
    GetElementsResponse,
    InvokeOperationRequest,
    InvokeOperationResponse,
    MethodCallRequest,
    MethodCallResponse,
    ReadHistoryDataRequest,
    ReadHistoryDataResponse,
    ReadMemberAttributeRequest,
    ReadMemberAttributeResponse,
    ReadObjectAttributeRequest,
    ReadObjectAttributeResponse,
    ReadPropertyHistoryValuesRequest,
    ReadPropertyHistoryValuesResponse,
    ReadPropertyValuesRequest,
    ReadPropertyValuesResponse,
    ReadVariableValueRequest,
    ReadVariableValueResponse,
    WritePropertyValuesRequest,
    WritePropertyValuesResponse,
    WriteVariableValueRequest,
    WriteVariableValueResponse
} from "../../service/message";
import { UaBrowseAdditionalInfo, UaChildId, UaObjectId, UaReferenceDescriptor } from "../../types";
import { ObjectServiceContext } from "../../types/digitaltwin/ObjectServiceContext";
import { ElementListType } from "./ElementListType";
import { ElementType } from "./ElementType";
import { EventElementType } from "./EventElementType";
import { ReferenceElementType } from "./ReferenceElementType";
import { DigitalTwinSpace } from "../DigitalTwinSpace";

export abstract class ElementCollectionType extends ElementType implements ElementCollectionCallback {
    constructor(
        typeId: string,
        displayName: UaLocalizedText,
        twinSpace: DigitalTwinSpace) {
        super(typeId, displayName, UaObjectTypes.ElementCollectionType, twinSpace);
    }

    // Override to read property values
    abstract onReadPropertyValues(request: ReadPropertyValuesRequest): Promise<ReadPropertyValuesResponse>;
    
    // Override to update property value
    async onWritePropertyValues(request: WritePropertyValuesRequest): Promise<WritePropertyValuesResponse> {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }
    
    // Override to process a method call
    async onInvokeOperation(request: InvokeOperationRequest): Promise<InvokeOperationResponse> {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    // Override to read history data
    async onReadPropertyHistoryValues(request: ReadPropertyHistoryValuesRequest): Promise<ReadPropertyHistoryValuesResponse>
    {        
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    // Can be override to provide customized descriptor
    async onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse>
    {
        const instance = request.context.objectId.instance;

        if (instance === null) {
            return new GetDescriptorResponse("NotImplemented");
        }

        return new GetDescriptorResponse(instance.displayName, instance.description);
    }

    // Can be override to provide customized elements
    async onGetElements(request: GetElementsRequest): Promise<GetElementsResponse>
    {        
        let response = new GetElementsResponse();
        for (const item of this.getMembers()) {
            response.elementNames.add(item.browseName);
        }
        return response;
    }

    addPropertyElement(
        name: string,
        displayName: UaLocalizedText,
        description: UaLocalizedText,
        dataType: UaDataType,
        writable: boolean,
    ): UaVariable;
    addPropertyElement(
        name: string,
        displayName: UaLocalizedText,
        description: UaLocalizedText,
        dataType: UaDataType,
        writable: boolean,
        historizing = false,
        valueRank: number = -1,
        variableType: UaVariableType = UaVariableTypes.PropertyType,
        mandatory = true): UaVariable {
        const newVariable = this.addVariableNode(name, displayName, dataType, writable, historizing, valueRank, variableType);
        if (description.text.length > 0) {
            newVariable.description = description;
        }
        newVariable.setModellingRule(mandatory ? UaModellingRule.Mandatory : UaModellingRule.Optional);
        return newVariable;
    }

    addSubElementOfProperty(property: UaVariable, subElementName: string, value: UaVariant): void {
        const subElement = property.addMemberByName(subElementName);
        if (subElement !== null) {
            subElement.value = value;
            this.nodeManager.addNode(subElement);
        }
    }

    addOperationElement(
        name: string,
        displayName: UaLocalizedText,
        description: UaLocalizedText,
        inputArguments: Array<UaArgument> | null,
        outputArguments: Array<UaArgument> | null,
        mandatory: boolean,
    ): UaMethod {
        const newMethod = this.addMethodNode(name, displayName, inputArguments, outputArguments);
        if (description.text.length > 0) {
            newMethod.description = description;
        }
        newMethod.setModellingRule(mandatory ? UaModellingRule.Mandatory : UaModellingRule.Optional);
        return newMethod;
    }

    addElementCollection(type: ElementCollectionType, name: string, displayName: UaLocalizedText, description: UaLocalizedText, mandatory: boolean): UaObject {
        return this.addChildObject(type, name, displayName, description, mandatory);
    }

    addReferenceElement(type: ReferenceElementType, name: string, displayName: UaLocalizedText, description: UaLocalizedText, mandatory: boolean): UaObject {
        return this.addChildObject(type, name, displayName, description, mandatory);
    }

    addEventElement(type: EventElementType, name: string, displayName: UaLocalizedText, description: UaLocalizedText, mandatory: boolean): UaObject {
        return this.addChildObject(type, name, displayName, description, mandatory);
    }

    addElementList(type: ElementListType, name: string, displayName: UaLocalizedText, description: UaLocalizedText, mandatory: boolean): UaObject {
        return this.addChildObject(type, name, displayName, description, mandatory);
    }

    // Implementation of parent type methods, don't override
    override async onReadObjectAttributes(request: ReadObjectAttributeRequest): Promise<ReadObjectAttributeResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetDescriptor(new GetDescriptorRequest(context));
        return new ReadObjectAttributeResponse(request.objectId.id, response.displayName, response.description);
    }

    // Implementation of parent type methods, don't override 
    override async onBrowseObjectChildren(request: BrowseObjectRequest): Promise<BrowseObjectResponse> {
        const referenceTypeId = request.browseDescription.referenceTypeId;
        const membersToReturn: Array<UaInstanceNode> = [];

        for (const item of this.getMembers()) {
            if (item.nodeClass === NodeClass.Object && !request.additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK)) {
                continue;
            }

            if (item.nodeClass === NodeClass.Method && !request.additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_METHOD_TASK)) {
                continue;
            }

            if (item.nodeClass === NodeClass.Variable) {
                if (!request.additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK)) {
                    continue;
                }

                const variableTypeId = (item as UaVariable).typeDefinition.nodeId;
                if (variableTypeId.equal(UaVariableTypes.PropertyType.nodeId)) {
                    if (referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasComponent))) {
                        continue;
                    }
                } else if (referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasProperty))) {
                    continue;
                }
            }

            membersToReturn.push(item);
        }

        if (membersToReturn.length === 0) {
            return new BrowseObjectResponse([], false);
        }

        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetElements(new GetElementsRequest(context));
        return this.processBrowseObjectChildrenResponse(request.objectId, membersToReturn, response);
    }

    // Implementation of parent type methods, don't override 
    override async onBrowseMemberChildren(request: BrowseMemberRequest): Promise<BrowseMemberResponse> {
        const referenceTypeId = request.browseDescription.referenceTypeId;
        const member = this.getMember(request.childId);
        if (member === null || member.nodeClass === NodeClass.Object) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
        }

        const childDescriptors: Array<UaReferenceDescriptor> = [];
        for (const item of member.getMembers()) {
            if (item.nodeClass !== NodeClass.Variable) {
                continue;
            }

            const variableTypeId = (item as UaVariable).typeDefinition.nodeId;
            if (referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasProperty))) {
                if (!variableTypeId.equal(UaVariableTypes.PropertyType.nodeId)) {
                    continue;
                }
            } else if (referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasComponent))) {
                if (variableTypeId.equal(UaVariableTypes.PropertyType.nodeId)) {
                    continue;
                }
            }

            childDescriptors.push(
                UaReferenceDescriptor.fromInstanceDeclaration(
                    item.browseName,
                    item,
                    variableTypeId.equal(UaVariableTypes.PropertyType.nodeId)
                        ? UaNodeId.from(ReferenceTypeIds.HasProperty)
                        : UaNodeId.from(ReferenceTypeIds.HasComponent),
                    true,
                ),
            );
        }

        return new BrowseMemberResponse(childDescriptors);
    }

    // Implementation of parent type methods, don't override 
    override async onReadMemberAttributes(request: ReadMemberAttributeRequest): Promise<ReadMemberAttributeResponse> {
        let memberNode = this.getMember(request.childId.id);

        if (memberNode !== null && request.childId.subElementName !== null) {
            memberNode = memberNode.getMember(request.childId.subElementName);
        }

        if (memberNode === null) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
        }

        return ReadMemberAttributeResponse.fromInstanceDeclaration(memberNode);
    }

    // Implementation of parent type methods, don't override 
    override async onReadVariablesValue(request: ReadVariableValueRequest): Promise<ReadVariableValueResponse> {
        const propertyNames = new Set<string>();
        const subElementNames = new Set<UaChildId>();

        for (const item of request.variableIds) {
            const childId = this.toChildId(item);
            if (childId.subElementName === null) {
                propertyNames.add(childId.id);
            } else {
                subElementNames.add(childId);
            }
        }

        const context = new ObjectServiceContext(request.objectId);
        const readResponse = propertyNames.size === 0
            ? new ReadPropertyValuesResponse()
            : await this.onReadPropertyValues(new ReadPropertyValuesRequest(context, propertyNames));

        return this.processReadVariableValueResponse(subElementNames, readResponse);
    }

    // Implementation of parent type methods, don't override 
    override async onWriteVariablesValue(request: WriteVariableValueRequest): Promise<WriteVariableValueResponse> {
        const elementValues = new Map<string, UaVariant>();

        for (const [childIdText, value] of request.variableValues) {
            const childId = this.toChildId(childIdText);
            if (childId.subElementName !== null) {
                continue;
            }

            const memberNode = this.getMember(childId.id);
            if (memberNode === null || memberNode.nodeClass !== NodeClass.Variable) {
                continue;
            }

            const variableToWrite = memberNode as UaVariable;
            if ((UaAccessLevel.CurrentWrite & variableToWrite.accessLevel) === 0) {
                continue;
            }

            elementValues.set(childId.id, value);
        }

        if (elementValues.size === 0) {
            return new WriteVariableValueResponse();
        }

        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onWritePropertyValues(new WritePropertyValuesRequest(context, elementValues));
        return new WriteVariableValueResponse(response.results);
    }

    // Implementation of parent type methods, don't override 
    override async onMethodCall(request: MethodCallRequest): Promise<MethodCallResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onInvokeOperation(
            new InvokeOperationRequest(context, request.methodName, request.inputArguments),
        );

        return new MethodCallResponse(response.outputArguments);
    }

    // Implementation of parent type methods, don't override 
    override async onReadHistoryData(request: ReadHistoryDataRequest): Promise<ReadHistoryDataResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onReadPropertyHistoryValues(
            new ReadPropertyHistoryValuesRequest(context, request.childId.id, request.details),
        );

        return this.processReadHistoryValueResponse(response);
    }
    
    private addChildObject(
        type: ElementType,
        name: string,
        displayName: UaLocalizedText,
        description: UaLocalizedText,
        mandatory: boolean,
    ): UaObject {
        const newObject = this.addObjectNode(name, displayName, type);
        if (description.text.length > 0) {
            newObject.description = description;
        }
        newObject.setModellingRule(mandatory ? UaModellingRule.Mandatory : UaModellingRule.Optional);
        return newObject;
    }

    private processBrowseObjectChildrenResponse(
        objectId: UaObjectId,
        members: Array<UaInstanceNode>,
        response: GetElementsResponse): BrowseObjectResponse {
        const childDescriptors: Array<UaReferenceDescriptor> = [];

        for (const item of members) {
            if (item.modellingRule === UaModellingRule.Optional && !response.elementNames.has(item.browseName)) {
                continue;
            }

            const referenceType = item.nodeClass === NodeClass.Variable &&
                (item as UaVariable).typeDefinition.nodeId.equal(UaVariableTypes.PropertyType.nodeId)
                ? UaNodeId.from(ReferenceTypeIds.HasProperty)
                : UaNodeId.from(ReferenceTypeIds.HasComponent);

            childDescriptors.push(
                UaReferenceDescriptor.fromInstanceDeclaration(
                    item.nodeClass === NodeClass.Object ? objectId.id : item.browseName,
                    item,
                    referenceType,
                    true,
                ),
            );
        }

        return new BrowseObjectResponse(childDescriptors, false);
    }

    private processReadVariableValueResponse(
        subElementNames: Set<UaChildId>,
        response: ReadPropertyValuesResponse,
    ): ReadVariableValueResponse {
        const readVariableValueResponse = new ReadVariableValueResponse();

        for (const [key, value] of response.results) {
            readVariableValueResponse.results.set(key, value);
        }

        for (const item of subElementNames) {
            if (item.subElementName === null) {
                continue;
            }

            const node = this.getMember(item.id);
            if (node === null || node.nodeClass !== NodeClass.Variable) {
                continue;
            }

            const subElementNode = node.getMember(item.subElementName);
            if (subElementNode === null || subElementNode.nodeClass !== NodeClass.Variable) {
                continue;
            }

            let subVariable = subElementNode as UaVariable;
            readVariableValueResponse.results.set(
                new UaChildId(item.id, item.subElementName).toString(),
                new UaDataValue(subVariable.value, makeUaStatusCode(StatusCodes.Good)),
            );
        }

        return readVariableValueResponse;
    }

    private processReadHistoryValueResponse(response: ReadPropertyHistoryValuesResponse): ReadHistoryDataResponse {
        return new ReadHistoryDataResponse(response.dataValues, response.containsMoreData);
    }

    private toChildId(value: string): UaChildId {
        const [id, subElementName] = value.split("#", 2);
        return new UaChildId(id, subElementName ?? null);
    }
}