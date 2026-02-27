package com.example.aklati.presentation.meal_list;


public enum FilterType {
    CATEGORY("category"),
    AREA("area");

    private final String value;

    FilterType(String value) {
        this.value = value;
    }

    // value string value ("category" or "area")
    // FilterType enum, defaults to CATEGORY if invalid

    public static FilterType fromString(String value) {
        if (value == null) return CATEGORY;

        for (FilterType type : FilterType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return CATEGORY;
    }

    public String getValue() {
        return value;
    }
}

