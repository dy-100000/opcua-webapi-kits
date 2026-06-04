import { UaEnumValueType, UaLocalizedText } from "opcua-webapi-ts";
import { UaEnumDataType } from "opcua-webservice-node";
import { EmployeeTwinSpace } from "../../EmployeeTwinSpace";

export class SkillCategoryEnumType extends UaEnumDataType {
    static readonly BACKEND_DEVELOPER = 0;
    static readonly UI_DEVELOPER = 1;
    static readonly TESTER = 2;
    static readonly UNKNOWN = 3;

    private static readonly descriptions = [
        new UaEnumValueType(SkillCategoryEnumType.BACKEND_DEVELOPER, new UaLocalizedText("Backend Developer"), null),
        new UaEnumValueType(SkillCategoryEnumType.UI_DEVELOPER, new UaLocalizedText("UI Developer"), null),
        new UaEnumValueType(SkillCategoryEnumType.TESTER, new UaLocalizedText("Tester"), null),
        new UaEnumValueType(SkillCategoryEnumType.UNKNOWN, new UaLocalizedText("Unknown"), null),
    ];

    constructor(twinSpace: EmployeeTwinSpace)
    {
        super(
                "SkillCategoryEnumType",
                new UaLocalizedText("SkillCategory"),
                SkillCategoryEnumType.descriptions,
                twinSpace);
    }

    override parse(value: unknown): number {
        switch (value) {
            case "BE_DEV":
                return SkillCategoryEnumType.BACKEND_DEVELOPER;
            case "UI_DEV":
                return SkillCategoryEnumType.UI_DEVELOPER;
            case "TST":
                return SkillCategoryEnumType.TESTER;
            default:
                return SkillCategoryEnumType.UNKNOWN;
        }
    }
}