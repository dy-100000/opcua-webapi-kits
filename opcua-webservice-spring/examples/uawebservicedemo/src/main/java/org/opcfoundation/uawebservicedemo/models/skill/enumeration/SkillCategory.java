package org.opcfoundation.uawebservicedemo.models.skill.enumeration;

import java.util.ArrayList;
import java.util.List;

public enum SkillCategory {
    BACKEND_DEVELOPER(0,"Backend developer","BE_DEV"),  // Backend developer
    UI_DEVELOPER(1,"UI developer","UI_DEV"), // UI developer
    TESTER(2,"Tester","TST"), // Tester
    UNKNOWN(3,"Unknown","");

    private final int code;
    private final String description;
    private final String code2;

    SkillCategory(int code, String description, String code2) {
        this.code = code;
        this.description = description;
        this.code2 = code2;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static List<String> getDescriptions()
    {
        List<String> descriptions = new ArrayList<>();

        for (SkillCategory item : SkillCategory.values()) {
            descriptions.add(item.description);
        }

        return descriptions;
    }

    public static SkillCategory fromString(String strValue) {
        for (SkillCategory item : SkillCategory.values()) {
            if (item.code2.equalsIgnoreCase(strValue)) {
                return item;
            }
        }

        return UNKNOWN;
    }
}
