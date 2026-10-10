package com.reclaim.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Date formatting helpers. JSTL's fmt:formatDate only understands java.util.Date,
 * so beans expose pre-formatted strings produced here.
 */
public final class DateUtil {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.ENGLISH);

    private DateUtil() {
    }

    public static String formatDate(LocalDate date) {
        return date == null ? "" : date.format(DATE);
    }

    public static String formatDate(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(DATE);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? "—" : dateTime.format(DATE_TIME);
    }

    /** Human-friendly relative time such as "just now", "5 min ago", "3 days ago". */
    public static String timeAgo(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        if (minutes < 1) return "just now";
        if (minutes < 60) return minutes + " min ago";
        long hours = minutes / 60;
        if (hours < 24) return hours + (hours == 1 ? " hour ago" : " hours ago");
        long days = hours / 24;
        if (days < 30) return days + (days == 1 ? " day ago" : " days ago");
        return formatDate(dateTime);
    }
}
