"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UaPayloadMapper = void 0;
const types_1 = require("../types");
const node_opcua_nodeid_1 = require("node-opcua-nodeid");
const node_opcua_variant_1 = require("node-opcua-variant");
const node_opcua_status_code_1 = require("node-opcua-status-code");
const node_opcua_data_model_1 = require("node-opcua-data-model");
const node_opcua_data_value_1 = require("node-opcua-data-value");
const UaExtensionObject_1 = require("../types/UaExtensionObject");
class UaPayloadMapper {
    static localizedTextFromWebApi(text) {
        return new node_opcua_data_model_1.LocalizedText({
            locale: text.Locale,
            text: text.Text
        });
    }
    static extensionObjectFromWebApi(extentionObject) {
        try {
            if (typeof extentionObject === "object" &&
                null !== extentionObject &&
                typeof extentionObject.UaTypeId === "string") {
                let typeId = (0, node_opcua_nodeid_1.coerceNodeId)(extentionObject.UaTypeId);
                let payload = extentionObject;
                delete payload.UaTypeId;
                return new UaExtensionObject_1.UaExtensionObject(typeId, payload);
            }
        }
        catch (e) { }
        console.log("Parsing error");
        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
    }
    static variantFromWebApi(variant) {
        let dataType = variant.UaType;
        let value = variant.Value;
        if (undefined == dataType || null == dataType || node_opcua_variant_1.DataType.Null == dataType ||
            undefined == value || null == value)
            return new node_opcua_variant_1.Variant();
        let isArray = Array.isArray(value);
        try {
            if (node_opcua_variant_1.DataType.Int32 == dataType ||
                node_opcua_variant_1.DataType.UInt32 == dataType ||
                node_opcua_variant_1.DataType.Float == dataType ||
                node_opcua_variant_1.DataType.Double == dataType ||
                node_opcua_variant_1.DataType.Int16 == dataType ||
                node_opcua_variant_1.DataType.UInt16 == dataType ||
                node_opcua_variant_1.DataType.SByte == dataType ||
                node_opcua_variant_1.DataType.Byte == dataType) {
                if (!isArray) {
                    if (typeof value === "number") {
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: value });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
                else {
                    if (value.every(item => typeof item === 'number')) {
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: value, arrayType: node_opcua_variant_1.VariantArrayType.Array });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
            }
            else if (node_opcua_variant_1.DataType.Int64 == dataType || node_opcua_variant_1.DataType.UInt64 == dataType) {
                if (!isArray) {
                    return new node_opcua_variant_1.Variant({ dataType: dataType, value: value });
                }
                else {
                    if (value.every(item => typeof item === 'number')) {
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: value, arrayType: node_opcua_variant_1.VariantArrayType.Array });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
            }
            else if (node_opcua_variant_1.DataType.Boolean == dataType) {
                if (!isArray) {
                    if (typeof value === "boolean") {
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: value });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
                else {
                    if (value.every(item => typeof item === 'boolean')) {
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: value, arrayType: node_opcua_variant_1.VariantArrayType.Array });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
            }
            else if (node_opcua_variant_1.DataType.String == dataType || node_opcua_variant_1.DataType.Guid == dataType) {
                if (!isArray) {
                    if (typeof value === "string") {
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: value });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
                else {
                    if (value.every(item => typeof item === 'string')) {
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: value, arrayType: node_opcua_variant_1.VariantArrayType.Array });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
            }
            else if (node_opcua_variant_1.DataType.NodeId == dataType) {
                if (!isArray) {
                    if (typeof value === "string") {
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: (0, node_opcua_nodeid_1.coerceNodeId)(value) });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
                else {
                    if (value.every(item => typeof item === 'string')) {
                        let nodeIds = [];
                        for (let item of value) {
                            nodeIds.push((0, node_opcua_nodeid_1.coerceNodeId)(item));
                        }
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: nodeIds, arrayType: node_opcua_variant_1.VariantArrayType.Array });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
            }
            else if (node_opcua_variant_1.DataType.ExpandedNodeId == dataType) {
                if (!isArray) {
                    if (typeof value === "string") {
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: (0, node_opcua_nodeid_1.coerceExpandedNodeId)(value) });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
                else {
                    if (value.every(item => typeof item === 'string')) {
                        let nodeIds = [];
                        for (let item of value) {
                            nodeIds.push((0, node_opcua_nodeid_1.coerceExpandedNodeId)(item));
                        }
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: nodeIds, arrayType: node_opcua_variant_1.VariantArrayType.Array });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
            }
            else if (node_opcua_variant_1.DataType.DateTime == dataType) {
                if (!isArray) {
                    if (typeof value === "string") {
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: new Date(value) });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
                else {
                    if (value.every(item => typeof item === 'string')) {
                        let datetimes = [];
                        for (let item of value) {
                            datetimes.push(new Date(item));
                        }
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: datetimes, arrayType: node_opcua_variant_1.VariantArrayType.Array });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
            }
            else if (node_opcua_variant_1.DataType.ByteString == dataType) {
                if (!isArray) {
                    if (typeof value === "string") {
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: Buffer.from(value) });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
                else {
                    if (value.every(item => typeof item === 'string')) {
                        let bytestrings = [];
                        for (let item of value) {
                            bytestrings.push(Buffer.from(item));
                        }
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: bytestrings, arrayType: node_opcua_variant_1.VariantArrayType.Array });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
            }
            else if (node_opcua_variant_1.DataType.QualifiedName == dataType) {
                if (!isArray) {
                    if (typeof value === "string") {
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: new node_opcua_data_model_1.QualifiedName(value) });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
                else {
                    if (value.every(item => typeof item === 'string')) {
                        let qualifiedNames = [];
                        for (let item of value) {
                            qualifiedNames.push(new node_opcua_data_model_1.QualifiedName(item));
                        }
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: qualifiedNames, arrayType: node_opcua_variant_1.VariantArrayType.Array });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
            }
            else if (node_opcua_variant_1.DataType.StatusCode == dataType) {
                if (!isArray) {
                    let statusCode = value;
                    if (typeof statusCode.Code === "number") {
                        return new node_opcua_variant_1.Variant({ dataType: dataType, value: (0, node_opcua_status_code_1.getStatusCodeFromCode)(statusCode.Code) });
                    }
                    else {
                        throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                    }
                }
                else {
                    let statusCodes = [];
                    for (let item of value) {
                        let statusCode = item;
                        if (typeof statusCode.Code === "number") {
                            statusCodes.push((0, node_opcua_status_code_1.getStatusCodeFromCode)(statusCode.Code));
                        }
                        else {
                            throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
                        }
                    }
                    return new node_opcua_variant_1.Variant({ dataType: dataType, value: statusCodes, arrayType: node_opcua_variant_1.VariantArrayType.Array });
                }
            }
            else if (node_opcua_variant_1.DataType.LocalizedText == dataType) {
                if (!isArray) {
                    let localizedText = value;
                    return new node_opcua_variant_1.Variant({ dataType: dataType, value: UaPayloadMapper.localizedTextFromWebApi(localizedText) });
                }
                else {
                    let localizedTexts = [];
                    for (let item of value) {
                        let localizedText = item;
                        localizedTexts.push(UaPayloadMapper.localizedTextFromWebApi(localizedText));
                    }
                    return new node_opcua_variant_1.Variant({ dataType: dataType, value: localizedTexts, arrayType: node_opcua_variant_1.VariantArrayType.Array });
                }
            }
            else if (node_opcua_variant_1.DataType.ExtensionObject == dataType) {
                if (!isArray) {
                    let variant = new node_opcua_variant_1.Variant({ dataType: dataType, value: UaPayloadMapper.extensionObjectFromWebApi(value) });
                    console.log(variant.value);
                    return variant;
                }
                else {
                    let extensionObjects = [];
                    for (let item of value) {
                        extensionObjects.push(UaPayloadMapper.extensionObjectFromWebApi(item));
                    }
                    return new node_opcua_variant_1.Variant({ dataType: dataType, value: extensionObjects, arrayType: node_opcua_variant_1.VariantArrayType.Array });
                }
            }
            else {
                return new node_opcua_variant_1.Variant();
            }
        }
        catch (e) {
            throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
        }
    }
    static dataValueFromWebApi(dataValue) {
        var _a;
        let variant = { Value: dataValue.Value, UaType: dataValue.UaType };
        let value = UaPayloadMapper.variantFromWebApi(variant);
        let statusCode = node_opcua_status_code_1.StatusCodes.Good;
        if ((_a = dataValue.StatusCode) === null || _a === void 0 ? void 0 : _a.Code)
            statusCode = (0, node_opcua_status_code_1.getStatusCodeFromCode)(dataValue.StatusCode.Code);
        let ret = new node_opcua_data_value_1.DataValue({
            value: value,
            statusCode: statusCode,
            sourceTimestamp: dataValue.SourceTimestamp,
            serverTimestamp: dataValue.ServerTimestamp
        });
        return ret;
    }
    static referenceDescriptionFromWebApi(referenceDesc) {
        if (!referenceDesc.NodeId ||
            !referenceDesc.NodeClass ||
            !referenceDesc.ReferenceTypeId ||
            undefined == referenceDesc.IsForward ||
            undefined == referenceDesc.BrowseName ||
            undefined == referenceDesc.DisplayName)
            throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
        try {
            let nodeId = (0, node_opcua_nodeid_1.coerceExpandedNodeId)(referenceDesc.NodeId);
            let referenceTypeId = (0, node_opcua_nodeid_1.coerceNodeId)(referenceDesc.ReferenceTypeId);
            let typeDefinitionId = (referenceDesc.TypeDefinition) ? (0, node_opcua_nodeid_1.coerceExpandedNodeId)(referenceDesc.TypeDefinition) : undefined;
            let displayName = UaPayloadMapper.localizedTextFromWebApi(referenceDesc.DisplayName);
            return Object.assign(new types_1.UaReferenceDescriptor(), {
                nodeId: nodeId,
                nodeClass: referenceDesc.NodeClass,
                referenceTypeId: referenceTypeId,
                isForward: referenceDesc.IsForward,
                browseName: referenceDesc.BrowseName,
                displayName: displayName,
                typeDefinition: typeDefinitionId
            });
        }
        catch (e) {
            throw new types_1.UaError(node_opcua_status_code_1.StatusCodes.BadDecodingError);
        }
    }
    static browseResultFromWebApi(browseResult) {
        let results = [];
        if (browseResult.References) {
            for (let item of browseResult.References) {
                results.push(UaPayloadMapper.referenceDescriptionFromWebApi(item));
            }
        }
        return Object.assign(new types_1.UaReferenceDescriptor(), {
            results: results,
            continuationPoint: browseResult.ContinuationPoint
        });
    }
}
exports.UaPayloadMapper = UaPayloadMapper;
