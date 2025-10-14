import { StatusCode } from "node-opcua-status-code";
import { ExpandedNodeId } from "node-opcua-nodeid";
import { UaGuid } from "./UaGuid";
import { UaNodeId } from "./UaNodeId";
export declare class UaVariant {
    private _value;
    private _type;
    private _arrayType;
    constructor();
    value(): any;
    type(): UaVariantType;
    arrayType(): UaVariantArrayType;
    isNull(): boolean;
    setNull(): void;
    setBoolean(value: boolean): void;
    setInt(value: number, type?: UaVariantType): void;
    setFloat(value: number): void;
    setDouble(value: number): void;
    setString(value: string): void;
    setDateTime(value: Date): void;
    setGuid(value: UaGuid): void;
    setByteString(value: string): void;
    setNodeId(value: UaNodeId): void;
    setExpandedNodeId(value: ExpandedNodeId): void;
    setStatusCode(value: StatusCode): void;
    setQualifiedName(value: string): void;
    setLocalizedText(text: string, locale?: string): void;
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
export declare enum UaVariantArrayType {
    Scalar = 0,
    Array = 1,
    Matrix = 2
}
