/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.Serializable;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.x500.X500Principal;

public final class TrustedCertificateIndex {
    private final Map<X500Principal, List<TrustAnchor>> subjectToTrustAnchors = new HashMap<X500Principal, List<TrustAnchor>>();

    public TrustedCertificateIndex() {
    }

    public TrustedCertificateIndex(Set<TrustAnchor> set) {
        this.index(set);
    }

    private void index(Set<TrustAnchor> object) {
        object = object.iterator();
        while (object.hasNext()) {
            TrustAnchor trustAnchor = (TrustAnchor)object.next();
            this.index(trustAnchor);
        }
    }

    public final TrustAnchor index(X509Certificate object) {
        object = new TrustAnchor((X509Certificate)object, null);
        this.index((TrustAnchor)object);
        return object;
    }

    public final void index(TrustAnchor trustAnchor) {
        X509Certificate x509Certificate = trustAnchor.getTrustedCert();
        X500Principal x500Principal = x509Certificate != null ? x509Certificate.getSubjectX500Principal() : trustAnchor.getCA();
        Map<X500Principal, List<TrustAnchor>> map = this.subjectToTrustAnchors;
        synchronized (map) {
            List<TrustAnchor> list2 = this.subjectToTrustAnchors.get(x500Principal);
            if (list2 == null) {
                list2 = new ArrayList<TrustAnchor>(1);
                this.subjectToTrustAnchors.put(x500Principal, list2);
            } else if (x509Certificate != null) {
                for (TrustAnchor trustAnchor2 : list2) {
                    if (!x509Certificate.equals(trustAnchor2.getTrustedCert())) continue;
                    return;
                }
            }
            list2.add(trustAnchor);
            return;
        }
    }

    public final void reset() {
        Map<X500Principal, List<TrustAnchor>> map = this.subjectToTrustAnchors;
        synchronized (map) {
            this.subjectToTrustAnchors.clear();
            return;
        }
    }

    public final void reset(Set<TrustAnchor> set) {
        Map<X500Principal, List<TrustAnchor>> map = this.subjectToTrustAnchors;
        synchronized (map) {
            this.reset();
            this.index(set);
            return;
        }
    }

    public final TrustAnchor findByIssuerAndSignature(X509Certificate x509Certificate) {
        Iterator iterator2 = x509Certificate.getIssuerX500Principal();
        Map<X500Principal, List<TrustAnchor>> map = this.subjectToTrustAnchors;
        synchronized (map) {
            iterator2 = this.subjectToTrustAnchors.get(iterator2);
            if (iterator2 == null) {
                return null;
            }
            iterator2 = iterator2.iterator();
            while (iterator2.hasNext()) {
                TrustAnchor trustAnchor = (TrustAnchor)iterator2.next();
                try {
                    Serializable serializable = trustAnchor.getTrustedCert();
                    serializable = serializable != null ? serializable.getPublicKey() : trustAnchor.getCAPublicKey();
                    x509Certificate.verify((PublicKey)serializable);
                    return trustAnchor;
                }
                catch (Exception exception) {
                }
            }
            return null;
        }
    }

    public final TrustAnchor findBySubjectAndPublicKey(X509Certificate x509Certificate) {
        Object object = x509Certificate.getSubjectX500Principal();
        Map<X500Principal, List<TrustAnchor>> map = this.subjectToTrustAnchors;
        synchronized (map) {
            object = this.subjectToTrustAnchors.get(object);
            if (object == null) {
                return null;
            }
            return TrustedCertificateIndex.findBySubjectAndPublicKey(x509Certificate, (Collection<TrustAnchor>)object);
        }
    }

    private static TrustAnchor findBySubjectAndPublicKey(X509Certificate serializable, Collection<TrustAnchor> object) {
        serializable = ((Certificate)serializable).getPublicKey();
        object = object.iterator();
        while (object.hasNext()) {
            TrustAnchor trustAnchor = (TrustAnchor)object.next();
            try {
                Object object2 = trustAnchor.getTrustedCert();
                object2 = object2 != null ? ((Certificate)object2).getPublicKey() : trustAnchor.getCAPublicKey();
                if (object2.equals(serializable)) {
                    return trustAnchor;
                }
                if (!"X.509".equals(object2.getFormat()) || !"X.509".equals(serializable.getFormat())) continue;
                object2 = object2.getEncoded();
                byte[] byArray = serializable.getEncoded();
                if (byArray == null || object2 == null || !Arrays.equals((byte[])object2, byArray)) continue;
                return trustAnchor;
            }
            catch (Exception exception) {
            }
        }
        return null;
    }

    public final Set<TrustAnchor> findAllByIssuerAndSignature(X509Certificate x509Certificate) {
        Iterator iterator2 = x509Certificate.getIssuerX500Principal();
        Map<X500Principal, List<TrustAnchor>> map = this.subjectToTrustAnchors;
        synchronized (map) {
            iterator2 = this.subjectToTrustAnchors.get(iterator2);
            if (iterator2 == null) {
                return Collections.emptySet();
            }
            HashSet<TrustAnchor> hashSet = new HashSet<TrustAnchor>();
            iterator2 = iterator2.iterator();
            while (iterator2.hasNext()) {
                TrustAnchor trustAnchor = (TrustAnchor)iterator2.next();
                try {
                    Serializable serializable = trustAnchor.getTrustedCert();
                    serializable = serializable != null ? serializable.getPublicKey() : trustAnchor.getCAPublicKey();
                    if (serializable == null) continue;
                    x509Certificate.verify((PublicKey)serializable);
                    hashSet.add(trustAnchor);
                }
                catch (Exception exception) {}
            }
            return hashSet;
        }
    }
}

