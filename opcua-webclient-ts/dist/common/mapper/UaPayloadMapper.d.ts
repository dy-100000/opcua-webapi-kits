import { BrowseResult, ReferenceDescription } from "opcua-webapi";
import { LocalizedText as LocalizedTextNodeWebApi, Variant as VariantWebApi, DataValue as DataValueWebApi } from "opcua-webapi";
import { UaBrowseResult, UaReferenceDescriptor } from "../types";
import { Variant } from "node-opcua-variant";
import { LocalizedText } from "node-opcua-data-model";
import { DataValue } from "node-opcua-data-value";
import { UaExtensionObject } from "../types/UaExtensionObject";
export declare class UaPayloadMapper {
    static localizedTextFromWebApi(text: LocalizedTextNodeWebApi): LocalizedText;
    static extensionObjectFromWebApi(extentionObject: any): UaExtensionObject;
    static variantFromWebApi(variant: VariantWebApi): Variant;
    static dataValueFromWebApi(dataValue: DataValueWebApi): DataValue;
    static referenceDescriptionFromWebApi(referenceDesc: ReferenceDescription): UaReferenceDescriptor;
    static browseResultFromWebApi(browseResult: BrowseResult): UaBrowseResult;
}
