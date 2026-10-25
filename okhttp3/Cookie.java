/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
 */
package okhttp3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.internal.HostnamesKt;
import okhttp3.internal.Util;
import okhttp3.internal.http.DatesKt;
import okhttp3.internal.publicsuffix.PublicSuffixDatabase;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u0000 &2\u00020\u0001:\u0002%&BO\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\n\u0012\u0006\u0010\f\u001a\u00020\n\u0012\u0006\u0010\r\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000eJ\r\u0010\u0007\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u0012J\u0013\u0010\u0013\u001a\u00020\n2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\r\u0010\u0005\u001a\u00020\u0006H\u0007\u00a2\u0006\u0002\b\u0015J\b\u0010\u0016\u001a\u00020\u0017H\u0017J\r\u0010\r\u001a\u00020\nH\u0007\u00a2\u0006\u0002\b\u0018J\r\u0010\u000b\u001a\u00020\nH\u0007\u00a2\u0006\u0002\b\u0019J\u000e\u0010\u001a\u001a\u00020\n2\u0006\u0010\u001b\u001a\u00020\u001cJ\r\u0010\u0002\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u001dJ\r\u0010\b\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u001eJ\r\u0010\f\u001a\u00020\nH\u0007\u00a2\u0006\u0002\b\u001fJ\r\u0010\t\u001a\u00020\nH\u0007\u00a2\u0006\u0002\b J\b\u0010!\u001a\u00020\u0003H\u0016J\u0015\u0010!\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020\nH\u0000\u00a2\u0006\u0002\b#J\r\u0010\u0004\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b$R\u0013\u0010\u0007\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u000fR\u0013\u0010\u0005\u001a\u00020\u00068\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0010R\u0013\u0010\r\u001a\u00020\n8\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u0011R\u0013\u0010\u000b\u001a\u00020\n8\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0011R\u0013\u0010\u0002\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u000fR\u0013\u0010\b\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u000fR\u0013\u0010\f\u001a\u00020\n8\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0011R\u0013\u0010\t\u001a\u00020\n8\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0011R\u0013\u0010\u0004\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u000f\u00a8\u0006'"}, d2={"Lokhttp3/Cookie;", "", "name", "", "value", "expiresAt", "", "domain", "path", "secure", "", "httpOnly", "persistent", "hostOnly", "(Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;ZZZZ)V", "()Ljava/lang/String;", "()J", "()Z", "-deprecated_domain", "equals", "other", "-deprecated_expiresAt", "hashCode", "", "-deprecated_hostOnly", "-deprecated_httpOnly", "matches", "url", "Lokhttp3/HttpUrl;", "-deprecated_name", "-deprecated_path", "-deprecated_persistent", "-deprecated_secure", "toString", "forObsoleteRfc2965", "toString$okhttp", "-deprecated_value", "Builder", "Companion", "okhttp"})
public final class Cookie {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final String name;
    @NotNull
    private final String value;
    private final long expiresAt;
    @NotNull
    private final String domain;
    @NotNull
    private final String path;
    private final boolean secure;
    private final boolean httpOnly;
    private final boolean persistent;
    private final boolean hostOnly;
    private static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{2,4})[^\\d]*");
    private static final Pattern MONTH_PATTERN = Pattern.compile("(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*");
    private static final Pattern DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})[^\\d]*");
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})[^\\d]*");

    private Cookie(String string, String string2, long l2, String string3, String string4, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        this.name = string;
        this.value = string2;
        this.expiresAt = l2;
        this.domain = string3;
        this.path = string4;
        this.secure = bl;
        this.httpOnly = bl2;
        this.persistent = bl3;
        this.hostOnly = bl4;
    }

    @JvmName(name="name")
    @NotNull
    public final String name() {
        return this.name;
    }

    @JvmName(name="value")
    @NotNull
    public final String value() {
        return this.value;
    }

    @JvmName(name="expiresAt")
    public final long expiresAt() {
        return this.expiresAt;
    }

    @JvmName(name="domain")
    @NotNull
    public final String domain() {
        return this.domain;
    }

    @JvmName(name="path")
    @NotNull
    public final String path() {
        return this.path;
    }

    @JvmName(name="secure")
    public final boolean secure() {
        return this.secure;
    }

    @JvmName(name="httpOnly")
    public final boolean httpOnly() {
        return this.httpOnly;
    }

    @JvmName(name="persistent")
    public final boolean persistent() {
        return this.persistent;
    }

    @JvmName(name="hostOnly")
    public final boolean hostOnly() {
        return this.hostOnly;
    }

    public final boolean matches(@NotNull HttpUrl httpUrl) {
        Intrinsics.checkNotNullParameter(httpUrl, "");
        boolean bl = this.hostOnly ? Intrinsics.areEqual(httpUrl.host(), this.domain) : Cookie.Companion.domainMatch(httpUrl.host(), this.domain);
        if (!bl) {
            return false;
        }
        if (!Cookie.Companion.pathMatch(httpUrl, this.path)) {
            return false;
        }
        return !this.secure || httpUrl.isHttps();
    }

    public final boolean equals(@Nullable Object object) {
        return object instanceof Cookie && Intrinsics.areEqual(((Cookie)object).name, this.name) && Intrinsics.areEqual(((Cookie)object).value, this.value) && ((Cookie)object).expiresAt == this.expiresAt && Intrinsics.areEqual(((Cookie)object).domain, this.domain) && Intrinsics.areEqual(((Cookie)object).path, this.path) && ((Cookie)object).secure == this.secure && ((Cookie)object).httpOnly == this.httpOnly && ((Cookie)object).persistent == this.persistent && ((Cookie)object).hostOnly == this.hostOnly;
    }

    @IgnoreJRERequirement
    public final int hashCode() {
        int n2 = 527 + this.name.hashCode();
        n2 = n2 * 31 + this.value.hashCode();
        n2 = n2 * 31 + Long.hashCode(this.expiresAt);
        n2 = n2 * 31 + this.domain.hashCode();
        n2 = n2 * 31 + this.path.hashCode();
        n2 = n2 * 31 + Boolean.hashCode(this.secure);
        n2 = n2 * 31 + Boolean.hashCode(this.httpOnly);
        n2 = n2 * 31 + Boolean.hashCode(this.persistent);
        n2 = n2 * 31 + Boolean.hashCode(this.hostOnly);
        return n2;
    }

    @NotNull
    public final String toString() {
        return this.toString$okhttp(false);
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="name", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_name")
    @NotNull
    public final String -deprecated_name() {
        return this.name;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="value", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_value")
    @NotNull
    public final String -deprecated_value() {
        return this.value;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="persistent", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_persistent")
    public final boolean -deprecated_persistent() {
        return this.persistent;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="expiresAt", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_expiresAt")
    public final long -deprecated_expiresAt() {
        return this.expiresAt;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="hostOnly", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_hostOnly")
    public final boolean -deprecated_hostOnly() {
        return this.hostOnly;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="domain", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_domain")
    @NotNull
    public final String -deprecated_domain() {
        return this.domain;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="path", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_path")
    @NotNull
    public final String -deprecated_path() {
        return this.path;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="httpOnly", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_httpOnly")
    public final boolean -deprecated_httpOnly() {
        return this.httpOnly;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="secure", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_secure")
    public final boolean -deprecated_secure() {
        return this.secure;
    }

    @NotNull
    public final String toString$okhttp(boolean bl) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.name);
        stringBuilder.append('=');
        stringBuilder.append(this.value);
        if (this.persistent) {
            if (this.expiresAt == Long.MIN_VALUE) {
                stringBuilder.append("; max-age=0");
            } else {
                stringBuilder.append("; expires=").append(DatesKt.toHttpDateString(new Date(this.expiresAt)));
            }
        }
        if (!this.hostOnly) {
            stringBuilder.append("; domain=");
            if (bl) {
                stringBuilder.append(".");
            }
            stringBuilder.append(this.domain);
        }
        stringBuilder.append("; path=").append(this.path);
        if (this.secure) {
            stringBuilder.append("; secure");
        }
        if (this.httpOnly) {
            stringBuilder.append("; httponly");
        }
        String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    @JvmStatic
    @Nullable
    public static final Cookie parse(@NotNull HttpUrl httpUrl, @NotNull String string) {
        return Companion.parse(httpUrl, string);
    }

    @JvmStatic
    @NotNull
    public static final List<Cookie> parseAll(@NotNull HttpUrl httpUrl, @NotNull Headers headers) {
        return Companion.parseAll(httpUrl, headers);
    }

    public /* synthetic */ Cookie(String string, String string2, long l2, String string3, String string4, boolean bl, boolean bl2, boolean bl3, boolean bl4, DefaultConstructorMarker defaultConstructorMarker) {
        this(string, string2, l2, string3, string4, bl, bl2, bl3, bl4);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0003\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0004J\u0018\u0010\u0003\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u000e\u0010\u0005\u001a\u00020\u00002\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0004J\u0006\u0010\t\u001a\u00020\u0000J\u000e\u0010\n\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0004J\u000e\u0010\u000b\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\u0004J\u0006\u0010\r\u001a\u00020\u0000J\u000e\u0010\u000e\u001a\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lokhttp3/Cookie$Builder;", "", "()V", "domain", "", "expiresAt", "", "hostOnly", "", "httpOnly", "name", "path", "persistent", "secure", "value", "build", "Lokhttp3/Cookie;", "hostOnlyDomain", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nCookie.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Cookie.kt\nokhttp3/Cookie$Builder\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,614:1\n1#2:615\n*E\n"})
    public static final class Builder {
        @Nullable
        private String name;
        @Nullable
        private String value;
        private long expiresAt = 253402300799999L;
        @Nullable
        private String domain;
        @NotNull
        private String path = "/";
        private boolean secure;
        private boolean httpOnly;
        private boolean persistent;
        private boolean hostOnly;

        @NotNull
        public final Builder name(@NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, "");
            Builder builder2 = builder = this;
            if (!Intrinsics.areEqual(((Object)StringsKt.trim((CharSequence)string)).toString(), string)) {
                string = "name is not trimmed";
                throw new IllegalArgumentException(string.toString());
            }
            builder2.name = string;
            return builder;
        }

        @NotNull
        public final Builder value(@NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, "");
            Builder builder2 = builder = this;
            if (!Intrinsics.areEqual(((Object)StringsKt.trim((CharSequence)string)).toString(), string)) {
                string = "value is not trimmed";
                throw new IllegalArgumentException(string.toString());
            }
            builder2.value = string;
            return builder;
        }

        @NotNull
        public final Builder expiresAt(long l2) {
            Builder builder;
            Builder builder2 = builder = this;
            long l3 = l2;
            if (l2 <= 0L) {
                l3 = Long.MIN_VALUE;
            }
            if (l3 > 253402300799999L) {
                l3 = 253402300799999L;
            }
            builder2.expiresAt = l3;
            builder2.persistent = true;
            return builder;
        }

        @NotNull
        public final Builder domain(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, "");
            return this.domain(string, false);
        }

        @NotNull
        public final Builder hostOnlyDomain(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, "");
            return this.domain(string, true);
        }

        private final Builder domain(String string, boolean bl) {
            Builder builder;
            Builder builder2 = builder = this;
            String string2 = HostnamesKt.toCanonicalHost(string);
            if (string2 == null) {
                throw new IllegalArgumentException("unexpected domain: " + string);
            }
            builder2.domain = string = string2;
            builder2.hostOnly = bl;
            return builder;
        }

        @NotNull
        public final Builder path(@NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, "");
            Builder builder2 = builder = this;
            if (!StringsKt.startsWith$default(string, "/", false, 2, null)) {
                string = "path must start with '/'";
                throw new IllegalArgumentException(string.toString());
            }
            builder2.path = string;
            return builder;
        }

        @NotNull
        public final Builder secure() {
            Builder builder;
            Builder builder2 = builder = this;
            builder.secure = true;
            return builder;
        }

        @NotNull
        public final Builder httpOnly() {
            Builder builder;
            Builder builder2 = builder = this;
            builder.httpOnly = true;
            return builder;
        }

        @NotNull
        public final Cookie build() {
            String string = this.name;
            if (string == null) {
                throw new NullPointerException("builder.name == null");
            }
            String string2 = this.value;
            if (string2 == null) {
                throw new NullPointerException("builder.value == null");
            }
            String string3 = this.domain;
            if (string3 == null) {
                throw new NullPointerException("builder.domain == null");
            }
            return new Cookie(string, string2, this.expiresAt, string3, this.path, this.secure, this.httpOnly, this.persistent, this.hostOnly, null);
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J(\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0018\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\fH\u0002J'\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\fH\u0000\u00a2\u0006\u0002\b\u001bJ\u001a\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\fH\u0007J\u001e\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00150\u001d2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001e\u001a\u00020\u001fH\u0007J\u0010\u0010 \u001a\u00020\f2\u0006\u0010!\u001a\u00020\fH\u0002J \u0010\"\u001a\u00020\u00172\u0006\u0010!\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\nH\u0002J\u0010\u0010#\u001a\u00020\u00172\u0006\u0010!\u001a\u00020\fH\u0002J\u0018\u0010$\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010%\u001a\u00020\fH\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2={"Lokhttp3/Cookie$Companion;", "", "()V", "DAY_OF_MONTH_PATTERN", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "MONTH_PATTERN", "TIME_PATTERN", "YEAR_PATTERN", "dateCharacterOffset", "", "input", "", "pos", "limit", "invert", "", "domainMatch", "urlHost", "domain", "parse", "Lokhttp3/Cookie;", "currentTimeMillis", "", "url", "Lokhttp3/HttpUrl;", "setCookie", "parse$okhttp", "parseAll", "", "headers", "Lokhttp3/Headers;", "parseDomain", "s", "parseExpires", "parseMaxAge", "pathMatch", "path", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        private final boolean domainMatch(String string, String string2) {
            if (Intrinsics.areEqual(string, string2)) {
                return true;
            }
            if (StringsKt.endsWith$default(string, string2, false, 2, null)) {
                String string3 = string;
                if (string3.charAt(string3.length() - string2.length() - 1) == '.' && !Util.canParseAsIpAddress(string)) {
                    return true;
                }
            }
            return false;
        }

        private final boolean pathMatch(HttpUrl object, String string) {
            if (Intrinsics.areEqual(object = ((HttpUrl)object).encodedPath(), string)) {
                return true;
            }
            if (StringsKt.startsWith$default((String)object, string, false, 2, null)) {
                if (StringsKt.endsWith$default(string, "/", false, 2, null)) {
                    return true;
                }
                if (((String)object).charAt(string.length()) == '/') {
                    return true;
                }
            }
            return false;
        }

        @JvmStatic
        @Nullable
        public final Cookie parse(@NotNull HttpUrl httpUrl, @NotNull String string) {
            Intrinsics.checkNotNullParameter(httpUrl, "");
            Intrinsics.checkNotNullParameter(string, "");
            return this.parse$okhttp(System.currentTimeMillis(), httpUrl, string);
        }

        @Nullable
        public final Cookie parse$okhttp(long l2, @NotNull HttpUrl httpUrl, @NotNull String string) {
            long l3;
            Intrinsics.checkNotNullParameter(httpUrl, "");
            Intrinsics.checkNotNullParameter(string, "");
            int n2 = Util.delimiterOffset$default(string, ';', 0, 0, 6, null);
            int n3 = Util.delimiterOffset$default(string, '=', 0, n2, 2, null);
            if (n3 == n2) {
                return null;
            }
            String string2 = Util.trimSubstring$default(string, 0, n3, 1, null);
            if (((CharSequence)string2).length() == 0 || Util.indexOfControlOrNonAscii(string2) != -1) {
                return null;
            }
            String string3 = Util.trimSubstring(string, n3 + 1, n2);
            if (Util.indexOfControlOrNonAscii(string3) != -1) {
                return null;
            }
            long l4 = 253402300799999L;
            long l5 = -1L;
            String string4 = null;
            String string5 = null;
            boolean bl = false;
            boolean bl2 = false;
            boolean bl3 = true;
            boolean bl4 = false;
            ++n2;
            int n4 = string.length();
            while (n2 < n4) {
                String string6;
                int n5 = Util.delimiterOffset(string, ';', n2, n4);
                int n6 = Util.delimiterOffset(string, '=', n2, n5);
                String string7 = Util.trimSubstring(string, n2, n6);
                String string8 = string6 = n6 < n5 ? Util.trimSubstring(string, n6 + 1, n5) : "";
                if (StringsKt.equals(string7, "expires", true)) {
                    try {
                        l4 = this.parseExpires(string6, 0, string6.length());
                        bl4 = true;
                    }
                    catch (IllegalArgumentException illegalArgumentException) {}
                } else if (StringsKt.equals(string7, "max-age", true)) {
                    try {
                        l5 = this.parseMaxAge(string6);
                        bl4 = true;
                    }
                    catch (NumberFormatException numberFormatException) {}
                } else if (StringsKt.equals(string7, "domain", true)) {
                    try {
                        string4 = this.parseDomain(string6);
                        bl3 = false;
                    }
                    catch (IllegalArgumentException illegalArgumentException) {}
                } else if (StringsKt.equals(string7, "path", true)) {
                    string5 = string6;
                } else if (StringsKt.equals(string7, "secure", true)) {
                    bl = true;
                } else if (StringsKt.equals(string7, "httponly", true)) {
                    bl2 = true;
                }
                n2 = n5 + 1;
            }
            if (l5 == Long.MIN_VALUE) {
                l4 = Long.MIN_VALUE;
            } else if (l5 != -1L && ((l4 = l2 + (l3 = l5 <= 9223372036854775L ? l5 * 1000L : Long.MAX_VALUE)) < l2 || l4 > 253402300799999L)) {
                l4 = 253402300799999L;
            }
            String string9 = httpUrl.host();
            if (string4 == null) {
                string4 = string9;
            } else if (!this.domainMatch(string9, string4)) {
                return null;
            }
            if (string9.length() != string4.length() && PublicSuffixDatabase.Companion.get().getEffectiveTldPlusOne(string4) == null) {
                return null;
            }
            if (string5 == null || !StringsKt.startsWith$default(string5, "/", false, 2, null)) {
                String string10;
                String string11 = httpUrl.encodedPath();
                n2 = StringsKt.lastIndexOf$default((CharSequence)string11, '/', 0, false, 6, null);
                if (n2 != 0) {
                    String string12 = string11.substring(0, n2);
                    string10 = string12;
                    Intrinsics.checkNotNullExpressionValue(string12, "");
                } else {
                    string10 = "/";
                }
                string5 = string10;
            }
            return new Cookie(string2, string3, l4, string4, string5, bl, bl2, bl4, bl3, null);
        }

        private final long parseExpires(String string, int n2, int n3) {
            n2 = this.dateCharacterOffset(string, n2, n3, false);
            int n4 = -1;
            int n5 = -1;
            int n6 = -1;
            int n7 = -1;
            int n8 = -1;
            int n9 = -1;
            Matcher matcher = TIME_PATTERN.matcher(string);
            while (n2 < n3) {
                int n10 = this.dateCharacterOffset(string, n2 + 1, n3, true);
                matcher.region(n2, n10);
                if (n4 == -1 && matcher.usePattern(TIME_PATTERN).matches()) {
                    String string2 = matcher.group(1);
                    Intrinsics.checkNotNullExpressionValue(string2, "");
                    n4 = Integer.parseInt(string2);
                    String string3 = matcher.group(2);
                    Intrinsics.checkNotNullExpressionValue(string3, "");
                    n5 = Integer.parseInt(string3);
                    String string4 = matcher.group(3);
                    Intrinsics.checkNotNullExpressionValue(string4, "");
                    n6 = Integer.parseInt(string4);
                } else if (n7 == -1 && matcher.usePattern(DAY_OF_MONTH_PATTERN).matches()) {
                    String string5 = matcher.group(1);
                    Intrinsics.checkNotNullExpressionValue(string5, "");
                    n7 = Integer.parseInt(string5);
                } else if (n8 == -1 && matcher.usePattern(MONTH_PATTERN).matches()) {
                    String string6 = matcher.group(1);
                    Intrinsics.checkNotNullExpressionValue(string6, "");
                    String string7 = string6;
                    Locale locale = Locale.US;
                    Intrinsics.checkNotNullExpressionValue(locale, "");
                    String string8 = string7.toLowerCase(locale);
                    Intrinsics.checkNotNullExpressionValue(string8, "");
                    string7 = string8;
                    String string9 = MONTH_PATTERN.pattern();
                    Intrinsics.checkNotNullExpressionValue(string9, "");
                    n8 = StringsKt.indexOf$default((CharSequence)string9, string7, 0, false, 6, null) / 4;
                } else if (n9 == -1 && matcher.usePattern(YEAR_PATTERN).matches()) {
                    String string10 = matcher.group(1);
                    Intrinsics.checkNotNullExpressionValue(string10, "");
                    n9 = Integer.parseInt(string10);
                }
                n2 = this.dateCharacterOffset(string, n10 + 1, n3, false);
            }
            boolean bl = 70 <= n9 ? n9 < 100 : false;
            if (bl) {
                n9 += 1900;
            }
            boolean bl2 = n9 >= 0 ? n9 < 70 : false;
            if (bl2) {
                n9 += 2000;
            }
            if (!(n9 >= 1601)) {
                String string11 = "Failed requirement.";
                throw new IllegalArgumentException(string11.toString());
            }
            if (!(n8 != -1)) {
                String string12 = "Failed requirement.";
                throw new IllegalArgumentException(string12.toString());
            }
            if (!(n7 > 0 ? n7 < 32 : false)) {
                String string13 = "Failed requirement.";
                throw new IllegalArgumentException(string13.toString());
            }
            if (!(n4 >= 0 ? n4 < 24 : false)) {
                String string14 = "Failed requirement.";
                throw new IllegalArgumentException(string14.toString());
            }
            if (!(n5 >= 0 ? n5 < 60 : false)) {
                String string15 = "Failed requirement.";
                throw new IllegalArgumentException(string15.toString());
            }
            if (!(n6 >= 0 ? n6 < 60 : false)) {
                String string16 = "Failed requirement.";
                throw new IllegalArgumentException(string16.toString());
            }
            GregorianCalendar gregorianCalendar = new GregorianCalendar(Util.UTC);
            gregorianCalendar.setLenient(false);
            gregorianCalendar.set(1, n9);
            gregorianCalendar.set(2, n8 - 1);
            gregorianCalendar.set(5, n7);
            gregorianCalendar.set(11, n4);
            gregorianCalendar.set(12, n5);
            gregorianCalendar.set(13, n6);
            gregorianCalendar.set(14, 0);
            return gregorianCalendar.getTimeInMillis();
        }

        /*
         * Unable to fully structure code
         */
        private final int dateCharacterOffset(String var1_1, int var2_2, int var3_3, boolean var4_4) {
            while (var2_2 < var3_3) {
                var5_5 = var1_1.charAt(var2_2);
                if (var5_5 < ' ' && var5_5 != '\t' || var5_5 >= '\u007f') ** GOTO lbl-1000
                if ('0' <= var5_5 ? var5_5 < ':' : false) ** GOTO lbl-1000
                if ('a' <= var5_5 ? var5_5 < '{' : false) ** GOTO lbl-1000
                v0 = 'A' <= var5_5 ? var5_5 < '[' : false;
                if (v0 || var5_5 == ':') lbl-1000:
                // 4 sources

                {
                    v1 = '\u0001';
                } else {
                    v1 = var5_5 = '\u0000';
                }
                if (v1 == (var4_4 == false ? '\u0001' : '\u0000')) {
                    return var2_2;
                }
                ++var2_2;
            }
            return var3_3;
        }

        private final long parseMaxAge(String string) {
            try {
                long l2 = Long.parseLong(string);
                if (l2 <= 0L) {
                    return Long.MIN_VALUE;
                }
                return l2;
            }
            catch (NumberFormatException numberFormatException) {
                CharSequence charSequence = string;
                if (new Regex("-?\\d+").matches(charSequence)) {
                    if (StringsKt.startsWith$default(string, "-", false, 2, null)) {
                        return Long.MIN_VALUE;
                    }
                    return Long.MAX_VALUE;
                }
                throw numberFormatException;
            }
        }

        private final String parseDomain(String string) {
            if (!(!StringsKt.endsWith$default(string, ".", false, 2, null))) {
                string = "Failed requirement.";
                throw new IllegalArgumentException(string.toString());
            }
            String string2 = HostnamesKt.toCanonicalHost(StringsKt.removePrefix(string, (CharSequence)"."));
            if (string2 == null) {
                throw new IllegalArgumentException();
            }
            return string2;
        }

        @JvmStatic
        @NotNull
        public final List<Cookie> parseAll(@NotNull HttpUrl httpUrl, @NotNull Headers iterable) {
            Intrinsics.checkNotNullParameter(httpUrl, "");
            Intrinsics.checkNotNullParameter(iterable, "");
            iterable = ((Headers)iterable).values("Set-Cookie");
            List list2 = null;
            int n2 = iterable.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Cookie cookie;
                if (this.parse(httpUrl, (String)iterable.get(i2)) == null) continue;
                if (list2 == null) {
                    list2 = new ArrayList();
                }
                list2.add(cookie);
            }
            if (list2 != null) {
                List<Cookie> list3 = Collections.unmodifiableList(list2);
                Intrinsics.checkNotNullExpressionValue(list3, "");
                return list3;
            }
            return CollectionsKt.emptyList();
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

