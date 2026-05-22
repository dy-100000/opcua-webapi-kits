import {
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
    ReadPropertyHistoryValuesRequest,
    ReadPropertyHistoryValuesResponse,
    ReadPropertyListValueRequest,
    ReadPropertyListValueResponse,
    WritePropertyListValuesRequest,
    WritePropertyListValuesResponse,
} from "../../service/message";

export interface ElementListCallback {
    onGetObjectElementList(request: GetObjectElementListRequest): Promise<GetObjectElementListResponse>;
    onGetPropertyElementList(request: GetPropertyElementListRequest): Promise<GetPropertyElementListResponse>;
    onGetPropertyDescriptor(request: GetPropertyDescriptorRequest): Promise<GetPropertyDescriptorResponse>;
    onReadPropertyValues(request: ReadPropertyListValueRequest): Promise<ReadPropertyListValueResponse>;
    onWritePropertyValues(request: WritePropertyListValuesRequest): Promise<WritePropertyListValuesResponse>;
    onReadPropertyHistoryValues(request: ReadPropertyHistoryValuesRequest): Promise<ReadPropertyHistoryValuesResponse>;
    onGetPropertySubElements(request: GetPropertySubElementsRequest): Promise<GetPropertySubElementsResponse>;
    onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse>;
}