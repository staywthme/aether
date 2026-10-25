/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.WasExperimental;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.collections.IntIterator;
import kotlin.internal.InlineOnly;
import kotlin.internal.LowPriorityInOverloadResolution;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import kotlin.text.StringsKt__StringNumberConversionsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000~\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0019\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\r\n\u0002\b\n\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\n\n\u0002\u0010\f\n\u0002\b\u0011\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\tH\u0087\b\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\u000bH\u0087\b\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\u0019\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0087\b\u001a!\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0087\b\u001a)\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u000e\u001a\u00020\u000fH\u0087\b\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0014H\u0087\b\u001a!\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0087\b\u001a!\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0087\b\u001a\f\u0010\u0017\u001a\u00020\u0002*\u00020\u0002H\u0007\u001a\u0014\u0010\u0017\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0007\u001a\u0015\u0010\u001a\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0011H\u0087\b\u001a\u0015\u0010\u001c\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0011H\u0087\b\u001a\u001d\u0010\u001d\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u0011H\u0087\b\u001a\u001c\u0010 \u001a\u00020\u0011*\u00020\u00022\u0006\u0010!\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a\f\u0010$\u001a\u00020\u0002*\u00020\u0014H\u0007\u001a \u0010$\u001a\u00020\u0002*\u00020\u00142\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u0011H\u0007\u001a\u0019\u0010&\u001a\u00020#*\u0004\u0018\u00010'2\b\u0010!\u001a\u0004\u0018\u00010'H\u0087\u0004\u001a \u0010&\u001a\u00020#*\u0004\u0018\u00010'2\b\u0010!\u001a\u0004\u0018\u00010'2\u0006\u0010\"\u001a\u00020#H\u0007\u001a\u0015\u0010&\u001a\u00020#*\u00020\u00022\u0006\u0010\n\u001a\u00020\tH\u0087\b\u001a\u0015\u0010&\u001a\u00020#*\u00020\u00022\u0006\u0010(\u001a\u00020'H\u0087\b\u001a\f\u0010)\u001a\u00020\u0002*\u00020\u0002H\u0007\u001a\u0014\u0010)\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0007\u001a\f\u0010*\u001a\u00020\u0002*\u00020\rH\u0007\u001a*\u0010*\u001a\u00020\u0002*\u00020\r2\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u00112\b\b\u0002\u0010+\u001a\u00020#H\u0007\u001a\f\u0010,\u001a\u00020\r*\u00020\u0002H\u0007\u001a*\u0010,\u001a\u00020\r*\u00020\u00022\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u00112\b\b\u0002\u0010+\u001a\u00020#H\u0007\u001a\u001c\u0010-\u001a\u00020#*\u00020\u00022\u0006\u0010.\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a \u0010/\u001a\u00020#*\u0004\u0018\u00010\u00022\b\u0010!\u001a\u0004\u0018\u00010\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a2\u00100\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u00192\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0002\u00104\u001a6\u00100\u001a\u00020\u0002*\u00020\u00022\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0004\b5\u00104\u001a*\u00100\u001a\u00020\u0002*\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0002\u00106\u001a:\u00100\u001a\u00020\u0002*\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u00100\u001a\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0002\u00107\u001a>\u00100\u001a\u00020\u0002*\u00020\u00042\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u00100\u001a\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0004\b5\u00107\u001a2\u00100\u001a\u00020\u0002*\u00020\u00042\u0006\u00100\u001a\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0002\u00108\u001a\r\u00109\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\n\u0010:\u001a\u00020#*\u00020'\u001a\r\u0010;\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010;\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\u001a\u001d\u0010<\u001a\u00020\u0011*\u00020\u00022\u0006\u0010=\u001a\u00020>2\u0006\u0010?\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010<\u001a\u00020\u0011*\u00020\u00022\u0006\u0010@\u001a\u00020\u00022\u0006\u0010?\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010A\u001a\u00020\u0011*\u00020\u00022\u0006\u0010=\u001a\u00020>2\u0006\u0010?\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010A\u001a\u00020\u0011*\u00020\u00022\u0006\u0010@\u001a\u00020\u00022\u0006\u0010?\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010B\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u00112\u0006\u0010C\u001a\u00020\u0011H\u0087\b\u001a4\u0010D\u001a\u00020#*\u00020'2\u0006\u0010E\u001a\u00020\u00112\u0006\u0010!\u001a\u00020'2\u0006\u0010F\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020#\u001a4\u0010D\u001a\u00020#*\u00020\u00022\u0006\u0010E\u001a\u00020\u00112\u0006\u0010!\u001a\u00020\u00022\u0006\u0010F\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020#\u001a\u0012\u0010G\u001a\u00020\u0002*\u00020'2\u0006\u0010H\u001a\u00020\u0011\u001a$\u0010I\u001a\u00020\u0002*\u00020\u00022\u0006\u0010J\u001a\u00020>2\u0006\u0010K\u001a\u00020>2\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010I\u001a\u00020\u0002*\u00020\u00022\u0006\u0010L\u001a\u00020\u00022\u0006\u0010M\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010N\u001a\u00020\u0002*\u00020\u00022\u0006\u0010J\u001a\u00020>2\u0006\u0010K\u001a\u00020>2\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010N\u001a\u00020\u0002*\u00020\u00022\u0006\u0010L\u001a\u00020\u00022\u0006\u0010M\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a\"\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00020P*\u00020'2\u0006\u0010Q\u001a\u00020R2\b\b\u0002\u0010S\u001a\u00020\u0011\u001a\u001c\u0010T\u001a\u00020#*\u00020\u00022\u0006\u0010U\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010T\u001a\u00020#*\u00020\u00022\u0006\u0010U\u001a\u00020\u00022\u0006\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020#\u001a\u0015\u0010V\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0011H\u0087\b\u001a\u001d\u0010V\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u0011H\u0087\b\u001a\u0017\u0010W\u001a\u00020\r*\u00020\u00022\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0087\b\u001a\r\u0010X\u001a\u00020\u0014*\u00020\u0002H\u0087\b\u001a3\u0010X\u001a\u00020\u0014*\u00020\u00022\u0006\u0010Y\u001a\u00020\u00142\b\b\u0002\u0010Z\u001a\u00020\u00112\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u0011H\u0087\b\u001a \u0010X\u001a\u00020\u0014*\u00020\u00022\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u0011H\u0007\u001a\r\u0010[\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010[\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\u001a\u0017\u0010\\\u001a\u00020R*\u00020\u00022\b\b\u0002\u0010]\u001a\u00020\u0011H\u0087\b\u001a\r\u0010^\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010^\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\u001a\r\u0010_\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010_\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\"%\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u00020\u00020\u0001j\b\u0012\u0004\u0012\u00020\u0002`\u0003*\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006`"}, d2={"CASE_INSENSITIVE_ORDER", "Ljava/util/Comparator;", "", "Lkotlin/Comparator;", "Lkotlin/String$Companion;", "getCASE_INSENSITIVE_ORDER", "(Lkotlin/jvm/internal/StringCompanionObject;)Ljava/util/Comparator;", "String", "stringBuffer", "Ljava/lang/StringBuffer;", "stringBuilder", "Ljava/lang/StringBuilder;", "bytes", "", "charset", "Ljava/nio/charset/Charset;", "offset", "", "length", "chars", "", "codePoints", "", "capitalize", "locale", "Ljava/util/Locale;", "codePointAt", "index", "codePointBefore", "codePointCount", "beginIndex", "endIndex", "compareTo", "other", "ignoreCase", "", "concatToString", "startIndex", "contentEquals", "", "charSequence", "decapitalize", "decodeToString", "throwOnInvalidSequence", "encodeToByteArray", "endsWith", "suffix", "equals", "format", "args", "", "", "(Ljava/lang/String;Ljava/util/Locale;[Ljava/lang/Object;)Ljava/lang/String;", "formatNullable", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "intern", "isBlank", "lowercase", "nativeIndexOf", "ch", "", "fromIndex", "str", "nativeLastIndexOf", "offsetByCodePoints", "codePointOffset", "regionMatches", "thisOffset", "otherOffset", "repeat", "n", "replace", "oldChar", "newChar", "oldValue", "newValue", "replaceFirst", "split", "", "regex", "Ljava/util/regex/Pattern;", "limit", "startsWith", "prefix", "substring", "toByteArray", "toCharArray", "destination", "destinationOffset", "toLowerCase", "toPattern", "flags", "toUpperCase", "uppercase", "kotlin-stdlib"}, xs="kotlin/text/StringsKt")
@SourceDebugExtension(value={"SMAP\nStringsJVM.kt\nKotlin\n*S Kotlin\n*F\n+ 1 StringsJVM.kt\nkotlin/text/StringsKt__StringsJVMKt\n+ 2 _Strings.kt\nkotlin/text/StringsKt___StringsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,825:1\n1174#2,2:826\n1#3:828\n1726#4,3:829\n*S KotlinDebug\n*F\n+ 1 StringsJVM.kt\nkotlin/text/StringsKt__StringsJVMKt\n*L\n73#1:826,2\n621#1:829,3\n*E\n"})
public class StringsKt__StringsJVMKt
extends StringsKt__StringNumberConversionsKt {
    @InlineOnly
    private static final int nativeIndexOf(String string, char c2, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        return string.indexOf(c2, n2);
    }

    @InlineOnly
    private static final int nativeIndexOf(String string, String string2, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        return string.indexOf(string2, n2);
    }

    @InlineOnly
    private static final int nativeLastIndexOf(String string, char c2, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        return string.lastIndexOf(c2, n2);
    }

    @InlineOnly
    private static final int nativeLastIndexOf(String string, String string2, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        return string.lastIndexOf(string2, n2);
    }

    public static final boolean equals(@Nullable String string, @Nullable String string2, boolean bl) {
        if (string == null) {
            return string2 == null;
        }
        if (!bl) {
            return string.equals(string2);
        }
        return string.equalsIgnoreCase(string2);
    }

    public static /* synthetic */ boolean equals$default(String string, String string2, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return StringsKt.equals(string, string2, bl);
    }

    @NotNull
    public static final String replace(@NotNull String charSequence, char c2, char c3, boolean bl) {
        StringBuilder stringBuilder;
        Intrinsics.checkNotNullParameter(charSequence, "");
        if (!bl) {
            String string = ((String)charSequence).replace(c2, c3);
            Intrinsics.checkNotNullExpressionValue(string, "");
            return string;
        }
        int n2 = ((String)charSequence).length();
        StringBuilder stringBuilder2 = stringBuilder = new StringBuilder(n2);
        charSequence = charSequence;
        for (int i2 = 0; i2 < charSequence.length(); ++i2) {
            char c4 = charSequence.charAt(i2);
            stringBuilder2.append(CharsKt.equals(c4, c2, bl) ? c3 : c4);
        }
        String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    public static /* synthetic */ String replace$default(String string, char c2, char c3, boolean bl, int n2, Object object) {
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.replace(string, c2, c3, bl);
    }

    @NotNull
    public static final String replace(@NotNull String string, @NotNull String string2, @NotNull String string3, boolean bl) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Intrinsics.checkNotNullParameter(string3, "");
        String string4 = string;
        string = string4;
        string = string4;
        int n2 = StringsKt.indexOf((CharSequence)string4, string2, 0, bl);
        if (n2 < 0) {
            return string;
        }
        int n3 = string2.length();
        int n4 = RangesKt.coerceAtLeast(n3, 1);
        int n5 = string.length() - n3 + string3.length();
        if (n5 < 0) {
            throw new OutOfMemoryError();
        }
        StringBuilder stringBuilder = new StringBuilder(n5);
        int n6 = 0;
        do {
            stringBuilder.append(string, n6, n2).append(string3);
            n6 = n2 + n3;
        } while (n2 < string.length() && (n2 = StringsKt.indexOf((CharSequence)string, string2, n2 + n4, bl)) > 0);
        String string5 = stringBuilder.append(string, n6, string.length()).toString();
        Intrinsics.checkNotNullExpressionValue(string5, "");
        return string5;
    }

    public static /* synthetic */ String replace$default(String string, String string2, String string3, boolean bl, int n2, Object object) {
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.replace(string, string2, string3, bl);
    }

    @NotNull
    public static final String replaceFirst(@NotNull String string, char c2, char c3, boolean bl) {
        Intrinsics.checkNotNullParameter(string, "");
        int n2 = StringsKt.indexOf$default((CharSequence)string, c2, 0, bl, 2, null);
        c2 = (char)n2;
        if (n2 < 0) {
            return string;
        }
        int n3 = c2 + '\u0001';
        CharSequence charSequence = String.valueOf(c3);
        return ((Object)StringsKt.replaceRange((CharSequence)string, (int)c2, n3, charSequence)).toString();
    }

    public static /* synthetic */ String replaceFirst$default(String string, char c2, char c3, boolean bl, int n2, Object object) {
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.replaceFirst(string, c2, c3, bl);
    }

    @NotNull
    public static final String replaceFirst(@NotNull String string, @NotNull String string2, @NotNull String string3, boolean n2) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Intrinsics.checkNotNullParameter(string3, "");
        n2 = StringsKt.indexOf$default((CharSequence)string, string2, 0, n2 != 0, 2, null);
        if (n2 < 0) {
            return string;
        }
        int n3 = n2 + string2.length();
        return ((Object)StringsKt.replaceRange((CharSequence)string, n2, n3, (CharSequence)string3)).toString();
    }

    public static /* synthetic */ String replaceFirst$default(String string, String string2, String string3, boolean bl, int n2, Object object) {
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.replaceFirst(string, string2, string3, bl);
    }

    @Deprecated(message="Use uppercase() instead.", replaceWith=@ReplaceWith(expression="uppercase(Locale.getDefault())", imports={"java.util.Locale"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @InlineOnly
    private static final String toUpperCase(String string) {
        Intrinsics.checkNotNullParameter(string, "");
        String string2 = string.toUpperCase();
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final String uppercase(String string) {
        Intrinsics.checkNotNullParameter(string, "");
        String string2 = string.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @Deprecated(message="Use lowercase() instead.", replaceWith=@ReplaceWith(expression="lowercase(Locale.getDefault())", imports={"java.util.Locale"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @InlineOnly
    private static final String toLowerCase(String string) {
        Intrinsics.checkNotNullParameter(string, "");
        String string2 = string.toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final String lowercase(String string) {
        Intrinsics.checkNotNullParameter(string, "");
        String string2 = string.toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final String concatToString(@NotNull char[] cArray) {
        Intrinsics.checkNotNullParameter(cArray, "");
        return new String(cArray);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final String concatToString(@NotNull char[] cArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(cArray, "");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(n2, n3, cArray.length);
        return new String(cArray, n2, n3 - n2);
    }

    public static /* synthetic */ String concatToString$default(char[] cArray, int n2, int n3, int n4, Object object) {
        if ((n4 & 1) != 0) {
            n2 = 0;
        }
        if ((n4 & 2) != 0) {
            n3 = cArray.length;
        }
        return StringsKt.concatToString(cArray, n2, n3);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final char[] toCharArray(@NotNull String string, int n2, int n3) {
        Intrinsics.checkNotNullParameter(string, "");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(n2, n3, string.length());
        char[] cArray = new char[n3 - n2];
        string.getChars(n2, n3, cArray, 0);
        return cArray;
    }

    public static /* synthetic */ char[] toCharArray$default(String string, int n2, int n3, int n4, Object object) {
        if ((n4 & 1) != 0) {
            n2 = 0;
        }
        if ((n4 & 2) != 0) {
            n3 = string.length();
        }
        return StringsKt.toCharArray(string, n2, n3);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final String decodeToString(@NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return new String(byArray, Charsets.UTF_8);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final String decodeToString(@NotNull byte[] byArray, int n2, int n3, boolean bl) {
        Intrinsics.checkNotNullParameter(byArray, "");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(n2, n3, byArray.length);
        if (!bl) {
            return new String(byArray, n2, n3 - n2, Charsets.UTF_8);
        }
        CharsetDecoder charsetDecoder = Charsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
        String string = charsetDecoder.decode(ByteBuffer.wrap(byArray, n2, n3 - n2)).toString();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    public static /* synthetic */ String decodeToString$default(byte[] byArray, int n2, int n3, boolean bl, int n4, Object object) {
        if ((n4 & 1) != 0) {
            n2 = 0;
        }
        if ((n4 & 2) != 0) {
            n3 = byArray.length;
        }
        if ((n4 & 4) != 0) {
            bl = false;
        }
        return StringsKt.decodeToString(byArray, n2, n3, bl);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final byte[] encodeToByteArray(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        byte[] byArray = string.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(byArray, "");
        return byArray;
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final byte[] encodeToByteArray(@NotNull String object, int n2, int n3, boolean bl) {
        byte[] byArray;
        Intrinsics.checkNotNullParameter(object, "");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(n2, n3, ((String)object).length());
        if (!bl) {
            String string = ((String)object).substring(n2, n3);
            Intrinsics.checkNotNullExpressionValue(string, "");
            String string2 = string;
            object = Charsets.UTF_8;
            Intrinsics.checkNotNull(string2);
            byte[] byArray2 = string2.getBytes((Charset)object);
            Intrinsics.checkNotNullExpressionValue(byArray2, "");
            return byArray2;
        }
        CharsetEncoder charsetEncoder = Charsets.UTF_8.newEncoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
        object = charsetEncoder.encode(CharBuffer.wrap((CharSequence)object, n2, n3));
        if (((ByteBuffer)object).hasArray() && ((ByteBuffer)object).arrayOffset() == 0) {
            int n4 = ((Buffer)object).remaining();
            byte[] byArray3 = ((ByteBuffer)object).array();
            Intrinsics.checkNotNull(byArray3);
            if (n4 == byArray3.length) {
                byte[] byArray4 = ((ByteBuffer)object).array();
                Intrinsics.checkNotNullExpressionValue(byArray4, "");
                return byArray4;
            }
        }
        byte[] byArray5 = byArray = new byte[((Buffer)object).remaining()];
        ((ByteBuffer)object).get(byArray5);
        return byArray;
    }

    public static /* synthetic */ byte[] encodeToByteArray$default(String string, int n2, int n3, boolean bl, int n4, Object object) {
        if ((n4 & 1) != 0) {
            n2 = 0;
        }
        if ((n4 & 2) != 0) {
            n3 = string.length();
        }
        if ((n4 & 4) != 0) {
            bl = false;
        }
        return StringsKt.encodeToByteArray(string, n2, n3, bl);
    }

    @InlineOnly
    private static final char[] toCharArray(String string) {
        Intrinsics.checkNotNullParameter(string, "");
        char[] cArray = string.toCharArray();
        Intrinsics.checkNotNullExpressionValue(cArray, "");
        return cArray;
    }

    @InlineOnly
    private static final char[] toCharArray(String string, char[] cArray, int n2, int n3, int n4) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(cArray, "");
        string.getChars(n3, n4, cArray, n2);
        return cArray;
    }

    static /* synthetic */ char[] toCharArray$default(String string, char[] cArray, int n2, int n3, int n4, int n5, Object object) {
        if ((n5 & 2) != 0) {
            n2 = 0;
        }
        if ((n5 & 4) != 0) {
            n3 = 0;
        }
        if ((n5 & 8) != 0) {
            n4 = string.length();
        }
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(cArray, "");
        string.getChars(n3, n4, cArray, n2);
        return cArray;
    }

    @InlineOnly
    private static final String format(String string, Object ... objectArray) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(objectArray, "");
        String string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @InlineOnly
    private static final String format(StringCompanionObject stringCompanionObject, String string, Object ... objectArray) {
        Intrinsics.checkNotNullParameter(stringCompanionObject, "");
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(objectArray, "");
        String string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @Deprecated(message="Use Kotlin compiler 1.4 to avoid deprecation warning.")
    @DeprecatedSinceKotlin(hiddenSince="1.4")
    @InlineOnly
    private static final /* synthetic */ String format(String string, Locale locale, Object ... objectArray) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(locale, "");
        Intrinsics.checkNotNullParameter(objectArray, "");
        String string2 = String.format(locale, string, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @SinceKotlin(version="1.4")
    @JvmName(name="formatNullable")
    @InlineOnly
    private static final String formatNullable(String string, Locale locale, Object ... objectArray) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(objectArray, "");
        String string2 = String.format(locale, string, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @Deprecated(message="Use Kotlin compiler 1.4 to avoid deprecation warning.")
    @DeprecatedSinceKotlin(hiddenSince="1.4")
    @InlineOnly
    private static final /* synthetic */ String format(StringCompanionObject stringCompanionObject, Locale locale, String string, Object ... objectArray) {
        Intrinsics.checkNotNullParameter(stringCompanionObject, "");
        Intrinsics.checkNotNullParameter(locale, "");
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(objectArray, "");
        String string2 = String.format(locale, string, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @SinceKotlin(version="1.4")
    @JvmName(name="formatNullable")
    @InlineOnly
    private static final String formatNullable(StringCompanionObject stringCompanionObject, Locale locale, String string, Object ... objectArray) {
        Intrinsics.checkNotNullParameter(stringCompanionObject, "");
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(objectArray, "");
        String string2 = String.format(locale, string, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @NotNull
    public static final List<String> split(@NotNull CharSequence charSequence, @NotNull Pattern pattern, int n2) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(pattern, "");
        StringsKt.requireNonNegativeLimit(n2);
        String[] stringArray = pattern.split(charSequence, n2 == 0 ? -1 : n2);
        Intrinsics.checkNotNullExpressionValue(stringArray, "");
        return ArraysKt.asList((Object[])stringArray);
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, Pattern pattern, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = 0;
        }
        return StringsKt.split(charSequence, pattern, n2);
    }

    @InlineOnly
    private static final String substring(String string, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        String string2 = string.substring(n2);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @InlineOnly
    private static final String substring(String string, int n2, int n3) {
        Intrinsics.checkNotNullParameter(string, "");
        String string2 = string.substring(n2, n3);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    public static final boolean startsWith(@NotNull String string, @NotNull String string2, boolean bl) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        if (!bl) {
            return string.startsWith(string2);
        }
        return StringsKt.regionMatches(string, 0, string2, 0, string2.length(), bl);
    }

    public static /* synthetic */ boolean startsWith$default(String string, String string2, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return StringsKt.startsWith(string, string2, bl);
    }

    public static final boolean startsWith(@NotNull String string, @NotNull String string2, int n2, boolean bl) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        if (!bl) {
            return string.startsWith(string2, n2);
        }
        return StringsKt.regionMatches(string, n2, string2, 0, string2.length(), bl);
    }

    public static /* synthetic */ boolean startsWith$default(String string, String string2, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.startsWith(string, string2, n2, bl);
    }

    public static final boolean endsWith(@NotNull String string, @NotNull String string2, boolean bl) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        if (!bl) {
            return string.endsWith(string2);
        }
        String string3 = string;
        return StringsKt.regionMatches(string3, string3.length() - string2.length(), string2, 0, string2.length(), true);
    }

    public static /* synthetic */ boolean endsWith$default(String string, String string2, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return StringsKt.endsWith(string, string2, bl);
    }

    @InlineOnly
    private static final String String(byte[] byArray, int n2, int n3, Charset charset) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(charset, "");
        return new String(byArray, n2, n3, charset);
    }

    @InlineOnly
    private static final String String(byte[] byArray, Charset charset) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(charset, "");
        return new String(byArray, charset);
    }

    @InlineOnly
    private static final String String(byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return new String(byArray, n2, n3, Charsets.UTF_8);
    }

    @InlineOnly
    private static final String String(byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return new String(byArray, Charsets.UTF_8);
    }

    @InlineOnly
    private static final String String(char[] cArray) {
        Intrinsics.checkNotNullParameter(cArray, "");
        return new String(cArray);
    }

    @InlineOnly
    private static final String String(char[] cArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(cArray, "");
        return new String(cArray, n2, n3);
    }

    @InlineOnly
    private static final String String(int[] nArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(nArray, "");
        return new String(nArray, n2, n3);
    }

    @InlineOnly
    private static final String String(StringBuffer stringBuffer) {
        Intrinsics.checkNotNullParameter(stringBuffer, "");
        return new String(stringBuffer);
    }

    @InlineOnly
    private static final String String(StringBuilder stringBuilder) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        return new String(stringBuilder);
    }

    @InlineOnly
    private static final int codePointAt(String string, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        return string.codePointAt(n2);
    }

    @InlineOnly
    private static final int codePointBefore(String string, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        return string.codePointBefore(n2);
    }

    @InlineOnly
    private static final int codePointCount(String string, int n2, int n3) {
        Intrinsics.checkNotNullParameter(string, "");
        return string.codePointCount(n2, n3);
    }

    public static final int compareTo(@NotNull String string, @NotNull String string2, boolean bl) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        if (bl) {
            return string.compareToIgnoreCase(string2);
        }
        return string.compareTo(string2);
    }

    public static /* synthetic */ int compareTo$default(String string, String string2, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return StringsKt.compareTo(string, string2, bl);
    }

    @InlineOnly
    private static final boolean contentEquals(String string, CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(charSequence, "");
        return string.contentEquals(charSequence);
    }

    @InlineOnly
    private static final boolean contentEquals(String string, StringBuffer stringBuffer) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(stringBuffer, "");
        return string.contentEquals(stringBuffer);
    }

    @SinceKotlin(version="1.5")
    public static final boolean contentEquals(@Nullable CharSequence charSequence, @Nullable CharSequence charSequence2) {
        if (charSequence instanceof String && charSequence2 != null) {
            return ((String)charSequence).contentEquals(charSequence2);
        }
        return StringsKt.contentEqualsImpl(charSequence, charSequence2);
    }

    @SinceKotlin(version="1.5")
    public static final boolean contentEquals(@Nullable CharSequence charSequence, @Nullable CharSequence charSequence2, boolean bl) {
        if (bl) {
            return StringsKt.contentEqualsIgnoreCaseImpl(charSequence, charSequence2);
        }
        return StringsKt.contentEquals(charSequence, charSequence2);
    }

    @InlineOnly
    private static final String intern(String string) {
        Intrinsics.checkNotNullParameter(string, "");
        String string2 = string.intern();
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    public static final boolean isBlank(@NotNull CharSequence charSequence) {
        block7: {
            block6: {
                boolean bl;
                Intrinsics.checkNotNullParameter(charSequence, "");
                if (charSequence.length() == 0) break block6;
                Object object = StringsKt.getIndices(charSequence);
                if (!(object instanceof Collection) || !((Collection)object).isEmpty()) {
                    object = object.iterator();
                    while (object.hasNext()) {
                        int n2 = ((IntIterator)object).nextInt();
                        if (CharsKt.isWhitespace(charSequence.charAt(n2))) continue;
                        bl = false;
                        break;
                    }
                } else {
                    bl = true;
                }
                if (!bl) break block7;
            }
            return true;
        }
        return false;
    }

    @InlineOnly
    private static final int offsetByCodePoints(String string, int n2, int n3) {
        Intrinsics.checkNotNullParameter(string, "");
        return string.offsetByCodePoints(n2, n3);
    }

    public static final boolean regionMatches(@NotNull CharSequence charSequence, int n2, @NotNull CharSequence charSequence2, int n3, int n4, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(charSequence2, "");
        if (charSequence instanceof String && charSequence2 instanceof String) {
            return StringsKt.regionMatches((String)charSequence, n2, (String)charSequence2, n3, n4, bl);
        }
        return StringsKt.regionMatchesImpl(charSequence, n2, charSequence2, n3, n4, bl);
    }

    public static /* synthetic */ boolean regionMatches$default(CharSequence charSequence, int n2, CharSequence charSequence2, int n3, int n4, boolean bl, int n5, Object object) {
        if ((n5 & 0x10) != 0) {
            bl = false;
        }
        return StringsKt.regionMatches(charSequence, n2, charSequence2, n3, n4, bl);
    }

    public static final boolean regionMatches(@NotNull String string, int n2, @NotNull String string2, int n3, int n4, boolean bl) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        if (!bl) {
            return string.regionMatches(n2, string2, n3, n4);
        }
        return string.regionMatches(bl, n2, string2, n3, n4);
    }

    public static /* synthetic */ boolean regionMatches$default(String string, int n2, String string2, int n3, int n4, boolean bl, int n5, Object object) {
        if ((n5 & 0x10) != 0) {
            bl = false;
        }
        return StringsKt.regionMatches(string, n2, string2, n3, n4, bl);
    }

    @Deprecated(message="Use lowercase() instead.", replaceWith=@ReplaceWith(expression="lowercase(locale)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @InlineOnly
    private static final String toLowerCase(String string, Locale locale) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(locale, "");
        String string2 = string.toLowerCase(locale);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final String lowercase(String string, Locale locale) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(locale, "");
        String string2 = string.toLowerCase(locale);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @Deprecated(message="Use uppercase() instead.", replaceWith=@ReplaceWith(expression="uppercase(locale)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @InlineOnly
    private static final String toUpperCase(String string, Locale locale) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(locale, "");
        String string2 = string.toUpperCase(locale);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final String uppercase(String string, Locale locale) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(locale, "");
        String string2 = string.toUpperCase(locale);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @InlineOnly
    private static final byte[] toByteArray(String string, Charset charset) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(charset, "");
        byte[] byArray = string.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(byArray, "");
        return byArray;
    }

    static /* synthetic */ byte[] toByteArray$default(String string, Charset charset, int n2, Object object) {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(charset, "");
        byte[] byArray = string.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(byArray, "");
        return byArray;
    }

    @InlineOnly
    private static final Pattern toPattern(String string, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        Pattern pattern = Pattern.compile(string, n2);
        Intrinsics.checkNotNullExpressionValue(pattern, "");
        return pattern;
    }

    static /* synthetic */ Pattern toPattern$default(String string, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n2 = 0;
        }
        Intrinsics.checkNotNullParameter(string, "");
        Pattern pattern = Pattern.compile(string, n2);
        Intrinsics.checkNotNullExpressionValue(pattern, "");
        return pattern;
    }

    @Deprecated(message="Use replaceFirstChar instead.", replaceWith=@ReplaceWith(expression="replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }", imports={"java.util.Locale"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @NotNull
    public static final String capitalize(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        Locale locale = Locale.getDefault();
        Intrinsics.checkNotNullExpressionValue(locale, "");
        return StringsKt.capitalize(string, locale);
    }

    @Deprecated(message="Use replaceFirstChar instead.", replaceWith=@ReplaceWith(expression="replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @LowPriorityInOverloadResolution
    @NotNull
    public static final String capitalize(@NotNull String string, @NotNull Locale locale) {
        char c2;
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(locale, "");
        if (((CharSequence)string).length() > 0 && Character.isLowerCase(c2 = string.charAt(0))) {
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2 = stringBuilder = new StringBuilder();
            char c3 = Character.toTitleCase(c2);
            if (c3 != Character.toUpperCase(c2)) {
                stringBuilder2.append(c3);
            } else {
                String string2 = string;
                String string3 = string2.substring(0, 1);
                Intrinsics.checkNotNullExpressionValue(string3, "");
                string2 = string3;
                Intrinsics.checkNotNull(string3);
                String string4 = string2.toUpperCase(locale);
                Intrinsics.checkNotNullExpressionValue(string4, "");
                stringBuilder2.append(string4);
            }
            String string5 = string;
            String string6 = string5.substring(1);
            Intrinsics.checkNotNullExpressionValue(string6, "");
            stringBuilder2.append(string6);
            String string7 = stringBuilder.toString();
            Intrinsics.checkNotNullExpressionValue(string7, "");
            return string7;
        }
        return string;
    }

    @Deprecated(message="Use replaceFirstChar instead.", replaceWith=@ReplaceWith(expression="replaceFirstChar { it.lowercase(Locale.getDefault()) }", imports={"java.util.Locale"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @NotNull
    public static final String decapitalize(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        if (((CharSequence)string).length() > 0 && !Character.isLowerCase(string.charAt(0))) {
            StringBuilder stringBuilder = new StringBuilder();
            String string2 = string;
            String string3 = string2.substring(0, 1);
            Intrinsics.checkNotNullExpressionValue(string3, "");
            string2 = string3;
            Intrinsics.checkNotNull(string3);
            String string4 = string2.toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string4, "");
            StringBuilder stringBuilder2 = stringBuilder.append(string4);
            string2 = string;
            String string5 = string2.substring(1);
            Intrinsics.checkNotNullExpressionValue(string5, "");
            return stringBuilder2.append(string5).toString();
        }
        return string;
    }

    @Deprecated(message="Use replaceFirstChar instead.", replaceWith=@ReplaceWith(expression="replaceFirstChar { it.lowercase(locale) }", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @LowPriorityInOverloadResolution
    @NotNull
    public static final String decapitalize(@NotNull String string, @NotNull Locale locale) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(locale, "");
        if (((CharSequence)string).length() > 0 && !Character.isLowerCase(string.charAt(0))) {
            StringBuilder stringBuilder = new StringBuilder();
            String string2 = string;
            String string3 = string2.substring(0, 1);
            Intrinsics.checkNotNullExpressionValue(string3, "");
            string2 = string3;
            Intrinsics.checkNotNull(string3);
            String string4 = string2.toLowerCase(locale);
            Intrinsics.checkNotNullExpressionValue(string4, "");
            StringBuilder stringBuilder2 = stringBuilder.append(string4);
            string2 = string;
            String string5 = string2.substring(1);
            Intrinsics.checkNotNullExpressionValue(string5, "");
            return stringBuilder2.append(string5).toString();
        }
        return string;
    }

    @NotNull
    public static final String repeat(@NotNull CharSequence charSequence, int n2) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        int n3 = n2 >= 0 ? 1 : 0;
        if (n3 == 0) {
            charSequence = "Count 'n' must be non-negative, but was " + n2 + '.';
            throw new IllegalArgumentException(((Object)charSequence).toString());
        }
        switch (n2) {
            case 0: {
                return "";
            }
            case 1: {
                return ((Object)charSequence).toString();
            }
        }
        switch (charSequence.length()) {
            case 0: {
                return "";
            }
            case 1: {
                char c2 = charSequence.charAt(0);
                n3 = 0;
                char[] cArray = new char[n2];
                while (n3 < n2) {
                    int n4 = n3++;
                    cArray[n4] = c2;
                }
                char[] cArray2 = cArray;
                return new String(cArray2);
            }
        }
        StringBuilder stringBuilder = new StringBuilder(n2 * charSequence.length());
        IntIterator intIterator = new IntRange(1, n2).iterator();
        while (intIterator.hasNext()) {
            intIterator.nextInt();
            stringBuilder.append(charSequence);
        }
        charSequence = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(charSequence, "");
        return charSequence;
    }

    @NotNull
    public static final Comparator<String> getCASE_INSENSITIVE_ORDER(@NotNull StringCompanionObject stringCompanionObject) {
        Intrinsics.checkNotNullParameter(stringCompanionObject, "");
        Comparator comparator = String.CASE_INSENSITIVE_ORDER;
        Intrinsics.checkNotNullExpressionValue(comparator, "");
        return comparator;
    }
}

