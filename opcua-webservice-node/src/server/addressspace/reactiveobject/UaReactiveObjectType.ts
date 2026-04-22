import { NodeClass, StatusCodes } from "opcua-webapi";
import {
    UaAccessLevel,
    UaArgument,
    UaError,
    UaLocalizedText,
    UaNodeId,
    UaValueRank,
    makeUaStatusCode,
} from "opcua-webapi-ts";
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
import { NodeManager } from "../nodemanager";
import { UaDataType } from "../nodes/UaDataType";
import { UaInstanceNode } from "../nodes/UaInstanceNode";
import { UaMethod } from "../nodes/UaMethod";
import { UaModellingRule } from "../nodes/UaModellingRule";
import { UaObject } from "../nodes/UaObject";
import { UaObjectType } from "../nodes/UaObjectType";
import { UaVariable } from "../nodes/UaVariable";
import type { UaVariableType } from "../nodes/UaVariableType";
import { UaObjectTypes, UaVariableTypes } from "../nodes/builtin";

function notImplemented(): never {
    throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
}

export abstract class UaReactiveObjectType extends UaObjectType {
    protected readonly nodeManager: NodeManager;

    constructor(
        objectTypeId: string,
        displayName: UaLocalizedText,
        parentType: UaObjectType | null,
        nodeManager: NodeManager,
    ) {
        if (objectTypeId.length === 0) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdRejected));
        }
        if (displayName.text.length === 0) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadInvalidArgument));
        }

        super(
            new UaNodeId(objectTypeId, nodeManager.nsIndex()),
            objectTypeId,
            displayName,
            false,
        );

        this.setParentType(parentType ?? UaObjectTypes.BaseObjectType);
        this.nodeManager = nodeManager;
        this.nodeManager.addNode(this);
    }

    isGetParentSupported(): boolean {
        return false;
    }

    isGetLinkSupported(): boolean {
        return false;
    }

    protected addObjectNode(
        memberId: string,
        displayName: UaLocalizedText,
        objectType: UaReactiveObjectType): UaObject {
        if (memberId.length === 0) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdRejected));
        }
        if (displayName.text.length === 0) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadInvalidArgument));
        }

        const objectId = `${this.browseName}-${memberId}`;
        const newObject = new UaObject(
            new UaNodeId(objectId, this.nodeManager.nsIndex()),
            memberId,
            displayName,
            objectType,
        );

        this.addMember(newObject);
        this.nodeManager.addNode(newObject);

        for (const item of objectType.getMembers()) {
            const memberIdOfObject = `${objectId}-${item.browseName}`;
            const newMember = this.copyNode(item, memberIdOfObject);
            newObject.addMember(newMember);
        }

        return newObject;
    }

    protected addVariableNode(
        memberId: string,
        displayName: UaLocalizedText,
        dataType: UaDataType,
        writable: boolean,
        historizing: boolean,
        valueRank: number | null,
        variableType: UaVariableType | null): UaVariable {
        if (memberId.length === 0) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdRejected));
        }
        if (displayName.text.length === 0) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadInvalidArgument));
        }

        const variableId = `${this.browseName}-${memberId}`;
        let accessLevel = UaAccessLevel.CurrentRead;

        if (writable) {
            accessLevel |= UaAccessLevel.CurrentWrite;
        }
        if (historizing) {
            accessLevel |= UaAccessLevel.HistoryRead;
        }

        const newVariable = new UaVariable(
            new UaNodeId(variableId, this.nodeManager.nsIndex()),
            memberId,
            displayName,
            dataType.nodeId,
            valueRank ?? UaValueRank.Scalar,
            accessLevel,
            variableType ?? UaVariableTypes.PropertyType,
        );

        newVariable.historizing = historizing;

        this.addMember(newVariable);
        this.nodeManager.addNode(newVariable);

        return newVariable;
    }

    protected addMethodNode(
        memberId: string,
        displayName: UaLocalizedText,
        inputArguments: Array<UaArgument> | null,
        outputArguments: Array<UaArgument> | null): UaMethod {
        if (memberId.length === 0) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdRejected));
        }
        if (displayName.text.length === 0) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadInvalidArgument));
        }

        const methodId = `${this.browseName}-${memberId}`;
        const newMethod = new UaMethod(
            new UaNodeId(methodId, this.nodeManager.nsIndex()),
            memberId,
            displayName,
        );

        this.addMember(newMethod);
        this.nodeManager.addNode(newMethod);

        if (inputArguments !== null && inputArguments.length > 0) {
            const inputArgumentId = `${methodId}-InputArguments`;
            const argumentVariable = newMethod.setInputArguments(
                new UaNodeId(inputArgumentId, this.nodeManager.nsIndex()),
                inputArguments,
            );

            if (argumentVariable !== null) {
                argumentVariable.setModellingRule(UaModellingRule.Mandatory);
                this.nodeManager.addNode(argumentVariable);
            }
        }

        if (outputArguments !== null && outputArguments.length > 0) {
            const outputArgumentId = `${methodId}-OutputArguments`;
            const argumentVariable = newMethod.setOutputArguments(
                new UaNodeId(outputArgumentId, this.nodeManager.nsIndex()),
                outputArguments,
            );

            if (argumentVariable !== null) {
                argumentVariable.setModellingRule(UaModellingRule.Mandatory);
                this.nodeManager.addNode(argumentVariable);
            }
        }

        return newMethod;
    }

    onBrowseObjectChildren(_request: BrowseObjectRequest): Promise<BrowseObjectResponse> {
        return Promise.resolve(new BrowseObjectResponse([], false));
    }

    onBrowseMemberChildren(_request: BrowseMemberRequest): Promise<BrowseMemberResponse> {
        return Promise.reject(notImplemented());
    }

    onBrowseObjectParent(_request: BrowseObjectRequest): Promise<BrowseObjectResponse> {
        return Promise.reject(notImplemented());
    }

    onBrowseObjectLinks(_request: BrowseObjectRequest): Promise<BrowseObjectResponse> {
        return Promise.reject(notImplemented());
    }

    onReadObjectAttributes(_request: ReadObjectAttributeRequest): Promise<ReadObjectAttributeResponse> {
        return Promise.reject(notImplemented());
    }

    onReadMemberAttributes(_request: ReadMemberAttributeRequest): Promise<ReadMemberAttributeResponse> {
        return Promise.reject(notImplemented());
    }

    onReadVariablesValue(_request: ReadVariableValueRequest): Promise<ReadVariableValueResponse> {
        return Promise.reject(notImplemented());
    }

    onWriteVariablesValue(_request: WriteVariableValueRequest): Promise<WriteVariableValueResponse> {
        return Promise.reject(notImplemented());
    }

    onMethodCall(_request: MethodCallRequest): Promise<MethodCallResponse> {
        return Promise.reject(notImplemented());
    }

    onReadHistoryData(_request: ReadHistoryDataRequest): Promise<ReadHistoryDataResponse> {
        return Promise.reject(notImplemented());
    }

    onReadHistoryEvent(_request: ReadHistoryEventRequest): Promise<ReadHistoryEventResponse> {
        return Promise.reject(notImplemented());
    }

    private copyNode(node: UaInstanceNode, newInstanceId: string): UaInstanceNode {
        if (node.nodeClass === NodeClass.Object) {
            const sourceObject = node as UaObject;
            const newObject = new UaObject(
                new UaNodeId(newInstanceId, this.nodeManager.nsIndex()),
                sourceObject.browseName,
                sourceObject.displayName,
                sourceObject.typeDefinition,
            );

            newObject.writeMask = sourceObject.writeMask;
            newObject.description = sourceObject.description;
            newObject.setModellingRule(sourceObject.modellingRule);

            this.nodeManager.addNode(newObject);

            for (const item of sourceObject.getMembers()) {
                const memberId = `${newInstanceId}-${item.browseName}`;
                const member = this.copyNode(item, memberId);
                newObject.addMember(member);
            }

            return newObject;
        }

        if (node.nodeClass === NodeClass.Variable) {
            const sourceVariable = node as UaVariable;
            const newVariable = new UaVariable(
                new UaNodeId(newInstanceId, this.nodeManager.nsIndex()),
                sourceVariable.browseName,
                sourceVariable.displayName,
                sourceVariable.dataType,
                sourceVariable.valueRank,
                sourceVariable.accessLevel,
                sourceVariable.typeDefinition,
            );

            newVariable.writeMask = sourceVariable.writeMask;
            newVariable.description = sourceVariable.description;
            newVariable.setModellingRule(sourceVariable.modellingRule);
            newVariable.value = sourceVariable.value;
            newVariable.historizing = sourceVariable.historizing;

            this.nodeManager.addNode(newVariable);

            for (const item of sourceVariable.getMembers()) {
                if (item.nodeClass !== NodeClass.Variable) {
                    continue;
                }

                const memberId = `${newInstanceId}-${item.browseName}`;
                const member = this.copyNode(item, memberId);
                newVariable.addMember(member as UaVariable);
            }

            return newVariable;
        }

        const sourceMethod = node as UaMethod;
        const newMethod = new UaMethod(
            new UaNodeId(newInstanceId, this.nodeManager.nsIndex()),
            sourceMethod.browseName,
            sourceMethod.displayName,
        );

        newMethod.writeMask = sourceMethod.writeMask;
        newMethod.description = sourceMethod.description;
        newMethod.setModellingRule(sourceMethod.modellingRule);

        this.nodeManager.addNode(newMethod);

        if (sourceMethod.inputArguments !== null) {
            const inputArgumentsId = `${newInstanceId}-InputArguments`;
            const argumentVariable = newMethod.setInputArguments(
                new UaNodeId(inputArgumentsId, this.nodeManager.nsIndex()),
                sourceMethod.inputArguments,
            );

            if (argumentVariable !== null) {
                this.nodeManager.addNode(argumentVariable);
            }
        }

        if (sourceMethod.outputArguments !== null) {
            const outputArgumentsId = `${newInstanceId}-OutputArguments`;
            const argumentVariable = newMethod.setOutputArguments(
                new UaNodeId(outputArgumentsId, this.nodeManager.nsIndex()),
                sourceMethod.outputArguments,
            );

            if (argumentVariable !== null) {
                this.nodeManager.addNode(argumentVariable);
            }
        }

        return newMethod;
    }
}

