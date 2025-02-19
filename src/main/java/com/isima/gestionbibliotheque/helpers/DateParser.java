package com.isima.gestionbibliotheque.helpers;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 *
 * */
@Component
public class DateParser {
    private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
            DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
            );

    public static LocalDate parseDate(String dateStr) {
        if (dateStr.startsWith("/")) {
            dateStr = dateStr.substring(1);
        }
        if (dateStr.matches("\\d{4}")) {
            return LocalDate.of(Integer.parseInt(dateStr), 1, 1);
        }

        if (dateStr.matches("^\\d{4}[-\\s]\\d{2}$") ) {
            return LocalDate.of(Integer.parseInt(dateStr.substring(0, 4)), Integer.parseInt(dateStr.substring(5)), 1);
        }

        for(DateTimeFormatter dateTimeFormatter: FORMATTERS) {
            try {
                return LocalDate.parse(dateStr, dateTimeFormatter);
            }
            catch (Exception ignored){
            }
        }

        throw new IllegalArgumentException("Unknown date format: " + dateStr);
    }
}
