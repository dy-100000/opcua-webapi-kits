import { UaEnumValueType, UaLocalizedText } from "opcua-webapi-ts";
import { UaEnumDataType } from "opcua-webservice-node";
import { EmployeeTwinSpace } from "../../EmployeeTwinSpace";

export class SkillLevelEnumType extends UaEnumDataType {
    static readonly JUNIOR = 0;
    static readonly MIDDLE = 1;
    static readonly SENIOR = 2;
    static readonly UNKNOWN = 3;

    private static readonly descriptions = [
        new UaEnumValueType(SkillLevelEnumType.JUNIOR, new UaLocalizedText("Junior"), null),
        new UaEnumValueType(SkillLevelEnumType.MIDDLE, new UaLocalizedText("Middle"), null),
        new UaEnumValueType(SkillLevelEnumType.SENIOR, new UaLocalizedText("Senior"), null),
        new UaEnumValueType(SkillLevelEnumType.UNKNOWN, new UaLocalizedText("Unknown"), null),
    ];

    constructor(twinSpace: EmployeeTwinSpace)
    {
        super(
                "SkillLevelEnumType",
                new UaLocalizedText("SkillLevel"),
                SkillLevelEnumType.descriptions,
                twinSpace);
    }

    override parse(value: unknown): number {
        switch (value) {
            case SkillLevelEnumType.JUNIOR:
                return SkillLevelEnumType.JUNIOR;
            case SkillLevelEnumType.MIDDLE:
                return SkillLevelEnumType.MIDDLE;
            case SkillLevelEnumType.SENIOR:
                return SkillLevelEnumType.SENIOR;
            default:
                return SkillLevelEnumType.UNKNOWN;
        }
    }
}