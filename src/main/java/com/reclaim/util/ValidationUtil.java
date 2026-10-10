package com.reclaim.util;

import java.util.regex.Pattern;

/** Small, reusable input checks shared by the service layer. */
public final class ValidationUtil {

    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    /** Indian mobile number: 10 digits starting with 6-9. */
    private static final Pattern PHONE = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern NAME = Pattern.compile("^[\\p{L} .'-]+$");
    private static final Pattern UNIVERSITY_ID = Pattern.compile("^[A-Za-z0-9/-]{3,20}$");

    private ValidationUtil() {
    }

    /** Trims the value; returns null for null or blank input. */
    public static String clean(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.length() <= 150 && EMAIL.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE.matcher(phone).matches();
    }

    public static boolean isValidName(String name) {
        return name != null && name.length() >= 2 && name.length() <= 100 && NAME.matcher(name).matches();
    }

    public static boolean isValidUniversityId(String id) {
        return id != null && UNIVERSITY_ID.matcher(id).matches();
    }

    /**
     * Password policy: at least 8 characters, containing at least one letter and one digit.
     * Returns an error message, or null when the password is acceptable.
     */
    public static String checkPasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        if (password.length() > 64) {
            return "Password must be at most 64 characters long.";
        }
        boolean hasLetter = password.chars().anyMatch(Character::isLetter);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        if (!hasLetter || !hasDigit) {
            return "Password must contain at least one letter and one number.";
        }
        return null;
    }

    public static boolean lengthBetween(String value, int min, int max) {
        return value != null && value.length() >= min && value.length() <= max;
    }
}
