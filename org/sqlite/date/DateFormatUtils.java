/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.date;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.sqlite.date.FastDateFormat;

public class DateFormatUtils {
    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("GMT");
    public static final FastDateFormat ISO_DATETIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss");
    public static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZZ");
    public static final FastDateFormat ISO_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
    public static final FastDateFormat ISO_DATE_TIME_ZONE_FORMAT = FastDateFormat.getInstance("yyyy-MM-ddZZ");
    public static final FastDateFormat ISO_TIME_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ss");
    public static final FastDateFormat ISO_TIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ssZZ");
    public static final FastDateFormat ISO_TIME_NO_T_FORMAT = FastDateFormat.getInstance("HH:mm:ss");
    public static final FastDateFormat ISO_TIME_NO_T_TIME_ZONE_FORMAT = FastDateFormat.getInstance("HH:mm:ssZZ");
    public static final FastDateFormat SMTP_DATETIME_FORMAT = FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

    public static String formatUTC(long l2, String string) {
        return DateFormatUtils.format(new Date(l2), string, UTC_TIME_ZONE, null);
    }

    public static String formatUTC(Date date, String string) {
        return DateFormatUtils.format(date, string, UTC_TIME_ZONE, null);
    }

    public static String formatUTC(long l2, String string, Locale locale) {
        return DateFormatUtils.format(new Date(l2), string, UTC_TIME_ZONE, locale);
    }

    public static String formatUTC(Date date, String string, Locale locale) {
        return DateFormatUtils.format(date, string, UTC_TIME_ZONE, locale);
    }

    public static String format(long l2, String string) {
        return DateFormatUtils.format(new Date(l2), string, null, null);
    }

    public static String format(Date date, String string) {
        return DateFormatUtils.format(date, string, null, null);
    }

    public static String format(Calendar calendar, String string) {
        return DateFormatUtils.format(calendar, string, null, null);
    }

    public static String format(long l2, String string, TimeZone timeZone) {
        return DateFormatUtils.format(new Date(l2), string, timeZone, null);
    }

    public static String format(Date date, String string, TimeZone timeZone) {
        return DateFormatUtils.format(date, string, timeZone, null);
    }

    public static String format(Calendar calendar, String string, TimeZone timeZone) {
        return DateFormatUtils.format(calendar, string, timeZone, null);
    }

    public static String format(long l2, String string, Locale locale) {
        return DateFormatUtils.format(new Date(l2), string, null, locale);
    }

    public static String format(Date date, String string, Locale locale) {
        return DateFormatUtils.format(date, string, null, locale);
    }

    public static String format(Calendar calendar, String string, Locale locale) {
        return DateFormatUtils.format(calendar, string, null, locale);
    }

    public static String format(long l2, String string, TimeZone timeZone, Locale locale) {
        return DateFormatUtils.format(new Date(l2), string, timeZone, locale);
    }

    public static String format(Date date, String object, TimeZone timeZone, Locale locale) {
        object = FastDateFormat.getInstance((String)object, timeZone, locale);
        return ((FastDateFormat)object).format(date);
    }

    public static String format(Calendar calendar, String object, TimeZone timeZone, Locale locale) {
        object = FastDateFormat.getInstance((String)object, timeZone, locale);
        return ((FastDateFormat)object).format(calendar);
    }
}

