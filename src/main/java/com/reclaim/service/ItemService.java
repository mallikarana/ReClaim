package com.reclaim.service;

import com.reclaim.dao.ItemDAO;
import com.reclaim.exception.ValidationException;
import com.reclaim.model.CampusLocation;
import com.reclaim.model.Category;
import com.reclaim.model.Colour;
import com.reclaim.model.Item;
import com.reclaim.model.ItemFilter;
import com.reclaim.model.ItemStatus;
import com.reclaim.model.ItemType;
import com.reclaim.util.ValidationUtil;
import jakarta.servlet.http.Part;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Business logic for Module 2: creating and managing lost / found reports,
 * plus the queries used by the Browse page (Module 3).
 */
public class ItemService {

    /** Reports older than this are rejected - the matching would not be useful anyway. */
    public static final int MAX_AGE_DAYS = 365;

    private final ItemDAO itemDAO;
    private final ImageStorageService imageStorage;

    public ItemService() {
        this(new ItemDAO(), new ImageStorageService());
    }

    public ItemService(ItemDAO itemDAO, ImageStorageService imageStorage) {
        this.itemDAO = itemDAO;
        this.imageStorage = imageStorage;
    }

    /**
     * Validates the report form, stores the optional photo and saves the report.
     * New reports always start with status OPEN.
     */
    public Item createReport(int userId, String type, String title, String category, String description,
                             String colour, String location, String locationDetails, String incidentDate,
                             Part image) throws ValidationException {

        Item item = new Item();
        item.setUserId(userId);
        item.setType(ItemType.fromString(type));
        item.setTitle(ValidationUtil.clean(title));
        item.setCategory(Category.fromString(category));
        item.setDescription(ValidationUtil.clean(description));
        item.setColour(Colour.fromString(colour));
        item.setLocation(CampusLocation.fromString(location));
        item.setLocationDetails(ValidationUtil.clean(locationDetails));
        item.setStatus(ItemStatus.OPEN);

        Map<String, String> errors = new LinkedHashMap<>();
        if (item.getType() == null) {
            errors.put("type", "Choose whether you lost or found this item.");
        }
        if (!ValidationUtil.lengthBetween(item.getTitle(), 3, 100)) {
            errors.put("title", "Give the item a short name (3-100 characters), e.g. \"Black Samsung phone\".");
        }
        if (item.getCategory() == null) {
            errors.put("category", "Select a category.");
        }
        if (!ValidationUtil.lengthBetween(item.getDescription(), 10, 1000)) {
            errors.put("description", "Describe the item in 10-1000 characters (brand, marks, contents...).");
        }
        if (item.getColour() == null) {
            errors.put("colour", "Select the main colour.");
        }
        if (item.getLocation() == null) {
            errors.put("location", "Select where on campus it happened.");
        }
        if (item.getLocationDetails() != null && item.getLocationDetails().length() > 150) {
            errors.put("locationDetails", "Location details must be at most 150 characters.");
        }
        String dateError = parseIncidentDate(item, incidentDate);
        if (dateError != null) {
            errors.put("incidentDate", dateError);
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        // Save the photo only after the form is valid, so invalid forms leave no orphan files.
        String storedImage = imageStorage.store(image);
        item.setImagePath(storedImage);
        try {
            itemDAO.create(item);
        } catch (RuntimeException e) {
            imageStorage.delete(storedImage);
            throw e;
        }
        return item;
    }

    private String parseIncidentDate(Item item, String value) {
        String cleaned = ValidationUtil.clean(value);
        if (cleaned == null) {
            return "Enter the date.";
        }
        LocalDate date;
        try {
            date = LocalDate.parse(cleaned);
        } catch (DateTimeParseException e) {
            return "Enter a valid date.";
        }
        LocalDate today = LocalDate.now();
        if (date.isAfter(today)) {
            return "The date cannot be in the future.";
        }
        if (date.isBefore(today.minusDays(MAX_AGE_DAYS))) {
            return "The date must be within the last 12 months.";
        }
        item.setIncidentDate(date);
        return null;
    }

    public Item findById(int id) {
        return itemDAO.findById(id);
    }

    public List<Item> findByUser(int userId) {
        return itemDAO.findByUser(userId);
    }

    public List<Item> browse(ItemFilter filter) {
        return itemDAO.search(filter);
    }

    public List<Item> recentOpen(int limit) {
        return itemDAO.findRecentOpen(limit);
    }

    public Map<String, Integer> countByUser(int userId) {
        return itemDAO.countByUser(userId);
    }

    public Map<String, Integer> campusCounts() {
        return itemDAO.campusCounts();
    }

    /** Lets the owner mark a report as Resolved / Closed (or re-open it). */
    public void updateStatus(int itemId, int userId, String status) throws ValidationException {
        ItemStatus newStatus = ItemStatus.fromString(status);
        if (newStatus == null) {
            throw new ValidationException("status", "Unknown status.");
        }
        if (!itemDAO.updateStatus(itemId, userId, newStatus)) {
            throw new ValidationException("status", "You can only update your own reports.");
        }
    }
}
