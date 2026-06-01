import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaError, UaQuery, UaReadEventDetails } from "opcua-webapi-ts";
import { ObjectServiceContext } from "../../../types/digitaltwin/ObjectServiceContext";

export class ReadEventsRequest {
    private readonly _id: string;
    private readonly _startTime: Date;
    private readonly _endTime: Date;
    private readonly _select: Array<string>;
    private readonly _where: UaQuery | null;
    private readonly _ascendingOrder: boolean;
    private readonly _limit: number;
    private readonly _offset: number;
    private readonly _context: ObjectServiceContext;

    constructor(
        context: ObjectServiceContext,
        startTime: Date,
        endTime: Date,
        select: Array<string>,
        where: UaQuery | null,
        ascendingOrder: boolean,
        limit: number,
        offset: number,
    ) {
        this._id = context.objectId.id;
        this._context = context;
        this._startTime = startTime;
        this._endTime = endTime;
        this._select = select;
        this._where = where;
        this._ascendingOrder = ascendingOrder;
        this._limit = limit;
        this._offset = offset;
    }

    get id(): string {
        return this._id;
    }

    get startTime(): Date {
        return this._startTime;
    }

    get endTime(): Date {
        return this._endTime;
    }

    get select(): Array<string> {
        return this._select;
    }

    get where(): UaQuery | null {
        return this._where;
    }

    get ascendingOrder(): boolean {
        return this._ascendingOrder;
    }

    get limit(): number {
        return this._limit;
    }

    get offset(): number {
        return this._offset;
    }

    get context(): ObjectServiceContext {
        return this._context;
    }

    static getRequest(
        context: ObjectServiceContext,
        details: UaReadEventDetails,
        offset: number,
    ): ReadEventsRequest {
        let startTime = details.startTime;
        let endTime = details.endTime;
        let ascendingOrder = true;

        if (endTime.getTime() < startTime.getTime()) {
            ascendingOrder = false;
            const tmp = endTime;
            endTime = startTime;
            startTime = tmp;
        }

        const eventFilter = details.filter;
        const selectClauses = eventFilter.selectClauses;
        if (selectClauses.length === 0) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadContentFilterInvalid));
        }

        let selects : Array<string> = [];
        for (const item of selectClauses) {
            const fieldName = ReadEventsRequest.getSelectField(item.browsePath);
            selects.push(fieldName);
        }
        
        const where = eventFilter.whereClause ? UaQuery.fromContentFilter(eventFilter.whereClause) : null;

        return new ReadEventsRequest(
            context,
            startTime,
            endTime,
            selects,
            where,
            ascendingOrder,
            details.numValuesPerNode,
            offset,
        );
    }

    private static getSelectField(browsePath: Array<string>): string {
        if (browsePath.length === 0 || browsePath.length > 2) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadContentFilterInvalid));
        }

        let fieldName = browsePath[0];
        if (browsePath.length === 2) {
            let subFieldName = browsePath[1];
            if (null == subFieldName || subFieldName.length == 0) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadContentFilterInvalid));
            }
            fieldName = `${fieldName}/${subFieldName}`;
        }  

        return fieldName;
    }
}