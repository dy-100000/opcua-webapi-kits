import { UaObject } from "../../addressspace/nodes/UaObject";
import {
    GetDescriptorRequest,
    GetDescriptorResponse,
    GetSubmodelsRequest,
    GetSubmodelsResponse,
} from "../../service/message";

export interface DigitalTwinCallback {
    onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse>;
    onGetSubmodels(request: GetSubmodelsRequest): Promise<GetSubmodelsResponse>;
}