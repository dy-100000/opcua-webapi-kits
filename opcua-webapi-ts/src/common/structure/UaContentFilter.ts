import { ContentFilter, ContentFilterElement, ContentFilterFromJSON, ContentFilterToJSON, ContentFilterToJSONTyped } from "opcua-webapi";
import { UaExtensionObject, UaNodeId } from "../types";
import { DataTypeIds } from "../nodes";
import { UaContentFilterElement } from "./UaContentFilterElement";

export class UaContentFilter
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.ContentFilter);
    
    private _elements: Array<UaContentFilterElement>;
    
    constructor(elements: Array<UaContentFilterElement>)
    {        
        this._elements = elements;    
    }

    get elements() : Array<UaContentFilterElement>
    {
        return this._elements;
    }
    
    toStruct() : ContentFilter
    {
        let elements: Array<ContentFilterElement> = [];

        for (let item of this._elements)
        {
            elements.push(item.toStruct());
        }

        let contentFilter : ContentFilter = {
            Elements: elements
        };

        return contentFilter;
    }

    static fromStruct(filter : ContentFilter) : UaContentFilter | null
    {
        if (null == filter.Elements) return null;

        let elements: Array<UaContentFilterElement> = [];

        for (let item of filter.Elements)
        {
            let element = UaContentFilterElement.fromStruct(item);
            if (null == element) return null;
            elements.push(element);
        }

        return new UaContentFilter(elements); 
    }

    toExtensionObject() : UaExtensionObject
    {       
        return new UaExtensionObject(UaContentFilter.dataTypeId, ContentFilterToJSONTyped(this.toStruct()));
    }    

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaContentFilter | null
    {
        if (!UaContentFilter.dataTypeId.equal(extensionObject.typeId)) return null;
        let filter : ContentFilter = ContentFilterFromJSON(extensionObject.body);
        return UaContentFilter.fromStruct(filter);            
    }
}