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
    AddObjectRequest,
    AddObjectResponse,
    DeleteObjectRequest,
    DeleteObjectResponse,
    AddReferenceRequest,
    AddReferenceResponse,
    DeleteReferenceRequest,
    DeleteReferenceResponse,
    WriteObjectAttributeRequest,
    WriteObjectAttributeResponse,
} from "../../service/message";

export interface UaReactiveObjectCallback {
    onBrowseObject(request: BrowseObjectRequest): Promise<BrowseObjectResponse>;
    onBrowseMember(request: BrowseMemberRequest): Promise<BrowseMemberResponse>;
    onReadObjectAttributes(request: ReadObjectAttributeRequest): Promise<ReadObjectAttributeResponse>;
    onReadMemberAttributes(request: ReadMemberAttributeRequest): Promise<ReadMemberAttributeResponse>;
    onReadVariablesValue(request: ReadVariableValueRequest): Promise<ReadVariableValueResponse>;
    onWriteVariablesValue(request: WriteVariableValueRequest): Promise<WriteVariableValueResponse>;
    onWriteObjectAttributes(request: WriteObjectAttributeRequest): Promise<WriteObjectAttributeResponse>;
    onMethodCall(request: MethodCallRequest): Promise<MethodCallResponse>;
    onReadHistoryData(request: ReadHistoryDataRequest): Promise<ReadHistoryDataResponse>;
    onReadHistoryEvent(request: ReadHistoryEventRequest): Promise<ReadHistoryEventResponse>;
    onAddObject(request: AddObjectRequest): Promise<AddObjectResponse>;
    onDeleteObject(request: DeleteObjectRequest): Promise<DeleteObjectResponse>;
    onAddReference(request: AddReferenceRequest): Promise<AddReferenceResponse>;
    onDeleteReference(request: DeleteReferenceRequest): Promise<DeleteReferenceResponse>;
}
