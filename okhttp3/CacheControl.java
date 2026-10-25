/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.util.concurrent.TimeUnit;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import okhttp3.Headers;
import okhttp3.internal.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\u0011\u0018\u0000 !2\u00020\u0001:\u0002 !Bq\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0006\u0012\u0006\u0010\f\u001a\u00020\u0006\u0012\u0006\u0010\r\u001a\u00020\u0003\u0012\u0006\u0010\u000e\u001a\u00020\u0003\u0012\u0006\u0010\u000f\u001a\u00020\u0003\u0012\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011\u00a2\u0006\u0002\u0010\u0012J\r\u0010\u000f\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u0015J\r\u0010\u0005\u001a\u00020\u0006H\u0007\u00a2\u0006\u0002\b\u0016J\r\u0010\u000b\u001a\u00020\u0006H\u0007\u00a2\u0006\u0002\b\u0017J\r\u0010\f\u001a\u00020\u0006H\u0007\u00a2\u0006\u0002\b\u0018J\r\u0010\n\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u0019J\r\u0010\u0002\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u001aJ\r\u0010\u0004\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u001bJ\r\u0010\u000e\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u001cJ\r\u0010\r\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u001dJ\r\u0010\u0007\u001a\u00020\u0006H\u0007\u00a2\u0006\u0002\b\u001eJ\b\u0010\u001f\u001a\u00020\u0011H\u0016R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0013\u0010\u000f\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0013R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0013R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0013R\u0013\u0010\u0005\u001a\u00020\u00068\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0014R\u0013\u0010\u000b\u001a\u00020\u00068\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0014R\u0013\u0010\f\u001a\u00020\u00068\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0014R\u0013\u0010\n\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0013R\u0013\u0010\u0002\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0013R\u0013\u0010\u0004\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0013R\u0013\u0010\u000e\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0013R\u0013\u0010\r\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u0013R\u0013\u0010\u0007\u001a\u00020\u00068\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u0014\u00a8\u0006\""}, d2={"Lokhttp3/CacheControl;", "", "noCache", "", "noStore", "maxAgeSeconds", "", "sMaxAgeSeconds", "isPrivate", "isPublic", "mustRevalidate", "maxStaleSeconds", "minFreshSeconds", "onlyIfCached", "noTransform", "immutable", "headerValue", "", "(ZZIIZZZIIZZZLjava/lang/String;)V", "()Z", "()I", "-deprecated_immutable", "-deprecated_maxAgeSeconds", "-deprecated_maxStaleSeconds", "-deprecated_minFreshSeconds", "-deprecated_mustRevalidate", "-deprecated_noCache", "-deprecated_noStore", "-deprecated_noTransform", "-deprecated_onlyIfCached", "-deprecated_sMaxAgeSeconds", "toString", "Builder", "Companion", "okhttp"})
public final class CacheControl {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private final boolean noCache;
    private final boolean noStore;
    private final int maxAgeSeconds;
    private final int sMaxAgeSeconds;
    private final boolean isPrivate;
    private final boolean isPublic;
    private final boolean mustRevalidate;
    private final int maxStaleSeconds;
    private final int minFreshSeconds;
    private final boolean onlyIfCached;
    private final boolean noTransform;
    private final boolean immutable;
    @Nullable
    private String headerValue;
    @JvmField
    @NotNull
    public static final CacheControl FORCE_NETWORK = new Builder().noCache().build();
    @JvmField
    @NotNull
    public static final CacheControl FORCE_CACHE = new Builder().onlyIfCached().maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS).build();

    private CacheControl(boolean bl, boolean bl2, int n2, int n3, boolean bl3, boolean bl4, boolean bl5, int n4, int n5, boolean bl6, boolean bl7, boolean bl8, String string) {
        this.noCache = bl;
        this.noStore = bl2;
        this.maxAgeSeconds = n2;
        this.sMaxAgeSeconds = n3;
        this.isPrivate = bl3;
        this.isPublic = bl4;
        this.mustRevalidate = bl5;
        this.maxStaleSeconds = n4;
        this.minFreshSeconds = n5;
        this.onlyIfCached = bl6;
        this.noTransform = bl7;
        this.immutable = bl8;
        this.headerValue = string;
    }

    @JvmName(name="noCache")
    public final boolean noCache() {
        return this.noCache;
    }

    @JvmName(name="noStore")
    public final boolean noStore() {
        return this.noStore;
    }

    @JvmName(name="maxAgeSeconds")
    public final int maxAgeSeconds() {
        return this.maxAgeSeconds;
    }

    @JvmName(name="sMaxAgeSeconds")
    public final int sMaxAgeSeconds() {
        return this.sMaxAgeSeconds;
    }

    public final boolean isPrivate() {
        return this.isPrivate;
    }

    public final boolean isPublic() {
        return this.isPublic;
    }

    @JvmName(name="mustRevalidate")
    public final boolean mustRevalidate() {
        return this.mustRevalidate;
    }

    @JvmName(name="maxStaleSeconds")
    public final int maxStaleSeconds() {
        return this.maxStaleSeconds;
    }

    @JvmName(name="minFreshSeconds")
    public final int minFreshSeconds() {
        return this.minFreshSeconds;
    }

    @JvmName(name="onlyIfCached")
    public final boolean onlyIfCached() {
        return this.onlyIfCached;
    }

    @JvmName(name="noTransform")
    public final boolean noTransform() {
        return this.noTransform;
    }

    @JvmName(name="immutable")
    public final boolean immutable() {
        return this.immutable;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="noCache", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_noCache")
    public final boolean -deprecated_noCache() {
        return this.noCache;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="noStore", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_noStore")
    public final boolean -deprecated_noStore() {
        return this.noStore;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="maxAgeSeconds", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_maxAgeSeconds")
    public final int -deprecated_maxAgeSeconds() {
        return this.maxAgeSeconds;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="sMaxAgeSeconds", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_sMaxAgeSeconds")
    public final int -deprecated_sMaxAgeSeconds() {
        return this.sMaxAgeSeconds;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="mustRevalidate", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_mustRevalidate")
    public final boolean -deprecated_mustRevalidate() {
        return this.mustRevalidate;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="maxStaleSeconds", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_maxStaleSeconds")
    public final int -deprecated_maxStaleSeconds() {
        return this.maxStaleSeconds;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="minFreshSeconds", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_minFreshSeconds")
    public final int -deprecated_minFreshSeconds() {
        return this.minFreshSeconds;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="onlyIfCached", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_onlyIfCached")
    public final boolean -deprecated_onlyIfCached() {
        return this.onlyIfCached;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="noTransform", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_noTransform")
    public final boolean -deprecated_noTransform() {
        return this.noTransform;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="immutable", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_immutable")
    public final boolean -deprecated_immutable() {
        return this.immutable;
    }

    @NotNull
    public final String toString() {
        CharSequence charSequence = this.headerValue;
        if (charSequence == null) {
            charSequence = new StringBuilder();
            StringBuilder stringBuilder = charSequence;
            if (this.noCache) {
                stringBuilder.append("no-cache, ");
            }
            if (this.noStore) {
                stringBuilder.append("no-store, ");
            }
            if (this.maxAgeSeconds != -1) {
                stringBuilder.append("max-age=").append(this.maxAgeSeconds).append(", ");
            }
            if (this.sMaxAgeSeconds != -1) {
                stringBuilder.append("s-maxage=").append(this.sMaxAgeSeconds).append(", ");
            }
            if (this.isPrivate) {
                stringBuilder.append("private, ");
            }
            if (this.isPublic) {
                stringBuilder.append("public, ");
            }
            if (this.mustRevalidate) {
                stringBuilder.append("must-revalidate, ");
            }
            if (this.maxStaleSeconds != -1) {
                stringBuilder.append("max-stale=").append(this.maxStaleSeconds).append(", ");
            }
            if (this.minFreshSeconds != -1) {
                stringBuilder.append("min-fresh=").append(this.minFreshSeconds).append(", ");
            }
            if (this.onlyIfCached) {
                stringBuilder.append("only-if-cached, ");
            }
            if (this.noTransform) {
                stringBuilder.append("no-transform, ");
            }
            if (this.immutable) {
                stringBuilder.append("immutable, ");
            }
            if (((CharSequence)stringBuilder).length() == 0) {
                return "";
            }
            StringBuilder stringBuilder2 = stringBuilder;
            stringBuilder2.delete(stringBuilder2.length() - 2, stringBuilder.length());
            String string = ((StringBuilder)charSequence).toString();
            Intrinsics.checkNotNullExpressionValue(string, "");
            charSequence = string;
            this.headerValue = charSequence;
        }
        return charSequence;
    }

    @JvmStatic
    @NotNull
    public static final CacheControl parse(@NotNull Headers headers) {
        return Companion.parse(headers);
    }

    public /* synthetic */ CacheControl(boolean bl, boolean bl2, int n2, int n3, boolean bl3, boolean bl4, boolean bl5, int n4, int n5, boolean bl6, boolean bl7, boolean bl8, String string, DefaultConstructorMarker defaultConstructorMarker) {
        this(bl, bl2, n2, n3, bl3, bl4, bl5, n4, n5, bl6, bl7, bl8, string);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010\u0003\u001a\u00020\u0000J\u0016\u0010\u000f\u001a\u00020\u00002\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0011J\u0016\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0011J\u0016\u0010\u0013\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\t\u001a\u00020\u0000J\u0006\u0010\n\u001a\u00020\u0000J\u0006\u0010\u000b\u001a\u00020\u0000J\u0006\u0010\f\u001a\u00020\u0000J\f\u0010\u0014\u001a\u00020\u0006*\u00020\u0015H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2={"Lokhttp3/CacheControl$Builder;", "", "()V", "immutable", "", "maxAgeSeconds", "", "maxStaleSeconds", "minFreshSeconds", "noCache", "noStore", "noTransform", "onlyIfCached", "build", "Lokhttp3/CacheControl;", "maxAge", "timeUnit", "Ljava/util/concurrent/TimeUnit;", "maxStale", "minFresh", "clampToInt", "", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nCacheControl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 CacheControl.kt\nokhttp3/CacheControl$Builder\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,416:1\n1#2:417\n*E\n"})
    public static final class Builder {
        private boolean noCache;
        private boolean noStore;
        private int maxAgeSeconds = -1;
        private int maxStaleSeconds = -1;
        private int minFreshSeconds = -1;
        private boolean onlyIfCached;
        private boolean noTransform;
        private boolean immutable;

        @NotNull
        public final Builder noCache() {
            Builder builder;
            Builder builder2 = builder = this;
            builder.noCache = true;
            return builder;
        }

        @NotNull
        public final Builder noStore() {
            Builder builder;
            Builder builder2 = builder = this;
            builder.noStore = true;
            return builder;
        }

        @NotNull
        public final Builder maxAge(int n2, @NotNull TimeUnit timeUnit) {
            Builder builder;
            Intrinsics.checkNotNullParameter((Object)timeUnit, "");
            Builder builder2 = builder = this;
            if (!(n2 >= 0)) {
                String string = "maxAge < 0: " + n2;
                throw new IllegalArgumentException(string.toString());
            }
            long l2 = timeUnit.toSeconds(n2);
            builder2.maxAgeSeconds = builder2.clampToInt(l2);
            return builder;
        }

        @NotNull
        public final Builder maxStale(int n2, @NotNull TimeUnit timeUnit) {
            Builder builder;
            Intrinsics.checkNotNullParameter((Object)timeUnit, "");
            Builder builder2 = builder = this;
            if (!(n2 >= 0)) {
                String string = "maxStale < 0: " + n2;
                throw new IllegalArgumentException(string.toString());
            }
            long l2 = timeUnit.toSeconds(n2);
            builder2.maxStaleSeconds = builder2.clampToInt(l2);
            return builder;
        }

        @NotNull
        public final Builder minFresh(int n2, @NotNull TimeUnit timeUnit) {
            Builder builder;
            Intrinsics.checkNotNullParameter((Object)timeUnit, "");
            Builder builder2 = builder = this;
            if (!(n2 >= 0)) {
                String string = "minFresh < 0: " + n2;
                throw new IllegalArgumentException(string.toString());
            }
            long l2 = timeUnit.toSeconds(n2);
            builder2.minFreshSeconds = builder2.clampToInt(l2);
            return builder;
        }

        @NotNull
        public final Builder onlyIfCached() {
            Builder builder;
            Builder builder2 = builder = this;
            builder.onlyIfCached = true;
            return builder;
        }

        @NotNull
        public final Builder noTransform() {
            Builder builder;
            Builder builder2 = builder = this;
            builder.noTransform = true;
            return builder;
        }

        @NotNull
        public final Builder immutable() {
            Builder builder;
            Builder builder2 = builder = this;
            builder.immutable = true;
            return builder;
        }

        private final int clampToInt(long l2) {
            if (l2 > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            return (int)l2;
        }

        @NotNull
        public final CacheControl build() {
            return new CacheControl(this.noCache, this.noStore, this.maxAgeSeconds, -1, false, false, false, this.maxStaleSeconds, this.minFreshSeconds, this.onlyIfCached, this.noTransform, this.immutable, null, null);
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u001e\u0010\t\u001a\u00020\n*\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\nH\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2={"Lokhttp3/CacheControl$Companion;", "", "()V", "FORCE_CACHE", "Lokhttp3/CacheControl;", "FORCE_NETWORK", "parse", "headers", "Lokhttp3/Headers;", "indexOfElement", "", "", "characters", "startIndex", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final CacheControl parse(@NotNull Headers headers) {
            Intrinsics.checkNotNullParameter(headers, "");
            boolean bl = false;
            boolean bl2 = false;
            int n2 = -1;
            int n3 = -1;
            boolean bl3 = false;
            boolean bl4 = false;
            boolean bl5 = false;
            int n4 = -1;
            int n5 = -1;
            boolean bl6 = false;
            boolean bl7 = false;
            boolean bl8 = false;
            boolean bl9 = true;
            String string = null;
            int n6 = headers.size();
            for (int i2 = 0; i2 < n6; ++i2) {
                String string2 = headers.name(i2);
                String string3 = headers.value(i2);
                if (StringsKt.equals(string2, "Cache-Control", true)) {
                    if (string != null) {
                        bl9 = false;
                    } else {
                        string = string3;
                    }
                } else {
                    if (!StringsKt.equals(string2, "Pragma", true)) continue;
                    bl9 = false;
                }
                int n7 = 0;
                while (n7 < string3.length()) {
                    String string4;
                    int n8 = n7;
                    n7 = this.indexOfElement(string3, "=,;", n7);
                    String string5 = string3.substring(n8, n7);
                    Intrinsics.checkNotNullExpressionValue(string5, "");
                    String string6 = ((Object)StringsKt.trim((CharSequence)string5)).toString();
                    if (n7 == string3.length() || string3.charAt(n7) == ',' || string3.charAt(n7) == ';') {
                        ++n7;
                        string4 = null;
                    } else {
                        ++n7;
                        if ((n7 = Util.indexOfNonWhitespace(string3, n7)) < string3.length() && string3.charAt(n7) == '\"') {
                            int n9 = ++n7;
                            n7 = StringsKt.indexOf$default((CharSequence)string3, '\"', n7, false, 4, null);
                            Intrinsics.checkNotNullExpressionValue(string3.substring(n9, n7), "");
                            ++n7;
                        } else {
                            int n10 = n7;
                            n7 = this.indexOfElement(string3, ",;", n7);
                            String string7 = string3.substring(n10, n7);
                            Intrinsics.checkNotNullExpressionValue(string7, "");
                            string4 = ((Object)StringsKt.trim((CharSequence)string7)).toString();
                        }
                    }
                    if (StringsKt.equals("no-cache", string6, true)) {
                        bl = true;
                        continue;
                    }
                    if (StringsKt.equals("no-store", string6, true)) {
                        bl2 = true;
                        continue;
                    }
                    if (StringsKt.equals("max-age", string6, true)) {
                        n2 = Util.toNonNegativeInt(string4, -1);
                        continue;
                    }
                    if (StringsKt.equals("s-maxage", string6, true)) {
                        n3 = Util.toNonNegativeInt(string4, -1);
                        continue;
                    }
                    if (StringsKt.equals("private", string6, true)) {
                        bl3 = true;
                        continue;
                    }
                    if (StringsKt.equals("public", string6, true)) {
                        bl4 = true;
                        continue;
                    }
                    if (StringsKt.equals("must-revalidate", string6, true)) {
                        bl5 = true;
                        continue;
                    }
                    if (StringsKt.equals("max-stale", string6, true)) {
                        n4 = Util.toNonNegativeInt(string4, Integer.MAX_VALUE);
                        continue;
                    }
                    if (StringsKt.equals("min-fresh", string6, true)) {
                        n5 = Util.toNonNegativeInt(string4, -1);
                        continue;
                    }
                    if (StringsKt.equals("only-if-cached", string6, true)) {
                        bl6 = true;
                        continue;
                    }
                    if (StringsKt.equals("no-transform", string6, true)) {
                        bl7 = true;
                        continue;
                    }
                    if (!StringsKt.equals("immutable", string6, true)) continue;
                    bl8 = true;
                }
            }
            if (!bl9) {
                string = null;
            }
            return new CacheControl(bl, bl2, n2, n3, bl3, bl4, bl5, n4, n5, bl6, bl7, bl8, string, null);
        }

        private final int indexOfElement(String string, String string2, int n2) {
            int n3 = string.length();
            while (n2 < n3) {
                if (StringsKt.contains$default((CharSequence)string2, string.charAt(n2), false, 2, null)) {
                    return n2;
                }
                ++n2;
            }
            return string.length();
        }

        static /* synthetic */ int indexOfElement$default(Companion companion, String string, String string2, int n2, int n3, Object object) {
            if ((n3 & 2) != 0) {
                n2 = 0;
            }
            return companion.indexOfElement(string, string2, n2);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

