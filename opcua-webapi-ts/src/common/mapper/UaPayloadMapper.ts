import { BrowseResult, ExtensionObject, ExtensionObjectFromJSON, ReferenceDescription } from "opcua-webapi";
import { LocalizedText, Variant, StatusCode, DataValue,StatusCodes } from "opcua-webapi"
import { parseUaExpandedNodeId, parseUaNodeId, makeUaStatusCode, UaBrowseResult, UaError, UaReferenceDescriptor, UaVariantType } from "../types";
import { UaNodeId, UaExpandedNodeId, UaStatusCode, UaLocalizedText, UaExtensionObject, UaVariant, UaDataValue } from "../types";

export class UaPayloadMapper
{
    static statusCodeFromWebApi(statusCode? : StatusCode | null) : UaStatusCode
    {       
        if (!statusCode) return makeUaStatusCode();
        return makeUaStatusCode(statusCode.Code);
    }

    static statusCodeToWebApi(statusCode : UaStatusCode) : StatusCode
    {
        return { Code: statusCode.value };
    }

    static localizedTextFromWebApi(text?: LocalizedText | null) : UaLocalizedText | null
    {
        if (!text || !text.Text) return null;
        return new UaLocalizedText(text.Text, text.Locale);
    }

    static localizedTextToWebApi(text: UaLocalizedText) : LocalizedText
    {
        return {
            Text: text.text, 
            Locale: (text.locale) ? text.locale : undefined };
    }  

    static extensionObjectFromWebApi(json: any) : UaExtensionObject
    {        
        try
        {
            let extensionObject = ExtensionObjectFromJSON(json);

            if (null !== extensionObject)
            {
                let typeId = parseUaNodeId(extensionObject.UaTypeId);
                let jsonStr = atob(extensionObject.UaBody);                

                let body = JSON.parse(jsonStr);
                return new UaExtensionObject(typeId, body);                
            }
        } catch (e) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
        }
    }

    static extensionObjectToWebApi(extentionObject: UaExtensionObject) : ExtensionObject
    {
        return {
            UaTypeId: extentionObject.typeId.toString(),
            UaBody: btoa(JSON.stringify(extentionObject.body)) };
    }

    static variantFromWebApi(variant: Variant) : UaVariant
    {
        let dataType = variant.UaType;
        let value = variant.Value;
        let variantToReturn = new UaVariant();

        if (undefined == dataType || null == dataType || UaVariantType.Null == dataType ||            
            undefined == value || null == value) return variantToReturn;

        let isArray = Array.isArray(value);

        try
        {
            if (UaVariantType.Int32 == dataType ||
                UaVariantType.UInt32 == dataType ||                
                UaVariantType.Int16 == dataType ||
                UaVariantType.UInt16 == dataType ||
                UaVariantType.SByte == dataType ||
                UaVariantType.Byte == dataType || 
                UaVariantType.Int64 == dataType || 
                UaVariantType.UInt64 == dataType)
            {
                if (!isArray)
                {
                    if (typeof value === "number")
                    {
                        return UaVariant.integer(value, dataType);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                } else {
                    if (value.every(item => typeof item === 'number'))
                    {
                        return UaVariant.integers(value, dataType);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                }                   
            } else if (UaVariantType.Double == dataType) {
                if (!isArray)
                {
                    if (typeof value === "number")
                    {                        
                        return UaVariant.double(value);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    }  
                } else {                  
                    if (value.every(item => typeof item === 'number'))
                    {
                        return UaVariant.doubles(value);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    }
                }
            } else if (UaVariantType.Float == dataType) {
                if (!isArray)
                {
                    if (typeof value === "number")
                    {
                        return UaVariant.float(value);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    }  
                } else {                  
                    if (value.every(item => typeof item === 'number'))
                    {
                        return UaVariant.floats(value);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    }
                }
            } else if (UaVariantType.Boolean == dataType) {
                if (!isArray)
                {
                    if (typeof value === "boolean")
                    {
                        return UaVariant.boolean(value);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                } else {
                    if (value.every(item => typeof item === 'boolean'))
                    {
                        return UaVariant.booleans(value);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    }
                }
            } else if (UaVariantType.String == dataType) {
                if (!isArray)
                {
                    if (typeof value === "string")                    
                    {
                        return UaVariant.string(value);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                } else {
                    if (value.every(item => typeof item === 'string'))
                    {
                        return UaVariant.strings(value);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                }
            } else if (UaVariantType.NodeId == dataType) {
                if (!isArray)
                {
                    if (typeof value === "string")
                    {
                        return UaVariant.nodeId(parseUaNodeId(value));
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                } else {
                    if (value.every(item => typeof item === 'string'))
                    {
                        let nodeIds : Array<UaNodeId> = [];
                        for (let item of value)
                        {
                            nodeIds.push(parseUaNodeId(item));
                        }
                        return UaVariant.nodeIds(nodeIds);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                }
            } else if (UaVariantType.ExpandedNodeId == dataType) {
                if (!isArray)
                {
                    if (typeof value === "string")
                    {
                        return UaVariant.expandedNodeId(parseUaExpandedNodeId(value));
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                } else {
                    if (value.every(item => typeof item === 'string'))
                    {
                        let nodeIds : Array<UaExpandedNodeId> = [];
                        for (let item of value)
                        {
                            nodeIds.push(parseUaExpandedNodeId(item));
                        }
                        return UaVariant.expandedNodeIds(nodeIds);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                }
            } else if (UaVariantType.DateTime == dataType) {
                if (!isArray)
                {
                    if (typeof value === "string")
                    {
                        return UaVariant.dateTime(new Date(value));
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                } else {
                    if (value.every(item => typeof item === 'string'))
                    {
                        let datetimes : Array<Date> = [];
                        for (let item of value)
                        {
                            datetimes.push(new Date(item));
                        }
                        return UaVariant.dateTimes(datetimes);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                }
            } else if (UaVariantType.ByteString == dataType) {
                if (!isArray)
                {
                    if (typeof value === "string")
                    {
                        return UaVariant.byteString(value);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                } else {
                    if (value.every(item => typeof item === 'string'))
                    {
                        let byteStrings : Array<string> = [];
                        for (let item of value)
                        {
                            byteStrings.push(item);
                        }
                        return UaVariant.byteStrings(byteStrings);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                }
            } else if (UaVariantType.QualifiedName == dataType) {
                if (!isArray)
                {
                    if (typeof value === "string")
                    {
                        return UaVariant.qualifiedName(value);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                } else {
                    if (value.every(item => typeof item === 'string'))
                    {
                        let qualifiedNames : Array<string> = [];
                        for (let item of value)
                        {
                            qualifiedNames.push(item);
                        }
                        return UaVariant.qualifiedNames(qualifiedNames);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                }
            } else if (UaVariantType.Guid == dataType) {
                if (!isArray)
                {
                    if (typeof value === "string")
                    {
                        return UaVariant.guid(value);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                } else {
                    if (value.every(item => typeof item === 'string'))
                    {
                        let guids : Array<string> = [];
                        for (let item of value)
                        {
                            guids.push(item);
                        }
                        return UaVariant.guids(guids);
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    } 
                }
            }            
            else if (UaVariantType.StatusCode == dataType) {
                if (!isArray)
                {
                    let statusCode : StatusCode = value;
                    if (typeof statusCode.Code === "number")
                    {
                        return UaVariant.statusCode(makeUaStatusCode(statusCode.Code));
                    } else {
                        throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                    }
                } else {
                    let statusCodes : Array<UaStatusCode> = [];
                    for (let item of value)
                    {
                        let statusCode : StatusCode = item;
                        if (typeof statusCode.Code === "number")
                        {
                            statusCodes.push(makeUaStatusCode(statusCode.Code));
                        } else {
                            throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                        }
                    }                  
                    
                    return UaVariant.statusCodes(statusCodes);
                }
            } else if (UaVariantType.LocalizedText == dataType) {
                if (!isArray)
                {
                    return UaVariant.localizedText(UaPayloadMapper.localizedTextFromWebApi(value)); 
                } else {
                    let localizedTexts : Array<UaLocalizedText> = [];
                    for (let item of value)
                    {                        
                        localizedTexts.push(UaPayloadMapper.localizedTextFromWebApi(item));
                    }                        
                    
                    return UaVariant.localizedTexts(localizedTexts);  
                }
            } else if (UaVariantType.ExtensionObject == dataType) {
                if (!isArray)
                {                  
                    return UaVariant.extensionObject(UaPayloadMapper.extensionObjectFromWebApi(value)); 
                } else {
                    let extensionObjects : Array<UaExtensionObject> = [];
                    for (let item of value)
                    {                        
                        extensionObjects.push(UaPayloadMapper.extensionObjectFromWebApi(item));
                    }                        
                                      
                    return UaVariant.extensionObjects(extensionObjects);  
                } 
            } else {
                return variantToReturn;  
            }
        } catch(e) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
        }
    }

    static variantToWebApi(variant: UaVariant) : Variant
    {
        let dataType = variant.type;

        if (UaVariantType.NodeId == dataType)
        {
            if (variant.isScalar())
            {
                return { Value: (variant.value as UaNodeId).toString(), UaType: dataType };
            } else {
                let nodeIds = variant.value as Array<UaNodeId>;
                let value = [];
                for (let item of nodeIds)
                {
                    value.push(item.toString());
                }
                return { Value: value, UaType: dataType };
            }
        } else if (UaVariantType.ExpandedNodeId == dataType) {
            if (variant.isScalar())
            {
                return { Value: (variant.value as UaExpandedNodeId).toString(), UaType: dataType };
            } else {
                let nodeIds = variant.value as Array<UaExpandedNodeId>;
                let value = [];
                for (let item of nodeIds)
                {
                    value.push(item.toString());
                }
                return { Value: value, UaType: dataType };
            }
        } else if (UaVariantType.DateTime == dataType) {
            if (variant.isScalar())
            {
                return { Value: (variant.value as Date).toISOString(), UaType: dataType };
            } else {
                let dateTimes = variant.value as Array<Date>;
                let value = [];
                for (let item of dateTimes)
                {
                    value.push(item.toISOString());
                }
                return { Value: value, UaType: dataType };
            }
        } else if (UaVariantType.LocalizedText == dataType) {
            if (variant.isScalar())
            {
                return { Value: UaPayloadMapper.localizedTextToWebApi(variant.value), UaType: dataType };
            } else {
                let localizedTexts = variant.value as Array<UaLocalizedText>;
                let value = [];
                for (let item of localizedTexts)
                {
                    value.push(UaPayloadMapper.localizedTextToWebApi(item));
                }
                return { Value: value, UaType: dataType };
            }
        } else if (UaVariantType.StatusCode == dataType) {
            if (variant.isScalar())
            {
                return { Value: UaPayloadMapper.statusCodeToWebApi(variant.value), UaType: dataType };
            } else {
                let statusCodes = variant.value as Array<UaStatusCode>;
                let value = [];
                for (let item of statusCodes)
                {
                    value.push(UaPayloadMapper.statusCodeToWebApi(item));
                }
                return { Value: value, UaType: dataType };
            }
        } else if (UaVariantType.ExtensionObject == dataType) {
            if (variant.isScalar())
            {
                return { Value: UaPayloadMapper.extensionObjectToWebApi(variant.value), UaType: dataType };
            } else {
                let extensionObjects = variant.value as Array<UaExtensionObject>;
                let value = [];
                for (let item of extensionObjects)
                {
                    value.push(UaPayloadMapper.extensionObjectToWebApi(item));
                }
                return { Value: value, UaType: dataType };
            }
        } else { 
            return { Value: variant.value, UaType: dataType };
        }
    }

    static dataValueFromWebApi(dataValue: DataValue) : UaDataValue
    {
        let variant : Variant = { Value: dataValue.Value, UaType: dataValue.UaType };
        let value = UaPayloadMapper.variantFromWebApi(variant);

        let ret = new UaDataValue(
            value,
            UaPayloadMapper.statusCodeFromWebApi(dataValue.StatusCode),
            dataValue.SourceTimestamp,
            dataValue.ServerTimestamp);
                
        return ret;
    }

    static dataValueToWebApi(dataValue: UaDataValue) : DataValue
    {
        let variant = UaPayloadMapper.variantToWebApi(dataValue.value);

        let ret : DataValue = {
            Value: variant.Value,
            UaType: variant.UaType,
            StatusCode: (dataValue.statusCode.value == 0) ? undefined : UaPayloadMapper.statusCodeToWebApi(dataValue.statusCode),
            SourceTimestamp: (null != dataValue.sourceTimestamp) ? dataValue.sourceTimestamp : undefined,
            ServerTimestamp: (null != dataValue.serverTimestamp) ? dataValue.serverTimestamp : undefined
        };

        return ret;
    }

    static referenceDescriptionFromWebApi(referenceDesc : ReferenceDescription) : UaReferenceDescriptor
    {
        if (!referenceDesc.NodeId) throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));

        try
        {
            let nodeId = parseUaExpandedNodeId(referenceDesc.NodeId);
            let nodeClass = (referenceDesc.NodeClass) ? referenceDesc.NodeClass : 0;
            let referenceTypeId = (referenceDesc.ReferenceTypeId) ? parseUaNodeId(referenceDesc.ReferenceTypeId) : UaNodeId.nullNodeId;
            let isForward = (referenceDesc.IsForward) ? true : false;
            let browseName = (referenceDesc.BrowseName) ? referenceDesc.BrowseName : "";
            let displayName = (referenceDesc.DisplayName) ? UaPayloadMapper.localizedTextFromWebApi(referenceDesc.DisplayName) : UaLocalizedText.nullText;       
            let typeDefinitionId = (referenceDesc.TypeDefinition) ? parseUaExpandedNodeId(referenceDesc.TypeDefinition) : undefined;
            
            return {
                    nodeId: nodeId,
                    nodeClass: nodeClass,
                    referenceTypeId: referenceTypeId,
                    isForward: isForward,
                    browseName: browseName,
                    displayName: displayName,
                    typeDefinition: typeDefinitionId };

        } catch (e) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
        }
    }

    static browseResultFromWebApi(browseResult : BrowseResult) : UaBrowseResult
    {
        let results : Array<UaReferenceDescriptor> = [];

        if (browseResult.References)
        {
            for (let item of browseResult.References)
            {
                results.push(UaPayloadMapper.referenceDescriptionFromWebApi(item));
            }
        }

        return {
                results: results, 
                continuationPoint: browseResult.ContinuationPoint };
    }
}