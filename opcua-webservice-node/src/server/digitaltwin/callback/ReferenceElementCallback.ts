import {
    GetDescriptorRequest,
    GetDescriptorResponse,
    GetLinkRequest,
    GetLinkResponse,
} from "../../service/message";

export interface ReferenceElementCallback {
    onGetLinks(request: GetLinkRequest): Promise<GetLinkResponse>;
    onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse>;
}