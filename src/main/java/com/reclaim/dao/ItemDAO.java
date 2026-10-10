package com.reclaim.dao;

import com.reclaim.exception.DataAccessException;
import com.reclaim.model.CampusLocation;
import com.reclaim.model.Category;
import com.reclaim.model.Colour;
import com.reclaim.model.Item;
import com.reclaim.model.ItemFilter;
import com.reclaim.model.ItemStatus;
import com.reclaim.model.ItemType;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for the {@code items} table (lost / found reports).
 * Queries join {@code users} to show the reporter's name.
 */
public class ItemDAO {

    private static final Logger LOG = Logger.getLogger(ItemDAO.class.getName());

    private static final String SELECT =
            "SELECT i.id, i.user_id, i.type, i.title, i.category, i.description, i.colour, i.location, "
                    + "i.location_details, i.incident_date, i.image_path, i.status, i.created_at, i.updated_at, "
                    + "u.full_name AS reporter_name "
                    + "FROM items i JOIN users u ON u.id = i.user_id ";

    /** Upper bound on rows returned by the Browse page. */
    public static final int BROWSE_LIMIT = 100;

    public int create(Item item) {
        String sql = "INSERT INTO items (user_id, type, title, category, description, colour, location, "
                + "location_details, incident_date, image_path, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getUserId());
            ps.setString(2, item.getType().name());
            ps.setString(3, item.getTitle());
            ps.setString(4, item.getCategory().name());
            ps.setString(5, item.getDescription());
            ps.setString(6, item.getColour().name());
            ps.setString(7, item.getLocation().name());
            setNullableString(ps, 8, item.getLocationDetails());
            ps.setDate(9, Date.valueOf(item.getIncidentDate()));
            setNullableString(ps, 10, item.getImagePath());
            ps.setString(11, item.getStatus().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    item.setId(id);
                    return id;
                }
            }
            throw new SQLException("No id generated for new item");
        } catch (SQLException e) {
            throw fail("create item", e);
        }
    }

    public Item findById(int id) {
        String sql = SELECT + "WHERE i.id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw fail("find item", e);
        }
    }

    /** All reports created by one user, newest first ("My Reports"). */
    public List<Item> findByUser(int userId) {
        String sql = SELECT + "WHERE i.user_id = ? ORDER BY i.created_at DESC, i.id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return mapAll(ps);
        } catch (SQLException e) {
            throw fail("list user's items", e);
        }
    }

    /** All OPEN reports of one type: the candidate pool for the matching algorithm. */
    public List<Item> findOpenByType(ItemType type) {
        String sql = SELECT + "WHERE i.type = ? AND i.status = 'OPEN'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, type.name());
            return mapAll(ps);
        } catch (SQLException e) {
            throw fail("list open items", e);
        }
    }

    /** Most recent OPEN reports on campus (dashboard feed). */
    public List<Item> findRecentOpen(int limit) {
        String sql = SELECT + "WHERE i.status = 'OPEN' ORDER BY i.created_at DESC, i.id DESC LIMIT ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            return mapAll(ps);
        } catch (SQLException e) {
            throw fail("list recent items", e);
        }
    }

    /**
     * Browse / search. The WHERE clause is built from whichever filters are set,
     * but values are still bound as parameters - never concatenated into the SQL.
     */
    public List<Item> search(ItemFilter filter) {
        StringBuilder sql = new StringBuilder(SELECT).append("WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (filter.getType() != null) {
            sql.append("AND i.type = ? ");
            params.add(filter.getType().name());
        }
        if (filter.getCategory() != null) {
            sql.append("AND i.category = ? ");
            params.add(filter.getCategory().name());
        }
        if (filter.getLocation() != null) {
            sql.append("AND i.location = ? ");
            params.add(filter.getLocation().name());
        }
        if (filter.getStatus() != null) {
            sql.append("AND i.status = ? ");
            params.add(filter.getStatus().name());
        }
        if (filter.getKeyword() != null) {
            sql.append("AND (i.title LIKE ? ESCAPE '!' OR i.description LIKE ? ESCAPE '!' "
                    + "OR i.location_details LIKE ? ESCAPE '!') ");
            String like = "%" + escapeLike(filter.getKeyword()) + "%";
            params.add(like);
            params.add(like);
            params.add(like);
        }
        sql.append("ORDER BY i.created_at DESC, i.id DESC LIMIT ").append(BROWSE_LIMIT);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, (String) params.get(i));
            }
            return mapAll(ps);
        } catch (SQLException e) {
            throw fail("search items", e);
        }
    }

    /**
     * Changes the status of a report. The user_id condition guarantees that
     * users can only change their own reports.
     * @return true if a row was updated
     */
    public boolean updateStatus(int itemId, int ownerId, ItemStatus status) {
        String sql = "UPDATE items SET status = ? WHERE id = ? AND user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, itemId);
            ps.setInt(3, ownerId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw fail("update item status", e);
        }
    }

    /** Counts a user's reports grouped by type and status, keyed like "LOST_OPEN". */
    public Map<String, Integer> countByUser(int userId) {
        String sql = "SELECT type, status, COUNT(*) AS n FROM items WHERE user_id = ? GROUP BY type, status";
        Map<String, Integer> counts = new HashMap<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    counts.put(rs.getString("type") + "_" + rs.getString("status"), rs.getInt("n"));
                }
            }
            return counts;
        } catch (SQLException e) {
            throw fail("count user's items", e);
        }
    }

    /** Campus-wide counts of OPEN reports, keyed by type ("LOST", "FOUND"), plus "RESOLVED". */
    public Map<String, Integer> campusCounts() {
        String sql = "SELECT CASE WHEN status = 'OPEN' THEN type ELSE status END AS k, COUNT(*) AS n "
                + "FROM items GROUP BY k";
        Map<String, Integer> counts = new HashMap<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                counts.put(rs.getString("k"), rs.getInt("n"));
            }
            return counts;
        } catch (SQLException e) {
            throw fail("count campus items", e);
        }
    }

    private List<Item> mapAll(PreparedStatement ps) throws SQLException {
        List<Item> items = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                items.add(map(rs));
            }
        }
        return items;
    }

    /** Converts the current ResultSet row into an Item bean. */
    private Item map(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setId(rs.getInt("id"));
        item.setUserId(rs.getInt("user_id"));
        item.setType(ItemType.fromString(rs.getString("type")));
        item.setTitle(rs.getString("title"));
        Category category = Category.fromString(rs.getString("category"));
        item.setCategory(category != null ? category : Category.OTHER);
        item.setDescription(rs.getString("description"));
        Colour colour = Colour.fromString(rs.getString("colour"));
        item.setColour(colour != null ? colour : Colour.OTHER);
        CampusLocation location = CampusLocation.fromString(rs.getString("location"));
        item.setLocation(location != null ? location : CampusLocation.OTHER);
        item.setLocationDetails(rs.getString("location_details"));
        Date incident = rs.getDate("incident_date");
        item.setIncidentDate(incident == null ? null : incident.toLocalDate());
        item.setImagePath(rs.getString("image_path"));
        item.setStatus(ItemStatus.fromString(rs.getString("status")));
        Timestamp created = rs.getTimestamp("created_at");
        item.setCreatedAt(created == null ? null : created.toLocalDateTime());
        Timestamp updated = rs.getTimestamp("updated_at");
        item.setUpdatedAt(updated == null ? null : updated.toLocalDateTime());
        item.setReporterName(rs.getString("reporter_name"));
        return item;
    }

    /** Escapes LIKE wildcards so a search for "100%" matches literally. '!' is the escape char. */
    private static String escapeLike(String value) {
        return value.replace("!", "!!").replace("%", "!%").replace("_", "!_");
    }

    private static void setNullableString(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null) ps.setNull(index, Types.VARCHAR);
        else ps.setString(index, value);
    }

    private static DataAccessException fail(String action, SQLException e) {
        LOG.log(Level.SEVERE, "Database error while trying to " + action, e);
        return new DataAccessException("Database error while trying to " + action, e);
    }
}
