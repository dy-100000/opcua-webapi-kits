import { UaVariant } from "./UaVariant";

export class UaHistoryEventFieldList
{
    private _eventFields : Array<UaVariant>;

    constructor(eventFields : Array<UaVariant>)
    {
        this._eventFields = eventFields;
    }

    get eventFields() : Array<UaVariant>
    {
        return this._eventFields;
    }

    getEventData(fieldNames: Array<string>) : Map<string, UaVariant>
    {
        let eventData : Map<string, UaVariant> = new Map();
        let index = 0;

        for (let item of this._eventFields)
        {
            if (this._eventFields.length <= index) break;
            eventData.set(fieldNames[index], item);
            index++;
        }

        return eventData;
    }
}