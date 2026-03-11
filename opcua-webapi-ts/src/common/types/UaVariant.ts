import { StatusCodes } from "opcua-webapi";
import { UaStatusCode, UaNodeId, UaLocalizedText, UaExpandedNodeId, UaExtensionObject, UaError, makeUaStatusCode } from ".";

export class UaVariant {
    private _value : any = null;
    private _type : UaVariantType = UaVariantType.Null;
    private _arrayType : UaArrayType = UaArrayType.Scalar;

    get value() : any
    {
        return this._value;
    }

    get type() : UaVariantType
    {
        return this._type;
    }

    get arrayType() : UaArrayType
    {
        return this._arrayType;
    }

    public isScalar()
    {
        return (UaArrayType.Scalar == this._arrayType) ? true : false;
    }

    public isArray()
    {
        return (UaArrayType.Array == this._arrayType) ? true : false;
    }

    public isNull() : boolean
    {
        return (UaVariantType.Null == this._type) ? true : false;
    } 

    public toBoolean() : boolean | null
    {
        if (this._type == UaVariantType.Boolean &&
            this._arrayType == UaArrayType.Scalar) return this._value;

        return null;
    }

    public toNumber() : number | null
    {        
        if (this._type >= UaVariantType.SByte &&
            this._type <= UaVariantType.Double && 
            this._arrayType == UaArrayType.Scalar) return this._value;

        return null;
    }

    public toString() : string | null
    {
        if (this._arrayType != UaArrayType.Scalar) return null;       

        if (this._type == UaVariantType.String ||
            this._type == UaVariantType.ByteString || 
            this._type == UaVariantType.QualifiedName ||
            this._type == UaVariantType.Guid) return this._value;

        return null;
    }

    public toDateTime() : Date | null
    {
        if (this._type == UaVariantType.DateTime &&            
            this._arrayType == UaArrayType.Scalar) return this._value;

        return null;
    }

    public toNodeId() : UaNodeId | null
    {
        if (this._type == UaVariantType.NodeId &&            
            this._arrayType == UaArrayType.Scalar) return this._value;

        return null;
    }

    public toExpandedNodeId() : UaExpandedNodeId | null
    {
        if (this._type == UaVariantType.ExpandedNodeId &&            
            this._arrayType == UaArrayType.Scalar) return this._value;

        return null;
    }

    public toStatusCode() : UaStatusCode | null
    {
        if (this._type == UaVariantType.StatusCode &&            
            this._arrayType == UaArrayType.Scalar) return this._value;

        return null;
    }

    public toLocalizedText() : UaLocalizedText | null
    {
        if (this._type == UaVariantType.LocalizedText &&            
            this._arrayType == UaArrayType.Scalar) return this._value;

        return null;
    }

    public toExtensionObject() : UaExtensionObject | null
    {
        if (this._type == UaVariantType.ExtensionObject &&            
            this._arrayType == UaArrayType.Scalar) return this._value;

        return null;
    }

    public toBooleans() : Array<boolean> | null
    {
        if (this._type == UaVariantType.Boolean &&
            (this._arrayType == UaArrayType.Array || this._arrayType == UaArrayType.Matrix)) return this._value;

        return null;
    }

    public toNumbers() : Array<number> | null
    {        
        if (this._type >= UaVariantType.SByte &&
            this._type <= UaVariantType.Double && 
            (this._arrayType == UaArrayType.Array || this._arrayType == UaArrayType.Matrix)) return this._value;

        return null;
    }

    public toStrings() : Array<string> | null
    {
        if (this._arrayType != UaArrayType.Array && this._arrayType != UaArrayType.Matrix) return null;       

        if (this._type == UaVariantType.String ||
            this._type == UaVariantType.ByteString || 
            this._type == UaVariantType.QualifiedName ||
            this._type == UaVariantType.Guid) return this._value;

        return null;
    }

    public toDateTimes() : Array<Date> | null
    {
        if (this._type == UaVariantType.DateTime &&            
            (this._arrayType == UaArrayType.Array || this._arrayType == UaArrayType.Matrix)) return this._value;

        return null;
    }

    public toNodeIds() : Array<UaNodeId> | null
    {
        if (this._type == UaVariantType.NodeId &&            
            (this._arrayType == UaArrayType.Array || this._arrayType == UaArrayType.Matrix)) return this._value;

        return null;
    }

    public toExpandedNodeIds() : Array<UaExpandedNodeId> | null
    {
        if (this._type == UaVariantType.ExpandedNodeId &&            
            (this._arrayType == UaArrayType.Array || this._arrayType == UaArrayType.Matrix)) return this._value;

        return null;
    }

    public toStatusCodes() : Array<UaStatusCode> | null
    {
        if (this._type == UaVariantType.StatusCode &&            
            (this._arrayType == UaArrayType.Array || this._arrayType == UaArrayType.Matrix)) return this._value;

        return null;
    }

    public toLocalizedTexts() : Array<UaLocalizedText> | null
    {
        if (this._type == UaVariantType.LocalizedText &&            
            (this._arrayType == UaArrayType.Array || this._arrayType == UaArrayType.Matrix)) return this._value;

        return null;
    }

    public toExtensionObjects() : Array<UaExtensionObject> | null
    {
        if (this._type == UaVariantType.ExtensionObject &&            
            (this._arrayType == UaArrayType.Array || this._arrayType == UaArrayType.Matrix)) return this._value;

        return null;
    }

    public static null() : UaVariant
    {
        return new UaVariant();
    }   

    public static boolean(value : boolean) : UaVariant
    {
        let val = new UaVariant();

        val._type = UaVariantType.Boolean;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;

        return val;
    }

    public static integer(value : number, type?: UaVariantType) : UaVariant
    {
        if (type && UaVariantType.SByte > type && UaVariantType.UInt64 < type) 
            throw new UaError(makeUaStatusCode(StatusCodes.BadTypeMismatch));
        
        let val = new UaVariant();
        val._type = (type) ? type : UaVariantType.Int32;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;

        return val;
    } 

    public static float(value : number) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.Float;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }

    public static double(value : number) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.Double;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }

    public static string(value: string) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.String;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }
    
    public static dateTime(value: Date) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.DateTime;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }

    public static guid(value: string) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.Guid;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }

    public static byteString(value: string) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.ByteString;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }

    public static nodeId(value: UaNodeId) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.NodeId;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }

    public static expandedNodeId(value: UaExpandedNodeId) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.ExpandedNodeId;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }

    public static statusCode(value: UaStatusCode) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.StatusCode;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }

    public static qualifiedName(value: string) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.QualifiedName;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }

    public static localizedText(value: UaLocalizedText) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.LocalizedText;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }

    public static extensionObject(value : UaExtensionObject) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.ExtensionObject;
        val._value = value;
        val._arrayType = UaArrayType.Scalar;
        return val;
    }

    public static booleans(value : Array<boolean>) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.Boolean;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }

    public static integers(value : Array<number>, type?: UaVariantType) : UaVariant
    {
        if (type && UaVariantType.SByte > type && UaVariantType.UInt64 < type) 
                    throw new UaError(makeUaStatusCode(StatusCodes.BadTypeMismatch));        
        
        let val = new UaVariant();
        val._type = (type) ? type : UaVariantType.Int32;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    } 

    public static floats(value : Array<number>) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.Float;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }

    public static doubles(value : Array<number>) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.Double;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }

    public static strings(value: Array<string>) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.String;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
    
    public static dateTimes(value: Array<Date>) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.DateTime;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }

    public static guids(value: Array<string>) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.Guid;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }

    public static byteStrings(value: Array<string>) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.ByteString;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }

    public static nodeIds(value: Array<UaNodeId>) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.NodeId;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }

    public static expandedNodeIds(value: Array<UaExpandedNodeId>) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.ExpandedNodeId;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }

    public static statusCodes(value: Array<UaStatusCode>) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.StatusCode;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }

    public static qualifiedNames(value: Array<string>) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.QualifiedName;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }

    public static localizedTexts(value: Array<UaLocalizedText>) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.LocalizedText;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }

    public static extensionObjects(value : Array<UaExtensionObject>) : UaVariant
    {
        let val = new UaVariant();
        val._type = UaVariantType.ExtensionObject;
        val._value = value;
        val._arrayType = UaArrayType.Array;
        return val;
    }
}

export enum UaVariantType {
    Null = 0,
    Boolean = 1,
    SByte = 2,
    Byte = 3,
    Int16 = 4,
    UInt16 = 5,
    Int32 = 6,
    UInt32 = 7,
    Int64 = 8,
    UInt64 = 9,
    Float = 10,
    Double = 11,
    String = 12,
    DateTime = 13,
    Guid = 14,
    ByteString = 15,
    XmlElement = 16,
    NodeId = 17,
    ExpandedNodeId = 18,
    StatusCode = 19,
    QualifiedName = 20,
    LocalizedText = 21,
    ExtensionObject = 22
}

export enum UaArrayType {
    Scalar = 0,
    Array = 1,
    Matrix = 2
}