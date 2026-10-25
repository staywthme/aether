/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.tls;

import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import okhttp3.internal.HostnamesKt;
import okhttp3.internal.Util;
import okio.Utf8;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\nJ\u001e\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\u0004H\u0002J\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0018\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u001c\u0010\u0012\u001a\u00020\u000e2\b\u0010\u0013\u001a\u0004\u0018\u00010\b2\b\u0010\u0014\u001a\u0004\u0018\u00010\bH\u0002J\u0018\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\f\u0010\u0017\u001a\u00020\b*\u00020\bH\u0002J\f\u0010\u0018\u001a\u00020\u000e*\u00020\bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2={"Lokhttp3/internal/tls/OkHostnameVerifier;", "Ljavax/net/ssl/HostnameVerifier;", "()V", "ALT_DNS_NAME", "", "ALT_IPA_NAME", "allSubjectAltNames", "", "", "certificate", "Ljava/security/cert/X509Certificate;", "getSubjectAltNames", "type", "verify", "", "host", "session", "Ljavax/net/ssl/SSLSession;", "verifyHostname", "hostname", "pattern", "verifyIpAddress", "ipAddress", "asciiToLowercase", "isAscii", "okhttp"})
@SourceDebugExtension(value={"SMAP\nOkHostnameVerifier.kt\nKotlin\n*S Kotlin\n*F\n+ 1 OkHostnameVerifier.kt\nokhttp3/internal/tls/OkHostnameVerifier\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,208:1\n1747#2,3:209\n1747#2,3:212\n*S KotlinDebug\n*F\n+ 1 OkHostnameVerifier.kt\nokhttp3/internal/tls/OkHostnameVerifier\n*L\n63#1:209,3\n71#1:212,3\n*E\n"})
public final class OkHostnameVerifier
implements HostnameVerifier {
    @NotNull
    public static final OkHostnameVerifier INSTANCE = new OkHostnameVerifier();
    private static final int ALT_DNS_NAME = 2;
    private static final int ALT_IPA_NAME = 7;

    private OkHostnameVerifier() {
    }

    @Override
    public final boolean verify(@NotNull String string, @NotNull SSLSession sSLSession) {
        boolean bl;
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(sSLSession, "");
        if (!this.isAscii(string)) {
            return false;
        }
        try {
            Certificate certificate = sSLSession.getPeerCertificates()[0];
            Intrinsics.checkNotNull(certificate);
            bl = this.verify(string, (X509Certificate)certificate);
        }
        catch (SSLException sSLException) {
            bl = false;
        }
        return bl;
    }

    public final boolean verify(@NotNull String string, @NotNull X509Certificate x509Certificate) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(x509Certificate, "");
        if (Util.canParseAsIpAddress(string)) {
            return this.verifyIpAddress(string, x509Certificate);
        }
        return this.verifyHostname(string, x509Certificate);
    }

    private final boolean verifyIpAddress(String string, X509Certificate iterator2) {
        string = HostnamesKt.toCanonicalHost(string);
        if (!((iterator2 = (Iterable)this.getSubjectAltNames((X509Certificate)((Object)iterator2), 7)) instanceof Collection) || !((Collection)((Object)iterator2)).isEmpty()) {
            iterator2 = iterator2.iterator();
            while (iterator2.hasNext()) {
                Object object = iterator2.next();
                if (!Intrinsics.areEqual(string, HostnamesKt.toCanonicalHost(object = (String)object))) continue;
                return true;
            }
        }
        return false;
    }

    private final boolean verifyHostname(String string, X509Certificate iterator2) {
        string = this.asciiToLowercase(string);
        if (!((iterator2 = (Iterable)this.getSubjectAltNames((X509Certificate)((Object)iterator2), 2)) instanceof Collection) || !((Collection)((Object)iterator2)).isEmpty()) {
            iterator2 = iterator2.iterator();
            while (iterator2.hasNext()) {
                Object object = iterator2.next();
                if (!INSTANCE.verifyHostname(string, (String)(object = (String)object))) continue;
                return true;
            }
        }
        return false;
    }

    private final String asciiToLowercase(String string) {
        if (this.isAscii(string)) {
            Locale locale = Locale.US;
            Intrinsics.checkNotNullExpressionValue(locale, "");
            String string2 = string.toLowerCase(locale);
            Intrinsics.checkNotNullExpressionValue(string2, "");
            return string2;
        }
        return string;
    }

    private final boolean isAscii(String string) {
        return string.length() == (int)Utf8.size$default(string, 0, 0, 3, null);
    }

    private final boolean verifyHostname(String string, String string2) {
        CharSequence charSequence = string;
        if (charSequence == null || charSequence.length() == 0 || StringsKt.startsWith$default(string, ".", false, 2, null) || StringsKt.endsWith$default(string, "..", false, 2, null)) {
            return false;
        }
        charSequence = string2;
        if (charSequence == null || charSequence.length() == 0 || StringsKt.startsWith$default(string2, ".", false, 2, null) || StringsKt.endsWith$default(string2, "..", false, 2, null)) {
            return false;
        }
        if (!StringsKt.endsWith$default(string, ".", false, 2, null)) {
            string = string + '.';
        }
        if (!StringsKt.endsWith$default(string2, ".", false, 2, null)) {
            string2 = string2 + '.';
        }
        if (!StringsKt.contains$default((CharSequence)(string2 = this.asciiToLowercase(string2)), "*", false, 2, null)) {
            return Intrinsics.areEqual(string, string2);
        }
        if (!StringsKt.startsWith$default(string2, "*.", false, 2, null) || StringsKt.indexOf$default((CharSequence)string2, '*', 1, false, 4, null) != -1) {
            return false;
        }
        if (string.length() < string2.length()) {
            return false;
        }
        if (Intrinsics.areEqual("*.", string2)) {
            return false;
        }
        String string3 = string2.substring(1);
        Intrinsics.checkNotNullExpressionValue(string3, "");
        charSequence = string3;
        if (!StringsKt.endsWith$default(string, (String)charSequence, false, 2, null)) {
            return false;
        }
        int n2 = string.length() - ((String)charSequence).length();
        return n2 <= 0 || StringsKt.lastIndexOf$default((CharSequence)string, '.', n2 - 1, false, 4, null) == -1;
    }

    @NotNull
    public final List<String> allSubjectAltNames(@NotNull X509Certificate object) {
        Intrinsics.checkNotNullParameter(object, "");
        List<String> list2 = this.getSubjectAltNames((X509Certificate)object, 7);
        object = this.getSubjectAltNames((X509Certificate)object, 2);
        return CollectionsKt.plus((Collection)list2, (Iterable)object);
    }

    private final List<String> getSubjectAltNames(X509Certificate iterator2, int n2) {
        try {
            Collection<List<?>> collection = ((X509Certificate)((Object)iterator2)).getSubjectAlternativeNames();
            if (collection == null) {
                return CollectionsKt.emptyList();
            }
            iterator2 = collection;
            List list2 = new ArrayList();
            iterator2 = iterator2.iterator();
            while (iterator2.hasNext()) {
                List list3 = (List)iterator2.next();
                if (list3 == null || list3.size() < 2 || !Intrinsics.areEqual(list3.get(0), (Object)n2)) continue;
                Object e2 = list3.get(1);
                if (e2 == null) continue;
                list3 = e2;
                list2.add((String)((Object)list3));
            }
            return list2;
        }
        catch (CertificateParsingException certificateParsingException) {
            return CollectionsKt.emptyList();
        }
    }
}

