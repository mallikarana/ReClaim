package com.reclaim.model;

/**
 * Campus zones a report can be tied to. A fixed list (instead of free text)
 * lets the matching algorithm compare locations exactly; the reporter can add
 * free-text detail ("2nd floor, near the lift") in Item.locationDetails.
 */
public enum CampusLocation {
    MAIN_GATE("GEU Main gate : 1"),
    MAIN_GATE_2("GEU Main gate : 2"),
    CS_IT_GATE("GEU CS/IT Gate"),
    ACADEMIC_BLOCK("CS/IT Block"),
    COMPUTER_LAB("Param Lab"),
    LIBRARY("Digital Library"),
    CAFETERIA("Ravi Canteen"),
    AUDITORIUM("Happiness Hut"),
    ADMIN_BLOCK("Admin Block/ BTech Block"),
    SPORTS_COMPLEX("Badminton Court"),
    HOSTEL("Old MCA Block"),
    PARKING("BasketBall Court"),
    TRANSPORT("Quick Bites"),
    CHANAKYA_BLOCK("Chanakya Block"),
    KP_NAUTIYAL_BLOCK("K.P.Nautiyal Block"),
    MECHANICAL_BLOCK("Mechanical Block"),
    CIVIL_BLOCK("Civil Block"),
    PARAM_LAB("Param Lab"),
    ARYABHATTA_LAB("Aryabhatta Lab"),
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
