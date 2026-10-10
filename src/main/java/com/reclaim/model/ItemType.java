package com.reclaim.model;

/** Whether a report describes something the user LOST or something they FOUND. */
public enum ItemType {
    LOST("Lost"),
    FOUND("Found");

    private final String label;

    ItemType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /** A LOST report is matched against FOUND reports and vice versa. */
    public ItemType opposite() {
        return this == LOST ? FOUND : LOST;
    }

    /** Case-insensitive lookup; returns null for unknown/blank input instead of throwing. */
    public static ItemType fromString(String value) {
        if (value == null) return null;
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
