package com.reclaim.model;

/**
 * Campus zones a report can be tied to. A fixed list (instead of free text)
 * lets the matching algorithm compare locations exactly; the reporter can add
 * free-text detail ("2nd floor, near the lift") in Item.locationDetails.
 */
public enum CampusLocation {
    MAIN_GATE("Main Gate / Security"),
    ACADEMIC_BLOCK("Academic Block / Classrooms"),
    COMPUTER_LAB("Computer Labs"),
    LIBRARY("Library"),
    CAFETERIA("Cafeteria / Food Court"),
    AUDITORIUM("Auditorium / Seminar Hall"),
    ADMIN_BLOCK("Administrative Block"),
    SPORTS_COMPLEX("Sports Ground / Gym"),
    HOSTEL("Hostel"),
    PARKING("Parking Area"),
    TRANSPORT("Bus Stop / University Transport"),
    OTHER("Other (see details)");

    private final String label;

    CampusLocation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }

    public static CampusLocation fromString(String value) {
        if (value == null) return null;
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
