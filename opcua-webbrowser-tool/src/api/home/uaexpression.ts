import { UaValueType } from "./modellingapitypes";

// BoolEval
export type UaExpressionField = {
    browsePath : string[];
    name       : string;
    path       : string[];
    dataTypeId : string;
    dataType   : UaValueType;
}

export const UaExpressionOperator = { 
    Equals             : 0,
    IsNotGood          : 1,
    GreaterThan        : 2,  
    LessThan           : 3,      
    GreaterThanOrEqual : 4,
    LessThanOrEqual    : 5,
    Like               : 6,    
    Between            : 8,
    InList             : 9,
    NotEquals          : 100,
    IsGood             : 101,
    NotLike            : 106,
    NotBetween         : 108,
    NotInList          : 109
}

export type UaExpressionElement = {
    field    : UaExpressionField;
    operator : number;
    value    : any;
}

export class UaExpression
{
    expressions : UaExpressionElement[];
    // When requireAll = false, OR operation
    // When requireAll = true, And operation
    // When reuqireAll = true && expresions is empty, Transition to next state 
    requireAll  : boolean;   

    constructor()
    {
        this.expressions = [];
        this.requireAll = false;
    }

    public getDefaultValue(dataType : UaValueType, operator : number)
    {
        if (UaExpressionOperator.IsNotGood == operator || UaExpressionOperator.IsGood == operator) return null;

        if (UaValueType.Boolean == dataType)
        {
            if (UaExpressionOperator.Equals == operator ||
                UaExpressionOperator.NotEquals == operator)
            {
                return false;
            } else {
                return undefined;
            }
        } else if (UaValueType.Double == dataType ||
            UaValueType.UInteger == dataType ||
            UaValueType.Integer == dataType ||
            UaValueType.Enumeration == dataType ||
            UaValueType.Time == dataType) {

            if (UaExpressionOperator.Equals == operator ||
                UaExpressionOperator.NotEquals == operator ||
                UaExpressionOperator.GreaterThan == operator ||
                UaExpressionOperator.LessThan == operator ||  
                UaExpressionOperator.GreaterThanOrEqual == operator ||
                UaExpressionOperator.LessThanOrEqual == operator)
            {
                return 0;
            } else if (UaExpressionOperator.Between == operator ||
                UaExpressionOperator.NotBetween == operator)
            {
                return [0, 10];
            } else if (UaExpressionOperator.InList == operator &&
                UaExpressionOperator.NotInList == operator)
            {
                return [0];
            } else {
                return undefined;
            }
                
        } else if (UaValueType.String == dataType) {
            if (UaExpressionOperator.Equals == operator ||
                UaExpressionOperator.NotEquals == operator ||
                UaExpressionOperator.Like == operator ||
                UaExpressionOperator.NotLike == operator)
            {
                return "";
            } else if (UaExpressionOperator.InList == operator &&
                UaExpressionOperator.NotInList == operator)
            {
                return [""];
            } else {
                return undefined;
            }
        }

        return undefined;
    }

    public checkValue(dataType : UaValueType, operator : number, value : any) : boolean
    {        
        if (UaExpressionOperator.IsNotGood == operator || UaExpressionOperator.IsGood == operator) 
        {
            if (null == value)
            {
                return true;
            } else {
                return false;
            }            
        }            

        if (Array.isArray(value))
        {
            if (UaExpressionOperator.InList != operator &&
                UaExpressionOperator.Between != operator &&
                UaExpressionOperator.NotInList != operator &&
                UaExpressionOperator.NotBetween != operator) return false;

            for (let item of value)
            {
                if (!this.checkValue(dataType, operator, item)) return false;
            }

            if (UaExpressionOperator.Between == operator ||
                UaExpressionOperator.NotBetween == operator)
            {
                if (value.length != 2) return false;
            }            
        } else {
            if (UaExpressionOperator.InList == operator ||
                UaExpressionOperator.Between == operator ||
                UaExpressionOperator.NotInList == operator ||
                UaExpressionOperator.NotBetween == operator) return false;
                
            if (UaValueType.Boolean == dataType)
            {
                if (typeof(value) != 'boolean') return false; 
            } else if (UaValueType.UInteger == dataType ||
                       UaValueType.Integer == dataType ||
                       UaValueType.Enumeration == dataType ||
                       UaValueType.Time == dataType) {
                if (!Number.isInteger(value)) return false;
                if (UaValueType.Integer != dataType && value < 0) return false;
            } else if (UaValueType.Double == dataType) {
                if (typeof(value) != 'number') return false; 
            } else if (UaValueType.String == dataType) {
                if (typeof(value) != 'string') return false; 
            } else {
                return false;
            }
        }

        return true;
    }

    public toContentFilter() : any | undefined
    {        
      console.log(this.expressions,'llllll')

        // if (this.expressions.length == 0) return undefined;

        for (let item of this.expressions)
        {    
            if (!this.checkValue(item.field.dataType, item.operator, item.value)) {
              console.log(1234)
              return undefined
            };
        }
        
        let elements : any[] = []; 

        for (let item of this.expressions)
        {
            let elementsOfExpression = this.__expressionToContentFilterElement(item, elements.length);
            elements.push(...elementsOfExpression);
        }

        let element = this.__requireAllToContentFilterElement();
        if (element) elements.push(element);

        return { contentFilter : elements };
    }

    public fromContentFilter(contentFilter : any)
    {
        this.expressions = [];
        this.requireAll = false;
        console.log(this.expressions ,contentFilter)
        let expressions : UaExpressionElement[] = [];
        
        if (typeof(contentFilter) != 'object') return;

        let expressionsInFilter = contentFilter.contentFilter;

        if (!Array.isArray(expressionsInFilter)) return;
        
        for (let item of expressionsInFilter)
        { 
            let expression = this.__getExpression(item);
            if (expression)
            {
                expressions.push(expression);
                continue;
            }

            let requireAll = this.__getRequireAll(item);
            if (undefined != requireAll) this.requireAll = requireAll;            
        }
        this.expressions = expressions;
    }

    private __toSimpleAttributeOperand(browsePath : string[]) : any
    {
        return { type: 2, attribute: {browsePath: browsePath} };
    }

    private __toLiteralOperand(dataType : UaValueType, value : any) : any
    {
        let dt = 0;

        if (null != value)
        {
            if (UaValueType.Double == dataType) 
            {
                dt = 11;
            } else if (UaValueType.Boolean == dataType) {
                dt = 1;
            } else if (UaValueType.UInteger == dataType) {
                dt = 7;
            } else if (UaValueType.Integer == dataType) {
                dt = 6;
            } else if (UaValueType.Enumeration == dataType) {
                dt = 6;
            } else if (UaValueType.Time == dataType) {
                dt = 13;
            } else if (UaValueType.String == dataType) {
                dt = 12;
            } 
        }
        
        let variant = { dt: dt, v: value };
        return { type: 1, value: variant };
    }

    private __expressionToContentFilterElement(expression: UaExpressionElement, index : number) : any[]
    {
        let ret : any[] = [];        

        let operands : any[] = [];
        operands.push(this.__toSimpleAttributeOperand(expression.field.browsePath));
        operands.push(this.__toLiteralOperand(expression.field.dataType, expression.value));

        let expressionElement = { 
            elementIndex: index, 
            operator: expression.operator, 
            operands: operands,
            name : expression.field.name,
            path : expression.field.path,
            dataTypeId : expression.field.dataTypeId,
            dataType   : expression.field.dataType
        };

        ret.push(expressionElement);        

        return ret;
    }

    private __requireAllToContentFilterElement() : any
    {
        // Check if only one element
        if (this.expressions.length == 1) return undefined;

        // Check if is not transition to next
        if (this.expressions.length == 0 && !this.requireAll) return undefined;

        return {
            elementIndex: 0, 
            operator: (this.requireAll) ? 10 : 11, // (And/Or) 
            operands: []
        }
    }

    private __getExpression(contentFilter : any) : UaExpressionElement | undefined
    {
        let operator = contentFilter.operator;
        
        if (UaExpressionOperator.Equals != operator &&
            UaExpressionOperator.NotEquals != operator &&
            UaExpressionOperator.GreaterThan != operator &&
            UaExpressionOperator.LessThan != operator &&
            UaExpressionOperator.GreaterThanOrEqual != operator &&
            UaExpressionOperator.LessThanOrEqual != operator &&
            UaExpressionOperator.Like != operator && 
            UaExpressionOperator.NotLike != operator && 
            UaExpressionOperator.Between != operator &&
            UaExpressionOperator.NotBetween != operator &&
            UaExpressionOperator.InList != operator &&
            UaExpressionOperator.NotInList != operator) return undefined;

        if (typeof(contentFilter.name) != 'string' ||
            !Array.isArray(contentFilter.path) ||
            typeof(contentFilter.dataType) != 'string' ||
            typeof(contentFilter.dataTypeId) != 'string') return undefined;

        if (!Array.isArray(contentFilter.operands)) return undefined;
        if (2 != contentFilter.operands.length) return undefined;      

        let simpleAttributeOperand = contentFilter.operands[0];
        let literalOperand = contentFilter.operands[1];

        if (simpleAttributeOperand.type != 2 ||
            literalOperand.type != 1) return undefined;       

        if (undefined == simpleAttributeOperand.attribute ||
            !Array.isArray(simpleAttributeOperand.attribute.browsePath)) return undefined;
  
        if (typeof(literalOperand.value) != 'object' ||
            undefined == literalOperand.value.v) return undefined;

        let field : UaExpressionField = {
            browsePath: simpleAttributeOperand.attribute.browsePath,
            name: contentFilter.name,
            path: contentFilter.path,
            dataType: contentFilter.dataType,
            dataTypeId: contentFilter.dataTypeId
        };

        let expressionElement : UaExpressionElement = {
            field: field,
            operator: operator,
            value: literalOperand.value.v
        };
        return expressionElement;
    }

    private __getRequireAll(contentFilter : any) : boolean | undefined
    {
        if (10 == contentFilter.operator) return true;
        if (11 == contentFilter.operator) return false;
        return undefined;
    }
}

