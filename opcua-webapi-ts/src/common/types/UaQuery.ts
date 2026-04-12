import { FilterOperator } from "opcua-webapi";
import { UaContentFilter, UaContentFilterElement, UaElementOperand } from "../structure";
import { UaQueryFilter } from "./UaQueryFilter";

export class UaQuery {
    private _filters : Array<UaQueryFilter>;
    private _orAll: boolean;

    constructor(
        filters: Array<UaQueryFilter>,
        orAll?: boolean)
    {
        this._filters = filters;
        this._orAll = (orAll) ? true : false;
    }

    get filters()
    {
        return this._filters;
    }

    get orAll()
    {
        return this._orAll;
    }

    toContentFilter() : UaContentFilter
    {
        let queryElements : Array<UaContentFilterElement> = [];
        let notElements : Array<UaContentFilterElement> = [];
        let index = 0;

        for (let item of this.filters)
        {
            queryElements.push(item.toContentFilterElement());

            if (item.isNot)
            {
                let notOperand = new UaElementOperand(index);
                let notElement = new UaContentFilterElement([notOperand.toExtensionObject()], FilterOperator.Not);
                notElements.push(notElement);
            }

            index++;
        }

        let elements : Array<UaContentFilterElement> = [];
        elements.push(...queryElements);
        elements.push(...notElements);

        if (this.orAll)
        {
            let orElement = new UaContentFilterElement([], FilterOperator.Or);
            elements.push(orElement);
        }

        let filter = new UaContentFilter(elements);
        return filter;
    }

    static fromContentFilter(filter: UaContentFilter) : UaQuery | null
    {
        let queryElements : Map<number, UaQueryFilter> = new Map;
        let notElements : Array<number> = [];
        let orAll = false;

        let index = 0;
        for (let item of filter.elements)
        {
            if (item.operator == FilterOperator.Not)
            {
                if (item.operands.length != 1 ||
                    !item.operands[0].typeId.equal(UaElementOperand.dataTypeId)) return null;
                
                let operand = UaElementOperand.fromExtensionObject(item.operands[0]);
                if (null == operand) return null;

                notElements.push(operand.index);
            } else if (item.operator >= FilterOperator.Equals && item.operator <= FilterOperator.InList) {
                let query = UaQueryFilter.fromContentFilterElement(item);
                if (null == query) return null;
                queryElements.set(index, query);
            } else if (item.operator == FilterOperator.Or) {
                if (item.operands.length != 0) return null;
                orAll = true;
            } else { 
                return null; 
            }

            index++;
        }

        for (let item of notElements)
        {
            let query = queryElements.get(item);
            if (!query) return null;
            query.isNot = true;
        }

        let ret = new UaQuery(Array.from(queryElements.values()), orAll);
        return ret;
    }
}