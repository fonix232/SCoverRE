package org.apache.commons.lang.time;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatUtils {
    public static final FastDateFormat ISO_DATETIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss");
    public static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZZ");
    public static final FastDateFormat ISO_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
    public static final FastDateFormat ISO_DATE_TIME_ZONE_FORMAT = FastDateFormat.getInstance("yyyy-MM-ddZZ");
    public static final FastDateFormat ISO_TIME_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ss");
    public static final FastDateFormat ISO_TIME_NO_T_FORMAT = FastDateFormat.getInstance("HH:mm:ss");
    public static final FastDateFormat ISO_TIME_NO_T_TIME_ZONE_FORMAT = FastDateFormat.getInstance("HH:mm:ssZZ");
    public static final FastDateFormat ISO_TIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ssZZ");
    public static final FastDateFormat SMTP_DATETIME_FORMAT = FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

    public static String formatUTC(long millis, String pattern) {
        return format(new Date(millis), pattern, DateUtils.UTC_TIME_ZONE, null);
    }

    public static String formatUTC(Date date, String pattern) {
        return format(date, pattern, DateUtils.UTC_TIME_ZONE, null);
    }

    public static String formatUTC(long millis, String pattern, Locale locale) {
        return format(new Date(millis), pattern, DateUtils.UTC_TIME_ZONE, locale);
    }

    public static String formatUTC(Date date, String pattern, Locale locale) {
        return format(date, pattern, DateUtils.UTC_TIME_ZONE, locale);
    }

    public static String format(long millis, String pattern) {
        return format(new Date(millis), pattern, null, null);
    }

    public static String format(Date date, String pattern) {
        return format(date, pattern, null, null);
    }

    public static String format(long millis, String pattern, TimeZone timeZone) {
        return format(new Date(millis), pattern, timeZone, null);
    }

    public static String format(Date date, String pattern, TimeZone timeZone) {
        return format(date, pattern, timeZone, null);
    }

    public static String format(long millis, String pattern, Locale locale) {
        return format(new Date(millis), pattern, null, locale);
    }

    public static String format(Date date, String pattern, Locale locale) {
        return format(date, pattern, null, locale);
    }

    public static String format(long millis, String pattern, TimeZone timeZone, Locale locale) {
        return format(new Date(millis), pattern, timeZone, locale);
    }

    public static String format(Date date, String pattern, TimeZone timeZone, Locale locale) {
        return FastDateFormat.getInstance(pattern, timeZone, locale).format(date);
    }
}
