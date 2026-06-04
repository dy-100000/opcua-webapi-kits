import { UaEnumValueType, UaLocalizedText } from "opcua-webapi-ts";
import { UaEnumDataType } from "opcua-webservice-node";

import { EmployeeTwinSpace } from "../../EmployeeTwinSpace";

export class SexEnumType extends UaEnumDataType {
    static readonly MALE = 0;
    static readonly FEMALE = 1;
    static readonly UNKNOWN = 2;

    private static readonly descriptions = [
        new UaEnumValueType(SexEnumType.MALE, new UaLocalizedText("Male"), null),
        new UaEnumValueType(SexEnumType.FEMALE, new UaLocalizedText("Female"), null),
        new UaEnumValueType(SexEnumType.UNKNOWN, new UaLocalizedText("Unknown"), null),
    ];

    constructor(twinSpace: EmployeeTwinSpace) {
        super("Sex", new UaLocalizedText("Sex"), SexEnumType.descriptions, twinSpace);
    }

    override parse(value: unknown): number {
        switch (value) {
            case SexEnumType.MALE:
                return SexEnumType.MALE;
            case SexEnumType.FEMALE:
                return SexEnumType.FEMALE;
            default:
                return SexEnumType.UNKNOWN;
        }
    }
}