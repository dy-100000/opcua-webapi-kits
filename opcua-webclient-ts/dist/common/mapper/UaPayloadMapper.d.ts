import { BrowseResult, ReferenceDescription } from "opcua-webapi";
import { LocalizedText, Variant, StatusCode, DataValue } from "opcua-webapi";
import { UaBrowseResult, UaReferenceDescriptor } from "../types";
import { UaStatusCode, UaLocalizedText, UaExtensionObject, UaVariant, UaDataValue } from "../types";
export declare class UaPayloadMapper {
    static statusCodeFromWebApi(statusCode?: StatusCode | null): UaStatusCode;
    static statusCodeToWebApi(statusCode: UaStatusCode): StatusCode;
    static localizedTextFromWebApi(text?: LocalizedText | null): UaLocalizedText | null;
    static localizedTextToWebApi(text: UaLocalizedText): LocalizedText;
    static extensionObjectFromWebApi(extentionObject: any): UaExtensionObject;
    static extensionObjectToWebApi(extentionObject: UaExtensionObject): any;
    static variantFromWebApi(variant: Variant): UaVariant;
    static variantToWebApi(variant: UaVariant): Variant;
    static dataValueFromWebApi(dataValue: DataValue): UaDataValue;
    static referenceDescriptionFromWebApi(referenceDesc: ReferenceDescription): UaReferenceDescriptor;
    static browseResultFromWebApi(browseResult: BrowseResult): UaBrowseResult;
}
