/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.IntProgression;
import kotlin.ranges.RangesKt;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import okhttp3.internal.HostnamesKt;
import okhttp3.internal.Util;
import okhttp3.internal.publicsuffix.PublicSuffixDatabase;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\"\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 J2\u00020\u0001:\u0002IJBa\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\n\u0012\u0010\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0018\u00010\n\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\r\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u000eJ\u000f\u0010\u000f\u001a\u0004\u0018\u00010\u0003H\u0007\u00a2\u0006\u0002\b!J\r\u0010\u0011\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b\"J\r\u0010\u0012\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b#J\u0013\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\nH\u0007\u00a2\u0006\u0002\b$J\u000f\u0010\u0015\u001a\u0004\u0018\u00010\u0003H\u0007\u00a2\u0006\u0002\b%J\r\u0010\u0016\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b&J\u0013\u0010'\u001a\u00020\u00182\b\u0010(\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\u000f\u0010\f\u001a\u0004\u0018\u00010\u0003H\u0007\u00a2\u0006\u0002\b)J\b\u0010*\u001a\u00020\bH\u0016J\r\u0010\u0006\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b+J\u0006\u0010,\u001a\u00020-J\u0010\u0010,\u001a\u0004\u0018\u00010-2\u0006\u0010.\u001a\u00020\u0003J\r\u0010\u0005\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b/J\u0013\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\nH\u0007\u00a2\u0006\u0002\b0J\r\u0010\u001a\u001a\u00020\bH\u0007\u00a2\u0006\u0002\b1J\r\u0010\u0007\u001a\u00020\bH\u0007\u00a2\u0006\u0002\b2J\u000f\u0010\u001c\u001a\u0004\u0018\u00010\u0003H\u0007\u00a2\u0006\u0002\b3J\u0010\u00104\u001a\u0004\u0018\u00010\u00032\u0006\u00105\u001a\u00020\u0003J\u000e\u00106\u001a\u00020\u00032\u0006\u00107\u001a\u00020\bJ\u0013\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00030\u001eH\u0007\u00a2\u0006\u0002\b8J\u0010\u00109\u001a\u0004\u0018\u00010\u00032\u0006\u00107\u001a\u00020\bJ\u0016\u0010:\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\n2\u0006\u00105\u001a\u00020\u0003J\r\u0010 \u001a\u00020\bH\u0007\u00a2\u0006\u0002\b;J\u0006\u0010<\u001a\u00020\u0003J\u0010\u0010=\u001a\u0004\u0018\u00010\u00002\u0006\u0010.\u001a\u00020\u0003J\r\u0010\u0002\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b>J\b\u0010?\u001a\u00020\u0003H\u0016J\r\u0010@\u001a\u00020AH\u0007\u00a2\u0006\u0002\bBJ\r\u0010C\u001a\u00020DH\u0007\u00a2\u0006\u0002\b\rJ\b\u0010E\u001a\u0004\u0018\u00010\u0003J\r\u0010B\u001a\u00020AH\u0007\u00a2\u0006\u0002\bFJ\r\u0010\r\u001a\u00020DH\u0007\u00a2\u0006\u0002\bGJ\r\u0010\u0004\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\bHR\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u00038G\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u00038G\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u0012\u001a\u00020\u00038G\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0010R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\n8G\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u0013\u0010\u0015\u001a\u0004\u0018\u00010\u00038G\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0010R\u0011\u0010\u0016\u001a\u00020\u00038G\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0010R\u0015\u0010\f\u001a\u0004\u0018\u00010\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0010R\u0013\u0010\u0006\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0010R\u0011\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0019R\u0013\u0010\u0005\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0010R\u0019\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\n8\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0014R\u0011\u0010\u001a\u001a\u00020\b8G\u00a2\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u0013\u0010\u0007\u001a\u00020\b8\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u001bR\u0013\u0010\u001c\u001a\u0004\u0018\u00010\u00038G\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\u0010R\u0018\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0018\u00010\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00030\u001e8G\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u001fR\u0011\u0010 \u001a\u00020\b8G\u00a2\u0006\u0006\u001a\u0004\b \u0010\u001bR\u0013\u0010\u0002\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0010R\u000e\u0010\r\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0013\u0010\u0004\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0010\u00a8\u0006K"}, d2={"Lokhttp3/HttpUrl;", "", "scheme", "", "username", "password", "host", "port", "", "pathSegments", "", "queryNamesAndValues", "fragment", "url", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/lang/String;)V", "encodedFragment", "()Ljava/lang/String;", "encodedPassword", "encodedPath", "encodedPathSegments", "()Ljava/util/List;", "encodedQuery", "encodedUsername", "isHttps", "", "()Z", "pathSize", "()I", "query", "queryParameterNames", "", "()Ljava/util/Set;", "querySize", "-deprecated_encodedFragment", "-deprecated_encodedPassword", "-deprecated_encodedPath", "-deprecated_encodedPathSegments", "-deprecated_encodedQuery", "-deprecated_encodedUsername", "equals", "other", "-deprecated_fragment", "hashCode", "-deprecated_host", "newBuilder", "Lokhttp3/HttpUrl$Builder;", "link", "-deprecated_password", "-deprecated_pathSegments", "-deprecated_pathSize", "-deprecated_port", "-deprecated_query", "queryParameter", "name", "queryParameterName", "index", "-deprecated_queryParameterNames", "queryParameterValue", "queryParameterValues", "-deprecated_querySize", "redact", "resolve", "-deprecated_scheme", "toString", "toUri", "Ljava/net/URI;", "uri", "toUrl", "Ljava/net/URL;", "topPrivateDomain", "-deprecated_uri", "-deprecated_url", "-deprecated_username", "Builder", "Companion", "okhttp"})
public final class HttpUrl {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final String scheme;
    @NotNull
    private final String username;
    @NotNull
    private final String password;
    @NotNull
    private final String host;
    private final int port;
    @NotNull
    private final List<String> pathSegments;
    @Nullable
    private final List<String> queryNamesAndValues;
    @Nullable
    private final String fragment;
    @NotNull
    private final String url;
    private final boolean isHttps;
    @NotNull
    private static final char[] HEX_DIGITS;
    @NotNull
    public static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    @NotNull
    public static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    @NotNull
    public static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
    @NotNull
    public static final String PATH_SEGMENT_ENCODE_SET_URI = "[]";
    @NotNull
    public static final String QUERY_ENCODE_SET = " \"'<>#";
    @NotNull
    public static final String QUERY_COMPONENT_REENCODE_SET = " \"'<>#&=";
    @NotNull
    public static final String QUERY_COMPONENT_ENCODE_SET = " !\"#$&'(),/:;<=>?@[]\\^`{|}~";
    @NotNull
    public static final String QUERY_COMPONENT_ENCODE_SET_URI = "\\^`{|}";
    @NotNull
    public static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";
    @NotNull
    public static final String FRAGMENT_ENCODE_SET = "";
    @NotNull
    public static final String FRAGMENT_ENCODE_SET_URI = " \"#<>\\^`{|}";

    public HttpUrl(@NotNull String string, @NotNull String string2, @NotNull String string3, @NotNull String string4, int n2, @NotNull List<String> list2, @Nullable List<String> list3, @Nullable String string5, @NotNull String string6) {
        Intrinsics.checkNotNullParameter(string, FRAGMENT_ENCODE_SET);
        Intrinsics.checkNotNullParameter(string2, FRAGMENT_ENCODE_SET);
        Intrinsics.checkNotNullParameter(string3, FRAGMENT_ENCODE_SET);
        Intrinsics.checkNotNullParameter(string4, FRAGMENT_ENCODE_SET);
        Intrinsics.checkNotNullParameter(list2, FRAGMENT_ENCODE_SET);
        Intrinsics.checkNotNullParameter(string6, FRAGMENT_ENCODE_SET);
        this.scheme = string;
        this.username = string2;
        this.password = string3;
        this.host = string4;
        this.port = n2;
        this.pathSegments = list2;
        this.queryNamesAndValues = list3;
        this.fragment = string5;
        this.url = string6;
        this.isHttps = Intrinsics.areEqual(this.scheme, "https");
    }

    @JvmName(name="scheme")
    @NotNull
    public final String scheme() {
        return this.scheme;
    }

    @JvmName(name="username")
    @NotNull
    public final String username() {
        return this.username;
    }

    @JvmName(name="password")
    @NotNull
    public final String password() {
        return this.password;
    }

    @JvmName(name="host")
    @NotNull
    public final String host() {
        return this.host;
    }

    @JvmName(name="port")
    public final int port() {
        return this.port;
    }

    @JvmName(name="pathSegments")
    @NotNull
    public final List<String> pathSegments() {
        return this.pathSegments;
    }

    @JvmName(name="fragment")
    @Nullable
    public final String fragment() {
        return this.fragment;
    }

    public final boolean isHttps() {
        return this.isHttps;
    }

    @JvmName(name="url")
    @NotNull
    public final URL url() {
        try {
            return new URL(this.url);
        }
        catch (MalformedURLException malformedURLException) {
            throw new RuntimeException(malformedURLException);
        }
    }

    @JvmName(name="uri")
    @NotNull
    public final URI uri() {
        Object object;
        Object object2 = this.newBuilder().reencodeForUri$okhttp().toString();
        try {
            object = new URI((String)object2);
        }
        catch (URISyntaxException uRISyntaxException) {
            try {
                object2 = (CharSequence)object2;
                Regex regex = new Regex("[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]");
                String string = FRAGMENT_ENCODE_SET;
                object2 = regex.replace((CharSequence)object2, string);
                object2 = URI.create((String)object2);
            }
            catch (Exception exception) {
                throw new RuntimeException(uRISyntaxException);
            }
            Object object3 = object2;
            Intrinsics.checkNotNullExpressionValue(object3, FRAGMENT_ENCODE_SET);
            object = object3;
        }
        return object;
    }

    @JvmName(name="encodedUsername")
    @NotNull
    public final String encodedUsername() {
        if (((CharSequence)this.username).length() == 0) {
            return FRAGMENT_ENCODE_SET;
        }
        int n2 = this.scheme.length() + 3;
        int n3 = Util.delimiterOffset(this.url, ":@", n2, this.url.length());
        String string = this.url.substring(n2, n3);
        Intrinsics.checkNotNullExpressionValue(string, FRAGMENT_ENCODE_SET);
        return string;
    }

    @JvmName(name="encodedPassword")
    @NotNull
    public final String encodedPassword() {
        if (((CharSequence)this.password).length() == 0) {
            return FRAGMENT_ENCODE_SET;
        }
        int n2 = StringsKt.indexOf$default((CharSequence)this.url, ':', this.scheme.length() + 3, false, 4, null) + 1;
        int n3 = StringsKt.indexOf$default((CharSequence)this.url, '@', 0, false, 6, null);
        String string = this.url.substring(n2, n3);
        Intrinsics.checkNotNullExpressionValue(string, FRAGMENT_ENCODE_SET);
        return string;
    }

    @JvmName(name="pathSize")
    public final int pathSize() {
        return this.pathSegments.size();
    }

    @JvmName(name="encodedPath")
    @NotNull
    public final String encodedPath() {
        int n2 = StringsKt.indexOf$default((CharSequence)this.url, '/', this.scheme.length() + 3, false, 4, null);
        int n3 = Util.delimiterOffset(this.url, "?#", n2, this.url.length());
        String string = this.url.substring(n2, n3);
        Intrinsics.checkNotNullExpressionValue(string, FRAGMENT_ENCODE_SET);
        return string;
    }

    @JvmName(name="encodedPathSegments")
    @NotNull
    public final List<String> encodedPathSegments() {
        int n2 = StringsKt.indexOf$default((CharSequence)this.url, '/', this.scheme.length() + 3, false, 4, null);
        int n3 = Util.delimiterOffset(this.url, "?#", n2, this.url.length());
        List list2 = new ArrayList();
        while (n2 < n3) {
            int n4 = Util.delimiterOffset(this.url, '/', ++n2, n3);
            String string = this.url.substring(n2, n4);
            Intrinsics.checkNotNullExpressionValue(string, FRAGMENT_ENCODE_SET);
            list2.add(string);
            n2 = n4;
        }
        return list2;
    }

    @JvmName(name="encodedQuery")
    @Nullable
    public final String encodedQuery() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        int n2 = StringsKt.indexOf$default((CharSequence)this.url, '?', 0, false, 6, null) + 1;
        int n3 = Util.delimiterOffset(this.url, '#', n2, this.url.length());
        String string = this.url.substring(n2, n3);
        Intrinsics.checkNotNullExpressionValue(string, FRAGMENT_ENCODE_SET);
        return string;
    }

    @JvmName(name="query")
    @Nullable
    public final String query() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Companion.toQueryString$okhttp(this.queryNamesAndValues, stringBuilder);
        return stringBuilder.toString();
    }

    @JvmName(name="querySize")
    public final int querySize() {
        if (this.queryNamesAndValues != null) {
            return this.queryNamesAndValues.size() / 2;
        }
        return 0;
    }

    @Nullable
    public final String queryParameter(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, FRAGMENT_ENCODE_SET);
        if (this.queryNamesAndValues == null) {
            return null;
        }
        IntProgression intProgression = RangesKt.step(RangesKt.until(0, this.queryNamesAndValues.size()), 2);
        int n2 = intProgression.getFirst();
        int n3 = intProgression.getLast();
        int n4 = intProgression.getStep();
        if (n4 > 0 && n2 <= n3 || n4 < 0 && n3 <= n2) {
            while (true) {
                if (Intrinsics.areEqual(string, this.queryNamesAndValues.get(n2))) {
                    return this.queryNamesAndValues.get(n2 + 1);
                }
                if (n2 == n3) break;
                n2 += n4;
            }
        }
        return null;
    }

    @JvmName(name="queryParameterNames")
    @NotNull
    public final Set<String> queryParameterNames() {
        if (this.queryNamesAndValues == null) {
            return SetsKt.emptySet();
        }
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        IntProgression intProgression = RangesKt.step(RangesKt.until(0, this.queryNamesAndValues.size()), 2);
        int n2 = intProgression.getFirst();
        int n3 = intProgression.getLast();
        int n4 = intProgression.getStep();
        if (n4 > 0 && n2 <= n3 || n4 < 0 && n3 <= n2) {
            while (true) {
                String string = this.queryNamesAndValues.get(n2);
                Intrinsics.checkNotNull(string);
                linkedHashSet.add(string);
                if (n2 == n3) break;
                n2 += n4;
            }
        }
        Set<String> set = Collections.unmodifiableSet((Set)linkedHashSet);
        Intrinsics.checkNotNullExpressionValue(set, FRAGMENT_ENCODE_SET);
        return set;
    }

    @NotNull
    public final List<String> queryParameterValues(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, FRAGMENT_ENCODE_SET);
        if (this.queryNamesAndValues == null) {
            return CollectionsKt.emptyList();
        }
        List list2 = new ArrayList();
        IntProgression intProgression = RangesKt.step(RangesKt.until(0, this.queryNamesAndValues.size()), 2);
        int n2 = intProgression.getFirst();
        int n3 = intProgression.getLast();
        int n4 = intProgression.getStep();
        if (n4 > 0 && n2 <= n3 || n4 < 0 && n3 <= n2) {
            while (true) {
                if (Intrinsics.areEqual(string, this.queryNamesAndValues.get(n2))) {
                    list2.add(this.queryNamesAndValues.get(n2 + 1));
                }
                if (n2 == n3) break;
                n2 += n4;
            }
        }
        List<String> list3 = Collections.unmodifiableList(list2);
        Intrinsics.checkNotNullExpressionValue(list3, FRAGMENT_ENCODE_SET);
        return list3;
    }

    @NotNull
    public final String queryParameterName(int n2) {
        if (this.queryNamesAndValues == null) {
            throw new IndexOutOfBoundsException();
        }
        String string = this.queryNamesAndValues.get(n2 << 1);
        Intrinsics.checkNotNull(string);
        return string;
    }

    @Nullable
    public final String queryParameterValue(int n2) {
        if (this.queryNamesAndValues == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.queryNamesAndValues.get((n2 << 1) + 1);
    }

    @JvmName(name="encodedFragment")
    @Nullable
    public final String encodedFragment() {
        if (this.fragment == null) {
            return null;
        }
        int n2 = StringsKt.indexOf$default((CharSequence)this.url, '#', 0, false, 6, null) + 1;
        String string = this.url.substring(n2);
        Intrinsics.checkNotNullExpressionValue(string, FRAGMENT_ENCODE_SET);
        return string;
    }

    @NotNull
    public final String redact() {
        Builder builder = this.newBuilder("/...");
        Intrinsics.checkNotNull(builder);
        return builder.username(FRAGMENT_ENCODE_SET).password(FRAGMENT_ENCODE_SET).build().toString();
    }

    @Nullable
    public final HttpUrl resolve(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, FRAGMENT_ENCODE_SET);
        Builder builder = this.newBuilder(string);
        if (builder != null) {
            return builder.build();
        }
        return null;
    }

    @NotNull
    public final Builder newBuilder() {
        Builder builder = new Builder();
        builder.setScheme$okhttp(this.scheme);
        builder.setEncodedUsername$okhttp(this.encodedUsername());
        builder.setEncodedPassword$okhttp(this.encodedPassword());
        builder.setHost$okhttp(this.host);
        builder.setPort$okhttp(this.port != Companion.defaultPort(this.scheme) ? this.port : -1);
        builder.getEncodedPathSegments$okhttp().clear();
        builder.getEncodedPathSegments$okhttp().addAll((Collection<String>)this.encodedPathSegments());
        builder.encodedQuery(this.encodedQuery());
        builder.setEncodedFragment$okhttp(this.encodedFragment());
        return builder;
    }

    @Nullable
    public final Builder newBuilder(@NotNull String object) {
        Intrinsics.checkNotNullParameter(object, FRAGMENT_ENCODE_SET);
        try {
            object = new Builder().parse$okhttp(this, (String)object);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            object = null;
        }
        return object;
    }

    public final boolean equals(@Nullable Object object) {
        return object instanceof HttpUrl && Intrinsics.areEqual(((HttpUrl)object).url, this.url);
    }

    public final int hashCode() {
        return this.url.hashCode();
    }

    @NotNull
    public final String toString() {
        return this.url;
    }

    @Nullable
    public final String topPrivateDomain() {
        if (Util.canParseAsIpAddress(this.host)) {
            return null;
        }
        return PublicSuffixDatabase.Companion.get().getEffectiveTldPlusOne(this.host);
    }

    @Deprecated(message="moved to toUrl()", replaceWith=@ReplaceWith(expression="toUrl()", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_url")
    @NotNull
    public final URL -deprecated_url() {
        return this.url();
    }

    @Deprecated(message="moved to toUri()", replaceWith=@ReplaceWith(expression="toUri()", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_uri")
    @NotNull
    public final URI -deprecated_uri() {
        return this.uri();
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="scheme", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_scheme")
    @NotNull
    public final String -deprecated_scheme() {
        return this.scheme;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="encodedUsername", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_encodedUsername")
    @NotNull
    public final String -deprecated_encodedUsername() {
        return this.encodedUsername();
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="username", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_username")
    @NotNull
    public final String -deprecated_username() {
        return this.username;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="encodedPassword", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_encodedPassword")
    @NotNull
    public final String -deprecated_encodedPassword() {
        return this.encodedPassword();
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="password", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_password")
    @NotNull
    public final String -deprecated_password() {
        return this.password;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="host", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_host")
    @NotNull
    public final String -deprecated_host() {
        return this.host;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="port", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_port")
    public final int -deprecated_port() {
        return this.port;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="pathSize", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_pathSize")
    public final int -deprecated_pathSize() {
        return this.pathSize();
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="encodedPath", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_encodedPath")
    @NotNull
    public final String -deprecated_encodedPath() {
        return this.encodedPath();
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="encodedPathSegments", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_encodedPathSegments")
    @NotNull
    public final List<String> -deprecated_encodedPathSegments() {
        return this.encodedPathSegments();
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="pathSegments", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_pathSegments")
    @NotNull
    public final List<String> -deprecated_pathSegments() {
        return this.pathSegments;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="encodedQuery", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_encodedQuery")
    @Nullable
    public final String -deprecated_encodedQuery() {
        return this.encodedQuery();
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="query", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_query")
    @Nullable
    public final String -deprecated_query() {
        return this.query();
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="querySize", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_querySize")
    public final int -deprecated_querySize() {
        return this.querySize();
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="queryParameterNames", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_queryParameterNames")
    @NotNull
    public final Set<String> -deprecated_queryParameterNames() {
        return this.queryParameterNames();
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="encodedFragment", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_encodedFragment")
    @Nullable
    public final String -deprecated_encodedFragment() {
        return this.encodedFragment();
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="fragment", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_fragment")
    @Nullable
    public final String -deprecated_fragment() {
        return this.fragment;
    }

    @JvmStatic
    public static final int defaultPort(@NotNull String string) {
        return Companion.defaultPort(string);
    }

    @JvmStatic
    @JvmName(name="get")
    @NotNull
    public static final HttpUrl get(@NotNull String string) {
        return Companion.get(string);
    }

    @JvmStatic
    @JvmName(name="parse")
    @Nullable
    public static final HttpUrl parse(@NotNull String string) {
        return Companion.parse(string);
    }

    @JvmStatic
    @JvmName(name="get")
    @Nullable
    public static final HttpUrl get(@NotNull URL uRL) {
        return Companion.get(uRL);
    }

    @JvmStatic
    @JvmName(name="get")
    @Nullable
    public static final HttpUrl get(@NotNull URI uRI) {
        return Companion.get(uRI);
    }

    static {
        char[] cArray = new char[16];
        char[] cArray2 = cArray;
        cArray[0] = 48;
        cArray2[1] = 49;
        cArray2[2] = 50;
        cArray2[3] = 51;
        cArray2[4] = 52;
        cArray2[5] = 53;
        cArray2[6] = 54;
        cArray2[7] = 55;
        cArray2[8] = 56;
        cArray2[9] = 57;
        cArray2[10] = 65;
        cArray2[11] = 66;
        cArray2[12] = 67;
        cArray2[13] = 68;
        cArray2[14] = 69;
        cArray2[15] = 70;
        HEX_DIGITS = cArray2;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010!\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0017\u0018\u0000 V2\u00020\u0001:\u0001VB\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010#\u001a\u00020\u00002\u0006\u0010$\u001a\u00020\u0004J\u000e\u0010%\u001a\u00020\u00002\u0006\u0010\f\u001a\u00020\u0004J\u0018\u0010&\u001a\u00020\u00002\u0006\u0010'\u001a\u00020\u00042\b\u0010(\u001a\u0004\u0018\u00010\u0004J\u000e\u0010)\u001a\u00020\u00002\u0006\u0010*\u001a\u00020\u0004J\u000e\u0010+\u001a\u00020\u00002\u0006\u0010,\u001a\u00020\u0004J\u0018\u0010+\u001a\u00020\u00002\u0006\u0010,\u001a\u00020\u00042\u0006\u0010-\u001a\u00020.H\u0002J\u0018\u0010/\u001a\u00020\u00002\u0006\u00100\u001a\u00020\u00042\b\u00101\u001a\u0004\u0018\u00010\u0004J\u0006\u00102\u001a\u000203J\b\u00104\u001a\u00020\u001bH\u0002J\u0010\u0010\u0003\u001a\u00020\u00002\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\t\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0004J\u000e\u00105\u001a\u00020\u00002\u0006\u00105\u001a\u00020\u0004J\u0010\u00106\u001a\u00020\u00002\b\u00106\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u0014\u001a\u00020\u00002\u0006\u0010\u0014\u001a\u00020\u0004J\u0010\u00107\u001a\u00020\u00002\b\u00107\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0017\u001a\u00020\u0004J\u0010\u00108\u001a\u00020.2\u0006\u00109\u001a\u00020\u0004H\u0002J\u0010\u0010:\u001a\u00020.2\u0006\u00109\u001a\u00020\u0004H\u0002J\u001f\u0010;\u001a\u00020\u00002\b\u0010<\u001a\u0004\u0018\u0001032\u0006\u00109\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\b=J\u000e\u0010>\u001a\u00020\u00002\u0006\u0010>\u001a\u00020\u0004J\b\u0010?\u001a\u00020@H\u0002J\u000e\u0010\u001a\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\u001bJ0\u0010A\u001a\u00020@2\u0006\u00109\u001a\u00020\u00042\u0006\u0010B\u001a\u00020\u001b2\u0006\u0010C\u001a\u00020\u001b2\u0006\u0010D\u001a\u00020.2\u0006\u0010-\u001a\u00020.H\u0002J\u0010\u0010E\u001a\u00020\u00002\b\u0010E\u001a\u0004\u0018\u00010\u0004J\r\u0010F\u001a\u00020\u0000H\u0000\u00a2\u0006\u0002\bGJ\u0010\u0010H\u001a\u00020@2\u0006\u0010I\u001a\u00020\u0004H\u0002J\u000e\u0010J\u001a\u00020\u00002\u0006\u0010'\u001a\u00020\u0004J\u000e\u0010K\u001a\u00020\u00002\u0006\u00100\u001a\u00020\u0004J\u000e\u0010L\u001a\u00020\u00002\u0006\u0010M\u001a\u00020\u001bJ \u0010N\u001a\u00020@2\u0006\u00109\u001a\u00020\u00042\u0006\u0010O\u001a\u00020\u001b2\u0006\u0010C\u001a\u00020\u001bH\u0002J\u000e\u0010 \u001a\u00020\u00002\u0006\u0010 \u001a\u00020\u0004J\u0016\u0010P\u001a\u00020\u00002\u0006\u0010M\u001a\u00020\u001b2\u0006\u0010$\u001a\u00020\u0004J\u0018\u0010Q\u001a\u00020\u00002\u0006\u0010'\u001a\u00020\u00042\b\u0010(\u001a\u0004\u0018\u00010\u0004J\u0016\u0010R\u001a\u00020\u00002\u0006\u0010M\u001a\u00020\u001b2\u0006\u0010*\u001a\u00020\u0004J\u0018\u0010S\u001a\u00020\u00002\u0006\u00100\u001a\u00020\u00042\b\u00101\u001a\u0004\u0018\u00010\u0004J\b\u0010T\u001a\u00020\u0004H\u0016J\u000e\u0010U\u001a\u00020\u00002\u0006\u0010U\u001a\u00020\u0004R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001a\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\rX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR$\u0010\u0010\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\rX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u000f\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\u00020\u0004X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0006\"\u0004\b\u0016\u0010\bR\u001c\u0010\u0017\u001a\u0004\u0018\u00010\u0004X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0006\"\u0004\b\u0019\u0010\bR\u001a\u0010\u001a\u001a\u00020\u001bX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u001c\u0010 \u001a\u0004\u0018\u00010\u0004X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u0006\"\u0004\b\"\u0010\b\u00a8\u0006W"}, d2={"Lokhttp3/HttpUrl$Builder;", "", "()V", "encodedFragment", "", "getEncodedFragment$okhttp", "()Ljava/lang/String;", "setEncodedFragment$okhttp", "(Ljava/lang/String;)V", "encodedPassword", "getEncodedPassword$okhttp", "setEncodedPassword$okhttp", "encodedPathSegments", "", "getEncodedPathSegments$okhttp", "()Ljava/util/List;", "encodedQueryNamesAndValues", "getEncodedQueryNamesAndValues$okhttp", "setEncodedQueryNamesAndValues$okhttp", "(Ljava/util/List;)V", "encodedUsername", "getEncodedUsername$okhttp", "setEncodedUsername$okhttp", "host", "getHost$okhttp", "setHost$okhttp", "port", "", "getPort$okhttp", "()I", "setPort$okhttp", "(I)V", "scheme", "getScheme$okhttp", "setScheme$okhttp", "addEncodedPathSegment", "encodedPathSegment", "addEncodedPathSegments", "addEncodedQueryParameter", "encodedName", "encodedValue", "addPathSegment", "pathSegment", "addPathSegments", "pathSegments", "alreadyEncoded", "", "addQueryParameter", "name", "value", "build", "Lokhttp3/HttpUrl;", "effectivePort", "encodedPath", "encodedQuery", "fragment", "isDot", "input", "isDotDot", "parse", "base", "parse$okhttp", "password", "pop", "", "push", "pos", "limit", "addTrailingSlash", "query", "reencodeForUri", "reencodeForUri$okhttp", "removeAllCanonicalQueryParameters", "canonicalName", "removeAllEncodedQueryParameters", "removeAllQueryParameters", "removePathSegment", "index", "resolvePath", "startPos", "setEncodedPathSegment", "setEncodedQueryParameter", "setPathSegment", "setQueryParameter", "toString", "username", "Companion", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nHttpUrl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 HttpUrl.kt\nokhttp3/HttpUrl$Builder\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,1869:1\n1#2:1870\n1549#3:1871\n1620#3,3:1872\n1549#3:1875\n1620#3,3:1876\n*S KotlinDebug\n*F\n+ 1 HttpUrl.kt\nokhttp3/HttpUrl$Builder\n*L\n1180#1:1871\n1180#1:1872,3\n1181#1:1875\n1181#1:1876,3\n*E\n"})
    public static final class Builder {
        @NotNull
        public static final Companion Companion = new Companion(null);
        @Nullable
        private String scheme;
        @NotNull
        private String encodedUsername = "";
        @NotNull
        private String encodedPassword = "";
        @Nullable
        private String host;
        private int port = -1;
        @NotNull
        private final List<String> encodedPathSegments = new ArrayList();
        @Nullable
        private List<String> encodedQueryNamesAndValues;
        @Nullable
        private String encodedFragment;
        @NotNull
        public static final String INVALID_HOST = "Invalid URL host";

        public Builder() {
            this.encodedPathSegments.add(HttpUrl.FRAGMENT_ENCODE_SET);
        }

        @Nullable
        public final String getScheme$okhttp() {
            return this.scheme;
        }

        public final void setScheme$okhttp(@Nullable String string) {
            this.scheme = string;
        }

        @NotNull
        public final String getEncodedUsername$okhttp() {
            return this.encodedUsername;
        }

        public final void setEncodedUsername$okhttp(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            this.encodedUsername = string;
        }

        @NotNull
        public final String getEncodedPassword$okhttp() {
            return this.encodedPassword;
        }

        public final void setEncodedPassword$okhttp(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            this.encodedPassword = string;
        }

        @Nullable
        public final String getHost$okhttp() {
            return this.host;
        }

        public final void setHost$okhttp(@Nullable String string) {
            this.host = string;
        }

        public final int getPort$okhttp() {
            return this.port;
        }

        public final void setPort$okhttp(int n2) {
            this.port = n2;
        }

        @NotNull
        public final List<String> getEncodedPathSegments$okhttp() {
            return this.encodedPathSegments;
        }

        @Nullable
        public final List<String> getEncodedQueryNamesAndValues$okhttp() {
            return this.encodedQueryNamesAndValues;
        }

        public final void setEncodedQueryNamesAndValues$okhttp(@Nullable List<String> list2) {
            this.encodedQueryNamesAndValues = list2;
        }

        @Nullable
        public final String getEncodedFragment$okhttp() {
            return this.encodedFragment;
        }

        public final void setEncodedFragment$okhttp(@Nullable String string) {
            this.encodedFragment = string;
        }

        @NotNull
        public final Builder scheme(@NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            if (StringsKt.equals(string, "http", true)) {
                builder2.scheme = "http";
            } else if (StringsKt.equals(string, "https", true)) {
                builder2.scheme = "https";
            } else {
                throw new IllegalArgumentException("unexpected scheme: " + string);
            }
            return builder;
        }

        @NotNull
        public final Builder username(@NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            builder.encodedUsername = okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, 0, 0, " \"':;<=>@[]^`{}|/\\?#", false, false, false, false, null, 251, null);
            return builder;
        }

        @NotNull
        public final Builder encodedUsername(@NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            builder.encodedUsername = okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, 0, 0, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, null, 243, null);
            return builder;
        }

        @NotNull
        public final Builder password(@NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            builder.encodedPassword = okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, 0, 0, " \"':;<=>@[]^`{}|/\\?#", false, false, false, false, null, 251, null);
            return builder;
        }

        @NotNull
        public final Builder encodedPassword(@NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            builder.encodedPassword = okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, 0, 0, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, null, 243, null);
            return builder;
        }

        @NotNull
        public final Builder host(@NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            String string2 = HostnamesKt.toCanonicalHost(okhttp3.HttpUrl$Companion.percentDecode$okhttp$default(Companion, string, 0, 0, false, 7, null));
            if (string2 == null) {
                throw new IllegalArgumentException("unexpected host: " + string);
            }
            builder2.host = string = string2;
            return builder;
        }

        @NotNull
        public final Builder port(int n2) {
            Builder builder;
            Builder builder2 = builder = this;
            if (!(n2 > 0 ? n2 < 65536 : false)) {
                String string = "unexpected port: " + n2;
                throw new IllegalArgumentException(string.toString());
            }
            builder2.port = n2;
            return builder;
        }

        private final int effectivePort() {
            if (this.port != -1) {
                return this.port;
            }
            String string = this.scheme;
            Intrinsics.checkNotNull(string);
            return Companion.defaultPort(string);
        }

        @NotNull
        public final Builder addPathSegment(@NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            builder.push(string, 0, string.length(), false, false);
            return builder;
        }

        @NotNull
        public final Builder addPathSegments(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            return this.addPathSegments(string, false);
        }

        @NotNull
        public final Builder addEncodedPathSegment(@NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            builder.push(string, 0, string.length(), false, true);
            return builder;
        }

        @NotNull
        public final Builder addEncodedPathSegments(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            return this.addPathSegments(string, true);
        }

        private final Builder addPathSegments(String string, boolean bl) {
            int n2;
            Builder builder;
            Builder builder2 = builder = this;
            int n3 = 0;
            do {
                boolean bl2 = (n2 = Util.delimiterOffset(string, "/\\", n3, string.length())) < string.length();
                builder2.push(string, n3, n2, bl2, bl);
            } while ((n3 = n2 + 1) <= string.length());
            return builder;
        }

        @NotNull
        public final Builder setPathSegment(int n2, @NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            String string2 = okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, 0, 0, HttpUrl.PATH_SEGMENT_ENCODE_SET, false, false, false, false, null, 251, null);
            if (!(!builder2.isDot(string2) && !builder2.isDotDot(string2))) {
                String string3 = "unexpected path segment: " + string;
                throw new IllegalArgumentException(string3.toString());
            }
            builder2.encodedPathSegments.set(n2, string2);
            return builder;
        }

        @NotNull
        public final Builder setEncodedPathSegment(int n2, @NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            String string2 = okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, 0, 0, HttpUrl.PATH_SEGMENT_ENCODE_SET, true, false, false, false, null, 243, null);
            builder2.encodedPathSegments.set(n2, string2);
            if (!(!builder2.isDot(string2) && !builder2.isDotDot(string2))) {
                String string3 = "unexpected path segment: " + string;
                throw new IllegalArgumentException(string3.toString());
            }
            return builder;
        }

        @NotNull
        public final Builder removePathSegment(int n2) {
            Builder builder;
            Builder builder2 = builder = this;
            builder.encodedPathSegments.remove(n2);
            if (builder2.encodedPathSegments.isEmpty()) {
                builder2.encodedPathSegments.add(HttpUrl.FRAGMENT_ENCODE_SET);
            }
            return builder;
        }

        @NotNull
        public final Builder encodedPath(@NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            if (!StringsKt.startsWith$default(string, "/", false, 2, null)) {
                string = "unexpected encodedPath: " + string;
                throw new IllegalArgumentException(string.toString());
            }
            builder2.resolvePath(string, 0, string.length());
            return builder;
        }

        @NotNull
        public final Builder query(@Nullable String string) {
            Builder builder;
            Builder builder2 = builder = this;
            builder.encodedQueryNamesAndValues = string != null && (string = okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, 0, 0, HttpUrl.QUERY_ENCODE_SET, false, false, true, false, null, 219, null)) != null ? Companion.toQueryNamesAndValues$okhttp(string) : null;
            return builder;
        }

        @NotNull
        public final Builder encodedQuery(@Nullable String string) {
            Builder builder;
            Builder builder2 = builder = this;
            builder.encodedQueryNamesAndValues = string != null && (string = okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, 0, 0, HttpUrl.QUERY_ENCODE_SET, true, false, true, false, null, 211, null)) != null ? Companion.toQueryNamesAndValues$okhttp(string) : null;
            return builder;
        }

        @NotNull
        public final Builder addQueryParameter(@NotNull String string, @Nullable String string2) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            if (builder.encodedQueryNamesAndValues == null) {
                builder2.encodedQueryNamesAndValues = new ArrayList();
            }
            List<String> list2 = builder2.encodedQueryNamesAndValues;
            Intrinsics.checkNotNull(list2);
            list2.add(okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, 0, 0, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, false, true, false, null, 219, null));
            List<String> list3 = builder2.encodedQueryNamesAndValues;
            Intrinsics.checkNotNull(list3);
            String string3 = string2;
            list3.add(string3 != null ? okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string3, 0, 0, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, false, true, false, null, 219, null) : null);
            return builder;
        }

        @NotNull
        public final Builder addEncodedQueryParameter(@NotNull String string, @Nullable String string2) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            if (builder.encodedQueryNamesAndValues == null) {
                builder2.encodedQueryNamesAndValues = new ArrayList();
            }
            List<String> list2 = builder2.encodedQueryNamesAndValues;
            Intrinsics.checkNotNull(list2);
            list2.add(okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, 0, 0, HttpUrl.QUERY_COMPONENT_REENCODE_SET, true, false, true, false, null, 211, null));
            List<String> list3 = builder2.encodedQueryNamesAndValues;
            Intrinsics.checkNotNull(list3);
            String string3 = string2;
            list3.add(string3 != null ? okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string3, 0, 0, HttpUrl.QUERY_COMPONENT_REENCODE_SET, true, false, true, false, null, 211, null) : null);
            return builder;
        }

        @NotNull
        public final Builder setQueryParameter(@NotNull String string, @Nullable String string2) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            builder.removeAllQueryParameters(string);
            builder2.addQueryParameter(string, string2);
            return builder;
        }

        @NotNull
        public final Builder setEncodedQueryParameter(@NotNull String string, @Nullable String string2) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            builder.removeAllEncodedQueryParameters(string);
            builder2.addEncodedQueryParameter(string, string2);
            return builder;
        }

        @NotNull
        public final Builder removeAllQueryParameters(@NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            if (builder.encodedQueryNamesAndValues == null) {
                return builder2;
            }
            string = okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, 0, 0, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, false, true, false, null, 219, null);
            builder2.removeAllCanonicalQueryParameters(string);
            return builder;
        }

        @NotNull
        public final Builder removeAllEncodedQueryParameters(@NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Builder builder2 = builder = this;
            if (builder.encodedQueryNamesAndValues == null) {
                return builder2;
            }
            builder2.removeAllCanonicalQueryParameters(okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, 0, 0, HttpUrl.QUERY_COMPONENT_REENCODE_SET, true, false, true, false, null, 211, null));
            return builder;
        }

        private final void removeAllCanonicalQueryParameters(String string) {
            int n2;
            List<String> list2 = this.encodedQueryNamesAndValues;
            Intrinsics.checkNotNull(list2);
            int n3 = n2 = list2.size() - 2;
            int n4 = ProgressionUtilKt.getProgressionLastElement(n2, 0, -2);
            if (n4 <= n2) {
                while (true) {
                    List<String> list3 = this.encodedQueryNamesAndValues;
                    Intrinsics.checkNotNull(list3);
                    if (Intrinsics.areEqual(string, list3.get(n3))) {
                        List<String> list4 = this.encodedQueryNamesAndValues;
                        Intrinsics.checkNotNull(list4);
                        list4.remove(n3 + 1);
                        List<String> list5 = this.encodedQueryNamesAndValues;
                        Intrinsics.checkNotNull(list5);
                        list5.remove(n3);
                        List<String> list6 = this.encodedQueryNamesAndValues;
                        Intrinsics.checkNotNull(list6);
                        if (list6.isEmpty()) {
                            this.encodedQueryNamesAndValues = null;
                            return;
                        }
                    }
                    if (n3 == n4) break;
                    n3 -= 2;
                }
            }
        }

        @NotNull
        public final Builder fragment(@Nullable String string) {
            Builder builder;
            Builder builder2 = builder = this;
            String string2 = string;
            builder.encodedFragment = string2 != null ? okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string2, 0, 0, HttpUrl.FRAGMENT_ENCODE_SET, false, false, false, true, null, 187, null) : null;
            return builder;
        }

        @NotNull
        public final Builder encodedFragment(@Nullable String string) {
            Builder builder;
            Builder builder2 = builder = this;
            String string2 = string;
            builder.encodedFragment = string2 != null ? okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string2, 0, 0, HttpUrl.FRAGMENT_ENCODE_SET, true, false, false, true, null, 179, null) : null;
            return builder;
        }

        @NotNull
        public final Builder reencodeForUri$okhttp() {
            String string;
            Builder builder;
            Builder builder2 = builder = this;
            String string2 = builder2.host;
            if (string2 != null) {
                CharSequence charSequence = string2;
                Regex regex = new Regex("[\"<>^`{|}]");
                String string3 = HttpUrl.FRAGMENT_ENCODE_SET;
                string = regex.replace(charSequence, string3);
            } else {
                string = null;
            }
            builder.host = string;
            int n2 = builder2.encodedPathSegments.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                builder2.encodedPathSegments.set(i2, okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, builder2.encodedPathSegments.get(i2), 0, 0, HttpUrl.PATH_SEGMENT_ENCODE_SET_URI, true, true, false, false, null, 227, null));
            }
            List<String> list2 = builder2.encodedQueryNamesAndValues;
            if (list2 != null) {
                int n3 = list2.size();
                for (n2 = 0; n2 < n3; ++n2) {
                    String string4 = list2.get(n2);
                    list2.set(n2, string4 != null ? okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string4, 0, 0, HttpUrl.QUERY_COMPONENT_ENCODE_SET_URI, true, true, true, false, null, 195, null) : null);
                }
            }
            String string5 = builder2.encodedFragment;
            builder2.encodedFragment = string5 != null ? okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string5, 0, 0, HttpUrl.FRAGMENT_ENCODE_SET_URI, true, true, false, true, null, 163, null) : null;
            return builder;
        }

        @NotNull
        public final HttpUrl build() {
            Object object;
            Collection<String> collection;
            Object object2;
            String string = this.scheme;
            if (string == null) {
                throw new IllegalStateException("scheme == null");
            }
            String string2 = this.host;
            if (string2 == null) {
                throw new IllegalStateException("host == null");
            }
            Object object3 = this.encodedPathSegments;
            int n2 = this.effectivePort();
            String string3 = string2;
            String string4 = okhttp3.HttpUrl$Companion.percentDecode$okhttp$default(Companion, this.encodedPassword, 0, 0, false, 7, null);
            String string5 = okhttp3.HttpUrl$Companion.percentDecode$okhttp$default(Companion, this.encodedUsername, 0, 0, false, 7, null);
            String string6 = string;
            Object object4 = object3;
            Object object5 = new ArrayList(CollectionsKt.collectionSizeOrDefault(object3, 10));
            object3 = object4.iterator();
            while (object3.hasNext()) {
                object4 = object3.next();
                object2 = (String)object4;
                collection = object5;
                collection.add(okhttp3.HttpUrl$Companion.percentDecode$okhttp$default(Companion, (String)object2, 0, 0, false, 7, null));
            }
            collection = (List)object5;
            String string7 = string6;
            String string8 = string5;
            String string9 = string4;
            String string10 = string3;
            int n3 = n2;
            Collection<String> collection2 = collection;
            List<String> list2 = this.encodedQueryNamesAndValues;
            if (list2 != null) {
                object3 = list2;
                collection = collection2;
                n2 = n3;
                string3 = string10;
                string4 = string9;
                string5 = string8;
                string6 = string7;
                object5 = object3;
                object3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(object3, 10));
                object4 = object5.iterator();
                while (object4.hasNext()) {
                    object2 = object4.next();
                    object5 = (String)object2;
                    object2 = object3;
                    Object object6 = object5;
                    object2.add(object6 != null ? okhttp3.HttpUrl$Companion.percentDecode$okhttp$default(Companion, (String)object6, 0, 0, true, 3, null) : null);
                }
                object2 = (List)object3;
                string7 = string6;
                string8 = string5;
                string9 = string4;
                string10 = string3;
                n3 = 0;
                collection2 = collection;
                object = object2;
            } else {
                object = null;
            }
            String string11 = this.encodedFragment;
            object3 = this.toString();
            object4 = string11 != null ? okhttp3.HttpUrl$Companion.percentDecode$okhttp$default(Companion, string11, 0, 0, false, 7, null) : null;
            object5 = object;
            object2 = collection2;
            string6 = string10;
            string5 = string9;
            string4 = string8;
            string3 = string7;
            return new HttpUrl(string3, string4, string5, string6, n2, (List<String>)object2, (List<String>)object5, (String)object4, (String)object3);
        }

        @NotNull
        public final String toString() {
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            block12: {
                int n2;
                block13: {
                    stringBuilder = stringBuilder2 = new StringBuilder();
                    if (this.scheme != null) {
                        stringBuilder.append(this.scheme);
                        stringBuilder.append("://");
                    } else {
                        stringBuilder.append("//");
                    }
                    if (((CharSequence)this.encodedUsername).length() > 0 || ((CharSequence)this.encodedPassword).length() > 0) {
                        stringBuilder.append(this.encodedUsername);
                        if (((CharSequence)this.encodedPassword).length() > 0) {
                            stringBuilder.append(':');
                            stringBuilder.append(this.encodedPassword);
                        }
                        stringBuilder.append('@');
                    }
                    if (this.host != null) {
                        String string = this.host;
                        Intrinsics.checkNotNull(string);
                        if (StringsKt.contains$default((CharSequence)string, ':', false, 2, null)) {
                            stringBuilder.append('[');
                            stringBuilder.append(this.host);
                            stringBuilder.append(']');
                        } else {
                            stringBuilder.append(this.host);
                        }
                    }
                    if (this.port == -1 && this.scheme == null) break block12;
                    n2 = this.effectivePort();
                    if (this.scheme == null) break block13;
                    String string = this.scheme;
                    Intrinsics.checkNotNull(string);
                    if (n2 == Companion.defaultPort(string)) break block12;
                }
                stringBuilder.append(':');
                stringBuilder.append(n2);
            }
            Companion.toPathString$okhttp(this.encodedPathSegments, stringBuilder);
            if (this.encodedQueryNamesAndValues != null) {
                stringBuilder.append('?');
                List<String> list2 = this.encodedQueryNamesAndValues;
                Intrinsics.checkNotNull(list2);
                Companion.toQueryString$okhttp(list2, stringBuilder);
            }
            if (this.encodedFragment != null) {
                stringBuilder.append('#');
                stringBuilder.append(this.encodedFragment);
            }
            String string = stringBuilder2.toString();
            Intrinsics.checkNotNullExpressionValue(string, HttpUrl.FRAGMENT_ENCODE_SET);
            return string;
        }

        /*
         * Enabled aggressive block sorting
         */
        @NotNull
        public final Builder parse$okhttp(@Nullable HttpUrl httpUrl, @NotNull String string) {
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            int n2 = Util.indexOfFirstNonAsciiWhitespace$default(string, 0, 0, 3, null);
            int n3 = Util.indexOfLastNonAsciiWhitespace$default(string, n2, 0, 2, null);
            int n4 = Builder.Companion.schemeDelimiterOffset(string, n2, n3);
            if (n4 != -1) {
                if (StringsKt.startsWith(string, "https:", n2, true)) {
                    this.scheme = "https";
                    n2 += 6;
                } else {
                    if (!StringsKt.startsWith(string, "http:", n2, true)) {
                        StringBuilder stringBuilder = new StringBuilder("Expected URL scheme 'http' or 'https' but was '");
                        String string2 = string.substring(0, n4);
                        Intrinsics.checkNotNullExpressionValue(string2, HttpUrl.FRAGMENT_ENCODE_SET);
                        throw new IllegalArgumentException(stringBuilder.append(string2).append('\'').toString());
                    }
                    this.scheme = "http";
                    n2 += 5;
                }
            } else if (httpUrl != null) {
                this.scheme = httpUrl.scheme();
            } else {
                String string3 = string.length() > 6 ? StringsKt.take(string, 6) + "..." : string;
                throw new IllegalArgumentException("Expected URL scheme 'http' or 'https' but no scheme was found for " + string3);
            }
            n4 = 0;
            boolean bl = false;
            int n5 = Builder.Companion.slashCount(string, n2, n3);
            if (n5 < 2 && httpUrl != null && Intrinsics.areEqual(httpUrl.scheme(), this.scheme)) {
                this.encodedUsername = httpUrl.encodedUsername();
                this.encodedPassword = httpUrl.encodedPassword();
                this.host = httpUrl.host();
                this.port = httpUrl.port();
                this.encodedPathSegments.clear();
                this.encodedPathSegments.addAll((Collection<String>)httpUrl.encodedPathSegments());
                if (n2 == n3 || string.charAt(n2) == '#') {
                    this.encodedQuery(httpUrl.encodedQuery());
                }
            } else {
                n2 += n5;
                block4: while (true) {
                    int n6;
                    n5 = (n6 = Util.delimiterOffset(string, "@/\\?#", n2, n3)) != n3 ? (int)string.charAt(n6) : -1;
                    switch (n5) {
                        case 64: {
                            if (!bl) {
                                n5 = Util.delimiterOffset(string, ':', n2, n6);
                                String string4 = okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, n2, n5, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, null, 240, null);
                                String string5 = this.encodedUsername = n4 != 0 ? this.encodedUsername + "%40" + string4 : string4;
                                if (n5 != n6) {
                                    bl = true;
                                    this.encodedPassword = okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, n5 + 1, n6, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, null, 240, null);
                                }
                                n4 = 1;
                            } else {
                                this.encodedPassword = this.encodedPassword + "%40" + okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, n2, n6, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, null, 240, null);
                            }
                            n2 = n6 + 1;
                            continue block4;
                        }
                        case -1: 
                        case 35: 
                        case 47: 
                        case 63: 
                        case 92: {
                            n5 = Builder.Companion.portColonOffset(string, n2, n6);
                            if (n5 + 1 < n6) {
                                this.host = HostnamesKt.toCanonicalHost(okhttp3.HttpUrl$Companion.percentDecode$okhttp$default(Companion, string, n2, n5, false, 4, null));
                                this.port = Builder.Companion.parsePort(string, n5 + 1, n6);
                                if (!(this.port != -1)) {
                                    StringBuilder stringBuilder = new StringBuilder("Invalid URL port: \"");
                                    String string6 = string.substring(n5 + 1, n6);
                                    Intrinsics.checkNotNullExpressionValue(string6, HttpUrl.FRAGMENT_ENCODE_SET);
                                    String string7 = stringBuilder.append(string6).append('\"').toString();
                                    throw new IllegalArgumentException(string7.toString());
                                }
                            } else {
                                this.host = HostnamesKt.toCanonicalHost(okhttp3.HttpUrl$Companion.percentDecode$okhttp$default(Companion, string, n2, n5, false, 4, null));
                                String string8 = this.scheme;
                                Intrinsics.checkNotNull(string8);
                                this.port = Companion.defaultPort(string8);
                            }
                            if (!(this.host != null)) {
                                StringBuilder stringBuilder = new StringBuilder("Invalid URL host: \"");
                                String string9 = string.substring(n2, n5);
                                Intrinsics.checkNotNullExpressionValue(string9, HttpUrl.FRAGMENT_ENCODE_SET);
                                String string10 = stringBuilder.append(string9).append('\"').toString();
                                throw new IllegalArgumentException(string10.toString());
                            }
                            n2 = n6;
                            break block4;
                        }
                        default: {
                            continue block4;
                        }
                    }
                    break;
                }
            }
            int n7 = Util.delimiterOffset(string, "?#", n2, n3);
            this.resolvePath(string, n2, n7);
            n2 = n7;
            if (n2 < n3 && string.charAt(n2) == '?') {
                n5 = Util.delimiterOffset(string, '#', n2, n3);
                okhttp3.HttpUrl$Companion companion = Companion;
                this.encodedQueryNamesAndValues = companion.toQueryNamesAndValues$okhttp(okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(companion, string, n2 + 1, n5, HttpUrl.QUERY_ENCODE_SET, true, false, true, false, null, 208, null));
                n2 = n5;
            }
            if (n2 < n3 && string.charAt(n2) == '#') {
                this.encodedFragment = okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, n2 + 1, n3, HttpUrl.FRAGMENT_ENCODE_SET, true, false, false, true, null, 176, null);
            }
            return this;
        }

        private final void resolvePath(String string, int n2, int n3) {
            int n4 = n2;
            if (n2 == n3) {
                return;
            }
            char c2 = string.charAt(n2);
            n2 = c2;
            if (c2 == '/' || n2 == 92) {
                this.encodedPathSegments.clear();
                this.encodedPathSegments.add(HttpUrl.FRAGMENT_ENCODE_SET);
                ++n4;
            } else {
                this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, HttpUrl.FRAGMENT_ENCODE_SET);
            }
            n2 = n4;
            while (n2 < n3) {
                n4 = Util.delimiterOffset(string, "/\\", n2, n3);
                boolean bl = n4 < n3;
                this.push(string, n2, n4, bl, true);
                n2 = n4;
                if (!bl) continue;
                ++n2;
            }
        }

        private final void push(String string, int n2, int n3, boolean bl, boolean bl2) {
            if (this.isDot(string = okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(Companion, string, n2, n3, HttpUrl.PATH_SEGMENT_ENCODE_SET, bl2, false, false, false, null, 240, null))) {
                return;
            }
            if (this.isDotDot(string)) {
                this.pop();
                return;
            }
            if (((CharSequence)this.encodedPathSegments.get(this.encodedPathSegments.size() - 1)).length() == 0) {
                this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, string);
            } else {
                this.encodedPathSegments.add(string);
            }
            if (bl) {
                this.encodedPathSegments.add(HttpUrl.FRAGMENT_ENCODE_SET);
            }
        }

        private final boolean isDot(String string) {
            return Intrinsics.areEqual(string, ".") || StringsKt.equals(string, "%2e", true);
        }

        private final boolean isDotDot(String string) {
            return Intrinsics.areEqual(string, "..") || StringsKt.equals(string, "%2e.", true) || StringsKt.equals(string, ".%2e", true) || StringsKt.equals(string, "%2e%2e", true);
        }

        private final void pop() {
            String string = this.encodedPathSegments.remove(this.encodedPathSegments.size() - 1);
            if (((CharSequence)string).length() == 0 && !((Collection)this.encodedPathSegments).isEmpty()) {
                this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, HttpUrl.FRAGMENT_ENCODE_SET);
                return;
            }
            this.encodedPathSegments.add(HttpUrl.FRAGMENT_ENCODE_SET);
        }

        @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0006H\u0002J \u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0006H\u0002J \u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0006H\u0002J\u001c\u0010\f\u001a\u00020\u0006*\u00020\u00042\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0006H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"Lokhttp3/HttpUrl$Builder$Companion;", "", "()V", "INVALID_HOST", "", "parsePort", "", "input", "pos", "limit", "portColonOffset", "schemeDelimiterOffset", "slashCount", "okhttp"})
        public static final class Companion {
            private Companion() {
            }

            private final int schemeDelimiterOffset(String string, int n2, int n3) {
                if (n3 - n2 < 2) {
                    return -1;
                }
                char c2 = string.charAt(n2);
                if (!(Intrinsics.compare(c2, 97) >= 0 && Intrinsics.compare(c2, 122) <= 0 || Intrinsics.compare(c2, 65) >= 0 && Intrinsics.compare(c2, 90) <= 0)) {
                    return -1;
                }
                ++n2;
                while (n2 < n3) {
                    c2 = string.charAt(n2);
                    if (!(((((('a' <= c2 ? c2 < '{' : false) ? true : ('A' <= c2 ? c2 < '[' : false)) ? true : ('0' <= c2 ? c2 < ':' : false)) ? true : c2 == '+') ? true : c2 == '-') ? true : c2 == '.')) {
                        if (c2 == ':') {
                            return n2;
                        }
                        return -1;
                    }
                    ++n2;
                }
                return -1;
            }

            private final int slashCount(String string, int n2, int n3) {
                char c2;
                int n4 = 0;
                while (n2 < n3 && ((c2 = string.charAt(n2)) == '\\' || c2 == '/')) {
                    ++n4;
                    ++n2;
                }
                return n4;
            }

            private final int portColonOffset(String string, int n2, int n3) {
                while (n2 < n3) {
                    char c2 = string.charAt(n2);
                    if (c2 == '[') {
                        while (++n2 < n3 && string.charAt(n2) != ']') {
                        }
                    } else if (c2 == ':') {
                        return n2;
                    }
                    ++n2;
                }
                return n3;
            }

            private final int parsePort(String string, int n2, int n3) {
                int n4;
                try {
                    string = okhttp3.HttpUrl$Companion.canonicalize$okhttp$default(HttpUrl.Companion, string, n2, n3, HttpUrl.FRAGMENT_ENCODE_SET, false, false, false, false, null, 248, null);
                    n4 = Integer.parseInt(string);
                    n4 = (n4 > 0 ? n4 < 65536 : false) ? n4 : -1;
                }
                catch (NumberFormatException numberFormatException) {
                    n4 = -1;
                }
                return n4;
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000p\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0019\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0004H\u0007J\u0017\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0007\u00a2\u0006\u0002\b\u0018J\u0017\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0019\u001a\u00020\u001aH\u0007\u00a2\u0006\u0002\b\u0018J\u0015\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u0004H\u0007\u00a2\u0006\u0002\b\u0018J\u0017\u0010\u001b\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0019\u001a\u00020\u0004H\u0007\u00a2\u0006\u0002\b\u001cJa\u0010\u001d\u001a\u00020\u0004*\u00020\u00042\b\b\u0002\u0010\u001e\u001a\u00020\u00122\b\b\u0002\u0010\u001f\u001a\u00020\u00122\u0006\u0010 \u001a\u00020\u00042\b\b\u0002\u0010!\u001a\u00020\"2\b\b\u0002\u0010#\u001a\u00020\"2\b\b\u0002\u0010$\u001a\u00020\"2\b\b\u0002\u0010%\u001a\u00020\"2\n\b\u0002\u0010&\u001a\u0004\u0018\u00010'H\u0000\u00a2\u0006\u0002\b(J\u001c\u0010)\u001a\u00020\"*\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u0012H\u0002J/\u0010*\u001a\u00020\u0004*\u00020\u00042\b\b\u0002\u0010\u001e\u001a\u00020\u00122\b\b\u0002\u0010\u001f\u001a\u00020\u00122\b\b\u0002\u0010$\u001a\u00020\"H\u0000\u00a2\u0006\u0002\b+J\u0011\u0010,\u001a\u00020\u0015*\u00020\u0004H\u0007\u00a2\u0006\u0002\b\u0014J\u0013\u0010-\u001a\u0004\u0018\u00010\u0015*\u00020\u0017H\u0007\u00a2\u0006\u0002\b\u0014J\u0013\u0010-\u001a\u0004\u0018\u00010\u0015*\u00020\u001aH\u0007\u00a2\u0006\u0002\b\u0014J\u0013\u0010-\u001a\u0004\u0018\u00010\u0015*\u00020\u0004H\u0007\u00a2\u0006\u0002\b\u001bJ#\u0010.\u001a\u00020/*\b\u0012\u0004\u0012\u00020\u0004002\n\u00101\u001a\u000602j\u0002`3H\u0000\u00a2\u0006\u0002\b4J\u0019\u00105\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000406*\u00020\u0004H\u0000\u00a2\u0006\u0002\b7J%\u00108\u001a\u00020/*\n\u0012\u0006\u0012\u0004\u0018\u00010\u0004002\n\u00101\u001a\u000602j\u0002`3H\u0000\u00a2\u0006\u0002\b9JV\u0010:\u001a\u00020/*\u00020;2\u0006\u0010<\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u00122\u0006\u0010 \u001a\u00020\u00042\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\"2\u0006\u0010$\u001a\u00020\"2\u0006\u0010%\u001a\u00020\"2\b\u0010&\u001a\u0004\u0018\u00010'H\u0002J,\u0010=\u001a\u00020/*\u00020;2\u0006\u0010>\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u00122\u0006\u0010$\u001a\u00020\"H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000\u00a8\u0006?"}, d2={"Lokhttp3/HttpUrl$Companion;", "", "()V", "FORM_ENCODE_SET", "", "FRAGMENT_ENCODE_SET", "FRAGMENT_ENCODE_SET_URI", "HEX_DIGITS", "", "PASSWORD_ENCODE_SET", "PATH_SEGMENT_ENCODE_SET", "PATH_SEGMENT_ENCODE_SET_URI", "QUERY_COMPONENT_ENCODE_SET", "QUERY_COMPONENT_ENCODE_SET_URI", "QUERY_COMPONENT_REENCODE_SET", "QUERY_ENCODE_SET", "USERNAME_ENCODE_SET", "defaultPort", "", "scheme", "get", "Lokhttp3/HttpUrl;", "uri", "Ljava/net/URI;", "-deprecated_get", "url", "Ljava/net/URL;", "parse", "-deprecated_parse", "canonicalize", "pos", "limit", "encodeSet", "alreadyEncoded", "", "strict", "plusIsSpace", "unicodeAllowed", "charset", "Ljava/nio/charset/Charset;", "canonicalize$okhttp", "isPercentEncoded", "percentDecode", "percentDecode$okhttp", "toHttpUrl", "toHttpUrlOrNull", "toPathString", "", "", "out", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "toPathString$okhttp", "toQueryNamesAndValues", "", "toQueryNamesAndValues$okhttp", "toQueryString", "toQueryString$okhttp", "writeCanonicalized", "Lokio/Buffer;", "input", "writePercentDecoded", "encoded", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        public final int defaultPort(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            if (Intrinsics.areEqual(string, "http")) {
                return 80;
            }
            if (Intrinsics.areEqual(string, "https")) {
                return 443;
            }
            return -1;
        }

        public final void toPathString$okhttp(@NotNull List<String> list2, @NotNull StringBuilder stringBuilder) {
            Intrinsics.checkNotNullParameter(list2, HttpUrl.FRAGMENT_ENCODE_SET);
            Intrinsics.checkNotNullParameter(stringBuilder, HttpUrl.FRAGMENT_ENCODE_SET);
            int n2 = list2.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                stringBuilder.append('/');
                stringBuilder.append(list2.get(i2));
            }
        }

        public final void toQueryString$okhttp(@NotNull List<String> list2, @NotNull StringBuilder stringBuilder) {
            Intrinsics.checkNotNullParameter(list2, HttpUrl.FRAGMENT_ENCODE_SET);
            Intrinsics.checkNotNullParameter(stringBuilder, HttpUrl.FRAGMENT_ENCODE_SET);
            IntProgression intProgression = RangesKt.step(RangesKt.until(0, list2.size()), 2);
            int n2 = intProgression.getFirst();
            int n3 = intProgression.getLast();
            int n4 = intProgression.getStep();
            if (n4 > 0 && n2 <= n3 || n4 < 0 && n3 <= n2) {
                while (true) {
                    String string = list2.get(n2);
                    String string2 = list2.get(n2 + 1);
                    if (n2 > 0) {
                        stringBuilder.append('&');
                    }
                    stringBuilder.append(string);
                    if (string2 != null) {
                        stringBuilder.append('=');
                        stringBuilder.append(string2);
                    }
                    if (n2 == n3) break;
                    n2 += n4;
                }
            }
        }

        @NotNull
        public final List<String> toQueryNamesAndValues$okhttp(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            List list2 = new ArrayList();
            int n2 = 0;
            while (n2 <= string.length()) {
                int n3;
                int n4 = StringsKt.indexOf$default((CharSequence)string, '&', n2, false, 4, null);
                if (n4 == -1) {
                    n4 = string.length();
                }
                if ((n3 = StringsKt.indexOf$default((CharSequence)string, '=', n2, false, 4, null)) == -1 || n3 > n4) {
                    String string2 = string.substring(n2, n4);
                    Intrinsics.checkNotNullExpressionValue(string2, HttpUrl.FRAGMENT_ENCODE_SET);
                    list2.add(string2);
                    list2.add(null);
                } else {
                    String string3 = string.substring(n2, n3);
                    Intrinsics.checkNotNullExpressionValue(string3, HttpUrl.FRAGMENT_ENCODE_SET);
                    list2.add(string3);
                    String string4 = string.substring(n3 + 1, n4);
                    Intrinsics.checkNotNullExpressionValue(string4, HttpUrl.FRAGMENT_ENCODE_SET);
                    list2.add(string4);
                }
                n2 = n4 + 1;
            }
            return list2;
        }

        @JvmStatic
        @JvmName(name="get")
        @NotNull
        public final HttpUrl get(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            return new Builder().parse$okhttp(null, string).build();
        }

        @JvmStatic
        @JvmName(name="parse")
        @Nullable
        public final HttpUrl parse(@NotNull String object) {
            Intrinsics.checkNotNullParameter(object, HttpUrl.FRAGMENT_ENCODE_SET);
            try {
                object = this.get((String)object);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                object = null;
            }
            return object;
        }

        @JvmStatic
        @JvmName(name="get")
        @Nullable
        public final HttpUrl get(@NotNull URL uRL) {
            Intrinsics.checkNotNullParameter(uRL, HttpUrl.FRAGMENT_ENCODE_SET);
            String string = uRL.toString();
            Intrinsics.checkNotNullExpressionValue(string, HttpUrl.FRAGMENT_ENCODE_SET);
            return this.parse(string);
        }

        @JvmStatic
        @JvmName(name="get")
        @Nullable
        public final HttpUrl get(@NotNull URI uRI) {
            Intrinsics.checkNotNullParameter(uRI, HttpUrl.FRAGMENT_ENCODE_SET);
            String string = uRI.toString();
            Intrinsics.checkNotNullExpressionValue(string, HttpUrl.FRAGMENT_ENCODE_SET);
            return this.parse(string);
        }

        @Deprecated(message="moved to extension function", replaceWith=@ReplaceWith(expression="url.toHttpUrl()", imports={"okhttp3.HttpUrl.Companion.toHttpUrl"}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_get")
        @NotNull
        public final HttpUrl -deprecated_get(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            return this.get(string);
        }

        @Deprecated(message="moved to extension function", replaceWith=@ReplaceWith(expression="url.toHttpUrlOrNull()", imports={"okhttp3.HttpUrl.Companion.toHttpUrlOrNull"}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_parse")
        @Nullable
        public final HttpUrl -deprecated_parse(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            return this.parse(string);
        }

        @Deprecated(message="moved to extension function", replaceWith=@ReplaceWith(expression="url.toHttpUrlOrNull()", imports={"okhttp3.HttpUrl.Companion.toHttpUrlOrNull"}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_get")
        @Nullable
        public final HttpUrl -deprecated_get(@NotNull URL uRL) {
            Intrinsics.checkNotNullParameter(uRL, HttpUrl.FRAGMENT_ENCODE_SET);
            return this.get(uRL);
        }

        @Deprecated(message="moved to extension function", replaceWith=@ReplaceWith(expression="uri.toHttpUrlOrNull()", imports={"okhttp3.HttpUrl.Companion.toHttpUrlOrNull"}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_get")
        @Nullable
        public final HttpUrl -deprecated_get(@NotNull URI uRI) {
            Intrinsics.checkNotNullParameter(uRI, HttpUrl.FRAGMENT_ENCODE_SET);
            return this.get(uRI);
        }

        @NotNull
        public final String percentDecode$okhttp(@NotNull String string, int n2, int n3, boolean bl) {
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            for (int i2 = n2; i2 < n3; ++i2) {
                char c2 = string.charAt(i2);
                if (c2 != '%' && (c2 != '+' || !bl)) continue;
                Buffer buffer = new Buffer();
                buffer.writeUtf8(string, n2, i2);
                this.writePercentDecoded(buffer, string, i2, n3, bl);
                return buffer.readUtf8();
            }
            String string2 = string.substring(n2, n3);
            Intrinsics.checkNotNullExpressionValue(string2, HttpUrl.FRAGMENT_ENCODE_SET);
            return string2;
        }

        public static /* synthetic */ String percentDecode$okhttp$default(Companion companion, String string, int n2, int n3, boolean bl, int n4, Object object) {
            if ((n4 & 1) != 0) {
                n2 = 0;
            }
            if ((n4 & 2) != 0) {
                n3 = string.length();
            }
            if ((n4 & 4) != 0) {
                bl = false;
            }
            return companion.percentDecode$okhttp(string, n2, n3, bl);
        }

        private final void writePercentDecoded(Buffer buffer, String string, int n2, int n3, boolean bl) {
            int n4 = n2;
            while (n4 < n3) {
                n2 = string.codePointAt(n4);
                if (n2 == 37 && n4 + 2 < n3) {
                    int n5 = Util.parseHexDigit(string.charAt(n4 + 1));
                    int n6 = Util.parseHexDigit(string.charAt(n4 + 2));
                    if (n5 != -1 && n6 != -1) {
                        buffer.writeByte((n5 << 4) + n6);
                        n4 += 2;
                        n4 += Character.charCount(n2);
                        continue;
                    }
                } else if (n2 == 43 && bl) {
                    buffer.writeByte(32);
                    ++n4;
                    continue;
                }
                buffer.writeUtf8CodePoint(n2);
                n4 += Character.charCount(n2);
            }
        }

        private final boolean isPercentEncoded(String string, int n2, int n3) {
            return n2 + 2 < n3 && string.charAt(n2) == '%' && Util.parseHexDigit(string.charAt(n2 + 1)) != -1 && Util.parseHexDigit(string.charAt(n2 + 2)) != -1;
        }

        @NotNull
        public final String canonicalize$okhttp(@NotNull String string, int n2, int n3, @NotNull String string2, boolean bl, boolean bl2, boolean bl3, boolean bl4, @Nullable Charset charset) {
            int n4;
            Intrinsics.checkNotNullParameter(string, HttpUrl.FRAGMENT_ENCODE_SET);
            Intrinsics.checkNotNullParameter(string2, HttpUrl.FRAGMENT_ENCODE_SET);
            for (int i2 = n2; i2 < n3; i2 += Character.charCount(n4)) {
                n4 = string.codePointAt(i2);
                if (!(n4 < 32 || n4 == 127 || n4 >= 128 && !bl4 || StringsKt.contains$default((CharSequence)string2, (char)n4, false, 2, null) || n4 == 37 && (!bl || bl2 && !this.isPercentEncoded(string, i2, n3))) && (n4 != 43 || !bl3)) continue;
                Buffer buffer = new Buffer();
                buffer.writeUtf8(string, n2, i2);
                this.writeCanonicalized(buffer, string, i2, n3, string2, bl, bl2, bl3, bl4, charset);
                return buffer.readUtf8();
            }
            String string3 = string.substring(n2, n3);
            Intrinsics.checkNotNullExpressionValue(string3, HttpUrl.FRAGMENT_ENCODE_SET);
            return string3;
        }

        public static /* synthetic */ String canonicalize$okhttp$default(Companion companion, String string, int n2, int n3, String string2, boolean bl, boolean bl2, boolean bl3, boolean bl4, Charset charset, int n4, Object object) {
            if ((n4 & 1) != 0) {
                n2 = 0;
            }
            if ((n4 & 2) != 0) {
                n3 = string.length();
            }
            if ((n4 & 8) != 0) {
                bl = false;
            }
            if ((n4 & 0x10) != 0) {
                bl2 = false;
            }
            if ((n4 & 0x20) != 0) {
                bl3 = false;
            }
            if ((n4 & 0x40) != 0) {
                bl4 = false;
            }
            if ((n4 & 0x80) != 0) {
                charset = null;
            }
            return companion.canonicalize$okhttp(string, n2, n3, string2, bl, bl2, bl3, bl4, charset);
        }

        private final void writeCanonicalized(Buffer buffer, String string, int n2, int n3, String string2, boolean bl, boolean bl2, boolean bl3, boolean bl4, Charset charset) {
            Buffer buffer2 = null;
            for (int i2 = n2; i2 < n3; i2 += Character.charCount(n2)) {
                n2 = string.codePointAt(i2);
                if (bl && (n2 == 9 || n2 == 10 || n2 == 12 || n2 == 13)) continue;
                if (n2 == 43 && bl3) {
                    buffer.writeUtf8(bl ? "+" : "%2B");
                    continue;
                }
                if (n2 < 32 || n2 == 127 || n2 >= 128 && !bl4 || StringsKt.contains$default((CharSequence)string2, (char)n2, false, 2, null) || n2 == 37 && (!bl || bl2 && !this.isPercentEncoded(string, i2, n3))) {
                    if (buffer2 == null) {
                        buffer2 = new Buffer();
                    }
                    if (charset == null || Intrinsics.areEqual(charset, StandardCharsets.UTF_8)) {
                        buffer2.writeUtf8CodePoint(n2);
                    } else {
                        int n4 = i2;
                        buffer2.writeString(string, n4, n4 + Character.charCount(n2), charset);
                    }
                    while (!buffer2.exhausted()) {
                        int n5 = buffer2.readByte() & 0xFF;
                        buffer.writeByte(37);
                        buffer.writeByte(HEX_DIGITS[n5 >> 4 & 0xF]);
                        buffer.writeByte(HEX_DIGITS[n5 & 0xF]);
                    }
                    continue;
                }
                buffer.writeUtf8CodePoint(n2);
            }
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

