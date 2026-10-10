package com.reclaim.model;

/**
 * Lifecycle of a report.
 * OPEN     - still active; takes part in browsing and matching.
 * RESOLVED - item was returned to / recovered by its owner.
 * CLOSED   - withdrawn by the reporter (e.g. reported by mistake).
 */
public enum ItemStatus {
    OPEN("Open"),
    RESOLVED("Resolved"),
    CLOSED("Closed");

    private final String label;

    ItemStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static ItemStatus fromString(String value) {
        if (value == null) return null;
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
