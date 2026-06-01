import {
    GetDescriptorRequest,
    GetDescriptorResponse,
    GetObjectElementListRequest,
    GetObjectElementListResponse,
} from "../../service/message";

export interface DynamicSubmodelCallback {
    onGetObjectElementList(request: GetObjectElementListRequest): Promise<GetObjectElementListResponse>;
    onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse>;
}