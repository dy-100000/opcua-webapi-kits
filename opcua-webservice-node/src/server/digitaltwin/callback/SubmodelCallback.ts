import { UaInstanceNode } from "../../addressspace/nodes/UaInstanceNode";
import {
    GetDescriptorRequest,
    GetDescriptorResponse,
    GetElementsRequest,
    GetElementsResponse,
    InvokeOperationRequest,
    InvokeOperationResponse,
    ReadPropertyHistoryValuesRequest,
    ReadPropertyHistoryValuesResponse,
    ReadPropertyValuesRequest,
    ReadPropertyValuesResponse,
    WritePropertyValuesRequest,
    WritePropertyValuesResponse,
} from "../../service/message";

export interface SubmodelCallback {
    onReadPropertyValues(request: ReadPropertyValuesRequest): Promise<ReadPropertyValuesResponse>;
    onWritePropertyValues(request: WritePropertyValuesRequest): Promise<WritePropertyValuesResponse>;
    onInvokeOperation(request: InvokeOperationRequest): Promise<InvokeOperationResponse>;
    onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse>;
    onGetElements(request: GetElementsRequest): Promise<GetElementsResponse>;
    onReadPropertyHistoryValues(request: ReadPropertyHistoryValuesRequest): Promise<ReadPropertyHistoryValuesResponse>;
}