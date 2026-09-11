import { NodeClass, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaModellingRule, ReferenceTypeIds, UaError, UaLocalizedText, UaNodeId, UaVariant, VariableTypeIds } from "opcua-webapi-ts";
import { UaObjectTypes } from "../../addressspace/nodes/builtin";
import {
    BrowseMemberRequest,
    BrowseMemberResponse,
    BrowseObjectRequest,
    BrowseObjectResponse,
    GetDescriptorRequest,
    GetObjectElementListRequest,
    GetObjectElementListResponse,
    GetPropertyDescriptorRequest,
    GetPropertyDescriptorResponse,
    GetPropertyElementListRequest,
    GetPropertyElementListResponse,
    GetPropertySubElementsRequest,
    GetPropertySubElementsResponse,
    ReadHistoryDataRequest,
    ReadHistoryDataResponse,
    ReadMemberAttributeRequest,
    ReadMemberAttributeResponse,
    ReadPropertyHistoryValuesRequest,
    ReadPropertyHistoryValuesResponse,
    ReadPropertyListValueRequest,
    ReadPropertyListValueResponse,
    ReadVariableValueRequest,
    ReadVariableValueResponse,
    WritePropertyListValuesRequest,
    WritePropertyListValuesResponse,
    WriteVariableValueRequest,
    WriteVariableValueResponse,
} from "../../service/message";
import { UaBrowseAdditionalInfo, UaChildId, UaReferenceDescriptor } from "../../types";
import { ObjectServiceContext } from "../../types/digitaltwin";
import { DigitalTwinSpace } from "../DigitalTwinSpace";
import { ElementType } from "./ElementType";

export abstract class ElementListType extends ElementType {
    constructor(
        typeId: string,
        displayName: UaLocalizedText,
        twinSpace: DigitalTwinSpace) {
        super(typeId, displayName, UaObjectTypes.ElementListType, twinSpace);
    }

    /**
     * Set the element type can be added to this element list.
     */
    mayAdd(type: ElementType): void {
        const newObject = this.addObjectNode(type.name, type.displayName, type);       
        newObject.setModellingRule(UaModellingRule.PlaceHolder);
    }

    /**
     * Optional override point to enable dynamic object-element listing.
     */
    supportObjectElementList(): boolean { return false; }

    /**
     * Override in subclasses to return dynamic object elements.
     */
    async onGetObjectElementList(request: GetObjectElementListRequest): Promise<GetObjectElementListResponse>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    /**
     * Optional override point to enable dynamic property-element listing.
     */
    supportPropertyElementList(): boolean { return false; }

    /**
     * Override in subclasses to return the property element list.
     */
    async onGetPropertyElementList(request: GetPropertyElementListRequest): Promise<GetPropertyElementListResponse>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    /**
     * Override in subclasses to return a property descriptor.
     */
    async onGetPropertyDescriptor(request: GetPropertyDescriptorRequest): Promise<GetPropertyDescriptorResponse>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    /**
     * Override in subclasses to read property values.
     */
    async onReadPropertyValues(request: ReadPropertyListValueRequest): Promise<ReadPropertyListValueResponse>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }
    
    /**
     * Override in subclasses to write property values.
     */
    async onWritePropertyValues(request: WritePropertyListValuesRequest): Promise<WritePropertyListValuesResponse>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    /**
     * Override in subclasses to read historical property values.
     */
    async onReadPropertyHistoryValues(request: ReadPropertyHistoryValuesRequest): Promise<ReadPropertyHistoryValuesResponse>
    {
        throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
    }

    /**
     * Optional override point to return sub-elements of a property.
     */
    async onGetPropertySubElements(request: GetPropertySubElementsRequest): Promise<GetPropertySubElementsResponse>
    {
        return new GetPropertySubElementsResponse();
    }     

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
        const context = new ObjectServiceContext(request.objectId);

        request.additionalInfo.taskComplete(UaBrowseAdditionalInfo.GET_CHILD_METHOD_TASK);

        if (request.additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_RELATED_OBJECT_TASK) &&
            this.supportObjectElementList()) {
            const response = await this.onGetObjectElementList(
                new GetObjectElementListRequest(
                    context,
                    request.additionalInfo.maxReferencesPerNode,
                    request.additionalInfo.referenceOffset,
                ),
            );

            return this.processBrowseObjectResponse(response,request.additionalInfo);
        }

        if (request.additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK) &&
            this.supportPropertyElementList()) {
            const response = await this.onGetPropertyElementList(
                new GetPropertyElementListRequest(
                    context,
                    request.additionalInfo.maxReferencesPerNode,
                    request.additionalInfo.referenceOffset,
                ),
            );

            return this.processBrowsePropertyResponse(response,request.additionalInfo);
        }

        return new BrowseObjectResponse([]);
    }

    /**
     * Internal framework callback used by the base type to browse member children.
     * Do not call or override this method directly.
     */
    override async onBrowseMember(request: BrowseMemberRequest): Promise<BrowseMemberResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetPropertySubElements(
            new GetPropertySubElementsRequest(context, request.childId),
        );

        return this.processBrowseMember(response);
    }

    /**
     * Internal framework callback used by the base type to read member attributes.
     * Do not call or override this method directly.
     */
    override async onReadMemberAttributes(request: ReadMemberAttributeRequest): Promise<ReadMemberAttributeResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetPropertyDescriptor(
            new GetPropertyDescriptorRequest(
                context,
                request.childId.id,
                request.childId.subElementName,
            ),
        );

        return this.processReadMemberAttributeResponse(request.childId, response);
    }

    /**
     * Internal framework callback used by the base type to read variable values.
     * Do not call or override this method directly.
     */
    override async onReadVariablesValue(request: ReadVariableValueRequest): Promise<ReadVariableValueResponse> {
        const propertyIds = new Set<string>();
        const subPropertyIds = new Set<UaChildId>();

        for (const item of request.variableIds) {
            const childId = UaChildId.fromString(item);
            if (childId.subElementName === null) {
                propertyIds.add(childId.id);
            } else {
                subPropertyIds.add(childId);
            }
        }

        const context = new ObjectServiceContext(request.objectId);
        const readPropertyValuesResponse = await this.onReadPropertyValues(
            new ReadPropertyListValueRequest(context, propertyIds, subPropertyIds),
        );

        const response = new ReadVariableValueResponse();
        for (const [key, value] of readPropertyValuesResponse.results) {
            response.results.set(key, value);
        }
        return response;
    }

    /**
     * Internal framework callback used by the base type to write variable values.
     * Do not call or override this method directly.
     */
    override async onWriteVariablesValue(request: WriteVariableValueRequest): Promise<WriteVariableValueResponse> {
        const propertyIdAndValues = new Map<string, UaVariant>();
        const subPropertyIdsAndValues = new Map<UaChildId, UaVariant>();

        for (const [childId, value] of request.variableValues) {
            const variableId = UaChildId.fromString(childId);
            if (variableId.subElementName === null) {
                propertyIdAndValues.set(variableId.id, value);
            } else {
                subPropertyIdsAndValues.set(variableId, value);
            }
        }

        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onWritePropertyValues(
            new WritePropertyListValuesRequest(context, propertyIdAndValues, subPropertyIdsAndValues),
        );

        return new WriteVariableValueResponse(response.results);
    }

    /**
     * Internal framework callback used by the base type to read history data.
     * Do not call or override this method directly.
     */
    override async onReadHistoryData(request: ReadHistoryDataRequest): Promise<ReadHistoryDataResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onReadPropertyHistoryValues(
            new ReadPropertyHistoryValuesRequest(
                context,
                request.childId.id,
                request.details,
            ),
        );

        return this.processReadHistoryValueResponse(response);
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
                ),
            );
        }

        let taskMask = (response.containsMoreData) ? additionalInfo.taskCheckListMasks : additionalInfo.taskCheckListMasks & ~UaBrowseAdditionalInfo.GET_RELATED_OBJECT_TASK;
        let offset = (response.containsMoreData) ? additionalInfo.referenceOffset + response.elements.length : 0;
        return new BrowseObjectResponse(childDescriptors, taskMask, offset);
    }

    private processBrowsePropertyResponse(
        response: GetPropertyElementListResponse,
        additionalInfo: UaBrowseAdditionalInfo): BrowseObjectResponse {
        const childDescriptors: Array<UaReferenceDescriptor> = [];
        const referenceType = (response.isProperty) ? UaNodeId.from(ReferenceTypeIds.HasProperty) : UaNodeId.from(ReferenceTypeIds.HasComponent);

        for (const item of response.elements) {
            childDescriptors.push(
                new UaReferenceDescriptor(
                    item.id,
                    NodeClass.Variable,
                    item.id,
                    item.displayName,
                    item.typeId,
                    referenceType
                ),
            );
        }

        let taskMask = (response.containsMoreData) ? additionalInfo.taskCheckListMasks : additionalInfo.taskCheckListMasks & ~UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK;
        let offset = (response.containsMoreData) ? additionalInfo.referenceOffset + response.elements.length : 0;
        return new BrowseObjectResponse(childDescriptors,taskMask,offset);
    }

    private processBrowseMember(response: GetPropertySubElementsResponse): BrowseMemberResponse {
        const childDescriptors: Array<UaReferenceDescriptor> = [];
        const variableType = UaNodeId.from(VariableTypeIds.PropertyType);
        const referenceType = UaNodeId.from(ReferenceTypeIds.HasProperty);

        for (const item of response.subElementNames) {
            childDescriptors.push(
                new UaReferenceDescriptor(
                    item,
                    NodeClass.Variable,
                    item,
                    new UaLocalizedText(item),
                    variableType,
                    referenceType
                ),
            );
        }

        return new BrowseMemberResponse(childDescriptors);
    }

    private processReadMemberAttributeResponse(childId: UaChildId, response: GetPropertyDescriptorResponse): ReadMemberAttributeResponse {
        return new ReadMemberAttributeResponse(
            NodeClass.Variable,
            childId.subElementName === null ? childId.id : childId.subElementName,
            response.displayName,
            response.description,
            response.dataTypeId,
            response.valueRank,
            response.accessLevel,
            response.historizing,
            null,
        );
    }

    private processReadHistoryValueResponse(response: ReadPropertyHistoryValuesResponse): ReadHistoryDataResponse {
        return new ReadHistoryDataResponse(response.dataValues, response.containsMoreData);
    }
}