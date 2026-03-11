import { FilterOperator } from "opcua-webapi";
import { UaContentFilterElement, UaLiteralOperand, UaSimpleAttributeOperand } from "../structure";
import { UaExtensionObject } from "./UaExtensionObject";
import { UaVariant } from "./UaVariant";

export enum UaQueryFilterType {
    Equals = 0,
    IsNull = 1,
    GreaterThan = 2,
    LessThan = 3,
    GreaterThanOrEqual = 4,
    LessThanOrEqual = 5,
    Like = 6,
    Between = 8,
    InList = 9
}

export class UaQueryFilter {
    private _fieldName : string;
    private _operator : UaQueryFilterType;
    private _value : UaVariant;
    private _isNot : boolean;

    constructor(
        fieldName: string, 
        operator : UaQueryFilterType,
        value : UaVariant,
        isNot? : boolean)
    {
        this._fieldName = fieldName;
        this._operator = operator;
        this._value = value;
        this._isNot = (isNot) ? true : false;
    }

    get fieldName()
    {
        FilterOperator
        return this._fieldName;
    }

    get operator()
    {
        return this._operator;
    }

    get value()
    {
        return this._value;
    }

    get isNot()
    {
        return this._isNot;
    }
    
    set isNot(isNot: boolean)
    {
        this._isNot = isNot;
    }

    toContentFilterElement() : UaContentFilterElement
    {
        let simpleOperand = new UaSimpleAttributeOperand([this.fieldName]);
        let literalOperand = new UaLiteralOperand(this.value);

        let operands : Array<UaExtensionObject> = [
            simpleOperand.toExtensionObject(),
            literalOperand.toExtensionObject()
        ];

        let element = new UaContentFilterElement(operands, this.operator);
        return element;
    }

    static fromContentFilterElement(element: UaContentFilterElement) : UaQueryFilter | null
    {
        if (element.operator < UaQueryFilterType.Equals ||
            element.operator > UaQueryFilterType.InList ||
            element.operator == UaQueryFilterType.Like + 1) return null;
        
        let simpleOperand : UaSimpleAttributeOperand = null;
        let literalOperand : UaLiteralOperand = null;

        for (let item of element.operands)
        {
            if (item.typeId.equal(UaSimpleAttributeOperand.dataTypeId))
            {
                simpleOperand = UaSimpleAttributeOperand.fromExtensionObject(item);
            } else if (item.typeId.equal(UaLiteralOperand.dataTypeId)) {
                literalOperand = UaLiteralOperand.fromExtensionObject(item);
            }
        }

        if (null == simpleOperand || 
            0 == simpleOperand.browsePath.length ||
            null == literalOperand) return null;

        let filter = new UaQueryFilter(simpleOperand.browsePath[0], element.operator, literalOperand.value);
        return filter;
    }
}
    