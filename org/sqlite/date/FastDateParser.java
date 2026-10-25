/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.date;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.sqlite.date.DateParser;

public class FastDateParser
implements Serializable,
DateParser {
    private static final long serialVersionUID = 2L;
    static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
    private final String pattern;
    private final TimeZone timeZone;
    private final Locale locale;
    private final int century;
    private final int startYear;
    private transient Pattern parsePattern;
    private transient Strategy[] strategies;
    private transient String currentFormatField;
    private transient Strategy nextStrategy;
    private static final Pattern formatPattern = Pattern.compile("D+|E+|F+|G+|H+|K+|M+|S+|W+|X+|Z+|a+|d+|h+|k+|m+|s+|w+|y+|z+|''|'[^']++(''[^']*+)*+'|[^'A-Za-z]++");
    private static final ConcurrentMap<Locale, Strategy>[] caches = new ConcurrentMap[17];
    private static final Strategy ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(1){

        @Override
        void setCalendar(FastDateParser fastDateParser, Calendar calendar, String string) {
            int n2 = Integer.parseInt(string);
            if (n2 < 100) {
                n2 = fastDateParser.adjustYear(n2);
            }
            calendar.set(1, n2);
        }
    };
    private static final Strategy NUMBER_MONTH_STRATEGY = new NumberStrategy(2){

        @Override
        int modify(int n2) {
            return n2 - 1;
        }
    };
    private static final Strategy LITERAL_YEAR_STRATEGY = new NumberStrategy(1);
    private static final Strategy WEEK_OF_YEAR_STRATEGY = new NumberStrategy(3);
    private static final Strategy WEEK_OF_MONTH_STRATEGY = new NumberStrategy(4);
    private static final Strategy DAY_OF_YEAR_STRATEGY = new NumberStrategy(6);
    private static final Strategy DAY_OF_MONTH_STRATEGY = new NumberStrategy(5);
    private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(8);
    private static final Strategy HOUR_OF_DAY_STRATEGY = new NumberStrategy(11);
    private static final Strategy HOUR24_OF_DAY_STRATEGY = new NumberStrategy(11){

        @Override
        int modify(int n2) {
            if (n2 == 24) {
                return 0;
            }
            return n2;
        }
    };
    private static final Strategy HOUR12_STRATEGY = new NumberStrategy(10){

        @Override
        int modify(int n2) {
            if (n2 == 12) {
                return 0;
            }
            return n2;
        }
    };
    private static final Strategy HOUR_STRATEGY = new NumberStrategy(10);
    private static final Strategy MINUTE_STRATEGY = new NumberStrategy(12);
    private static final Strategy SECOND_STRATEGY = new NumberStrategy(13);
    private static final Strategy MILLISECOND_STRATEGY = new NumberStrategy(14);
    private static final Strategy ISO_8601_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::?\\d{2})?))");

    protected FastDateParser(String string, TimeZone timeZone, Locale locale) {
        this(string, timeZone, locale, null);
    }

    protected FastDateParser(String object, TimeZone timeZone, Locale locale, Date date) {
        int n2;
        this.pattern = object;
        this.timeZone = timeZone;
        this.locale = locale;
        object = Calendar.getInstance(timeZone, locale);
        if (date != null) {
            ((Calendar)object).setTime(date);
            n2 = ((Calendar)object).get(1);
        } else if (locale.equals(JAPANESE_IMPERIAL)) {
            n2 = 0;
        } else {
            ((Calendar)object).setTime(new Date());
            n2 = ((Calendar)object).get(1) - 80;
        }
        this.century = n2 / 100 * 100;
        this.startYear = n2 - this.century;
        this.init((Calendar)object);
    }

    private void init(Calendar calendar) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Strategy> arrayList = new ArrayList<Strategy>();
        Matcher matcher = formatPattern.matcher(this.pattern);
        if (!matcher.lookingAt()) {
            throw new IllegalArgumentException("Illegal pattern character '" + this.pattern.charAt(matcher.regionStart()) + "'");
        }
        this.currentFormatField = matcher.group();
        FastDateParser fastDateParser = this;
        Strategy strategy = fastDateParser.getStrategy(fastDateParser.currentFormatField, calendar);
        while (true) {
            Matcher matcher2 = matcher;
            matcher2.region(matcher2.end(), matcher.regionEnd());
            if (!matcher.lookingAt()) break;
            String string = matcher.group();
            this.nextStrategy = this.getStrategy(string, calendar);
            if (strategy.addRegex(this, stringBuilder)) {
                arrayList.add(strategy);
            }
            this.currentFormatField = string;
            strategy = this.nextStrategy;
        }
        this.nextStrategy = null;
        if (matcher.regionStart() != matcher.regionEnd()) {
            throw new IllegalArgumentException("Failed to parse \"" + this.pattern + "\" ; gave up at index " + matcher.regionStart());
        }
        if (strategy.addRegex(this, stringBuilder)) {
            arrayList.add(strategy);
        }
        this.currentFormatField = null;
        ArrayList<Strategy> arrayList2 = arrayList;
        this.strategies = arrayList2.toArray(new Strategy[arrayList2.size()]);
        this.parsePattern = Pattern.compile(stringBuilder.toString());
    }

    @Override
    public String getPattern() {
        return this.pattern;
    }

    @Override
    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    Pattern getParsePattern() {
        return this.parsePattern;
    }

    public boolean equals(Object object) {
        if (!(object instanceof FastDateParser)) {
            return false;
        }
        object = (FastDateParser)object;
        return this.pattern.equals(((FastDateParser)object).pattern) && this.timeZone.equals(((FastDateParser)object).timeZone) && this.locale.equals(((FastDateParser)object).locale);
    }

    public int hashCode() {
        return this.pattern.hashCode() + 13 * (this.timeZone.hashCode() + 13 * this.locale.hashCode());
    }

    public String toString() {
        return "FastDateParser[" + this.pattern + "," + this.locale + "," + this.timeZone.getID() + "]";
    }

    private void readObject(ObjectInputStream object) throws IOException, ClassNotFoundException {
        ((ObjectInputStream)object).defaultReadObject();
        object = Calendar.getInstance(this.timeZone, this.locale);
        this.init((Calendar)object);
    }

    @Override
    public Object parseObject(String string) throws ParseException {
        return this.parse(string);
    }

    @Override
    public Date parse(String string) throws ParseException {
        Date date = this.parse(string = string.length() == 19 ? string + ".000" : string, new ParsePosition(0));
        if (date == null) {
            if (this.locale.equals(JAPANESE_IMPERIAL)) {
                throw new ParseException("(The " + this.locale + " locale does not support dates before 1868 AD)\nUnparseable date: \"" + string + "\" does not match " + this.parsePattern.pattern(), 0);
            }
            throw new ParseException("Unparseable date: \"" + string + "\" does not match " + this.parsePattern.pattern(), 0);
        }
        return date;
    }

    @Override
    public Object parseObject(String string, ParsePosition parsePosition) {
        return this.parse(string, parsePosition);
    }

    @Override
    public Date parse(String object, ParsePosition parsePosition) {
        int n2 = parsePosition.getIndex();
        if (!((Matcher)(object = this.parsePattern.matcher(((String)object).substring(n2)))).lookingAt()) {
            return null;
        }
        Calendar calendar = Calendar.getInstance(this.timeZone, this.locale);
        calendar.clear();
        int n3 = 0;
        while (n3 < this.strategies.length) {
            Strategy strategy = this.strategies[n3++];
            strategy.setCalendar(this, calendar, ((Matcher)object).group(n3));
        }
        parsePosition.setIndex(n2 + ((Matcher)object).end());
        return calendar.getTime();
    }

    private static StringBuilder escapeRegex(StringBuilder stringBuilder, String string, boolean bl) {
        stringBuilder.append("\\Q");
        for (int i2 = 0; i2 < string.length(); ++i2) {
            int n2 = string.charAt(i2);
            switch (n2) {
                case 39: {
                    if (!bl) break;
                    if (++i2 == string.length()) {
                        return stringBuilder;
                    }
                    n2 = string.charAt(i2);
                    break;
                }
                case 92: {
                    if (++i2 == string.length()) break;
                    stringBuilder.append((char)n2);
                    n2 = string.charAt(i2);
                    if (n2 != 69) break;
                    stringBuilder.append("E\\\\E\\");
                    n2 = 81;
                }
            }
            stringBuilder.append((char)n2);
        }
        stringBuilder.append("\\E");
        return stringBuilder;
    }

    private static Map<String, Integer> getDisplayNames(int n2, Calendar calendar, Locale locale) {
        return calendar.getDisplayNames(n2, 0, locale);
    }

    private int adjustYear(int n2) {
        int n3 = this.century + n2;
        if (n2 >= this.startYear) {
            return n3;
        }
        return n3 + 100;
    }

    boolean isNextNumber() {
        return this.nextStrategy != null && this.nextStrategy.isNumber();
    }

    int getFieldWidth() {
        return this.currentFormatField.length();
    }

    private Strategy getStrategy(String string, Calendar calendar) {
        switch (string.charAt(0)) {
            case '\'': {
                if (string.length() > 2) {
                    return new CopyQuotedStrategy(string.substring(1, string.length() - 1));
                }
            }
            default: {
                return new CopyQuotedStrategy(string);
            }
            case 'D': {
                return DAY_OF_YEAR_STRATEGY;
            }
            case 'E': {
                return this.getLocaleSpecificStrategy(7, calendar);
            }
            case 'F': {
                return DAY_OF_WEEK_IN_MONTH_STRATEGY;
            }
            case 'G': {
                return this.getLocaleSpecificStrategy(0, calendar);
            }
            case 'H': {
                return HOUR_OF_DAY_STRATEGY;
            }
            case 'K': {
                return HOUR_STRATEGY;
            }
            case 'M': {
                if (string.length() >= 3) {
                    return this.getLocaleSpecificStrategy(2, calendar);
                }
                return NUMBER_MONTH_STRATEGY;
            }
            case 'S': {
                return MILLISECOND_STRATEGY;
            }
            case 'W': {
                return WEEK_OF_MONTH_STRATEGY;
            }
            case 'a': {
                return this.getLocaleSpecificStrategy(9, calendar);
            }
            case 'd': {
                return DAY_OF_MONTH_STRATEGY;
            }
            case 'h': {
                return HOUR12_STRATEGY;
            }
            case 'k': {
                return HOUR24_OF_DAY_STRATEGY;
            }
            case 'm': {
                return MINUTE_STRATEGY;
            }
            case 's': {
                return SECOND_STRATEGY;
            }
            case 'w': {
                return WEEK_OF_YEAR_STRATEGY;
            }
            case 'y': {
                if (string.length() > 2) {
                    return LITERAL_YEAR_STRATEGY;
                }
                return ABBREVIATED_YEAR_STRATEGY;
            }
            case 'X': {
                return ISO8601TimeZoneStrategy.getStrategy(string.length());
            }
            case 'Z': {
                if (!string.equals("ZZ")) break;
                return ISO_8601_STRATEGY;
            }
            case 'z': 
        }
        return this.getLocaleSpecificStrategy(15, calendar);
    }

    private static ConcurrentMap<Locale, Strategy> getCache(int n2) {
        ConcurrentMap<Locale, Strategy>[] concurrentMapArray = caches;
        synchronized (caches) {
            if (caches[n2] == null) {
                FastDateParser.caches[n2] = new ConcurrentHashMap<Locale, Strategy>(3);
            }
            // ** MonitorExit[var1_2] (shouldn't be in output)
            return caches[n2];
        }
    }

    private Strategy getLocaleSpecificStrategy(int n2, Calendar calendar) {
        Strategy strategy;
        ConcurrentMap<Locale, Strategy> concurrentMap = FastDateParser.getCache(n2);
        Strategy strategy2 = (Strategy)concurrentMap.get(this.locale);
        if (strategy2 == null && (strategy = concurrentMap.putIfAbsent(this.locale, strategy2 = n2 == 15 ? new TimeZoneStrategy(this.locale) : new CaseInsensitiveTextStrategy(n2, calendar, this.locale))) != null) {
            return strategy;
        }
        return strategy2;
    }

    static class ISO8601TimeZoneStrategy
    extends Strategy {
        private final String pattern;
        private static final Strategy ISO_8601_1_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}))");
        private static final Strategy ISO_8601_2_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}\\d{2}))");
        private static final Strategy ISO_8601_3_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::)\\d{2}))");

        ISO8601TimeZoneStrategy(String string) {
            this.pattern = string;
        }

        @Override
        boolean addRegex(FastDateParser fastDateParser, StringBuilder stringBuilder) {
            stringBuilder.append(this.pattern);
            return true;
        }

        @Override
        void setCalendar(FastDateParser fastDateParser, Calendar calendar, String string) {
            if (string.equals("Z")) {
                calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                return;
            }
            calendar.setTimeZone(TimeZone.getTimeZone("GMT" + string));
        }

        static Strategy getStrategy(int n2) {
            switch (n2) {
                case 1: {
                    return ISO_8601_1_STRATEGY;
                }
                case 2: {
                    return ISO_8601_2_STRATEGY;
                }
                case 3: {
                    return ISO_8601_3_STRATEGY;
                }
            }
            throw new IllegalArgumentException("invalid number of X");
        }
    }

    static class TimeZoneStrategy
    extends Strategy {
        private final String validTimeZoneChars;
        private final SortedMap<String, TimeZone> tzNames = new TreeMap<String, TimeZone>(String.CASE_INSENSITIVE_ORDER);
        private static final int ID = 0;
        private static final int LONG_STD = 1;
        private static final int SHORT_STD = 2;
        private static final int LONG_DST = 3;
        private static final int SHORT_DST = 4;

        TimeZoneStrategy(Locale object) {
            String[][] stringArray = DateFormatSymbols.getInstance((Locale)object).getZoneStrings();
            object = stringArray;
            object = stringArray;
            int n2 = stringArray.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                String[] stringArray2 = object[i2];
                if (stringArray2[0].startsWith("GMT")) continue;
                TimeZone timeZone = TimeZone.getTimeZone(stringArray2[0]);
                if (!this.tzNames.containsKey(stringArray2[1])) {
                    this.tzNames.put(stringArray2[1], timeZone);
                }
                if (!this.tzNames.containsKey(stringArray2[2])) {
                    this.tzNames.put(stringArray2[2], timeZone);
                }
                if (!timeZone.useDaylightTime()) continue;
                if (!this.tzNames.containsKey(stringArray2[3])) {
                    this.tzNames.put(stringArray2[3], timeZone);
                }
                if (this.tzNames.containsKey(stringArray2[4])) continue;
                this.tzNames.put(stringArray2[4], timeZone);
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("(GMT[+-]\\d{1,2}:\\d{2}|");
            ((StringBuilder)object).append("[+-]\\d{4}|");
            for (String string : this.tzNames.keySet()) {
                FastDateParser.escapeRegex((StringBuilder)object, string, false).append('|');
            }
            Object object2 = object;
            ((StringBuilder)object2).setCharAt(((StringBuilder)object2).length() - 1, ')');
            this.validTimeZoneChars = ((StringBuilder)object).toString();
        }

        @Override
        boolean addRegex(FastDateParser fastDateParser, StringBuilder stringBuilder) {
            stringBuilder.append(this.validTimeZoneChars);
            return true;
        }

        @Override
        void setCalendar(FastDateParser serializable, Calendar calendar, String string) {
            if (string.charAt(0) == '+' || string.charAt(0) == '-') {
                serializable = TimeZone.getTimeZone("GMT" + string);
            } else if (string.startsWith("GMT")) {
                serializable = TimeZone.getTimeZone(string);
            } else {
                serializable = (TimeZone)this.tzNames.get(string);
                if (serializable == null) {
                    throw new IllegalArgumentException(string + " is not a supported timezone name");
                }
            }
            calendar.setTimeZone((TimeZone)serializable);
        }
    }

    static class NumberStrategy
    extends Strategy {
        private final int field;

        NumberStrategy(int n2) {
            this.field = n2;
        }

        @Override
        boolean isNumber() {
            return true;
        }

        @Override
        boolean addRegex(FastDateParser fastDateParser, StringBuilder stringBuilder) {
            if (fastDateParser.isNextNumber()) {
                stringBuilder.append("(\\p{Nd}{").append(fastDateParser.getFieldWidth()).append("}+)");
            } else {
                stringBuilder.append("(\\p{Nd}++)");
            }
            return true;
        }

        @Override
        void setCalendar(FastDateParser fastDateParser, Calendar calendar, String string) {
            calendar.set(this.field, this.modify(Integer.parseInt(string)));
        }

        int modify(int n2) {
            return n2;
        }
    }

    static class CaseInsensitiveTextStrategy
    extends Strategy {
        private final int field;
        private final Locale locale;
        private final Map<String, Integer> lKeyValues;

        /*
         * WARNING - void declaration
         */
        CaseInsensitiveTextStrategy(int n2, Calendar object2, Locale locale) {
            void var3_6;
            this.field = n2;
            this.locale = var3_6;
            Map map = FastDateParser.getDisplayNames(n2, (Calendar)object2, (Locale)var3_6);
            this.lKeyValues = new HashMap<String, Integer>();
            for (Map.Entry entry : map.entrySet()) {
                this.lKeyValues.put(((String)entry.getKey()).toLowerCase((Locale)var3_6), (Integer)entry.getValue());
            }
        }

        @Override
        boolean addRegex(FastDateParser object, StringBuilder stringBuilder) {
            stringBuilder.append("((?iu)");
            for (String string : this.lKeyValues.keySet()) {
                FastDateParser.escapeRegex(stringBuilder, string, false).append('|');
            }
            StringBuilder stringBuilder2 = stringBuilder;
            stringBuilder2.setCharAt(stringBuilder2.length() - 1, ')');
            return true;
        }

        @Override
        void setCalendar(FastDateParser serializable, Calendar object, String string2) {
            serializable = this.lKeyValues.get(string2.toLowerCase(this.locale));
            if (serializable == null) {
                serializable = new StringBuilder(string2);
                ((StringBuilder)serializable).append(" not in (");
                for (String string2 : this.lKeyValues.keySet()) {
                    ((StringBuilder)serializable).append(string2).append(' ');
                }
                Serializable serializable2 = serializable;
                ((StringBuilder)serializable2).setCharAt(((StringBuilder)serializable2).length() - 1, ')');
                throw new IllegalArgumentException(((StringBuilder)serializable).toString());
            }
            ((Calendar)object).set(this.field, (Integer)serializable);
        }
    }

    static class CopyQuotedStrategy
    extends Strategy {
        private final String formatField;

        CopyQuotedStrategy(String string) {
            this.formatField = string;
        }

        @Override
        boolean isNumber() {
            char c2 = this.formatField.charAt(0);
            if (c2 == '\'') {
                c2 = this.formatField.charAt(1);
            }
            return Character.isDigit(c2);
        }

        @Override
        boolean addRegex(FastDateParser fastDateParser, StringBuilder stringBuilder) {
            FastDateParser.escapeRegex(stringBuilder, this.formatField, true);
            return false;
        }
    }

    static abstract class Strategy {
        private Strategy() {
        }

        boolean isNumber() {
            return false;
        }

        void setCalendar(FastDateParser fastDateParser, Calendar calendar, String string) {
        }

        abstract boolean addRegex(FastDateParser var1, StringBuilder var2);
    }
}

