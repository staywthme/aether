/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import org.conscrypt.ConscryptHostnameVerifier;

public final class OkHostnameVerifier
implements ConscryptHostnameVerifier {
    public static final OkHostnameVerifier INSTANCE = new OkHostnameVerifier(false);
    private static final Pattern VERIFY_AS_IP_ADDRESS = Pattern.compile("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
    private static final int ALT_DNS_NAME = 2;
    private static final int ALT_IPA_NAME = 7;
    private final boolean strictWildcardMode;

    private OkHostnameVerifier(boolean bl) {
        this.strictWildcardMode = bl;
    }

    public static OkHostnameVerifier strictInstance() {
        return new OkHostnameVerifier(true);
    }

    @Override
    public final boolean verify(X509Certificate[] certificateArray, String string, SSLSession sSLSession) {
        if (certificateArray.length > 0) {
            return this.verify(string, certificateArray[0]);
        }
        try {
            certificateArray = sSLSession.getPeerCertificates();
            return this.verify(string, (X509Certificate)certificateArray[0]);
        }
        catch (SSLException sSLException) {
            return false;
        }
    }

    public final boolean verify(String string, X509Certificate x509Certificate) {
        if (OkHostnameVerifier.verifyAsIpAddress(string)) {
            return this.verifyIpAddress(string, x509Certificate);
        }
        return this.verifyHostName(string, x509Certificate);
    }

    static boolean verifyAsIpAddress(String string) {
        return VERIFY_AS_IP_ADDRESS.matcher(string).matches();
    }

    private boolean verifyIpAddress(String string, X509Certificate object) {
        object = OkHostnameVerifier.getSubjectAltNames((X509Certificate)object, 7);
        int n2 = object.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!string.equalsIgnoreCase((String)object.get(i2))) continue;
            return true;
        }
        return false;
    }

    private boolean verifyHostName(String string, X509Certificate object) {
        string = string.toLowerCase(Locale.US);
        object = OkHostnameVerifier.getSubjectAltNames((X509Certificate)object, 2);
        int n2 = object.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!this.verifyHostName(string, (String)object.get(i2))) continue;
            return true;
        }
        return false;
    }

    public static List<String> allSubjectAltNames(X509Certificate object) {
        List<String> list2 = OkHostnameVerifier.getSubjectAltNames((X509Certificate)object, 7);
        object = OkHostnameVerifier.getSubjectAltNames((X509Certificate)object, 2);
        ArrayList<String> arrayList = new ArrayList<String>(list2.size() + object.size());
        arrayList.addAll(list2);
        arrayList.addAll((Collection<String>)object);
        return arrayList;
    }

    private static List<String> getSubjectAltNames(X509Certificate iterator2, int n2) {
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            iterator2 = ((X509Certificate)((Object)iterator2)).getSubjectAlternativeNames();
            if (iterator2 == null) {
                return Collections.emptyList();
            }
            iterator2 = iterator2.iterator();
            while (iterator2.hasNext()) {
                Integer n3;
                Object object = iterator2.next();
                if ((object = (List)object) == null || object.size() < 2 || (n3 = (Integer)object.get(0)) == null || n3 != n2 || (object = (String)object.get(1)) == null) continue;
                arrayList.add((String)object);
            }
            return arrayList;
        }
        catch (CertificateParsingException certificateParsingException) {
            return Collections.emptyList();
        }
    }

    private boolean verifyHostName(String string, String string2) {
        String string3;
        if (string == null || string.length() == 0 || string.startsWith(".") || string.endsWith("..")) {
            return false;
        }
        if (string2 == null || string2.length() == 0 || string2.startsWith(".") || string2.endsWith("..")) {
            return false;
        }
        if (!string.endsWith(".")) {
            string = string + '.';
        }
        if (!string2.endsWith(".")) {
            string2 = string2 + '.';
        }
        if (!(string2 = string2.toLowerCase(Locale.US)).contains("*")) {
            return string.equals(string2);
        }
        if (!string2.startsWith("*.") || string2.indexOf(42, 1) != -1) {
            return false;
        }
        if (string.length() < string2.length()) {
            return false;
        }
        if ("*.".equals(string2)) {
            return false;
        }
        if (this.strictWildcardMode && (string3 = string2.substring(2, string2.length() - 1)).indexOf(46) < 0) {
            return false;
        }
        string3 = string2.substring(1);
        if (!string.endsWith(string3)) {
            return false;
        }
        int n2 = string.length() - string3.length();
        return n2 <= 0 || string.lastIndexOf(46, n2 - 1) == -1;
    }
}

