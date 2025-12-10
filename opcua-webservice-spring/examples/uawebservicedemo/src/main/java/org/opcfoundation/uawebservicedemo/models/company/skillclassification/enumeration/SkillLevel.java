package org.opcfoundation.uawebservicedemo.models.company.skillclassification.enumeration;

import java.util.ArrayList;
import java.util.List;

public enum SkillLevel {
    JUNIOR(0,"Junior"),
    MIDDLE(1,"Middle"),
    SENIOR(2,"Senior"),
    UNKNOWN(3,"Unknown");

    private final int code;
    private final String description;

    SkillLevel(int code, String description) {
        this.code = code;
        this.description = description;
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

        for (SkillLevel item : SkillLevel.values()) {
            descriptions.add(item.description);
        }

        return descriptions;
    }

    public static SkillLevel fromInt(int intValue) {
        for (SkillLevel item : SkillLevel.values()) {
            if (item.code == intValue) {
                return item;
            }
        }

        return UNKNOWN;
    }
}
