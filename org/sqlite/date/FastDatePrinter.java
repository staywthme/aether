/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.date;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.sqlite.date.DatePrinter;

public class FastDatePrinter
implements Serializable,
DatePrinter {
    private static final long serialVersionUID = 1L;
    public static final int FULL = 0;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int SHORT = 3;
    private final String mPattern;
    private final TimeZone mTimeZone;
    private final Locale mLocale;
    private transient Rule[] mRules;
    private transient int mMaxLengthEstimate;
    private static final ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache = new ConcurrentHashMap<TimeZoneDisplayKey, String>(7);

    protected FastDatePrinter(String string, TimeZone timeZone, Locale locale) {
        this.mPattern = string;
        this.mTimeZone = timeZone;
        this.mLocale = locale;
        this.init();
    }

    private void init() {
        List<Rule> list2;
        List<Rule> list3 = list2 = this.parsePattern();
        this.mRules = list3.toArray(new Rule[list3.size()]);
        int n2 = 0;
        int n3 = this.mRules.length;
        while (--n3 >= 0) {
            n2 += this.mRules[n3].estimateLength();
        }
        this.mMaxLengthEstimate = n2;
    }

    protected List<Rule> parsePattern() {
        String[] stringArray = new DateFormatSymbols(this.mLocale);
        ArrayList<Rule> arrayList = new ArrayList<Rule>();
        String[] stringArray2 = stringArray.getEras();
        String[] stringArray3 = stringArray.getMonths();
        String[] stringArray4 = stringArray.getShortMonths();
        String[] stringArray5 = stringArray.getWeekdays();
        String[] stringArray6 = stringArray.getShortWeekdays();
        stringArray = stringArray.getAmPmStrings();
        int n2 = this.mPattern.length();
        int[] nArray = new int[1];
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray[0] = i2;
            FastDatePrinter fastDatePrinter = this;
            Object object = fastDatePrinter.parseToken(fastDatePrinter.mPattern, nArray);
            i2 = nArray[0];
            int n3 = ((String)object).length();
            if (n3 == 0) break;
            char c2 = ((String)object).charAt(0);
            switch (c2) {
                case 'G': {
                    object = new TextField(0, stringArray2);
                    break;
                }
                case 'y': {
                    if (n3 == 2) {
                        object = TwoDigitYearField.INSTANCE;
                        break;
                    }
                    object = this.selectNumberRule(1, n3 < 4 ? 4 : n3);
                    break;
                }
                case 'M': {
                    if (n3 >= 4) {
                        object = new TextField(2, stringArray3);
                        break;
                    }
                    if (n3 == 3) {
                        object = new TextField(2, stringArray4);
                        break;
                    }
                    if (n3 == 2) {
                        object = TwoDigitMonthField.INSTANCE;
                        break;
                    }
                    object = UnpaddedMonthField.INSTANCE;
                    break;
                }
                case 'd': {
                    object = this.selectNumberRule(5, n3);
                    break;
                }
                case 'h': {
                    object = new TwelveHourField(this.selectNumberRule(10, n3));
                    break;
                }
                case 'H': {
                    object = this.selectNumberRule(11, n3);
                    break;
                }
                case 'm': {
                    object = this.selectNumberRule(12, n3);
                    break;
                }
                case 's': {
                    object = this.selectNumberRule(13, n3);
                    break;
                }
                case 'S': {
                    object = this.selectNumberRule(14, n3);
                    break;
                }
                case 'E': {
                    object = new TextField(7, n3 < 4 ? stringArray6 : stringArray5);
                    break;
                }
                case 'D': {
                    object = this.selectNumberRule(6, n3);
                    break;
                }
                case 'F': {
                    object = this.selectNumberRule(8, n3);
                    break;
                }
                case 'w': {
                    object = this.selectNumberRule(3, n3);
                    break;
                }
                case 'W': {
                    object = this.selectNumberRule(4, n3);
                    break;
                }
                case 'a': {
                    object = new TextField(9, stringArray);
                    break;
                }
                case 'k': {
                    object = new TwentyFourHourField(this.selectNumberRule(11, n3));
                    break;
                }
                case 'K': {
                    object = this.selectNumberRule(10, n3);
                    break;
                }
                case 'X': {
                    object = Iso8601_Rule.getRule(n3);
                    break;
                }
                case 'z': {
                    if (n3 >= 4) {
                        object = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 1);
                        break;
                    }
                    object = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 0);
                    break;
                }
                case 'Z': {
                    if (n3 == 1) {
                        object = TimeZoneNumberRule.INSTANCE_NO_COLON;
                        break;
                    }
                    if (n3 == 2) {
                        object = TimeZoneNumberRule.INSTANCE_ISO_8601;
                        break;
                    }
                    object = TimeZoneNumberRule.INSTANCE_COLON;
                    break;
                }
                case '\'': {
                    object = ((String)object).substring(1);
                    if (((String)object).length() == 1) {
                        object = new CharacterLiteral(((String)object).charAt(0));
                        break;
                    }
                    object = new StringLiteral((String)object);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Illegal pattern component: " + (String)object);
                }
            }
            arrayList.add((Rule)object);
        }
        return arrayList;
    }

    protected String parseToken(String string, int[] nArray) {
        int n2;
        StringBuilder stringBuilder = new StringBuilder();
        int n3 = string.length();
        char c2 = string.charAt(n2);
        if (c2 >= 'A' && c2 <= 'Z' || c2 >= 'a' && c2 <= 'z') {
            char c3;
            stringBuilder.append(c2);
            while (n2 + 1 < n3 && (c3 = string.charAt(n2 + 1)) == c2) {
                stringBuilder.append(c2);
                ++n2;
            }
        } else {
            stringBuilder.append('\'');
            boolean bl = false;
            for (n2 = nArray[0]; n2 < n3; ++n2) {
                c2 = string.charAt(n2);
                if (c2 == '\'') {
                    if (n2 + 1 < n3 && string.charAt(n2 + 1) == '\'') {
                        ++n2;
                        stringBuilder.append(c2);
                        continue;
                    }
                    bl = !bl;
                    continue;
                }
                if (bl || (c2 < 'A' || c2 > 'Z') && (c2 < 'a' || c2 > 'z')) {
                    stringBuilder.append(c2);
                    continue;
                }
                break;
            }
        }
        nArray[0] = --n2;
        return stringBuilder.toString();
    }

    protected NumberRule selectNumberRule(int n2, int n3) {
        switch (n3) {
            case 1: {
                return new UnpaddedNumberField(n2);
            }
            case 2: {
                return new TwoDigitNumberField(n2);
            }
        }
        return new PaddedNumberField(n2, n3);
    }

    @Override
    public StringBuffer format(Object object, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        if (object instanceof Date) {
            return this.format((Date)object, stringBuffer);
        }
        if (object instanceof Calendar) {
            return this.format((Calendar)object, stringBuffer);
        }
        if (object instanceof Long) {
            return this.format((Long)object, stringBuffer);
        }
        throw new IllegalArgumentException("Unknown class: " + (object == null ? "<null>" : object.getClass().getName()));
    }

    @Override
    public String format(long l2) {
        GregorianCalendar gregorianCalendar = this.newCalendar();
        gregorianCalendar.setTimeInMillis(l2);
        return this.applyRulesToString(gregorianCalendar);
    }

    private String applyRulesToString(Calendar calendar) {
        return this.applyRules(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }

    private GregorianCalendar newCalendar() {
        return new GregorianCalendar(this.mTimeZone, this.mLocale);
    }

    @Override
    public String format(Date date) {
        GregorianCalendar gregorianCalendar = this.newCalendar();
        gregorianCalendar.setTime(date);
        return this.applyRulesToString(gregorianCalendar);
    }

    @Override
    public String format(Calendar calendar) {
        return this.format(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }

    @Override
    public StringBuffer format(long l2, StringBuffer stringBuffer) {
        return this.format(new Date(l2), stringBuffer);
    }

    @Override
    public StringBuffer format(Date date, StringBuffer stringBuffer) {
        GregorianCalendar gregorianCalendar = this.newCalendar();
        gregorianCalendar.setTime(date);
        return this.applyRules(gregorianCalendar, stringBuffer);
    }

    @Override
    public StringBuffer format(Calendar calendar, StringBuffer stringBuffer) {
        return this.applyRules(calendar, stringBuffer);
    }

    protected StringBuffer applyRules(Calendar calendar, StringBuffer stringBuffer) {
        Rule[] ruleArray = this.mRules;
        int n2 = this.mRules.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Rule rule = ruleArray[i2];
            rule.appendTo(stringBuffer, calendar);
        }
        return stringBuffer;
    }

    @Override
    public String getPattern() {
        return this.mPattern;
    }

    @Override
    public TimeZone getTimeZone() {
        return this.mTimeZone;
    }

    @Override
    public Locale getLocale() {
        return this.mLocale;
    }

    public int getMaxLengthEstimate() {
        return this.mMaxLengthEstimate;
    }

    public boolean equals(Object object) {
        if (!(object instanceof FastDatePrinter)) {
            return false;
        }
        object = (FastDatePrinter)object;
        return this.mPattern.equals(((FastDatePrinter)object).mPattern) && this.mTimeZone.equals(((FastDatePrinter)object).mTimeZone) && this.mLocale.equals(((FastDatePrinter)object).mLocale);
    }

    public int hashCode() {
        return this.mPattern.hashCode() + 13 * (this.mTimeZone.hashCode() + 13 * this.mLocale.hashCode());
    }

    public String toString() {
        return "FastDatePrinter[" + this.mPattern + "," + this.mLocale + "," + this.mTimeZone.getID() + "]";
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.init();
    }

    private static void appendDigits(StringBuffer stringBuffer, int n2) {
        stringBuffer.append((char)(n2 / 10 + 48));
        stringBuffer.append((char)(n2 % 10 + 48));
    }

    static String getTimeZoneDisplay(TimeZone object, boolean bl, int n2, Locale locale) {
        TimeZoneDisplayKey timeZoneDisplayKey = new TimeZoneDisplayKey((TimeZone)object, bl, n2, locale);
        Object object2 = (String)cTimeZoneDisplayCache.get(timeZoneDisplayKey);
        if (object2 == null && (object = cTimeZoneDisplayCache.putIfAbsent(timeZoneDisplayKey, (String)(object2 = ((TimeZone)object).getDisplayName(bl, n2, locale)))) != null) {
            object2 = object;
        }
        return object2;
    }

    static class TimeZoneDisplayKey {
        private final TimeZone mTimeZone;
        private final int mStyle;
        private final Locale mLocale;

        TimeZoneDisplayKey(TimeZone timeZone, boolean bl, int n2, Locale locale) {
            this.mTimeZone = timeZone;
            this.mStyle = bl ? n2 | Integer.MIN_VALUE : n2;
            this.mLocale = locale;
        }

        public int hashCode() {
            return (this.mStyle * 31 + this.mLocale.hashCode()) * 31 + this.mTimeZone.hashCode();
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object instanceof TimeZoneDisplayKey) {
                object = (TimeZoneDisplayKey)object;
                return this.mTimeZone.equals(((TimeZoneDisplayKey)object).mTimeZone) && this.mStyle == ((TimeZoneDisplayKey)object).mStyle && this.mLocale.equals(((TimeZoneDisplayKey)object).mLocale);
            }
            return false;
        }
    }

    static class Iso8601_Rule
    implements Rule {
        static final Iso8601_Rule ISO8601_HOURS = new Iso8601_Rule(3);
        static final Iso8601_Rule ISO8601_HOURS_MINUTES = new Iso8601_Rule(5);
        static final Iso8601_Rule ISO8601_HOURS_COLON_MINUTES = new Iso8601_Rule(6);
        final int length;

        static Iso8601_Rule getRule(int n2) {
            switch (n2) {
                case 1: {
                    return ISO8601_HOURS;
                }
                case 2: {
                    return ISO8601_HOURS_MINUTES;
                }
                case 3: {
                    return ISO8601_HOURS_COLON_MINUTES;
                }
            }
            throw new IllegalArgumentException("invalid number of X");
        }

        Iso8601_Rule(int n2) {
            this.length = n2;
        }

        @Override
        public int estimateLength() {
            return this.length;
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            int n2 = calendar.get(15);
            if (n2 == 0) {
                stringBuffer.append("Z");
                return;
            }
            int n3 = n2 + calendar.get(16);
            if (n3 < 0) {
                stringBuffer.append('-');
                n3 = -n3;
            } else {
                stringBuffer.append('+');
            }
            n2 = n3 / 3600000;
            FastDatePrinter.appendDigits(stringBuffer, n2);
            if (this.length < 5) {
                return;
            }
            if (this.length == 6) {
                stringBuffer.append(':');
            }
            n3 = n3 / 60000 - n2 * 60;
            FastDatePrinter.appendDigits(stringBuffer, n3);
        }
    }

    static class TimeZoneNumberRule
    implements Rule {
        static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true, false);
        static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false, false);
        static final TimeZoneNumberRule INSTANCE_ISO_8601 = new TimeZoneNumberRule(true, true);
        final boolean mColon;
        final boolean mISO8601;

        TimeZoneNumberRule(boolean bl, boolean bl2) {
            this.mColon = bl;
            this.mISO8601 = bl2;
        }

        @Override
        public int estimateLength() {
            return 5;
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            if (this.mISO8601 && calendar.getTimeZone().getID().equals("UTC")) {
                stringBuffer.append("Z");
                return;
            }
            int n2 = calendar.get(15) + calendar.get(16);
            if (n2 < 0) {
                stringBuffer.append('-');
                n2 = -n2;
            } else {
                stringBuffer.append('+');
            }
            int n3 = n2 / 3600000;
            FastDatePrinter.appendDigits(stringBuffer, n3);
            if (this.mColon) {
                stringBuffer.append(':');
            }
            n2 = n2 / 60000 - n3 * 60;
            FastDatePrinter.appendDigits(stringBuffer, n2);
        }
    }

    static class TimeZoneNameRule
    implements Rule {
        private final Locale mLocale;
        private final int mStyle;
        private final String mStandard;
        private final String mDaylight;

        TimeZoneNameRule(TimeZone timeZone, Locale locale, int n2) {
            this.mLocale = locale;
            this.mStyle = n2;
            this.mStandard = FastDatePrinter.getTimeZoneDisplay(timeZone, false, n2, locale);
            this.mDaylight = FastDatePrinter.getTimeZoneDisplay(timeZone, true, n2, locale);
        }

        @Override
        public int estimateLength() {
            return Math.max(this.mStandard.length(), this.mDaylight.length());
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            TimeZone timeZone = calendar.getTimeZone();
            if (calendar.get(16) != 0) {
                stringBuffer.append(FastDatePrinter.getTimeZoneDisplay(timeZone, true, this.mStyle, this.mLocale));
                return;
            }
            stringBuffer.append(FastDatePrinter.getTimeZoneDisplay(timeZone, false, this.mStyle, this.mLocale));
        }
    }

    static class TwentyFourHourField
    implements NumberRule {
        private final NumberRule mRule;

        TwentyFourHourField(NumberRule numberRule) {
            this.mRule = numberRule;
        }

        @Override
        public int estimateLength() {
            return this.mRule.estimateLength();
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            int n2 = calendar.get(11);
            if (n2 == 0) {
                n2 = calendar.getMaximum(11) + 1;
            }
            this.mRule.appendTo(stringBuffer, n2);
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, int n2) {
            this.mRule.appendTo(stringBuffer, n2);
        }
    }

    static class TwelveHourField
    implements NumberRule {
        private final NumberRule mRule;

        TwelveHourField(NumberRule numberRule) {
            this.mRule = numberRule;
        }

        @Override
        public int estimateLength() {
            return this.mRule.estimateLength();
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            int n2 = calendar.get(10);
            if (n2 == 0) {
                n2 = calendar.getLeastMaximum(10) + 1;
            }
            this.mRule.appendTo(stringBuffer, n2);
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, int n2) {
            this.mRule.appendTo(stringBuffer, n2);
        }
    }

    static class TwoDigitMonthField
    implements NumberRule {
        static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();

        TwoDigitMonthField() {
        }

        @Override
        public int estimateLength() {
            return 2;
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            this.appendTo(stringBuffer, calendar.get(2) + 1);
        }

        @Override
        public final void appendTo(StringBuffer stringBuffer, int n2) {
            FastDatePrinter.appendDigits(stringBuffer, n2);
        }
    }

    static class TwoDigitYearField
    implements NumberRule {
        static final TwoDigitYearField INSTANCE = new TwoDigitYearField();

        TwoDigitYearField() {
        }

        @Override
        public int estimateLength() {
            return 2;
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            this.appendTo(stringBuffer, calendar.get(1) % 100);
        }

        @Override
        public final void appendTo(StringBuffer stringBuffer, int n2) {
            FastDatePrinter.appendDigits(stringBuffer, n2);
        }
    }

    static class TwoDigitNumberField
    implements NumberRule {
        private final int mField;

        TwoDigitNumberField(int n2) {
            this.mField = n2;
        }

        @Override
        public int estimateLength() {
            return 2;
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            this.appendTo(stringBuffer, calendar.get(this.mField));
        }

        @Override
        public final void appendTo(StringBuffer stringBuffer, int n2) {
            if (n2 < 100) {
                FastDatePrinter.appendDigits(stringBuffer, n2);
                return;
            }
            stringBuffer.append(n2);
        }
    }

    static class PaddedNumberField
    implements NumberRule {
        private final int mField;
        private final int mSize;

        PaddedNumberField(int n2, int n3) {
            if (n3 < 3) {
                throw new IllegalArgumentException();
            }
            this.mField = n2;
            this.mSize = n3;
        }

        @Override
        public int estimateLength() {
            return this.mSize;
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            this.appendTo(stringBuffer, calendar.get(this.mField));
        }

        @Override
        public final void appendTo(StringBuffer stringBuffer, int n2) {
            int n3;
            for (n3 = 0; n3 < this.mSize; ++n3) {
                stringBuffer.append('0');
            }
            n3 = stringBuffer.length();
            while (n2 > 0) {
                stringBuffer.setCharAt(--n3, (char)(48 + n2 % 10));
                n2 /= 10;
            }
        }
    }

    static class UnpaddedMonthField
    implements NumberRule {
        static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();

        UnpaddedMonthField() {
        }

        @Override
        public int estimateLength() {
            return 2;
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            this.appendTo(stringBuffer, calendar.get(2) + 1);
        }

        @Override
        public final void appendTo(StringBuffer stringBuffer, int n2) {
            if (n2 < 10) {
                stringBuffer.append((char)(n2 + 48));
                return;
            }
            FastDatePrinter.appendDigits(stringBuffer, n2);
        }
    }

    static class UnpaddedNumberField
    implements NumberRule {
        private final int mField;

        UnpaddedNumberField(int n2) {
            this.mField = n2;
        }

        @Override
        public int estimateLength() {
            return 4;
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            this.appendTo(stringBuffer, calendar.get(this.mField));
        }

        @Override
        public final void appendTo(StringBuffer stringBuffer, int n2) {
            if (n2 < 10) {
                stringBuffer.append((char)(n2 + 48));
                return;
            }
            if (n2 < 100) {
                FastDatePrinter.appendDigits(stringBuffer, n2);
                return;
            }
            stringBuffer.append(n2);
        }
    }

    static class TextField
    implements Rule {
        private final int mField;
        private final String[] mValues;

        TextField(int n2, String[] stringArray) {
            this.mField = n2;
            this.mValues = stringArray;
        }

        @Override
        public int estimateLength() {
            int n2 = 0;
            int n3 = this.mValues.length;
            while (--n3 >= 0) {
                int n4 = this.mValues[n3].length();
                if (n4 <= n2) continue;
                n2 = n4;
            }
            return n2;
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            stringBuffer.append(this.mValues[calendar.get(this.mField)]);
        }
    }

    static class StringLiteral
    implements Rule {
        private final String mValue;

        StringLiteral(String string) {
            this.mValue = string;
        }

        @Override
        public int estimateLength() {
            return this.mValue.length();
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            stringBuffer.append(this.mValue);
        }
    }

    static class CharacterLiteral
    implements Rule {
        private final char mValue;

        CharacterLiteral(char c2) {
            this.mValue = c2;
        }

        @Override
        public int estimateLength() {
            return 1;
        }

        @Override
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            stringBuffer.append(this.mValue);
        }
    }

    static interface NumberRule
    extends Rule {
        public void appendTo(StringBuffer var1, int var2);
    }

    static interface Rule {
        public int estimateLength();

        public void appendTo(StringBuffer var1, Calendar var2);
    }
}

