import { UaStatusCode, UaNodeId, UaLocalizedText, UaExpandedNodeId, UaExtensionObject } from ".";
export declare class UaVariant {
    private _value;
    private _type;
    private _arrayType;
    get value(): any;
    get type(): UaVariantType;
    get arrayType(): UaArrayType;
    isScalar(): boolean;
    isArray(): boolean;
    isNull(): boolean;
    static null(): UaVariant;
    static boolean(value: boolean): UaVariant;
    static integer(value: number, type?: UaVariantType): UaVariant;
    static float(value: number): UaVariant;
    static double(value: number): UaVariant;
    static string(value: string): UaVariant;
    static dateTime(value: Date): UaVariant;
    static guid(value: string): UaVariant;
    static byteString(value: string): UaVariant;
    static nodeId(value: UaNodeId): UaVariant;
    static expandedNodeId(value: UaExpandedNodeId): UaVariant;
    static statusCode(value: UaStatusCode): UaVariant;
    static qualifiedName(value: string): UaVariant;
    static localizedText(value: UaLocalizedText): UaVariant;
    static extensionObject(value: UaExtensionObject): UaVariant;
    static booleans(value: Array<boolean>): UaVariant;
    static integers(value: Array<number>, type?: UaVariantType): UaVariant;
    static floats(value: Array<number>): UaVariant;
    static doubles(value: Array<number>): UaVariant;
    static strings(value: Array<string>): UaVariant;
    static dateTimes(value: Array<Date>): UaVariant;
    static guids(value: Array<string>): UaVariant;
    static byteStrings(value: Array<string>): UaVariant;
    static nodeIds(value: Array<UaNodeId>): UaVariant;
    static expandedNodeIds(value: Array<UaExpandedNodeId>): UaVariant;
    static statusCodes(value: Array<UaStatusCode>): UaVariant;
    static qualifiedNames(value: Array<string>): UaVariant;
    static localizedTexts(value: Array<UaLocalizedText>): UaVariant;
    static extensionObjects(value: Array<UaExtensionObject>): UaVariant;
}
export declare enum UaVariantType {
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
export declare enum UaArrayType {
    Scalar = 0,
    Array = 1,
    Matrix = 2
}
