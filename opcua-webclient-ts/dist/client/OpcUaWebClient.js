"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.OpcUaWebClient = void 0;
const opcua_webapi_1 = require("opcua-webapi");
const node_opcua_status_code_1 = require("node-opcua-status-code");
const node_opcua_variant_1 = require("node-opcua-variant");
const common_1 = require("../common");
class OpcUaWebClient {
    constructor(clientConfig) {
        this.api = new opcua_webapi_1.DefaultApi(clientConfig.apiConfig);
        this.clientConfig = clientConfig;
        this.requestHandle = 1;
    }
    getNativeApi() {
        return this.api;
    }
    // API wrappers by use cases
    browseChild(nodeId, nodeClass, maxReferences) {
        return __awaiter(this, void 0, void 0, function* () {
            var _a;
            let nodesToBrowse = {
                NodeId: nodeId.toString(),
                BrowseDirection: opcua_webapi_1.BrowseDirection.Forward,
                ReferenceTypeId: opcua_webapi_1.ReferenceTypeIds.HierarchicalReferences,
                IncludeSubtypes: true,
                NodeClassMask: (nodeClass) ? nodeClass : Number(opcua_webapi_1.NodeClass.Object | opcua_webapi_1.NodeClass.Variable | opcua_webapi_1.NodeClass.Method),
                ResultMask: 63
            };
            let results = yield this.browse([nodesToBrowse], maxReferences);
            if (results.length != 1)
                throw new common_1.UaError(node_opcua_status_code_1.StatusCodes.BadUnexpectedError);
            if ((_a = results[0].StatusCode) === null || _a === void 0 ? void 0 : _a.Code)
                throw new common_1.UaError((0, node_opcua_status_code_1.getStatusCodeFromCode)(results[0].StatusCode.Code));
            return common_1.UaPayloadMapper.browseResultFromWebApi(results[0]);
        });
    }
    browseNextChild(continuationPoint) {
        return __awaiter(this, void 0, void 0, function* () {
            var _a;
            let results = yield this.browseNext([continuationPoint], false);
            if (results.length != 1)
                throw new common_1.UaError(node_opcua_status_code_1.StatusCodes.BadUnexpectedError);
            if ((_a = results[0].StatusCode) === null || _a === void 0 ? void 0 : _a.Code)
                throw new common_1.UaError((0, node_opcua_status_code_1.getStatusCodeFromCode)(results[0].StatusCode.Code));
            return common_1.UaPayloadMapper.browseResultFromWebApi(results[0]);
        });
    }
    readValues(nodeIds, timestampsToReturn) {
        return __awaiter(this, void 0, void 0, function* () {
            if (nodeIds.length == 0)
                throw new common_1.UaError(node_opcua_status_code_1.StatusCodes.BadNothingToDo);
            let nodesToRead = [];
            for (let item of nodeIds) {
                nodesToRead.push({ NodeId: item.toString(), AttributeId: opcua_webapi_1.Attributes.Value });
            }
            let results = yield this.read(nodesToRead, timestampsToReturn);
            let dataValues = [];
            for (let item of results) {
                dataValues.push(common_1.UaPayloadMapper.dataValueFromWebApi(item));
            }
            return dataValues;
        });
    }
    readNodeAttributes(nodeId, returnDescription) {
        return __awaiter(this, void 0, void 0, function* () {
            let nodeIdStr = nodeId.toString();
            let nodesToRead = [
                { NodeId: nodeIdStr, AttributeId: opcua_webapi_1.Attributes.NodeClass },
                { NodeId: nodeIdStr, AttributeId: opcua_webapi_1.Attributes.BrowseName },
                { NodeId: nodeIdStr, AttributeId: opcua_webapi_1.Attributes.DisplayName }
            ];
            if (returnDescription) {
                nodesToRead.push({ NodeId: nodeIdStr, AttributeId: opcua_webapi_1.Attributes.Description });
            }
            let results = yield this.read(nodesToRead);
            let dataValues = [];
            for (let item of results) {
                dataValues.push(common_1.UaPayloadMapper.dataValueFromWebApi(item));
            }
            if (dataValues[0].statusCode.isNotGood())
                throw new common_1.UaError(dataValues[0].statusCode);
            if (dataValues[1].statusCode.isNotGood())
                throw new common_1.UaError(dataValues[1].statusCode);
            if (dataValues[2].statusCode.isNotGood())
                throw new common_1.UaError(dataValues[2].statusCode);
            let nodeClassValue = dataValues[0].value;
            let browseNameValue = dataValues[1].value;
            let displayNameValue = dataValues[2].value;
            if (node_opcua_variant_1.DataType.Int32 != nodeClassValue.dataType ||
                node_opcua_variant_1.DataType.QualifiedName != browseNameValue.dataType ||
                node_opcua_variant_1.DataType.LocalizedText != displayNameValue.dataType)
                throw new common_1.UaError(node_opcua_status_code_1.StatusCodes.BadNodeAttributesInvalid);
            let descriptionValue = (returnDescription &&
                dataValues[3].statusCode.isGood() &&
                dataValues[3].value.dataType == node_opcua_variant_1.DataType.LocalizedText) ? dataValues[3].value : undefined;
            let ret = {
                nodeClass: nodeClassValue.value,
                browseName: browseNameValue.value.name,
                displayName: displayNameValue.value,
                description: (descriptionValue) ? descriptionValue.value : undefined
            };
            return ret;
        });
    }
    readVariableAttributes(nodeId) {
        return __awaiter(this, void 0, void 0, function* () {
            let nodeIdStr = nodeId.toString();
            let nodesToRead = [
                { NodeId: nodeIdStr, AttributeId: opcua_webapi_1.Attributes.NodeClass },
                { NodeId: nodeIdStr, AttributeId: opcua_webapi_1.Attributes.DataType },
                { NodeId: nodeIdStr, AttributeId: opcua_webapi_1.Attributes.ValueRank },
                { NodeId: nodeIdStr, AttributeId: opcua_webapi_1.Attributes.AccessLevel },
                { NodeId: nodeIdStr, AttributeId: opcua_webapi_1.Attributes.UserAccessLevel },
                { NodeId: nodeIdStr, AttributeId: opcua_webapi_1.Attributes.Historizing }
            ];
            let results = yield this.read(nodesToRead);
            let dataValues = [];
            for (let item of results) {
                dataValues.push(common_1.UaPayloadMapper.dataValueFromWebApi(item));
            }
            if (dataValues[0].statusCode.isNotGood())
                throw new common_1.UaError(dataValues[0].statusCode);
            if (dataValues[0].value && dataValues[0].value.value != opcua_webapi_1.NodeClass.Variable)
                throw new common_1.UaError(node_opcua_status_code_1.StatusCodes.BadNodeClassInvalid);
            let dataTypeValue = (dataValues[1].statusCode.isGood() &&
                dataValues[1].value.dataType == node_opcua_variant_1.DataType.NodeId) ? dataValues[1].value : undefined;
            let valueRankValue = (dataValues[2].statusCode.isGood() &&
                dataValues[2].value.dataType == node_opcua_variant_1.DataType.Int32) ? dataValues[2].value : undefined;
            let accessLevelValue = (dataValues[3].statusCode.isGood() &&
                dataValues[3].value.dataType == node_opcua_variant_1.DataType.Byte) ? dataValues[3].value : undefined;
            let userAccessLevelValue = (dataValues[4].statusCode.isGood() &&
                dataValues[4].value.dataType == node_opcua_variant_1.DataType.Byte) ? dataValues[4].value : undefined;
            let historizingValue = (dataValues[5].statusCode.isGood() &&
                dataValues[5].value.dataType == node_opcua_variant_1.DataType.Boolean) ? dataValues[5].value : undefined;
            let ret = {
                dataType: (dataTypeValue) ? dataTypeValue.value : undefined,
                valueRank: (valueRankValue) ? valueRankValue.value : undefined,
                accessLevel: (accessLevelValue) ? accessLevelValue.value : undefined,
                userAccessLevel: (userAccessLevelValue) ? userAccessLevelValue.value : undefined,
                historizing: (historizingValue) ? historizingValue.value : undefined
            };
            return ret;
        });
    }
    // Native APIs
    browse(nodesToBrowse, maxReferencesPerNode, view, additionalParameters) {
        return __awaiter(this, void 0, void 0, function* () {
            var _a, _b;
            let request = (0, opcua_webapi_1.BrowseRequestFromJSON)({
                RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
                NodesToBrowse: nodesToBrowse,
                RequestedMaxReferencesPerNode: (maxReferencesPerNode && maxReferencesPerNode > 0) ? maxReferencesPerNode : undefined,
                View: view
            });
            let response = yield this.api.browse({ browseRequest: request });
            if ((_b = (_a = response === null || response === void 0 ? void 0 : response.ResponseHeader) === null || _a === void 0 ? void 0 : _a.ServiceResult) === null || _b === void 0 ? void 0 : _b.Code)
                throw new common_1.UaError((0, node_opcua_status_code_1.getStatusCodeFromCode)(response.ResponseHeader.ServiceResult.Code));
            if (!response.Results || nodesToBrowse.length != response.Results.length)
                throw new common_1.UaError(node_opcua_status_code_1.StatusCodes.BadDataLost);
            return response.Results;
        });
    }
    browseNext(continuationPoints, releaseContinuationPoints, additionalParameters) {
        return __awaiter(this, void 0, void 0, function* () {
            var _a, _b;
            let request = (0, opcua_webapi_1.BrowseNextRequestFromJSON)({
                RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
                ContinuationPoints: continuationPoints,
                ReleaseContinuationPoints: releaseContinuationPoints
            });
            let response = yield this.api.browseNext({ browseNextRequest: request });
            if ((_b = (_a = response === null || response === void 0 ? void 0 : response.ResponseHeader) === null || _a === void 0 ? void 0 : _a.ServiceResult) === null || _b === void 0 ? void 0 : _b.Code)
                throw new common_1.UaError((0, node_opcua_status_code_1.getStatusCodeFromCode)(response.ResponseHeader.ServiceResult.Code));
            if (!response.Results || continuationPoints.length != response.Results.length)
                throw new common_1.UaError(node_opcua_status_code_1.StatusCodes.BadDataLost);
            return response.Results;
        });
    }
    translate(browsePaths, additionalParameters) {
        return __awaiter(this, void 0, void 0, function* () {
            var _a, _b;
            let request = (0, opcua_webapi_1.TranslateBrowsePathsToNodeIdsRequestFromJSON)({
                RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
                BrowsePaths: browsePaths
            });
            let response = yield this.api.translateBrowsePathsToNodeIds({ translateBrowsePathsToNodeIdsRequest: request });
            if ((_b = (_a = response === null || response === void 0 ? void 0 : response.ResponseHeader) === null || _a === void 0 ? void 0 : _a.ServiceResult) === null || _b === void 0 ? void 0 : _b.Code)
                throw new common_1.UaError((0, node_opcua_status_code_1.getStatusCodeFromCode)(response.ResponseHeader.ServiceResult.Code));
            if (!response.Results || browsePaths.length != response.Results.length)
                throw new common_1.UaError(node_opcua_status_code_1.StatusCodes.BadUnknownResponse);
            return response.Results;
        });
    }
    read(nodesToRead, timestampsToReturn, maxAge, additionalParameters) {
        return __awaiter(this, void 0, void 0, function* () {
            var _a, _b;
            let request = (0, opcua_webapi_1.ReadRequestFromJSON)({
                RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
                NodesToRead: nodesToRead,
                MaxAge: (maxAge && maxAge > 0) ? maxAge : undefined,
                TimestampsToReturn: (timestampsToReturn &&
                    timestampsToReturn >= opcua_webapi_1.TimestampsToReturn.Source &&
                    timestampsToReturn < opcua_webapi_1.TimestampsToReturn.Invalid) ? timestampsToReturn : undefined
            });
            let response = yield this.api.read({ readRequest: request });
            if ((_b = (_a = response === null || response === void 0 ? void 0 : response.ResponseHeader) === null || _a === void 0 ? void 0 : _a.ServiceResult) === null || _b === void 0 ? void 0 : _b.Code)
                throw new common_1.UaError((0, node_opcua_status_code_1.getStatusCodeFromCode)(response.ResponseHeader.ServiceResult.Code));
            if (!response.Results || nodesToRead.length != response.Results.length)
                throw new common_1.UaError(node_opcua_status_code_1.StatusCodes.BadDataLost);
            return response.Results;
        });
    }
    write(nodesToWrite, additionalParameters) {
        return __awaiter(this, void 0, void 0, function* () {
            var _a, _b;
            let request = (0, opcua_webapi_1.WriteRequestFromJSON)({
                RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
                NodesToWrite: nodesToWrite
            });
            let response = yield this.api.write({ writeRequest: request });
            if ((_b = (_a = response === null || response === void 0 ? void 0 : response.ResponseHeader) === null || _a === void 0 ? void 0 : _a.ServiceResult) === null || _b === void 0 ? void 0 : _b.Code)
                throw new common_1.UaError((0, node_opcua_status_code_1.getStatusCodeFromCode)(response.ResponseHeader.ServiceResult.Code));
            if (!response.Results || nodesToWrite.length != response.Results.length)
                throw new common_1.UaError(node_opcua_status_code_1.StatusCodes.BadDataLost);
            return response.Results;
        });
    }
    call(methodsToCall, additionalParameters) {
        return __awaiter(this, void 0, void 0, function* () {
            var _a, _b;
            let request = (0, opcua_webapi_1.CallRequestFromJSON)({
                RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
                MethodsToCall: methodsToCall
            });
            let response = yield this.api.call({ callRequest: request });
            if ((_b = (_a = response === null || response === void 0 ? void 0 : response.ResponseHeader) === null || _a === void 0 ? void 0 : _a.ServiceResult) === null || _b === void 0 ? void 0 : _b.Code)
                throw new common_1.UaError((0, node_opcua_status_code_1.getStatusCodeFromCode)(response.ResponseHeader.ServiceResult.Code));
            if (!response.Results || methodsToCall.length != response.Results.length)
                throw new common_1.UaError(node_opcua_status_code_1.StatusCodes.BadDataLost);
            return response.Results;
        });
    }
    findServer(serverUris, endpointUrl, localeIds, additionalParameters) {
        return __awaiter(this, void 0, void 0, function* () {
            var _a, _b;
            let request = (0, opcua_webapi_1.FindServersRequestFromJSON)({
                RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
                ServerUris: serverUris,
                EndpointUrl: endpointUrl,
                LocaleIds: localeIds
            });
            let response = yield this.api.findServers({ findServersRequest: request });
            if ((_b = (_a = response === null || response === void 0 ? void 0 : response.ResponseHeader) === null || _a === void 0 ? void 0 : _a.ServiceResult) === null || _b === void 0 ? void 0 : _b.Code)
                throw new common_1.UaError((0, node_opcua_status_code_1.getStatusCodeFromCode)(response.ResponseHeader.ServiceResult.Code));
            if (!response.Servers)
                throw new common_1.UaError(node_opcua_status_code_1.StatusCodes.BadDataLost);
            return response.Servers;
        });
    }
    getEndpoints(endpointUrl, localeIds, profileUris, additionalParameters) {
        return __awaiter(this, void 0, void 0, function* () {
            var _a, _b;
            let request = (0, opcua_webapi_1.GetEndpointsRequestFromJSON)({
                RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
                EndpointUrl: endpointUrl,
                LocaleIds: localeIds,
                ProfileUris: profileUris
            });
            let response = yield this.api.getEndpoints({ getEndpointsRequest: request });
            if ((_b = (_a = response === null || response === void 0 ? void 0 : response.ResponseHeader) === null || _a === void 0 ? void 0 : _a.ServiceResult) === null || _b === void 0 ? void 0 : _b.Code)
                throw new common_1.UaError((0, node_opcua_status_code_1.getStatusCodeFromCode)(response.ResponseHeader.ServiceResult.Code));
            if (!response.Endpoints)
                throw new common_1.UaError(node_opcua_status_code_1.StatusCodes.BadDataLost);
            return response.Endpoints;
        });
    }
    requestHeader(clientConfig, additionalParameters) {
        let timeout = clientConfig.defaultTimeout;
        if (additionalParameters && additionalParameters.timeout)
            timeout = additionalParameters.timeout;
        let ret = {
            RequestHandle: this.requestHandle,
            TimeoutHint: timeout,
            Timestamp: new Date(),
            AuthenticationToken: this.authenticationToken,
            ReturnDiagnostics: (additionalParameters) ? additionalParameters.returnDiagnostics : undefined,
            AuditEntryId: (additionalParameters) ? additionalParameters.auditEntryId : undefined,
        };
        this.requestHandle++;
        if (0xFFFFFFFF == this.requestHandle)
            this.requestHandle = 1;
        return ret;
    }
}
exports.OpcUaWebClient = OpcUaWebClient;
