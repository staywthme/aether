/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ISO8601Utils {
    private static final String UTC_ID = "UTC";
    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");

    public static String format(Date date) {
        return ISO8601Utils.format(date, false, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean bl) {
        return ISO8601Utils.format(date, bl, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean n2, TimeZone timeZone) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone, Locale.US);
        gregorianCalendar.setTime(date);
        int n3 = 19;
        n3 = 19 + (n2 != 0 ? 4 : 0);
        StringBuilder stringBuilder = new StringBuilder(n3 += timeZone.getRawOffset() == 0 ? 1 : 6);
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(1), 4);
        stringBuilder.append('-');
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(2) + 1, 2);
        stringBuilder.append('-');
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(5), 2);
        stringBuilder.append('T');
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(11), 2);
        stringBuilder.append(':');
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(12), 2);
        stringBuilder.append(':');
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(13), 2);
        if (n2 != 0) {
            stringBuilder.append('.');
            ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(14), 3);
        }
        if ((n2 = timeZone.getOffset(gregorianCalendar.getTimeInMillis())) != 0) {
            int n4 = Math.abs(n2 / 60000 / 60);
            int n5 = Math.abs(n2 / 60000 % 60);
            stringBuilder.append(n2 < 0 ? (char)'-' : '+');
            ISO8601Utils.padInt(stringBuilder, n4, 2);
            stringBuilder.append(':');
            ISO8601Utils.padInt(stringBuilder, n5, 2);
        } else {
            stringBuilder.append('Z');
        }
        return stringBuilder.toString();
    }

    public static Date parse(String string, ParsePosition parsePosition) throws ParseException {
        RuntimeException runtimeException;
        RuntimeException runtimeException2;
        try {
            TimeZone timeZone;
            int n2 = parsePosition.getIndex();
            int n3 = ISO8601Utils.parseInt(string, n2, n2 += 4);
            if (ISO8601Utils.checkOffset(string, n2, '-')) {
                // empty if block
            }
            int n4 = ISO8601Utils.parseInt(string, ++n2, n2 += 2);
            if (ISO8601Utils.checkOffset(string, n2, '-')) {
                // empty if block
            }
            int n5 = ISO8601Utils.parseInt(string, ++n2, n2 += 2);
            int n6 = 0;
            int n7 = 0;
            int n8 = 0;
            int n9 = 0;
            int n10 = ISO8601Utils.checkOffset(string, n2, 'T');
            if (n10 == 0 && string.length() <= n2) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(n3, n4 - 1, n5);
                gregorianCalendar.setLenient(false);
                parsePosition.setIndex(n2);
                return gregorianCalendar.getTime();
            }
            if (n10 != 0) {
                n6 = ISO8601Utils.parseInt(string, ++n2, n2 += 2);
                if (ISO8601Utils.checkOffset(string, n2, ':')) {
                    // empty if block
                }
                n7 = ISO8601Utils.parseInt(string, ++n2, n2 += 2);
                if (ISO8601Utils.checkOffset(string, n2, ':')) {
                    ++n2;
                }
                if (string.length() > n2 && (n10 = string.charAt(n2)) != 90 && n10 != 43 && n10 != 45) {
                    if ((n8 = ISO8601Utils.parseInt(string, n2, n2 += 2)) > 59 && n8 < 63) {
                        n8 = 59;
                    }
                    if (ISO8601Utils.checkOffset(string, n2, '.')) {
                        n10 = ISO8601Utils.indexOfNonDigit(string, ++n2 + 1);
                        int n11 = Math.min(n10, n2 + 3);
                        int n12 = ISO8601Utils.parseInt(string, n2, n11);
                        switch (n11 - n2) {
                            case 2: {
                                n9 = n12 * 10;
                                break;
                            }
                            case 1: {
                                n9 = n12 * 100;
                                break;
                            }
                            default: {
                                n9 = n12;
                            }
                        }
                        n2 = n10;
                    }
                }
            }
            if (string.length() <= n2) {
                throw new IllegalArgumentException("No time zone indicator");
            }
            n10 = string.charAt(n2);
            if (n10 == 90) {
                timeZone = TIMEZONE_UTC;
                ++n2;
            } else if (n10 == 43 || n10 == 45) {
                String string2 = string.substring(n2);
                string2 = string2.length() >= 5 ? string2 : string2 + "00";
                n2 += string2.length();
                if ("+0000".equals(string2) || "+00:00".equals(string2)) {
                    timeZone = TIMEZONE_UTC;
                } else {
                    String string3 = "GMT" + string2;
                    timeZone = TimeZone.getTimeZone(string3);
                    if (!(string2 = timeZone.getID()).equals(string3) && !(string2 = string2.replace(":", "")).equals(string3)) {
                        throw new IndexOutOfBoundsException("Mismatching time zone indicator: " + string3 + " given, resolves to " + timeZone.getID());
                    }
                }
            } else {
                throw new IndexOutOfBoundsException("Invalid time zone indicator '" + (char)n10 + "'");
            }
            GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone);
            gregorianCalendar.setLenient(false);
            gregorianCalendar.set(1, n3);
            gregorianCalendar.set(2, n4 - 1);
            gregorianCalendar.set(5, n5);
            gregorianCalendar.set(11, n6);
            gregorianCalendar.set(12, n7);
            gregorianCalendar.set(13, n8);
            gregorianCalendar.set(14, n9);
            parsePosition.setIndex(n2);
            return gregorianCalendar.getTime();
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            runtimeException2 = indexOutOfBoundsException;
            runtimeException = indexOutOfBoundsException;
        }
        catch (NumberFormatException numberFormatException) {
            runtimeException2 = numberFormatException;
            runtimeException = numberFormatException;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            runtimeException2 = illegalArgumentException;
            runtimeException = illegalArgumentException;
        }
        runtimeException2 = string == null ? null : "\"" + string + '\"';
        String string4 = runtimeException.getMessage();
        if (string4 == null || string4.isEmpty()) {
            string4 = "(" + runtimeException.getClass().getName() + ")";
        }
        ParseException parseException = new ParseException("Failed to parse date [" + (String)((Object)runtimeException2) + "]: " + string4, parsePosition.getIndex());
        parseException.initCause(runtimeException);
        throw parseException;
    }

    private static boolean checkOffset(String string, int n2, char c2) {
        return n2 < string.length() && string.charAt(n2) == c2;
    }

    private static int parseInt(String string, int n2, int n3) throws NumberFormatException {
        int n4;
        if (n2 < 0 || n3 > string.length() || n2 > n3) {
            throw new NumberFormatException(string);
        }
        int n5 = n2;
        int n6 = 0;
        if (n2 < n3) {
            ++n5;
            n4 = Character.digit(string.charAt(n2), 10);
            if (n4 < 0) {
                throw new NumberFormatException("Invalid number: " + string.substring(n2, n3));
            }
            n6 = -n4;
        }
        while (n5 < n3) {
            if ((n4 = Character.digit(string.charAt(n5++), 10)) < 0) {
                throw new NumberFormatException("Invalid number: " + string.substring(n2, n3));
            }
            n6 *= 10;
            n6 -= n4;
        }
        return -n6;
    }

    private static void padInt(StringBuilder stringBuilder, int n2, int n3) {
        String string = Integer.toString(n2);
        n3 -= string.length();
        while (n3 > 0) {
            stringBuilder.append('0');
            --n3;
        }
        stringBuilder.append(string);
    }

    private static int indexOfNonDigit(String string, int n2) {
        while (n2 < string.length()) {
            char c2 = string.charAt(n2);
            if (c2 < '0' || c2 > '9') {
                return n2;
            }
            ++n2;
        }
        return string.length();
    }
}

