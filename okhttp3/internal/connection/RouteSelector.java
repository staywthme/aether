/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RouteDatabase;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000d\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 !2\u00020\u0001:\u0002!\"B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0015\u001a\u00020\u0016H\u0086\u0002J\b\u0010\u0017\u001a\u00020\u0016H\u0002J\t\u0010\u0018\u001a\u00020\u0019H\u0086\u0002J\b\u0010\u001a\u001a\u00020\u0014H\u0002J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0014H\u0002J\u001a\u0010\u001e\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020 2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0014H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2={"Lokhttp3/internal/connection/RouteSelector;", "", "address", "Lokhttp3/Address;", "routeDatabase", "Lokhttp3/internal/connection/RouteDatabase;", "call", "Lokhttp3/Call;", "eventListener", "Lokhttp3/EventListener;", "(Lokhttp3/Address;Lokhttp3/internal/connection/RouteDatabase;Lokhttp3/Call;Lokhttp3/EventListener;)V", "inetSocketAddresses", "", "Ljava/net/InetSocketAddress;", "nextProxyIndex", "", "postponedRoutes", "", "Lokhttp3/Route;", "proxies", "Ljava/net/Proxy;", "hasNext", "", "hasNextProxy", "next", "Lokhttp3/internal/connection/RouteSelector$Selection;", "nextProxy", "resetNextInetSocketAddress", "", "proxy", "resetNextProxy", "url", "Lokhttp3/HttpUrl;", "Companion", "Selection", "okhttp"})
public final class RouteSelector {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final Address address;
    @NotNull
    private final RouteDatabase routeDatabase;
    @NotNull
    private final Call call;
    @NotNull
    private final EventListener eventListener;
    @NotNull
    private List<? extends Proxy> proxies;
    private int nextProxyIndex;
    @NotNull
    private List<? extends InetSocketAddress> inetSocketAddresses;
    @NotNull
    private final List<Route> postponedRoutes;

    public RouteSelector(@NotNull Address address, @NotNull RouteDatabase routeDatabase, @NotNull Call call, @NotNull EventListener eventListener) {
        Intrinsics.checkNotNullParameter(address, "");
        Intrinsics.checkNotNullParameter(routeDatabase, "");
        Intrinsics.checkNotNullParameter(call, "");
        Intrinsics.checkNotNullParameter(eventListener, "");
        this.address = address;
        this.routeDatabase = routeDatabase;
        this.call = call;
        this.eventListener = eventListener;
        this.proxies = CollectionsKt.emptyList();
        this.inetSocketAddresses = CollectionsKt.emptyList();
        this.postponedRoutes = new ArrayList();
        RouteSelector routeSelector = this;
        routeSelector.resetNextProxy(routeSelector.address.url(), this.address.proxy());
    }

    public final boolean hasNext() {
        return this.hasNextProxy() || !((Collection)this.postponedRoutes).isEmpty();
    }

    @NotNull
    public final Selection next() throws IOException {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        List list2 = new ArrayList();
        while (this.hasNextProxy()) {
            Proxy proxy = this.nextProxy();
            for (InetSocketAddress inetSocketAddress : this.inetSocketAddresses) {
                Route object = new Route(this.address, proxy, inetSocketAddress);
                if (this.routeDatabase.shouldPostpone(object)) {
                    ((Collection)this.postponedRoutes).add(object);
                    continue;
                }
                ((Collection)list2).add(object);
            }
            if (!(!((Collection)list2).isEmpty())) continue;
        }
        if (list2.isEmpty()) {
            CollectionsKt.addAll((Collection)list2, (Iterable)this.postponedRoutes);
            this.postponedRoutes.clear();
        }
        return new Selection(list2);
    }

    private final void resetNextProxy(HttpUrl httpUrl, Proxy proxy) {
        this.eventListener.proxySelectStart(this.call, httpUrl);
        this.proxies = RouteSelector.resetNextProxy$selectProxies(proxy, httpUrl, this);
        this.nextProxyIndex = 0;
        this.eventListener.proxySelectEnd(this.call, httpUrl, this.proxies);
    }

    private final boolean hasNextProxy() {
        return this.nextProxyIndex < this.proxies.size();
    }

    private final Proxy nextProxy() throws IOException {
        if (!this.hasNextProxy()) {
            throw new SocketException("No route to " + this.address.url().host() + "; exhausted proxy configurations: " + this.proxies);
        }
        int n2 = this.nextProxyIndex;
        this.nextProxyIndex = n2 + 1;
        Proxy proxy = this.proxies.get(n2);
        this.resetNextInetSocketAddress(proxy);
        return proxy;
    }

    /*
     * WARNING - void declaration
     */
    private final void resetNextInetSocketAddress(Proxy list2) throws IOException {
        List<InetAddress> list3;
        void var3_6;
        Object object;
        int n2;
        List list4;
        this.inetSocketAddresses = list4 = (List)new ArrayList();
        if (((Proxy)((Object)list2)).type() == Proxy.Type.DIRECT || ((Proxy)((Object)list2)).type() == Proxy.Type.SOCKS) {
            String object22 = this.address.url().host();
            n2 = this.address.url().port();
        } else {
            object = ((Proxy)((Object)list2)).address();
            if (!(object instanceof InetSocketAddress)) {
                String string = "Proxy.address() is not an InetSocketAddress: " + object.getClass();
                throw new IllegalArgumentException(string.toString());
            }
            Intrinsics.checkNotNullExpressionValue(object, "");
            String string = Companion.getSocketHost((InetSocketAddress)object);
            n2 = ((InetSocketAddress)object).getPort();
        }
        if (!(n2 > 0 ? n2 < 65536 : false)) {
            throw new SocketException("No route to " + (String)var3_6 + ':' + n2 + "; port is out of range");
        }
        if (((Proxy)((Object)list2)).type() == Proxy.Type.SOCKS) {
            ((Collection)list4).add(InetSocketAddress.createUnresolved((String)var3_6, n2));
            return;
        }
        if (Util.canParseAsIpAddress((String)var3_6)) {
            list3 = CollectionsKt.listOf(InetAddress.getByName((String)var3_6));
        } else {
            this.eventListener.dnsStart(this.call, (String)var3_6);
            list2 = this.address.dns().lookup((String)var3_6);
            if (list2.isEmpty()) {
                throw new UnknownHostException(this.address.dns() + " returned no addresses for " + (String)var3_6);
            }
            this.eventListener.dnsEnd(this.call, (String)var3_6, list2);
            list3 = list2;
        }
        object = list3;
        for (InetAddress inetAddress : list3) {
            ((Collection)list4).add(new InetSocketAddress(inetAddress, n2));
        }
    }

    private static final List<Proxy> resetNextProxy$selectProxies(Proxy object, HttpUrl object2, RouteSelector routeSelector) {
        Proxy proxy = object;
        if (proxy != null) {
            return CollectionsKt.listOf(proxy);
        }
        object = object2.uri();
        if (object.getHost() == null) {
            Proxy[] proxyArray = new Proxy[1];
            object = proxyArray;
            proxyArray[0] = Proxy.NO_PROXY;
            return Util.immutableListOf(object);
        }
        object = routeSelector.address.proxySelector().select((URI)object);
        object2 = (Collection)object;
        if (object2 == null || object2.isEmpty()) {
            Proxy[] proxyArray = new Proxy[1];
            object2 = proxyArray;
            proxyArray[0] = Proxy.NO_PROXY;
            return Util.immutableListOf(object2);
        }
        Intrinsics.checkNotNullExpressionValue(object, "");
        return Util.toImmutableList(object);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0015\u0010\u0003\u001a\u00020\u0004*\u00020\u00058F\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2={"Lokhttp3/internal/connection/RouteSelector$Companion;", "", "()V", "socketHost", "", "Ljava/net/InetSocketAddress;", "getSocketHost", "(Ljava/net/InetSocketAddress;)Ljava/lang/String;", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final String getSocketHost(@NotNull InetSocketAddress serializable) {
            Intrinsics.checkNotNullParameter(serializable, "");
            InetAddress inetAddress = serializable.getAddress();
            if (inetAddress == null) {
                String string = serializable.getHostName();
                Intrinsics.checkNotNullExpressionValue(string, "");
                return string;
            }
            serializable = inetAddress;
            String string = inetAddress.getHostAddress();
            Intrinsics.checkNotNullExpressionValue(string, "");
            return string;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\t\u0010\n\u001a\u00020\u000bH\u0086\u0002J\t\u0010\f\u001a\u00020\u0004H\u0086\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\r"}, d2={"Lokhttp3/internal/connection/RouteSelector$Selection;", "", "routes", "", "Lokhttp3/Route;", "(Ljava/util/List;)V", "nextRouteIndex", "", "getRoutes", "()Ljava/util/List;", "hasNext", "", "next", "okhttp"})
    public static final class Selection {
        @NotNull
        private final List<Route> routes;
        private int nextRouteIndex;

        public Selection(@NotNull List<Route> list2) {
            Intrinsics.checkNotNullParameter(list2, "");
            this.routes = list2;
        }

        @NotNull
        public final List<Route> getRoutes() {
            return this.routes;
        }

        public final boolean hasNext() {
            return this.nextRouteIndex < this.routes.size();
        }

        @NotNull
        public final Route next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            int n2 = this.nextRouteIndex;
            this.nextRouteIndex = n2 + 1;
            return this.routes.get(n2);
        }
    }
}

