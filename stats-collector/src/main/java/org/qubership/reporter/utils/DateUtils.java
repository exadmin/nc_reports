package org.qubership.reporter.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    private static final SimpleDateFormat FILE_SUFFIX_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");

    public static String getCurrentDateTimeStamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static Date toDate(String speciallyFormattedStr) {
        try {
            return FILE_SUFFIX_DATE_FORMAT.parse(speciallyFormattedStr);
        } catch (ParseException pe) {
            throw new IllegalArgumentException(pe);
        }
    }

    public static boolean firstDateEarlierThenSecond(String firstDate, String secondDate) {
        Date date1 = toDate(firstDate);
        Date date2 = toDate(secondDate);

        return date1.before(date2);
    }
}
