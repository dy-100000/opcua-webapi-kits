package org.opcfoundation.uawebservicedemo.models.employee.enumeration;

import java.util.ArrayList;
import java.util.List;

public enum Sex {
    MALE(0,"Male"),
    FEMALE(1,"Female"),
    UNKNOWN(2,"Unknown");

    private final int code;
    private final String description;

    Sex(int code, String description) {
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

        for (Sex item : Sex.values()) {
            descriptions.add(item.description);
        }

        return descriptions;
    }

    public static Sex fromInt(int intValue) {
        for (Sex item : Sex.values()) {
            if (item.code == intValue) {
                return item;
            }
        }

        return UNKNOWN;
    }
}
