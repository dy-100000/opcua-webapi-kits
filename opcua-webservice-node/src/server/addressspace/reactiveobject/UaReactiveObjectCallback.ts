import {
    BrowseMemberRequest,
    BrowseMemberResponse,
    BrowseObjectRequest,
    BrowseObjectResponse,
    MethodCallRequest,
    MethodCallResponse,
    ReadHistoryDataRequest,
    ReadHistoryDataResponse,
    ReadHistoryEventRequest,
    ReadHistoryEventResponse,
    ReadMemberAttributeRequest,
    ReadMemberAttributeResponse,
    ReadObjectAttributeRequest,
    ReadObjectAttributeResponse,
    ReadVariableValueRequest,
    ReadVariableValueResponse,
    WriteVariableValueRequest,
    WriteVariableValueResponse,
} from "../../service/message";

export interface UaReactiveObjectCallback {
    onBrowseObjectChildren(request: BrowseObjectRequest): Promise<BrowseObjectResponse>;
    onBrowseMemberChildren(request: BrowseMemberRequest): Promise<BrowseMemberResponse>;
    onBrowseObjectParent(request: BrowseObjectRequest): Promise<BrowseObjectResponse>;
    onBrowseObjectLinks(request: BrowseObjectRequest): Promise<BrowseObjectResponse>;
    onReadObjectAttributes(request: ReadObjectAttributeRequest): Promise<ReadObjectAttributeResponse>;
    onReadMemberAttributes(request: ReadMemberAttributeRequest): Promise<ReadMemberAttributeResponse>;
    onReadVariablesValue(request: ReadVariableValueRequest): Promise<ReadVariableValueResponse>;
    onWriteVariablesValue(request: WriteVariableValueRequest): Promise<WriteVariableValueResponse>;
    onMethodCall(request: MethodCallRequest): Promise<MethodCallResponse>;
    onReadHistoryData(request: ReadHistoryDataRequest): Promise<ReadHistoryDataResponse>;
    onReadHistoryEvent(request: ReadHistoryEventRequest): Promise<ReadHistoryEventResponse>;
}
