package com.reclaim.model;

/**
 * Fixed list of colours. Each colour belongs to a "shade family" so that the
 * matching algorithm can give partial credit for colours people commonly
 * confuse (grey vs silver, gold vs yellow, ...).
 */
public enum Colour {
    BLACK("Black", "#1f2937", "BLACK"),
    WHITE("White", "#f9fafb", "WHITE"),
    GREY("Grey", "#9ca3af", "GREY"),
    SILVER("Silver", "#cbd5e1", "GREY"),
    GOLD("Gold", "#d4a017", "YELLOW"),
    YELLOW("Yellow", "#facc15", "YELLOW"),
    ORANGE("Orange", "#f97316", "ORANGE"),
    RED("Red", "#dc2626", "RED"),
    PINK("Pink", "#ec4899", "RED"),
    PURPLE("Purple", "#9333ea", "PURPLE"),
    BLUE("Blue", "#2563eb", "BLUE"),
    GREEN("Green", "#16a34a", "GREEN"),
    BROWN("Brown", "#92400e", "BROWN"),
    BEIGE("Beige", "#e7d3b0", "BROWN"),
    MULTICOLOUR("Multicolour", "linear-gradient(135deg,#ef4444,#f59e0b,#10b981,#3b82f6)", "MULTI"),
    OTHER("Other / Not sure", "#e5e7eb", "UNKNOWN");

    private final String label;
    private final String swatch;
    private final String family;

    Colour(String label, String swatch, String family) {
        this.label = label;
        this.swatch = swatch;
        this.family = family;
    }

    public String getLabel() {
        return label;
    }

    /** CSS colour value used to draw a small swatch in the UI. */
    public String getSwatch() {
        return swatch;
    }

    public String getFamily() {
        return family;
    }

    public String getName() {
        return name();
    }

    /** True when the colour does not describe one definite shade (multicolour / unknown). */
    public boolean isUncertain() {
        return this == MULTICOLOUR || this == OTHER;
    }

    public static Colour fromString(String value) {
        if (value == null) return null;
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
