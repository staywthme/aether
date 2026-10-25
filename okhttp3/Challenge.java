/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.MapsKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005B#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0014\u0010\u0006\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0012\u0004\u0012\u00020\u00030\u0007\u00a2\u0006\u0002\u0010\bJ\u001b\u0010\u0006\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0012\u0004\u0012\u00020\u00030\u0007H\u0007\u00a2\u0006\u0002\b\u000eJ\r\u0010\n\u001a\u00020\u000bH\u0007\u00a2\u0006\u0002\b\u000fJ\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\u000f\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u0007\u00a2\u0006\u0002\b\u0015J\r\u0010\u0002\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u0016J\b\u0010\u0017\u001a\u00020\u0003H\u0016J\u000e\u0010\u0018\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u000bR!\u0010\u0006\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0012\u0004\u0012\u00020\u00030\u00078G\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\tR\u0011\u0010\n\u001a\u00020\u000b8G\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\fR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u00038G\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\rR\u0013\u0010\u0002\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\r\u00a8\u0006\u0019"}, d2={"Lokhttp3/Challenge;", "", "scheme", "", "realm", "(Ljava/lang/String;Ljava/lang/String;)V", "authParams", "", "(Ljava/lang/String;Ljava/util/Map;)V", "()Ljava/util/Map;", "charset", "Ljava/nio/charset/Charset;", "()Ljava/nio/charset/Charset;", "()Ljava/lang/String;", "-deprecated_authParams", "-deprecated_charset", "equals", "", "other", "hashCode", "", "-deprecated_realm", "-deprecated_scheme", "toString", "withCharset", "okhttp"})
public final class Challenge {
    @NotNull
    private final String scheme;
    @NotNull
    private final Map<String, String> authParams;

    public Challenge(@NotNull String object, @NotNull Map<String, String> object2) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(object2, "");
        this.scheme = object;
        object = new LinkedHashMap();
        for (Map.Entry entry : object2.entrySet()) {
            String string;
            String string2 = (String)entry.getKey();
            String object3 = (String)entry.getValue();
            String string3 = string2;
            if (string3 != null) {
                string2 = string3;
                Locale locale = Locale.US;
                Intrinsics.checkNotNullExpressionValue(locale, "");
                String string4 = string2.toLowerCase(locale);
                string = string4;
                Intrinsics.checkNotNullExpressionValue(string4, "");
            } else {
                string = null;
            }
            string2 = string;
            object.put(string2, object3);
        }
        Map map = Collections.unmodifiableMap(object);
        Intrinsics.checkNotNullExpressionValue(map, "");
        this.authParams = map;
    }

    @JvmName(name="scheme")
    @NotNull
    public final String scheme() {
        return this.scheme;
    }

    @JvmName(name="authParams")
    @NotNull
    public final Map<String, String> authParams() {
        return this.authParams;
    }

    @JvmName(name="realm")
    @Nullable
    public final String realm() {
        return this.authParams.get("realm");
    }

    @JvmName(name="charset")
    @NotNull
    public final Charset charset() {
        String string = this.authParams.get("charset");
        if (string != null) {
            try {
                Charset charset = Charset.forName(string);
                Intrinsics.checkNotNullExpressionValue(charset, "");
                return charset;
            }
            catch (Exception exception) {}
        }
        Charset charset = StandardCharsets.ISO_8859_1;
        Intrinsics.checkNotNullExpressionValue(charset, "");
        return charset;
    }

    public Challenge(@NotNull String string, @NotNull String string2) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Map<String, String> map = Collections.singletonMap("realm", string2);
        Intrinsics.checkNotNullExpressionValue(map, "");
        this(string, map);
    }

    @NotNull
    public final Challenge withCharset(@NotNull Charset object) {
        Map<String, String> map;
        Intrinsics.checkNotNullParameter(object, "");
        Map<String, String> map2 = map = MapsKt.toMutableMap(this.authParams);
        String string = "charset";
        String string2 = ((Charset)object).name();
        Intrinsics.checkNotNullExpressionValue(string2, "");
        object = string2;
        map2.put(string, (String)object);
        return new Challenge(this.scheme, map);
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="scheme", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_scheme")
    @NotNull
    public final String -deprecated_scheme() {
        return this.scheme;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="authParams", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_authParams")
    @NotNull
    public final Map<String, String> -deprecated_authParams() {
        return this.authParams;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="realm", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_realm")
    @Nullable
    public final String -deprecated_realm() {
        return this.realm();
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="charset", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_charset")
    @NotNull
    public final Charset -deprecated_charset() {
        return this.charset();
    }

    public final boolean equals(@Nullable Object object) {
        return object instanceof Challenge && Intrinsics.areEqual(((Challenge)object).scheme, this.scheme) && Intrinsics.areEqual(((Challenge)object).authParams, this.authParams);
    }

    public final int hashCode() {
        int n2 = 899 + this.scheme.hashCode();
        n2 = n2 * 31 + ((Object)this.authParams).hashCode();
        return n2;
    }

    @NotNull
    public final String toString() {
        return this.scheme + " authParams=" + this.authParams;
    }
}

