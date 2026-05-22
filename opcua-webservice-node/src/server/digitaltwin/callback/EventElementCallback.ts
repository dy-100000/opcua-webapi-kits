import {
    GetDescriptorRequest,
    GetDescriptorResponse,
    ReadEventsRequest,
    ReadEventsResponse,
} from "../../service/message";

export interface EventElementCallback {
    onReadEvents(request: ReadEventsRequest): Promise<ReadEventsResponse>;
    onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse>;
}