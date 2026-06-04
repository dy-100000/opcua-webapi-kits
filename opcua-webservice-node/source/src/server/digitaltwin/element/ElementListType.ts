import { NodeClass, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, ReferenceTypeIds, UaError, UaLocalizedText, UaNodeId, UaVariant } from "opcua-webapi-ts";
import { UaObjectTypes } from "../../addressspace/nodes/builtin";
import {
    BrowseMemberRequest,
    BrowseMemberResponse,
    BrowseObjectRequest,
    BrowseObjectResponse,
    GetDescriptorRequest,
    GetDescriptorResponse,
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
    ReadObjectAttributeRequest,
    ReadObjectAttributeResponse,
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
import { ElementType } from "./ElementType";
import { DigitalTwinSpace } from "../DigitalTwinSpace";

export abstract class ElementListType extends ElementType {
    constructor(
        typeId: string,
        displayName: UaLocalizedText,
        twinSpace: DigitalTwinSpace) {
        super(typeId, displayName, UaObjectTypes.ElementListType, twinSpace);
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
     * Internal framework callback used by the base type to read object attributes.
     * Do not call or override this method directly.
     */
    override async onReadObjectAttributes(request: ReadObjectAttributeRequest): Promise<ReadObjectAttributeResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetDescriptor(new GetDescriptorRequest(context));

        return new ReadObjectAttributeResponse(
            request.objectId.id,
            response.displayName,
            response.description,
        );
    }

    /**
     * Internal framework callback used by the base type to browse child nodes.
     * Do not call or override this method directly.
     */
    override async onBrowseObjectChildren(request: BrowseObjectRequest): Promise<BrowseObjectResponse> {
        const context = new ObjectServiceContext(request.objectId);

        if (request.additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK) &&
            this.supportObjectElementList()) {
            const response = await this.onGetObjectElementList(
                new GetObjectElementListRequest(
                    context,
                    request.additionalInfo.maxReferencesPerNode,
                    request.additionalInfo.referenceOffset,
                ),
            );

            return this.processBrowseObjectChildrenObjectResponse(response);
        }

        if (request.additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK) &&
            this.supportPropertyElementList() &&
            !request.browseDescription.referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasProperty))) {
            const response = await this.onGetPropertyElementList(
                new GetPropertyElementListRequest(
                    context,
                    request.additionalInfo.maxReferencesPerNode,
                    request.additionalInfo.referenceOffset,
                ),
            );

            return this.processBrowseObjectChildrenPropertyResponse(response);
        }

        return new BrowseObjectResponse([], false);
    }

    /**
     * Internal framework callback used by the base type to browse member children.
     * Do not call or override this method directly.
     */
    override async onBrowseMemberChildren(request: BrowseMemberRequest): Promise<BrowseMemberResponse> {
        const context = new ObjectServiceContext(request.objectId);
        const response = await this.onGetPropertySubElements(
            new GetPropertySubElementsRequest(context, request.childId),
        );

        return this.processBrowseMemberChildren(response);
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
            const childId = this.toChildId(item);
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
            const variableId = this.toChildId(childId);
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

    private processBrowseObjectChildrenObjectResponse(response: GetObjectElementListResponse): BrowseObjectResponse {
        const childDescriptors: Array<UaReferenceDescriptor> = [];

        for (const item of response.elements) {
            childDescriptors.push(
                new UaReferenceDescriptor(
                    item.id,
                    NodeClass.Object,
                    item.id,
                    item.displayName,
                    item.typeId,
                    UaNodeId.from(ReferenceTypeIds.HasComponent),
                    true,
                ),
            );
        }

        return new BrowseObjectResponse(
            childDescriptors,
            response.containsMoreData,
            UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK | UaBrowseAdditionalInfo.GET_CHILD_METHOD_TASK,
        );
    }

    private processBrowseObjectChildrenPropertyResponse(response: GetPropertyElementListResponse): BrowseObjectResponse {
        const childDescriptors: Array<UaReferenceDescriptor> = [];

        for (const item of response.elements) {
            childDescriptors.push(
                new UaReferenceDescriptor(
                    item.id,
                    NodeClass.Variable,
                    item.id,
                    item.displayName,
                    item.typeId,
                    UaNodeId.from(ReferenceTypeIds.HasComponent),
                    true,
                ),
            );
        }

        return new BrowseObjectResponse(
            childDescriptors,
            response.containsMoreData,
            UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK | UaBrowseAdditionalInfo.GET_CHILD_METHOD_TASK,
        );
    }

    private processBrowseMemberChildren(response: GetPropertySubElementsResponse): BrowseMemberResponse {
        const childDescriptors: Array<UaReferenceDescriptor> = [];

        for (const item of response.subElementNames) {
            childDescriptors.push(
                new UaReferenceDescriptor(
                    item,
                    NodeClass.Variable,
                    item,
                    new UaLocalizedText(item),
                    UaNodeId.from(ReferenceTypeIds.HasProperty),
                    UaNodeId.from(ReferenceTypeIds.HasProperty),
                    true,
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

    private toChildId(value: string): UaChildId {
        const [id, subElementName] = value.split("#", 2);
        return new UaChildId(id, subElementName ?? null);
    }
}