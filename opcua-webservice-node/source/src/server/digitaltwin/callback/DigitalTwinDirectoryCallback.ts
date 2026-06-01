import { GetDigitalTwinListRequest, GetDigitalTwinListResponse } from "../../service/message";

export interface DigitalTwinDirectoryCallback {
    onGetDigitalTwinList(request: GetDigitalTwinListRequest): Promise<GetDigitalTwinListResponse>;
}