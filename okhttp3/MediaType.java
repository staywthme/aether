/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B-\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00030\u0007\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\u000b\u001a\u0004\u0018\u00010\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\fH\u0007J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0014\u001a\u00020\u0003J\r\u0010\u0005\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u0015J\b\u0010\u0016\u001a\u00020\u0003H\u0016J\r\u0010\u0004\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00030\u0007X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\tR\u0013\u0010\u0005\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\nR\u0013\u0010\u0004\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\n\u00a8\u0006\u0019"}, d2={"Lokhttp3/MediaType;", "", "mediaType", "", "type", "subtype", "parameterNamesAndValues", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)V", "[Ljava/lang/String;", "()Ljava/lang/String;", "charset", "Ljava/nio/charset/Charset;", "defaultValue", "equals", "", "other", "hashCode", "", "parameter", "name", "-deprecated_subtype", "toString", "-deprecated_type", "Companion", "okhttp"})
public final class MediaType {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final String mediaType;
    @NotNull
    private final String type;
    @NotNull
    private final String subtype;
    @NotNull
    private final String[] parameterNamesAndValues;
    @NotNull
    private static final String TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";
    @NotNull
    private static final String QUOTED = "\"([^\"]*)\"";
    private static final Pattern TYPE_SUBTYPE = Pattern.compile("([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)/([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)");
    private static final Pattern PARAMETER = Pattern.compile(";\\s*(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)=(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)|\"([^\"]*)\"))?");

    private MediaType(String string, String string2, String string3, String[] stringArray) {
        this.mediaType = string;
        this.type = string2;
        this.subtype = string3;
        this.parameterNamesAndValues = stringArray;
    }

    @JvmName(name="type")
    @NotNull
    public final String type() {
        return this.type;
    }

    @JvmName(name="subtype")
    @NotNull
    public final String subtype() {
        return this.subtype;
    }

    @JvmOverloads
    @Nullable
    public final Charset charset(@Nullable Charset charset) {
        String string = this.parameter("charset");
        if (string == null) {
            return charset;
        }
        Object object = string;
        try {
            object = Charset.forName((String)object);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            object = charset;
        }
        return object;
    }

    public static /* synthetic */ Charset charset$default(MediaType mediaType, Charset charset, int n2, Object object) {
        if ((n2 & 1) != 0) {
            charset = null;
        }
        return mediaType.charset(charset);
    }

    @Nullable
    public final String parameter(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        int n2 = this.parameterNamesAndValues.length + -1;
        int n3 = 0;
        n2 = ProgressionUtilKt.getProgressionLastElement(0, n2, 2);
        if (n2 >= 0) {
            while (true) {
                if (StringsKt.equals(this.parameterNamesAndValues[n3], string, true)) {
                    return this.parameterNamesAndValues[n3 + 1];
                }
                if (n3 == n2) break;
                n3 += 2;
            }
        }
        return null;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="type", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_type")
    @NotNull
    public final String -deprecated_type() {
        return this.type;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="subtype", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_subtype")
    @NotNull
    public final String -deprecated_subtype() {
        return this.subtype;
    }

    @NotNull
    public final String toString() {
        return this.mediaType;
    }

    public final boolean equals(@Nullable Object object) {
        return object instanceof MediaType && Intrinsics.areEqual(((MediaType)object).mediaType, this.mediaType);
    }

    public final int hashCode() {
        return this.mediaType.hashCode();
    }

    @JvmOverloads
    @Nullable
    public final Charset charset() {
        return MediaType.charset$default(this, null, 1, null);
    }

    @JvmStatic
    @JvmName(name="get")
    @NotNull
    public static final MediaType get(@NotNull String string) {
        return Companion.get(string);
    }

    @JvmStatic
    @JvmName(name="parse")
    @Nullable
    public static final MediaType parse(@NotNull String string) {
        return Companion.parse(string);
    }

    public /* synthetic */ MediaType(String string, String string2, String string3, String[] stringArray, DefaultConstructorMarker defaultConstructorMarker) {
        this(string, string2, string3, stringArray);
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0015\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0007H\u0007\u00a2\u0006\u0002\b\rJ\u0017\u0010\u000e\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\u0007H\u0007\u00a2\u0006\u0002\b\u000fJ\u0011\u0010\u0010\u001a\u00020\u000b*\u00020\u0007H\u0007\u00a2\u0006\u0002\b\nJ\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u000b*\u00020\u0007H\u0007\u00a2\u0006\u0002\b\u000eR\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lokhttp3/MediaType$Companion;", "", "()V", "PARAMETER", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "QUOTED", "", "TOKEN", "TYPE_SUBTYPE", "get", "Lokhttp3/MediaType;", "mediaType", "-deprecated_get", "parse", "-deprecated_parse", "toMediaType", "toMediaTypeOrNull", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nMediaType.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MediaType.kt\nokhttp3/MediaType$Companion\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n*L\n1#1,181:1\n1#2:182\n37#3,2:183\n*S KotlinDebug\n*F\n+ 1 MediaType.kt\nokhttp3/MediaType$Companion\n*L\n148#1:183,2\n*E\n"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @JvmName(name="get")
        @NotNull
        public final MediaType get(@NotNull String string) {
            Object object;
            Intrinsics.checkNotNullParameter(string, "");
            Matcher matcher = TYPE_SUBTYPE.matcher(string);
            if (!matcher.lookingAt()) {
                String string2 = "No subtype found for: \"" + string + '\"';
                throw new IllegalArgumentException(string2.toString());
            }
            String string3 = matcher.group(1);
            Intrinsics.checkNotNullExpressionValue(string3, "");
            String string4 = string3;
            Locale locale = Locale.US;
            Intrinsics.checkNotNullExpressionValue(locale, "");
            String string5 = string4.toLowerCase(locale);
            Intrinsics.checkNotNullExpressionValue(string5, "");
            String string6 = string5;
            String string7 = matcher.group(2);
            Intrinsics.checkNotNullExpressionValue(string7, "");
            Object object2 = string7;
            Locale locale2 = Locale.US;
            Intrinsics.checkNotNullExpressionValue(locale2, "");
            String string8 = ((String)object2).toLowerCase(locale2);
            Intrinsics.checkNotNullExpressionValue(string8, "");
            string4 = string8;
            object2 = new ArrayList();
            Matcher matcher2 = PARAMETER.matcher(string);
            int n2 = matcher.end();
            while (n2 < string.length()) {
                Object object3;
                matcher2.region(n2, string.length());
                if (!matcher2.lookingAt()) {
                    StringBuilder stringBuilder = new StringBuilder("Parameter is not formatted correctly: \"");
                    String string9 = string.substring(n2);
                    Intrinsics.checkNotNullExpressionValue(string9, "");
                    object = stringBuilder.append(string9).append("\" for: \"").append(string).append('\"').toString();
                    throw new IllegalArgumentException(object.toString());
                }
                String string10 = matcher2.group(1);
                if (string10 == null) {
                    n2 = matcher2.end();
                    continue;
                }
                object = matcher2.group(2);
                if (object == null) {
                    object3 = matcher2.group(3);
                } else if (StringsKt.startsWith$default((String)object, "'", false, 2, null) && StringsKt.endsWith$default((String)object, "'", false, 2, null) && ((String)object).length() > 2) {
                    String string11 = ((String)object).substring(1, ((String)object).length() - 1);
                    object3 = string11;
                    Intrinsics.checkNotNullExpressionValue(string11, "");
                } else {
                    object3 = object;
                }
                object = object3;
                ((Collection)object2).add(string10);
                ((Collection)object2).add(object);
                n2 = matcher2.end();
            }
            Collection collection = (Collection)object2;
            object = collection;
            return new MediaType(string, string6, string4, collection.toArray(new String[0]), null);
        }

        @JvmStatic
        @JvmName(name="parse")
        @Nullable
        public final MediaType parse(@NotNull String object) {
            Intrinsics.checkNotNullParameter(object, "");
            try {
                object = this.get((String)object);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                object = null;
            }
            return object;
        }

        @Deprecated(message="moved to extension function", replaceWith=@ReplaceWith(expression="mediaType.toMediaType()", imports={"okhttp3.MediaType.Companion.toMediaType"}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_get")
        @NotNull
        public final MediaType -deprecated_get(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, "");
            return this.get(string);
        }

        @Deprecated(message="moved to extension function", replaceWith=@ReplaceWith(expression="mediaType.toMediaTypeOrNull()", imports={"okhttp3.MediaType.Companion.toMediaTypeOrNull"}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_parse")
        @Nullable
        public final MediaType -deprecated_parse(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, "");
            return this.parse(string);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

