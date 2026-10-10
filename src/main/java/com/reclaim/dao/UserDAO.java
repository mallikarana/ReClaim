package com.reclaim.dao;

import com.reclaim.exception.DataAccessException;
import com.reclaim.model.User;
import com.reclaim.model.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for the {@code users} table.
 * All SQL for users lives here and every query uses a PreparedStatement
 * (parameters are bound, never concatenated, which prevents SQL injection).
 */
public class UserDAO {

    private static final Logger LOG = Logger.getLogger(UserDAO.class.getName());

    private static final String COLUMNS =
            "id, full_name, email, password_hash, user_type, university_id, department, phone, role, created_at, last_login_at";

    /** Inserts a new user and returns the generated id. */
    public int create(User user) {
        String sql = "INSERT INTO users (full_name, email, password_hash, user_type, university_id, department, phone) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getUserType().name());
            setNullableString(ps, 5, user.getUniversityId());
            setNullableString(ps, 6, user.getDepartment());
            setNullableString(ps, 7, user.getPhone());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    user.setId(id);
                    return id;
                }
            }
            throw new SQLException("No id generated for new user");
        } catch (SQLException e) {
            throw fail("create user", e);
        }
    }

    public User findByEmail(String email) {
        String sql = "SELECT " + COLUMNS + " FROM users WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw fail("find user by email", e);
        }
    }

    public User findById(int id) {
        String sql = "SELECT " + COLUMNS + " FROM users WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw fail("find user by id", e);
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw fail("check email", e);
        }
    }

    /** Updates the editable profile fields (not email or password). */
    public void updateProfile(User user) {
        String sql = "UPDATE users SET full_name = ?, user_type = ?, university_id = ?, department = ?, phone = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getUserType().name());
            setNullableString(ps, 3, user.getUniversityId());
            setNullableString(ps, 4, user.getDepartment());
            setNullableString(ps, 5, user.getPhone());
            ps.setInt(6, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw fail("update profile", e);
        }
    }

    public void updatePassword(int userId, String passwordHash) {
        String sql = "UPDATE users SET password_hash = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, passwordHash);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw fail("update password", e);
        }
    }

    public void updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw fail("update last login", e);
        }
    }

    /** Converts the current ResultSet row into a User bean. */
    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setUserType(UserType.fromString(rs.getString("user_type")));
        u.setUniversityId(rs.getString("university_id"));
        u.setDepartment(rs.getString("department"));
        u.setPhone(rs.getString("phone"));
        u.setRole(rs.getString("role"));
        Timestamp created = rs.getTimestamp("created_at");
        u.setCreatedAt(created == null ? null : created.toLocalDateTime());
        Timestamp lastLogin = rs.getTimestamp("last_login_at");
        u.setLastLoginAt(lastLogin == null ? null : lastLogin.toLocalDateTime());
        return u;
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
