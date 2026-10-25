/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.authenticator;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.SocketAddress;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Address;
import okhttp3.Authenticator;
import okhttp3.Challenge;
import okhttp3.Credentials;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u001c\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0003H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2={"Lokhttp3/internal/authenticator/JavaNetAuthenticator;", "Lokhttp3/Authenticator;", "defaultDns", "Lokhttp3/Dns;", "(Lokhttp3/Dns;)V", "authenticate", "Lokhttp3/Request;", "route", "Lokhttp3/Route;", "response", "Lokhttp3/Response;", "connectToInetAddress", "Ljava/net/InetAddress;", "Ljava/net/Proxy;", "url", "Lokhttp3/HttpUrl;", "dns", "okhttp"})
public final class JavaNetAuthenticator
implements Authenticator {
    @NotNull
    private final Dns defaultDns;

    public JavaNetAuthenticator(@NotNull Dns dns) {
        Intrinsics.checkNotNullParameter(dns, "");
        this.defaultDns = dns;
    }

    public /* synthetic */ JavaNetAuthenticator(Dns dns, int n2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n2 & 1) != 0) {
            dns = Dns.SYSTEM;
        }
        this(dns);
    }

    @Override
    @Nullable
    public final Request authenticate(@Nullable Route object, @NotNull Response response) throws IOException {
        Intrinsics.checkNotNullParameter(response, "");
        Object object2 = response.challenges();
        Request request = response.request();
        HttpUrl httpUrl = request.url();
        boolean bl = response.code() == 407;
        Object object3 = object;
        if (object3 == null || (object3 = ((Route)object3).proxy()) == null) {
            object3 = Proxy.NO_PROXY;
        }
        Object object4 = object3;
        object2 = object2.iterator();
        while (object2.hasNext()) {
            Object object5;
            Object object6;
            Object object7;
            Challenge challenge = (Challenge)object2.next();
            if (!StringsKt.equals("Basic", challenge.scheme(), true)) continue;
            Object object8 = object;
            if (object8 == null || (object8 = ((Route)object8).address()) == null || (object8 = ((Address)object8).dns()) == null) {
                object8 = object7 = this.defaultDns;
            }
            if (bl) {
                SocketAddress socketAddress = ((Proxy)object4).address();
                Intrinsics.checkNotNull(socketAddress);
                object6 = (InetSocketAddress)socketAddress;
                String string = ((InetSocketAddress)object6).getHostName();
                Intrinsics.checkNotNullExpressionValue(object4, "");
                object5 = java.net.Authenticator.requestPasswordAuthentication(string, this.connectToInetAddress((Proxy)object4, httpUrl, (Dns)object7), ((InetSocketAddress)object6).getPort(), httpUrl.scheme(), challenge.realm(), challenge.scheme(), httpUrl.url(), Authenticator.RequestorType.PROXY);
            } else {
                String string = httpUrl.host();
                Intrinsics.checkNotNullExpressionValue(object4, "");
                object5 = object7 = java.net.Authenticator.requestPasswordAuthentication(string, this.connectToInetAddress((Proxy)object4, httpUrl, (Dns)object7), httpUrl.port(), httpUrl.scheme(), challenge.realm(), challenge.scheme(), httpUrl.url(), Authenticator.RequestorType.SERVER);
            }
            if (object5 == null) continue;
            object6 = bl ? "Proxy-Authorization" : "Authorization";
            String string = ((PasswordAuthentication)object7).getUserName();
            Intrinsics.checkNotNullExpressionValue(string, "");
            char[] cArray = ((PasswordAuthentication)object7).getPassword();
            Intrinsics.checkNotNullExpressionValue(cArray, "");
            object = cArray;
            object = Credentials.basic(string, new String((char[])object), challenge.charset());
            return request.newBuilder().header((String)object6, (String)object).build();
        }
        return null;
    }

    private final InetAddress connectToInetAddress(Proxy proxy, HttpUrl httpUrl, Dns dns) throws IOException {
        Proxy.Type type = proxy.type();
        if ((type == null ? -1 : WhenMappings.$EnumSwitchMapping$0[type.ordinal()]) == 1) {
            return CollectionsKt.first(dns.lookup(httpUrl.host()));
        }
        SocketAddress socketAddress = proxy.address();
        Intrinsics.checkNotNull(socketAddress);
        InetAddress inetAddress = ((InetSocketAddress)socketAddress).getAddress();
        Intrinsics.checkNotNullExpressionValue(inetAddress, "");
        return inetAddress;
    }

    public JavaNetAuthenticator() {
        this(null, 1, null);
    }

    @Metadata(mv={1, 8, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] nArray = new int[Proxy.Type.values().length];
            try {
                nArray[Proxy.Type.DIRECT.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            $EnumSwitchMapping$0 = nArray;
        }
    }
}

