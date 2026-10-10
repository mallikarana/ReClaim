package com.reclaim.model;

/** The kind of campus member a user is. */
public enum UserType {
    STUDENT("Student"),
    FACULTY("Faculty"),
    STAFF("Staff");

    private final String label;

    UserType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }

    public static UserType fromString(String value) {
        if (value == null) return null;
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
