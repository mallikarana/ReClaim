package com.reclaim.exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Thrown by the service layer when user input breaks a business rule.
 * Carries one message per form field so the JSP can show each error
 * next to the field that caused it.
 */
public class ValidationException extends Exception {

    private static final long serialVersionUID = 1L;

    private final Map<String, String> fieldErrors;

    public ValidationException(Map<String, String> fieldErrors) {
        super("Validation failed: " + fieldErrors);
        this.fieldErrors = Collections.unmodifiableMap(new LinkedHashMap<>(fieldErrors));
    }

    /** Convenience constructor for a single error. */
    public ValidationException(String field, String message) {
        this(Map.of(field, message));
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
