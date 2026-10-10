package com.reclaim.model;

import com.reclaim.util.DateUtil;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * JavaBean for a row of the {@code items} table: a single LOST or FOUND report.
 */
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private ItemType type;
    private String title;
    private Category category;
    private String description;
    private Colour colour;
    private CampusLocation location;
    private String locationDetails;
    private LocalDate incidentDate;
    private String imagePath;
    private ItemStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** Not a column of items: filled from a JOIN with users for display. */
    private String reporterName;

    public Item() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public boolean isLost() {
        return type == ItemType.LOST;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public CampusLocation getLocation() {
        return location;
    }

    public void setLocation(CampusLocation location) {
        this.location = location;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    public LocalDate getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(LocalDate incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getIncidentDateFormatted() {
        return DateUtil.formatDate(incidentDate);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isHasImage() {
        return imagePath != null && !imagePath.isBlank();
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public boolean isOpen() {
        return status == ItemStatus.OPEN;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAtFormatted() {
        return DateUtil.formatDateTime(createdAt);
    }

    public String getCreatedAgo() {
        return DateUtil.timeAgo(createdAt);
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }
}
