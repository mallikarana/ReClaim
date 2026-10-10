package com.reclaim.dao;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Central place that opens JDBC connections to MySQL.
 *
 * Settings are read once from {@code db.properties} on the classpath
 * (src/main/resources). Environment variables RECLAIM_DB_URL, RECLAIM_DB_USER
 * and RECLAIM_DB_PASSWORD override the file, which is handy on other machines.
 *
 * Every DAO method obtains its own connection and closes it with
 * try-with-resources, so connections are never leaked.
 */
public final class DBConnection {

    private static final Logger LOG = Logger.getLogger(DBConnection.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            } else {
                LOG.warning("db.properties not found on classpath - relying on environment variables. "
                        + "Copy src/main/resources/db.properties.example to db.properties.");
            }
        } catch (IOException e) {
            LOG.severe("Could not read db.properties: " + e.getMessage());
        }
        URL = setting("RECLAIM_DB_URL", props.getProperty("db.url"));
        USER = setting("RECLAIM_DB_USER", props.getProperty("db.user"));
        PASSWORD = setting("RECLAIM_DB_PASSWORD", props.getProperty("db.password"));

        try {
            // Explicit loading makes the driver dependency obvious and fails fast if the jar is missing.
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL JDBC driver not found: " + DRIVER);
        }
    }

    private DBConnection() {
    }

    private static String setting(String envName, String fileValue) {
        String env = System.getenv(envName);
        return (env != null && !env.isBlank()) ? env : fileValue;
    }

    /** Opens a new connection. The caller must close it (use try-with-resources). */
    public static Connection getConnection() throws SQLException {
        if (URL == null || USER == null) {
            throw new SQLException("Database is not configured: set db.url and db.user in db.properties");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** URL without credentials, safe to print in logs. */
    public static String describe() {
        return URL == null ? "(not configured)" : URL.replaceAll("\\?.*$", "");
    }
}
