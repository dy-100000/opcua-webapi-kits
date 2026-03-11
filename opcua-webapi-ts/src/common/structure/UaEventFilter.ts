import { EventFilter,  SimpleAttributeOperand } from "opcua-webapi";
import { UaNodeId } from "../types";
import { DataTypeIds } from "../nodes";
import { UaSimpleAttributeOperand } from "./UaSimpleAttributeOperand";
import { UaContentFilter } from "./UaContentFilter";

export class UaEventFilter
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.EventFilter);
    
    private _selectClauses: Array<UaSimpleAttributeOperand>;
    private _whereClause: UaContentFilter | null;
    
    constructor(
        selectClauses: Array<UaSimpleAttributeOperand>,
        whereClause?: UaContentFilter | null)
    {        
        this._selectClauses = selectClauses; 
        this._whereClause = (whereClause) ? whereClause : null;  
    }

    get selectClauses() : Array<UaSimpleAttributeOperand>
    {
        return this._selectClauses;
    }

    get whereClause() : UaContentFilter | null
    {
        return this._whereClause;
    }

    toStruct() : EventFilter
    {
        let selectClauses: Array<SimpleAttributeOperand> = [];

        for (let item of this._selectClauses)
        {
            selectClauses.push(item.toStruct());
        }

        let contentFilter = (this._whereClause) ? this._whereClause.toStruct() : undefined;

        let eventFilter : EventFilter = { SelectClauses: selectClauses, WhereClause: contentFilter };
        return eventFilter;
    }

    static fromStruct(filter : EventFilter) : UaEventFilter | null
    {
        if (undefined == filter.SelectClauses) return null;

        let selectClauses: Array<UaSimpleAttributeOperand> = [];
        for (let item of filter.SelectClauses)
        {
            let select = UaSimpleAttributeOperand.fromStruct(item);
            if (null == select) return null;
            selectClauses.push(select);
        }

        let where : UaContentFilter = null;

        if (undefined != filter.WhereClause)
        {
            where = UaContentFilter.fromStruct(filter.WhereClause);
            if (null == where) return null;
        }

        let eventFilter = new UaEventFilter(selectClauses, where);
        return eventFilter;
    }
}