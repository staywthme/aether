/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import java.util.Locale;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.WasExperimental;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.text.CharCategory;
import kotlin.text.CharDirectionality;
import kotlin.text.CharsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u00004\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0001\u001a\u0018\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\nH\u0000\u001a\r\u0010\u000e\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0010\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0011\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0012\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0013\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0014\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0016\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0018\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0019\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u001a\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u001b\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\n\u0010\u001c\u001a\u00020\u000f*\u00020\u0002\u001a\r\u0010\u001d\u001a\u00020\u001e*\u00020\u0002H\u0087\b\u001a\u0014\u0010\u001d\u001a\u00020\u001e*\u00020\u00022\u0006\u0010\u001f\u001a\u00020 H\u0007\u001a\r\u0010!\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0014\u0010\"\u001a\u00020\u001e*\u00020\u00022\u0006\u0010\u001f\u001a\u00020 H\u0007\u001a\r\u0010#\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010$\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010%\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010&\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010'\u001a\u00020\u001e*\u00020\u0002H\u0087\b\u001a\u0014\u0010'\u001a\u00020\u001e*\u00020\u00022\u0006\u0010\u001f\u001a\u00020 H\u0007\u001a\r\u0010(\u001a\u00020\u0002*\u00020\u0002H\u0087\b\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\b\u00a8\u0006)"}, d2={"category", "Lkotlin/text/CharCategory;", "", "getCategory", "(C)Lkotlin/text/CharCategory;", "directionality", "Lkotlin/text/CharDirectionality;", "getDirectionality", "(C)Lkotlin/text/CharDirectionality;", "checkRadix", "", "radix", "digitOf", "char", "isDefined", "", "isDigit", "isHighSurrogate", "isISOControl", "isIdentifierIgnorable", "isJavaIdentifierPart", "isJavaIdentifierStart", "isLetter", "isLetterOrDigit", "isLowSurrogate", "isLowerCase", "isTitleCase", "isUpperCase", "isWhitespace", "lowercase", "", "locale", "Ljava/util/Locale;", "lowercaseChar", "titlecase", "titlecaseChar", "toLowerCase", "toTitleCase", "toUpperCase", "uppercase", "uppercaseChar", "kotlin-stdlib"}, xs="kotlin/text/CharsKt")
public class CharsKt__CharJVMKt {
    @NotNull
    public static final CharCategory getCategory(char c2) {
        return CharCategory.Companion.valueOf(Character.getType(c2));
    }

    @InlineOnly
    private static final boolean isDefined(char c2) {
        return Character.isDefined(c2);
    }

    @InlineOnly
    private static final boolean isLetter(char c2) {
        return Character.isLetter(c2);
    }

    @InlineOnly
    private static final boolean isLetterOrDigit(char c2) {
        return Character.isLetterOrDigit(c2);
    }

    @InlineOnly
    private static final boolean isDigit(char c2) {
        return Character.isDigit(c2);
    }

    @InlineOnly
    private static final boolean isIdentifierIgnorable(char c2) {
        return Character.isIdentifierIgnorable(c2);
    }

    @InlineOnly
    private static final boolean isISOControl(char c2) {
        return Character.isISOControl(c2);
    }

    @InlineOnly
    private static final boolean isJavaIdentifierPart(char c2) {
        return Character.isJavaIdentifierPart(c2);
    }

    @InlineOnly
    private static final boolean isJavaIdentifierStart(char c2) {
        return Character.isJavaIdentifierStart(c2);
    }

    public static final boolean isWhitespace(char c2) {
        return Character.isWhitespace(c2) || Character.isSpaceChar(c2);
    }

    @InlineOnly
    private static final boolean isUpperCase(char c2) {
        return Character.isUpperCase(c2);
    }

    @InlineOnly
    private static final boolean isLowerCase(char c2) {
        return Character.isLowerCase(c2);
    }

    @Deprecated(message="Use uppercaseChar() instead.", replaceWith=@ReplaceWith(expression="uppercaseChar()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @InlineOnly
    private static final char toUpperCase(char c2) {
        return Character.toUpperCase(c2);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final char uppercaseChar(char c2) {
        return Character.toUpperCase(c2);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final String uppercase(char c2) {
        String string = String.valueOf(c2);
        Intrinsics.checkNotNull(string);
        String string2 = string.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final String uppercase(char c2, @NotNull Locale locale) {
        Intrinsics.checkNotNullParameter(locale, "");
        String string = String.valueOf(c2);
        Intrinsics.checkNotNull(string);
        String string2 = string.toUpperCase(locale);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @Deprecated(message="Use lowercaseChar() instead.", replaceWith=@ReplaceWith(expression="lowercaseChar()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @InlineOnly
    private static final char toLowerCase(char c2) {
        return Character.toLowerCase(c2);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final char lowercaseChar(char c2) {
        return Character.toLowerCase(c2);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final String lowercase(char c2) {
        String string = String.valueOf(c2);
        Intrinsics.checkNotNull(string);
        String string2 = string.toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final String lowercase(char c2, @NotNull Locale locale) {
        Intrinsics.checkNotNullParameter(locale, "");
        String string = String.valueOf(c2);
        Intrinsics.checkNotNull(string);
        String string2 = string.toLowerCase(locale);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @InlineOnly
    private static final boolean isTitleCase(char c2) {
        return Character.isTitleCase(c2);
    }

    @Deprecated(message="Use titlecaseChar() instead.", replaceWith=@ReplaceWith(expression="titlecaseChar()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @InlineOnly
    private static final char toTitleCase(char c2) {
        return Character.toTitleCase(c2);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final char titlecaseChar(char c2) {
        return Character.toTitleCase(c2);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @NotNull
    public static final String titlecase(char c2, @NotNull Locale object) {
        Intrinsics.checkNotNullParameter(object, "");
        object = CharsKt.uppercase(c2, (Locale)object);
        if (((String)object).length() > 1) {
            if (c2 == '\u0149') {
                return object;
            }
            c2 = ((String)object).charAt(0);
            Intrinsics.checkNotNull(object);
            String string = ((String)object).substring(1);
            Intrinsics.checkNotNullExpressionValue(string, "");
            object = string;
            Intrinsics.checkNotNull(string);
            String string2 = ((String)object).toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(string2, "");
            object = string2;
            return c2 + (String)object;
        }
        String string = String.valueOf(c2);
        Intrinsics.checkNotNull(string);
        String string3 = string.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(string3, "");
        if (!Intrinsics.areEqual(object, string3)) {
            return object;
        }
        return String.valueOf(Character.toTitleCase(c2));
    }

    @NotNull
    public static final CharDirectionality getDirectionality(char c2) {
        return CharDirectionality.Companion.valueOf(Character.getDirectionality(c2));
    }

    @InlineOnly
    private static final boolean isHighSurrogate(char c2) {
        return Character.isHighSurrogate(c2);
    }

    @InlineOnly
    private static final boolean isLowSurrogate(char c2) {
        return Character.isLowSurrogate(c2);
    }

    public static final int digitOf(char c2, int n2) {
        return Character.digit((int)c2, n2);
    }

    @PublishedApi
    public static final int checkRadix(int n2) {
        if (!new IntRange(2, 36).contains(n2)) {
            throw new IllegalArgumentException("radix " + n2 + " was not in valid range " + new IntRange(2, 36));
        }
        return n2;
    }
}

