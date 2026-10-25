/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.tls;

import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.internal.tls.TrustRootIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\"\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\u0018\u00002\u00020\u0001B\u0019\u0012\u0012\u0010\u0002\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u0003\"\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0096\u0002J\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u000f\u001a\u00020\u0004H\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016R \u0010\u0006\u001a\u0014\u0012\u0004\u0012\u00020\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\t0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lokhttp3/internal/tls/BasicTrustRootIndex;", "Lokhttp3/internal/tls/TrustRootIndex;", "caCerts", "", "Ljava/security/cert/X509Certificate;", "([Ljava/security/cert/X509Certificate;)V", "subjectToCaCerts", "", "Ljavax/security/auth/x500/X500Principal;", "", "equals", "", "other", "", "findByIssuerAndSignature", "cert", "hashCode", "", "okhttp"})
@SourceDebugExtension(value={"SMAP\nBasicTrustRootIndex.kt\nKotlin\n*S Kotlin\n*F\n+ 1 BasicTrustRootIndex.kt\nokhttp3/internal/tls/BasicTrustRootIndex\n+ 2 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,56:1\n372#2,7:57\n288#3,2:64\n*S KotlinDebug\n*F\n+ 1 BasicTrustRootIndex.kt\nokhttp3/internal/tls/BasicTrustRootIndex\n*L\n28#1:57,7\n37#1:64,2\n*E\n"})
public final class BasicTrustRootIndex
implements TrustRootIndex {
    @NotNull
    private final Map<X500Principal, Set<X509Certificate>> subjectToCaCerts;

    public BasicTrustRootIndex(X509Certificate ... x509CertificateArray) {
        Intrinsics.checkNotNullParameter(x509CertificateArray, "");
        Map map = new LinkedHashMap();
        for (X509Certificate x509Certificate : x509CertificateArray) {
            Object v2;
            X500Principal x500Principal;
            Map map2 = map;
            Intrinsics.checkNotNullExpressionValue(x509Certificate.getSubjectX500Principal(), "");
            Object object = map2.get(x500Principal);
            if (object == null) {
                object = new LinkedHashSet();
                map2.put(x500Principal, object);
                v2 = object;
            } else {
                v2 = object;
            }
            ((Set)v2).add(x509Certificate);
        }
        this.subjectToCaCerts = map;
    }

    @Override
    @Nullable
    public final X509Certificate findByIssuerAndSignature(@NotNull X509Certificate x509Certificate) {
        Object v3;
        block4: {
            Intrinsics.checkNotNullParameter(x509Certificate, "");
            Iterator iterator2 = x509Certificate.getIssuerX500Principal();
            Set<X509Certificate> set = this.subjectToCaCerts.get(iterator2);
            if (set == null) {
                return null;
            }
            iterator2 = set;
            iterator2 = set;
            iterator2 = iterator2.iterator();
            while (iterator2.hasNext()) {
                boolean bl;
                Object t2 = iterator2.next();
                X509Certificate x509Certificate2 = (X509Certificate)t2;
                try {
                    x509Certificate.verify(x509Certificate2.getPublicKey());
                    bl = true;
                }
                catch (Exception exception) {
                    bl = false;
                }
                if (!bl) continue;
                v3 = t2;
                break block4;
            }
            v3 = null;
        }
        return v3;
    }

    public final boolean equals(@Nullable Object object) {
        return object == this || object instanceof BasicTrustRootIndex && Intrinsics.areEqual(((BasicTrustRootIndex)object).subjectToCaCerts, this.subjectToCaCerts);
    }

    public final int hashCode() {
        return ((Object)this.subjectToCaCerts).hashCode();
    }
}

