package com.reclaim.model;

/** Fixed list of item categories. Using a fixed list keeps category matching exact and reliable. */
public enum Category {
    MOBILE_PHONE("Mobile Phone"),
    LAPTOP_TABLET("Laptop / Tablet"),
    EARPHONES("Earphones / Headphones"),
    OTHER_ELECTRONICS("Other Electronics"),
    WALLET("Wallet / Purse"),
    KEYS("Keys"),
    ID_CARD("ID Card / Documents"),
    BAG("Bag / Backpack"),
    BOOKS_STATIONERY("Books / Stationery"),
    CLOTHING("Clothing"),
    ACCESSORIES("Watch / Jewellery / Accessories"),
    WATER_BOTTLE("Water Bottle / Lunch Box"),
    OTHER("Other");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /** Needed so JSP EL can read the constant name, e.g. ${c.name}. */
    public String getName() {
        return name();
    }

    public static Category fromString(String value) {
        if (value == null) return null;
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
