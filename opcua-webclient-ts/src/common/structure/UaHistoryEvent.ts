import { DataValue,HistoryData, HistoryDataFromJSON, HistoryEvent, HistoryEventFieldList, HistoryEventFromJSON, HistoryEventToJSONTyped, Variant } from "opcua-webapi";
import { UaDataValue, UaExtensionObject, UaHistoryEventFieldList, UaNodeId, UaVariant } from "../types";
import { UaPayloadMapper } from "../mapper";
import { DataTypeIds } from "../nodes";

export class UaHistoryEvent
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.HistoryEvent);

    private _events : Array<UaHistoryEventFieldList>

    constructor(events : Array<UaHistoryEventFieldList>)
    {        
        this._events = events;
    }

    get events() : Array<UaHistoryEventFieldList>
    {
        return this._events;
    }

    toStruct() : HistoryEvent
    {
        let events: Array<HistoryEventFieldList> = [];

        for (let item of this._events)
        {
            let eventFields: Array<Variant> = [];

            for (let item2 of item.eventFields)
            {
                eventFields.push(UaPayloadMapper.variantToWebApi(item2));
            }

            let fieldList : HistoryEventFieldList = { EventFields: eventFields };
            events.push(fieldList);
        }

        let historyEvent : HistoryEvent = { Events: events };    
        return historyEvent;
    }
    
    static fromStruct(historyEvent : HistoryEvent) : UaHistoryEvent | null
    {
        try
        {
            let events : Array<UaHistoryEventFieldList> = [];

            if (historyEvent.Events)
            {
                for (let item of historyEvent.Events)
                {
                    if (undefined == item.EventFields) continue;

                    let eventFields : Array<UaVariant> = [];
                    for (let item2 of item.EventFields)
                    {
                        eventFields.push(UaPayloadMapper.variantFromWebApi(item2));
                    }

                    events.push(new UaHistoryEventFieldList(eventFields));
                }    
            }  

            return new UaHistoryEvent(events);
        } catch(e) { return null; }
    }

    toExtensionObject() : UaExtensionObject
    {        
        return new UaExtensionObject(UaHistoryEvent.dataTypeId, HistoryEventToJSONTyped(this.toStruct()));
    } 

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaHistoryEvent | null
    {
        if (!UaHistoryEvent.dataTypeId.equal(extensionObject.typeId)) return null;      
        let historyEvent : HistoryEvent = HistoryEventFromJSON(extensionObject.body);
        return UaHistoryEvent.fromStruct(historyEvent);
    }
}