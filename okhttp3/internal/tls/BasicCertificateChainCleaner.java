/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.tls;

import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.TrustRootIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J$\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\t\u001a\u00020\nH\u0016J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\u0018\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2={"Lokhttp3/internal/tls/BasicCertificateChainCleaner;", "Lokhttp3/internal/tls/CertificateChainCleaner;", "trustRootIndex", "Lokhttp3/internal/tls/TrustRootIndex;", "(Lokhttp3/internal/tls/TrustRootIndex;)V", "clean", "", "Ljava/security/cert/Certificate;", "chain", "hostname", "", "equals", "", "other", "", "hashCode", "", "verifySignature", "toVerify", "Ljava/security/cert/X509Certificate;", "signingCert", "Companion", "okhttp"})
public final class BasicCertificateChainCleaner
extends CertificateChainCleaner {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final TrustRootIndex trustRootIndex;
    private static final int MAX_SIGNERS = 9;

    public BasicCertificateChainCleaner(@NotNull TrustRootIndex trustRootIndex) {
        Intrinsics.checkNotNullParameter(trustRootIndex, "");
        this.trustRootIndex = trustRootIndex;
    }

    @Override
    @NotNull
    public final List<Certificate> clean(@NotNull List<? extends Certificate> collection, @NotNull String object) throws SSLPeerUnverifiedException {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(object, "");
        collection = new ArrayDeque(collection);
        object = new ArrayList();
        Object e2 = collection.removeFirst();
        Intrinsics.checkNotNullExpressionValue(e2, "");
        object.add(e2);
        boolean bl = false;
        block0: for (int i2 = 0; i2 < 9; ++i2) {
            Object object2 = object;
            Object e3 = object2.get(object2.size() - 1);
            Intrinsics.checkNotNull(e3);
            X509Certificate x509Certificate = (X509Certificate)e3;
            Object object3 = this.trustRootIndex.findByIssuerAndSignature(x509Certificate);
            if (object3 != null) {
                if (object.size() > 1 || !Intrinsics.areEqual(x509Certificate, object3)) {
                    object.add(object3);
                }
                Object object4 = object3;
                if (this.verifySignature((X509Certificate)object4, (X509Certificate)object4)) {
                    return object;
                }
                bl = true;
                continue;
            }
            Iterator iterator2 = collection.iterator();
            Intrinsics.checkNotNullExpressionValue(iterator2, "");
            object3 = iterator2;
            while (object3.hasNext()) {
                Object e4 = object3.next();
                Intrinsics.checkNotNull(e4);
                X509Certificate x509Certificate2 = (X509Certificate)e4;
                if (!this.verifySignature(x509Certificate, x509Certificate2)) continue;
                object3.remove();
                object.add(x509Certificate2);
                continue block0;
            }
            if (bl) {
                return object;
            }
            throw new SSLPeerUnverifiedException("Failed to find a trusted cert that signed " + x509Certificate);
        }
        throw new SSLPeerUnverifiedException("Certificate chain too long: " + object);
    }

    private final boolean verifySignature(X509Certificate x509Certificate, X509Certificate x509Certificate2) {
        boolean bl;
        if (!Intrinsics.areEqual(x509Certificate.getIssuerDN(), x509Certificate2.getSubjectDN())) {
            return false;
        }
        try {
            x509Certificate.verify(x509Certificate2.getPublicKey());
            bl = true;
        }
        catch (GeneralSecurityException generalSecurityException) {
            bl = false;
        }
        return bl;
    }

    public final int hashCode() {
        return this.trustRootIndex.hashCode();
    }

    public final boolean equals(@Nullable Object object) {
        if (object == this) {
            return true;
        }
        return object instanceof BasicCertificateChainCleaner && Intrinsics.areEqual(((BasicCertificateChainCleaner)object).trustRootIndex, this.trustRootIndex);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2={"Lokhttp3/internal/tls/BasicCertificateChainCleaner$Companion;", "", "()V", "MAX_SIGNERS", "", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

