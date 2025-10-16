"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UaPayloadMapper = void 0;
const opcua_webapi_1 = require("opcua-webapi");
const types_1 = require("../types");
const types_2 = require("../types");
class UaPayloadMapper {
    static statusCodeFromWebApi(statusCode) {
        if (!statusCode)
            return (0, types_1.makeUaStatusCode)();
        return (0, types_1.makeUaStatusCode)(statusCode.Code);
    }
    static statusCodeToWebApi(statusCode) {
        return { Code: statusCode.value };
    }
    static localizedTextFromWebApi(text) {
        if (!text)
            return null;
        return new types_2.UaLocalizedText(text.Text, text.Locale);
    }
    static localizedTextToWebApi(text) {
        return {
            Text: text.text,
            Locale: text.locale
        };
    }
    static extensionObjectFromWebApi(extentionObject) {
        try {
            if (typeof extentionObject === "object" &&
                null !== extentionObject &&
                typeof extentionObject.UaTypeId === "string") {
                let typeId = (0, types_1.parseUaNodeId)(extentionObject.UaTypeId);
                let payload = extentionObject;
                delete payload.UaTypeId;
                return new types_2.UaExtensionObject(typeId, payload);
            }
        }
        catch (e) { }
        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
    }
    static extensionObjectToWebApi(extentionObject) {
        return extentionObject.toJson();
    }
    static variantFromWebApi(variant) {
        let dataType = variant.UaType;
        let value = variant.Value;
        let variantToReturn = new types_2.UaVariant();
        if (undefined == dataType || null == dataType || types_1.UaVariantType.Null == dataType ||
            undefined == value || null == value)
            return variantToReturn;
        let isArray = Array.isArray(value);
        try {
            if (types_1.UaVariantType.Int32 == dataType ||
                types_1.UaVariantType.UInt32 == dataType ||
                types_1.UaVariantType.Int16 == dataType ||
                types_1.UaVariantType.UInt16 == dataType ||
                types_1.UaVariantType.SByte == dataType ||
                types_1.UaVariantType.Byte == dataType ||
                types_1.UaVariantType.Int64 == dataType ||
                types_1.UaVariantType.UInt64 == dataType) {
                if (!isArray) {
                    if (typeof value === "number") {
                        return types_2.UaVariant.integer(value, dataType);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
                else {
                    if (value.every(item => typeof item === 'number')) {
                        return types_2.UaVariant.integers(value, dataType);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
            }
            else if (types_1.UaVariantType.Double == dataType) {
                if (!isArray) {
                    if (typeof value === "number") {
                        return types_2.UaVariant.double(value);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
                else {
                    if (value.every(item => typeof item === 'number')) {
                        return types_2.UaVariant.doubles(value);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
            }
            else if (types_1.UaVariantType.Float == dataType) {
                if (!isArray) {
                    if (typeof value === "number") {
                        return types_2.UaVariant.float(value);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
                else {
                    if (value.every(item => typeof item === 'number')) {
                        return types_2.UaVariant.floats(value);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
            }
            else if (types_1.UaVariantType.Boolean == dataType) {
                if (!isArray) {
                    if (typeof value === "boolean") {
                        return types_2.UaVariant.boolean(value);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
                else {
                    if (value.every(item => typeof item === 'boolean')) {
                        return types_2.UaVariant.booleans(value);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
            }
            else if (types_1.UaVariantType.String == dataType) {
                if (!isArray) {
                    if (typeof value === "string") {
                        return types_2.UaVariant.string(value);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
                else {
                    if (value.every(item => typeof item === 'string')) {
                        return types_2.UaVariant.strings(value);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
            }
            else if (types_1.UaVariantType.NodeId == dataType) {
                if (!isArray) {
                    if (typeof value === "string") {
                        return types_2.UaVariant.nodeId((0, types_1.parseUaNodeId)(value));
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
                else {
                    if (value.every(item => typeof item === 'string')) {
                        let nodeIds = [];
                        for (let item of value) {
                            nodeIds.push((0, types_1.parseUaNodeId)(item));
                        }
                        return types_2.UaVariant.nodeIds(nodeIds);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
            }
            else if (types_1.UaVariantType.ExpandedNodeId == dataType) {
                if (!isArray) {
                    if (typeof value === "string") {
                        return types_2.UaVariant.expandedNodeId((0, types_1.parseUaExpandedNodeId)(value));
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
                else {
                    if (value.every(item => typeof item === 'string')) {
                        let nodeIds = [];
                        for (let item of value) {
                            nodeIds.push((0, types_1.parseUaExpandedNodeId)(item));
                        }
                        return types_2.UaVariant.expandedNodeIds(nodeIds);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
            }
            else if (types_1.UaVariantType.DateTime == dataType) {
                if (!isArray) {
                    if (typeof value === "string") {
                        return types_2.UaVariant.dateTime(new Date(value));
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
                else {
                    if (value.every(item => typeof item === 'string')) {
                        let datetimes = [];
                        for (let item of value) {
                            datetimes.push(new Date(item));
                        }
                        return types_2.UaVariant.dateTimes(datetimes);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
            }
            else if (types_1.UaVariantType.ByteString == dataType) {
                if (!isArray) {
                    if (typeof value === "string") {
                        return types_2.UaVariant.byteString(value);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
                else {
                    if (value.every(item => typeof item === 'string')) {
                        let byteStrings = [];
                        for (let item of value) {
                            byteStrings.push(item);
                        }
                        return types_2.UaVariant.byteStrings(byteStrings);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
            }
            else if (types_1.UaVariantType.QualifiedName == dataType) {
                if (!isArray) {
                    if (typeof value === "string") {
                        return types_2.UaVariant.qualifiedName(value);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
                else {
                    if (value.every(item => typeof item === 'string')) {
                        let qualifiedNames = [];
                        for (let item of value) {
                            qualifiedNames.push(item);
                        }
                        return types_2.UaVariant.qualifiedNames(qualifiedNames);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
            }
            else if (types_1.UaVariantType.Guid == dataType) {
                if (!isArray) {
                    if (typeof value === "string") {
                        return types_2.UaVariant.guid(value);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
                else {
                    if (value.every(item => typeof item === 'string')) {
                        let guids = [];
                        for (let item of value) {
                            guids.push(item);
                        }
                        return types_2.UaVariant.guids(guids);
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
            }
            else if (types_1.UaVariantType.StatusCode == dataType) {
                if (!isArray) {
                    let statusCode = value;
                    if (typeof statusCode.Code === "number") {
                        return types_2.UaVariant.statusCode((0, types_1.makeUaStatusCode)(statusCode.Code));
                    }
                    else {
                        throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                    }
                }
                else {
                    let statusCodes = [];
                    for (let item of value) {
                        let statusCode = item;
                        if (typeof statusCode.Code === "number") {
                            statusCodes.push((0, types_1.makeUaStatusCode)(statusCode.Code));
                        }
                        else {
                            throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
                        }
                    }
                    return types_2.UaVariant.statusCodes(statusCodes);
                }
            }
            else if (types_1.UaVariantType.LocalizedText == dataType) {
                if (!isArray) {
                    return types_2.UaVariant.localizedText(UaPayloadMapper.localizedTextFromWebApi(value));
                }
                else {
                    let localizedTexts = [];
                    for (let item of value) {
                        localizedTexts.push(UaPayloadMapper.localizedTextFromWebApi(item));
                    }
                    return types_2.UaVariant.localizedTexts(localizedTexts);
                }
            }
            else if (types_1.UaVariantType.ExtensionObject == dataType) {
                if (!isArray) {
                    return types_2.UaVariant.extensionObject(UaPayloadMapper.extensionObjectFromWebApi(value));
                }
                else {
                    let extensionObjects = [];
                    for (let item of value) {
                        extensionObjects.push(UaPayloadMapper.extensionObjectFromWebApi(item));
                    }
                    return types_2.UaVariant.extensionObjects(extensionObjects);
                }
            }
            else {
                return variantToReturn;
            }
        }
        catch (e) {
            throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
        }
    }
    static variantToWebApi(variant) {
        let dataType = variant.type;
        if (types_1.UaVariantType.NodeId == dataType) {
            if (variant.isScalar()) {
                return { Value: variant.value.toString(), UaType: dataType };
            }
            else {
                let nodeIds = variant.value;
                let value = [];
                for (let item of nodeIds) {
                    value.push(item.toString());
                }
                return { Value: value, UaType: dataType };
            }
        }
        else if (types_1.UaVariantType.ExpandedNodeId == dataType) {
            if (variant.isScalar()) {
                return { Value: variant.value.toString(), UaType: dataType };
            }
            else {
                let nodeIds = variant.value;
                let value = [];
                for (let item of nodeIds) {
                    value.push(item.toString());
                }
                return { Value: value, UaType: dataType };
            }
        }
        else if (types_1.UaVariantType.DateTime == dataType) {
            if (variant.isScalar()) {
                return { Value: variant.value.toISOString(), UaType: dataType };
            }
            else {
                let dateTimes = variant.value;
                let value = [];
                for (let item of dateTimes) {
                    value.push(item.toISOString());
                }
                return { Value: value, UaType: dataType };
            }
        }
        else if (types_1.UaVariantType.LocalizedText == dataType) {
            if (variant.isScalar()) {
                return { Value: UaPayloadMapper.localizedTextToWebApi(variant.value), UaType: dataType };
            }
            else {
                let localizedTexts = variant.value;
                let value = [];
                for (let item of localizedTexts) {
                    value.push(UaPayloadMapper.localizedTextToWebApi(item));
                }
                return { Value: value, UaType: dataType };
            }
        }
        else if (types_1.UaVariantType.StatusCode == dataType) {
            if (variant.isScalar()) {
                return { Value: UaPayloadMapper.statusCodeToWebApi(variant.value), UaType: dataType };
            }
            else {
                let statusCodes = variant.value;
                let value = [];
                for (let item of statusCodes) {
                    value.push(UaPayloadMapper.statusCodeToWebApi(item));
                }
                return { Value: value, UaType: dataType };
            }
        }
        else if (types_1.UaVariantType.ExtensionObject == dataType) {
            if (variant.isScalar()) {
                return { Value: UaPayloadMapper.extensionObjectToWebApi(variant.value), UaType: dataType };
            }
            else {
                let extensionObjects = variant.value;
                let value = [];
                for (let item of extensionObjects) {
                    value.push(UaPayloadMapper.extensionObjectToWebApi(item));
                }
                return { Value: value, UaType: dataType };
            }
        }
        else {
            return { Value: variant.value, UaType: dataType };
        }
    }
    static dataValueFromWebApi(dataValue) {
        let variant = { Value: dataValue.Value, UaType: dataValue.UaType };
        let value = UaPayloadMapper.variantFromWebApi(variant);
        let ret = new types_2.UaDataValue(value, UaPayloadMapper.statusCodeFromWebApi(dataValue.StatusCode), dataValue.SourceTimestamp, dataValue.ServerTimestamp);
        return ret;
    }
    static referenceDescriptionFromWebApi(referenceDesc) {
        if (!referenceDesc.NodeId ||
            !referenceDesc.NodeClass ||
            !referenceDesc.ReferenceTypeId ||
            undefined == referenceDesc.IsForward ||
            undefined == referenceDesc.BrowseName ||
            undefined == referenceDesc.DisplayName)
            throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
        try {
            let nodeId = (0, types_1.parseUaExpandedNodeId)(referenceDesc.NodeId);
            let referenceTypeId = (0, types_1.parseUaNodeId)(referenceDesc.ReferenceTypeId);
            let typeDefinitionId = (referenceDesc.TypeDefinition) ? (0, types_1.parseUaExpandedNodeId)(referenceDesc.TypeDefinition) : undefined;
            let displayName = UaPayloadMapper.localizedTextFromWebApi(referenceDesc.DisplayName);
            return {
                nodeId: nodeId,
                nodeClass: referenceDesc.NodeClass,
                referenceTypeId: referenceTypeId,
                isForward: referenceDesc.IsForward,
                browseName: referenceDesc.BrowseName,
                displayName: displayName,
                typeDefinition: typeDefinitionId
            };
        }
        catch (e) {
            throw new types_1.UaError((0, types_1.makeUaStatusCode)(opcua_webapi_1.StatusCodes.BadDecodingError));
        }
    }
    static browseResultFromWebApi(browseResult) {
        let results = [];
        if (browseResult.References) {
            for (let item of browseResult.References) {
                results.push(UaPayloadMapper.referenceDescriptionFromWebApi(item));
            }
        }
        return {
            results: results,
            continuationPoint: browseResult.ContinuationPoint
        };
    }
}
exports.UaPayloadMapper = UaPayloadMapper;
