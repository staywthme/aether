/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.Util;
import okhttp3.internal.http.DatesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000+\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\n\u001a\u000e\u0010\f\u001a\u0004\u0018\u00010\r*\u00020\u0005H\u0000\u001a\f\u0010\u000e\u001a\u00020\u0005*\u00020\rH\u0000\"\u0018\u0010\u0000\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0003\"\u0016\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00050\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006\"\u000e\u0010\u0007\u001a\u00020\bX\u0080T\u00a2\u0006\u0002\n\u0000\"\u0010\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u000b\u00a8\u0006\u000f"}, d2={"BROWSER_COMPATIBLE_DATE_FORMATS", "", "Ljava/text/DateFormat;", "[Ljava/text/DateFormat;", "BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS", "", "[Ljava/lang/String;", "MAX_DATE", "", "STANDARD_DATE_FORMAT", "okhttp3/internal/http/DatesKt$STANDARD_DATE_FORMAT$1", "Lokhttp3/internal/http/DatesKt$STANDARD_DATE_FORMAT$1;", "toHttpDateOrNull", "Ljava/util/Date;", "toHttpDateString", "okhttp"})
public final class DatesKt {
    public static final long MAX_DATE = 253402300799999L;
    @NotNull
    private static final STANDARD_DATE_FORMAT.1 STANDARD_DATE_FORMAT = new ThreadLocal<DateFormat>(){

        @NotNull
        protected final DateFormat initialValue() {
            SimpleDateFormat simpleDateFormat;
            SimpleDateFormat simpleDateFormat2 = simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            simpleDateFormat.setLenient(false);
            simpleDateFormat2.setTimeZone(Util.UTC);
            return simpleDateFormat;
        }
    };
    @NotNull
    private static final String[] BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS;
    @NotNull
    private static final DateFormat[] BROWSER_COMPATIBLE_DATE_FORMATS;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Nullable
    public static final Date toHttpDateOrNull(@NotNull String object) {
        Intrinsics.checkNotNullParameter(object, "");
        if (((CharSequence)object).length() == 0) {
            return null;
        }
        boolean bl = false;
        if (bl) {
            return null;
        }
        ParsePosition parsePosition = new ParsePosition(0);
        Cloneable cloneable = ((DateFormat)STANDARD_DATE_FORMAT.get()).parse((String)object, parsePosition);
        if (parsePosition.getIndex() == ((String)object).length()) {
            return cloneable;
        }
        String[] stringArray = BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS;
        synchronized (BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS) {
            int n2 = 0;
            int n3 = BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS.length;
            while (n2 < n3) {
                cloneable = BROWSER_COMPATIBLE_DATE_FORMATS[n2];
                if (cloneable == null) {
                    cloneable = new SimpleDateFormat(BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS[n2], Locale.US);
                    SimpleDateFormat simpleDateFormat = cloneable;
                    ((DateFormat)cloneable).setTimeZone(Util.UTC);
                    cloneable = (DateFormat)cloneable;
                    DatesKt.BROWSER_COMPATIBLE_DATE_FORMATS[n2] = cloneable;
                }
                parsePosition.setIndex(0);
                cloneable = ((DateFormat)cloneable).parse((String)object, parsePosition);
                if (parsePosition.getIndex() != 0) {
                    return cloneable;
                }
                ++n2;
            }
            return null;
        }
    }

    @NotNull
    public static final String toHttpDateString(@NotNull Date date) {
        Intrinsics.checkNotNullParameter(date, "");
        String string = ((DateFormat)STANDARD_DATE_FORMAT.get()).format(date);
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    static {
        String[] stringArray = new String[15];
        String[] stringArray2 = stringArray;
        stringArray[0] = "EEE, dd MMM yyyy HH:mm:ss zzz";
        stringArray2[1] = "EEEE, dd-MMM-yy HH:mm:ss zzz";
        stringArray2[2] = "EEE MMM d HH:mm:ss yyyy";
        stringArray2[3] = "EEE, dd-MMM-yyyy HH:mm:ss z";
        stringArray2[4] = "EEE, dd-MMM-yyyy HH-mm-ss z";
        stringArray2[5] = "EEE, dd MMM yy HH:mm:ss z";
        stringArray2[6] = "EEE dd-MMM-yyyy HH:mm:ss z";
        stringArray2[7] = "EEE dd MMM yyyy HH:mm:ss z";
        stringArray2[8] = "EEE dd-MMM-yyyy HH-mm-ss z";
        stringArray2[9] = "EEE dd-MMM-yy HH:mm:ss z";
        stringArray2[10] = "EEE dd MMM yy HH:mm:ss z";
        stringArray2[11] = "EEE,dd-MMM-yy HH:mm:ss z";
        stringArray2[12] = "EEE,dd-MMM-yyyy HH:mm:ss z";
        stringArray2[13] = "EEE, dd-MM-yyyy HH:mm:ss z";
        stringArray2[14] = "EEE MMM d yyyy HH:mm:ss z";
        BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS = stringArray2;
        BROWSER_COMPATIBLE_DATE_FORMATS = new DateFormat[stringArray2.length];
    }
}

