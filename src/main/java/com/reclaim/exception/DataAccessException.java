package com.reclaim.exception;

/**
 * Unchecked wrapper for database (JDBC) failures.
 *
 * DAOs catch {@link java.sql.SQLException}, log it and rethrow it as this
 * exception so controllers are not cluttered with SQL-specific try/catch
 * blocks. Unhandled, it reaches the container which shows the friendly
 * error page configured in web.xml.
 */
public class DataAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
