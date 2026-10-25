/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.WasExperimental;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import kotlin.text.StringsKt__RegexExtensionsKt;
import kotlin.text.SystemProperties;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000\\\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u0019\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0005\u001a\u001f\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0006H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\tH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\nH\u0087\b\u001a%\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u000e\u0010\u0003\u001a\n\u0018\u00010\u0001j\u0004\u0018\u0001`\u0002H\u0087\b\u001a-\u0010\u000b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a-\u0010\u000b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a\u0014\u0010\u0010\u001a\u00060\u0011j\u0002`\u0012*\u00060\u0011j\u0002`\u0012H\u0007\u001a\u001d\u0010\u0010\u001a\u00060\u0011j\u0002`\u0012*\u00060\u0011j\u0002`\u00122\u0006\u0010\u0003\u001a\u00020\u0013H\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0011j\u0002`\u0012*\u00060\u0011j\u0002`\u00122\b\u0010\u0003\u001a\u0004\u0018\u00010\u000fH\u0087\b\u001a\u0014\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u0002H\u0007\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0014H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0015H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0013H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\fH\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u000fH\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0006H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\tH\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\nH\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0016H\u0087\b\u001a%\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u000e\u0010\u0003\u001a\n\u0018\u00010\u0001j\u0004\u0018\u0001`\u0002H\u0087\b\u001a\u0014\u0010\u0017\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u0002H\u0007\u001a\u001d\u0010\u0018\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\bH\u0087\b\u001a%\u0010\u001a\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a5\u0010\u001b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a5\u0010\u001b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a!\u0010\u001c\u001a\u00020\u001d*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0013H\u0087\n\u001a-\u0010\u001e\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0016H\u0087\b\u001a7\u0010\u001f\u001a\u00020\u001d*\u00060\u0001j\u0002`\u00022\u0006\u0010 \u001a\u00020\f2\b\b\u0002\u0010!\u001a\u00020\b2\b\b\u0002\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\u000e\u001a\u00020\bH\u0087\b\u00a8\u0006\""}, d2={"appendLine", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "value", "Ljava/lang/StringBuffer;", "", "", "", "", "", "", "appendRange", "", "startIndex", "endIndex", "", "appendln", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "", "", "", "", "clear", "deleteAt", "index", "deleteRange", "insertRange", "set", "", "setRange", "toCharArray", "destination", "destinationOffset", "kotlin-stdlib"}, xs="kotlin/text/StringsKt")
@SourceDebugExtension(value={"SMAP\nStringBuilderJVM.kt\nKotlin\n*S Kotlin\n*F\n+ 1 StringBuilderJVM.kt\nkotlin/text/StringsKt__StringBuilderJVMKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,381:1\n1#2:382\n*E\n"})
class StringsKt__StringBuilderJVMKt
extends StringsKt__RegexExtensionsKt {
    @SinceKotlin(version="1.3")
    @NotNull
    public static final StringBuilder clear(@NotNull StringBuilder stringBuilder) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder;
        stringBuilder.setLength(0);
        return stringBuilder;
    }

    @InlineOnly
    private static final void set(StringBuilder stringBuilder, int n2, char c2) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        stringBuilder.setCharAt(n2, c2);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final StringBuilder setRange(StringBuilder stringBuilder, int n2, int n3, String string) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        Intrinsics.checkNotNullParameter(string, "");
        StringBuilder stringBuilder2 = stringBuilder.replace(n2, n3, string);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return stringBuilder2;
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final StringBuilder deleteAt(StringBuilder stringBuilder, int n2) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.deleteCharAt(n2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return stringBuilder2;
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final StringBuilder deleteRange(StringBuilder stringBuilder, int n2, int n3) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.delete(n2, n3);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return stringBuilder2;
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final void toCharArray(StringBuilder stringBuilder, char[] cArray, int n2, int n3, int n4) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        Intrinsics.checkNotNullParameter(cArray, "");
        stringBuilder.getChars(n3, n4, cArray, n2);
    }

    static /* synthetic */ void toCharArray$default(StringBuilder stringBuilder, char[] cArray, int n2, int n3, int n4, int n5, Object object) {
        if ((n5 & 2) != 0) {
            n2 = 0;
        }
        if ((n5 & 4) != 0) {
            n3 = 0;
        }
        if ((n5 & 8) != 0) {
            n4 = stringBuilder.length();
        }
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        Intrinsics.checkNotNullParameter(cArray, "");
        stringBuilder.getChars(n3, n4, cArray, n2);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final StringBuilder appendRange(StringBuilder stringBuilder, char[] cArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        Intrinsics.checkNotNullParameter(cArray, "");
        StringBuilder stringBuilder2 = stringBuilder.append(cArray, n2, n3 - n2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return stringBuilder2;
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final StringBuilder appendRange(StringBuilder stringBuilder, CharSequence charSequence, int n2, int n3) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        Intrinsics.checkNotNullParameter(charSequence, "");
        StringBuilder stringBuilder2 = stringBuilder.append(charSequence, n2, n3);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return stringBuilder2;
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final StringBuilder insertRange(StringBuilder stringBuilder, int n2, char[] cArray, int n3, int n4) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        Intrinsics.checkNotNullParameter(cArray, "");
        StringBuilder stringBuilder2 = stringBuilder.insert(n2, cArray, n3, n4 - n3);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return stringBuilder2;
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final StringBuilder insertRange(StringBuilder stringBuilder, int n2, CharSequence charSequence, int n3, int n4) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        Intrinsics.checkNotNullParameter(charSequence, "");
        StringBuilder stringBuilder2 = stringBuilder.insert(n2, charSequence, n3, n4);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return stringBuilder2;
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder stringBuilder, StringBuffer stringBuffer) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(stringBuffer);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        StringBuilder stringBuilder3 = stringBuilder2.append('\n');
        Intrinsics.checkNotNullExpressionValue(stringBuilder3, "");
        return stringBuilder3;
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder stringBuilder, StringBuilder stringBuilder2) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder3 = stringBuilder.append((CharSequence)stringBuilder2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder3, "");
        StringBuilder stringBuilder4 = stringBuilder3.append('\n');
        Intrinsics.checkNotNullExpressionValue(stringBuilder4, "");
        return stringBuilder4;
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder stringBuilder, int n2) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(n2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        StringBuilder stringBuilder3 = stringBuilder2.append('\n');
        Intrinsics.checkNotNullExpressionValue(stringBuilder3, "");
        return stringBuilder3;
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder stringBuilder, short s2) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(s2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        StringBuilder stringBuilder3 = stringBuilder2.append('\n');
        Intrinsics.checkNotNullExpressionValue(stringBuilder3, "");
        return stringBuilder3;
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder stringBuilder, byte by) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(by);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        StringBuilder stringBuilder3 = stringBuilder2.append('\n');
        Intrinsics.checkNotNullExpressionValue(stringBuilder3, "");
        return stringBuilder3;
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder stringBuilder, long l2) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(l2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        StringBuilder stringBuilder3 = stringBuilder2.append('\n');
        Intrinsics.checkNotNullExpressionValue(stringBuilder3, "");
        return stringBuilder3;
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder stringBuilder, float f2) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(f2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        StringBuilder stringBuilder3 = stringBuilder2.append('\n');
        Intrinsics.checkNotNullExpressionValue(stringBuilder3, "");
        return stringBuilder3;
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder stringBuilder, double d2) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(d2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        StringBuilder stringBuilder3 = stringBuilder2.append('\n');
        Intrinsics.checkNotNullExpressionValue(stringBuilder3, "");
        return stringBuilder3;
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine()", imports={}), level=DeprecationLevel.WARNING)
    @NotNull
    public static final Appendable appendln(@NotNull Appendable appendable) {
        Intrinsics.checkNotNullParameter(appendable, "");
        Appendable appendable2 = appendable.append(SystemProperties.LINE_SEPARATOR);
        Intrinsics.checkNotNullExpressionValue(appendable2, "");
        return appendable2;
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final Appendable appendln(Appendable appendable, CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(appendable, "");
        Appendable appendable2 = appendable.append(charSequence);
        Intrinsics.checkNotNullExpressionValue(appendable2, "");
        return StringsKt.appendln(appendable2);
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final Appendable appendln(Appendable appendable, char c2) {
        Intrinsics.checkNotNullParameter(appendable, "");
        Appendable appendable2 = appendable.append(c2);
        Intrinsics.checkNotNullExpressionValue(appendable2, "");
        return StringsKt.appendln(appendable2);
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine()", imports={}), level=DeprecationLevel.WARNING)
    @NotNull
    public static final StringBuilder appendln(@NotNull StringBuilder stringBuilder) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(SystemProperties.LINE_SEPARATOR);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return stringBuilder2;
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder stringBuilder, StringBuffer stringBuffer) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(stringBuffer);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return StringsKt.appendln(stringBuilder2);
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder stringBuilder, CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(charSequence);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return StringsKt.appendln(stringBuilder2);
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder stringBuilder, String string) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(string);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return StringsKt.appendln(stringBuilder2);
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder stringBuilder, Object object) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(object);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return StringsKt.appendln(stringBuilder2);
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder stringBuilder, StringBuilder stringBuilder2) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder3 = stringBuilder.append((CharSequence)stringBuilder2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder3, "");
        return StringsKt.appendln(stringBuilder3);
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder stringBuilder, char[] cArray) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        Intrinsics.checkNotNullParameter(cArray, "");
        StringBuilder stringBuilder2 = stringBuilder.append(cArray);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return StringsKt.appendln(stringBuilder2);
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder stringBuilder, char c2) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(c2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return StringsKt.appendln(stringBuilder2);
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder stringBuilder, boolean bl) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(bl);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return StringsKt.appendln(stringBuilder2);
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder stringBuilder, int n2) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(n2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return StringsKt.appendln(stringBuilder2);
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder stringBuilder, short s2) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(s2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return StringsKt.appendln(stringBuilder2);
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder stringBuilder, byte by) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(by);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return StringsKt.appendln(stringBuilder2);
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder stringBuilder, long l2) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(l2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return StringsKt.appendln(stringBuilder2);
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder stringBuilder, float f2) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(f2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return StringsKt.appendln(stringBuilder2);
    }

    @Deprecated(message="Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith=@ReplaceWith(expression="appendLine(value)", imports={}), level=DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder stringBuilder, double d2) {
        Intrinsics.checkNotNullParameter(stringBuilder, "");
        StringBuilder stringBuilder2 = stringBuilder.append(d2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder2, "");
        return StringsKt.appendln(stringBuilder2);
    }
}

