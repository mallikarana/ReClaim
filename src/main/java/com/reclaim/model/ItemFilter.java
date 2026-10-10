package com.reclaim.model;

/**
 * Search criteria from the Browse page. Every field is optional:
 * a null field means "don't filter on this".
 */
public class ItemFilter {

    private ItemType type;
    private Category category;
    private CampusLocation location;
    private ItemStatus status = ItemStatus.OPEN;
    private String keyword;

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CampusLocation getLocation() {
        return location;
    }

    public void setLocation(CampusLocation location) {
        this.location = location;
    }

    /** Defaults to OPEN; null means all statuses. */
    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /** True when any filter other than the default "open only" is active. */
    public boolean isActive() {
        return type != null || category != null || location != null || keyword != null || status != ItemStatus.OPEN;
    }
}
