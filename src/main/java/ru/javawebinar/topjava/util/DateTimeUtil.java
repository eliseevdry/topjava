package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpenTime(LocalDateTime ldt, LocalTime startTime, LocalTime endTime) {
        return ldt.toLocalTime().compareTo(startTime) >= 0 && ldt.toLocalTime().compareTo(endTime) < 0;
    }

    public static boolean isBetweenHalfOpenDate(LocalDateTime ldt, LocalDate startDate, LocalDate endDate) {
        return ldt.toLocalDate().compareTo(startDate) >= 0 && ldt.toLocalDate().compareTo(endDate) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

