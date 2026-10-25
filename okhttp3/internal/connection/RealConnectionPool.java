/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.connection;

import java.lang.ref.Reference;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.Address;
import okhttp3.ConnectionPool;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.concurrent.Task;
import okhttp3.internal.concurrent.TaskQueue;
import okhttp3.internal.concurrent.TaskRunner;
import okhttp3.internal.connection.RealCall;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RealConnectionPool;
import okhttp3.internal.platform.Platform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0005*\u0001\u000e\u0018\u0000 (2\u00020\u0001:\u0001(B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ.\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u000e\u0010\u001a\u001a\n\u0012\u0004\u0012\u00020\u001c\u0018\u00010\u001b2\u0006\u0010\u001d\u001a\u00020\u0015J\u000e\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\u0007J\u000e\u0010 \u001a\u00020\u00152\u0006\u0010!\u001a\u00020\u0012J\u0006\u0010\"\u001a\u00020\u0005J\u0006\u0010#\u001a\u00020$J\u0006\u0010%\u001a\u00020\u0005J\u0018\u0010&\u001a\u00020\u00052\u0006\u0010!\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u0007H\u0002J\u000e\u0010'\u001a\u00020$2\u0006\u0010!\u001a\u00020\u0012R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u000fR\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2={"Lokhttp3/internal/connection/RealConnectionPool;", "", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "maxIdleConnections", "", "keepAliveDuration", "", "timeUnit", "Ljava/util/concurrent/TimeUnit;", "(Lokhttp3/internal/concurrent/TaskRunner;IJLjava/util/concurrent/TimeUnit;)V", "cleanupQueue", "Lokhttp3/internal/concurrent/TaskQueue;", "cleanupTask", "okhttp3/internal/connection/RealConnectionPool$cleanupTask$1", "Lokhttp3/internal/connection/RealConnectionPool$cleanupTask$1;", "connections", "Ljava/util/concurrent/ConcurrentLinkedQueue;", "Lokhttp3/internal/connection/RealConnection;", "keepAliveDurationNs", "callAcquirePooledConnection", "", "address", "Lokhttp3/Address;", "call", "Lokhttp3/internal/connection/RealCall;", "routes", "", "Lokhttp3/Route;", "requireMultiplexed", "cleanup", "now", "connectionBecameIdle", "connection", "connectionCount", "evictAll", "", "idleConnectionCount", "pruneAndGetAllocationCount", "put", "Companion", "okhttp"})
@SourceDebugExtension(value={"SMAP\nRealConnectionPool.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RealConnectionPool.kt\nokhttp3/internal/connection/RealConnectionPool\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 4 Util.kt\nokhttp3/internal/Util\n*L\n1#1,250:1\n1#2:251\n1774#3,4:252\n608#4,4:256\n608#4,4:260\n608#4,4:264\n*S KotlinDebug\n*F\n+ 1 RealConnectionPool.kt\nokhttp3/internal/connection/RealConnectionPool\n*L\n60#1:252,4\n95#1:256,4\n106#1:260,4\n215#1:264,4\n*E\n"})
public final class RealConnectionPool {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private final int maxIdleConnections;
    private final long keepAliveDurationNs;
    @NotNull
    private final TaskQueue cleanupQueue;
    @NotNull
    private final cleanupTask.1 cleanupTask;
    @NotNull
    private final ConcurrentLinkedQueue<RealConnection> connections;

    public RealConnectionPool(@NotNull TaskRunner object, int n2, long l2, @NotNull TimeUnit timeUnit) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter((Object)timeUnit, "");
        this.maxIdleConnections = n2;
        this.keepAliveDurationNs = timeUnit.toNanos(l2);
        this.cleanupQueue = ((TaskRunner)object).newQueue();
        object = Util.okHttpName + " ConnectionPool";
        this.cleanupTask = new Task(this, (String)object){
            final /* synthetic */ RealConnectionPool this$0;
            {
                this.this$0 = realConnectionPool;
                super(string, false, 2, null);
            }

            public final long runOnce() {
                return this.this$0.cleanup(System.nanoTime());
            }
        };
        this.connections = new ConcurrentLinkedQueue();
        if (!(l2 > 0L)) {
            object = "keepAliveDuration <= 0: " + l2;
            throw new IllegalArgumentException(object.toString());
        }
    }

    public final int idleConnectionCount() {
        Object object = this.connections;
        if (object instanceof Collection && ((Collection)object).isEmpty()) {
            return 0;
        }
        int n2 = 0;
        object = object.iterator();
        while (object.hasNext()) {
            boolean bl;
            Object object2 = object.next();
            object2 = (RealConnection)object2;
            Intrinsics.checkNotNullExpressionValue(object2, "");
            Object object3 = object2;
            synchronized (object3) {
                bl = ((RealConnection)object2).getCalls().isEmpty();
            }
            if (!bl || ++n2 >= 0) continue;
            CollectionsKt.throwCountOverflow();
        }
        return n2;
    }

    public final int connectionCount() {
        return this.connections.size();
    }

    public final boolean callAcquirePooledConnection(@NotNull Address address, @NotNull RealCall realCall, @Nullable List<Route> list2, boolean bl) {
        Intrinsics.checkNotNullParameter(address, "");
        Intrinsics.checkNotNullParameter(realCall, "");
        for (RealConnection realConnection : this.connections) {
            Intrinsics.checkNotNullExpressionValue(realConnection, "");
            RealConnection realConnection2 = realConnection;
            synchronized (realConnection2) {
                block5: {
                    if (bl && !realConnection.isMultiplexed$okhttp() || !realConnection.isEligible$okhttp(address, list2)) break block5;
                    realCall.acquireConnectionNoEvents(realConnection);
                    return true;
                }
            }
        }
        return false;
    }

    public final void put(@NotNull RealConnection realConnection) {
        Intrinsics.checkNotNullParameter(realConnection, "");
        RealConnection realConnection2 = realConnection;
        if (Util.assertionsEnabled && !Thread.holdsLock(realConnection2)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + realConnection2));
        }
        this.connections.add(realConnection);
        TaskQueue.schedule$default(this.cleanupQueue, this.cleanupTask, 0L, 2, null);
    }

    public final boolean connectionBecameIdle(@NotNull RealConnection realConnection) {
        Intrinsics.checkNotNullParameter(realConnection, "");
        RealConnection realConnection2 = realConnection;
        if (Util.assertionsEnabled && !Thread.holdsLock(realConnection2)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + realConnection2));
        }
        if (realConnection.getNoNewExchanges() || this.maxIdleConnections == 0) {
            realConnection.setNoNewExchanges(true);
            this.connections.remove(realConnection);
            if (this.connections.isEmpty()) {
                this.cleanupQueue.cancelAll();
            }
            return true;
        }
        TaskQueue.schedule$default(this.cleanupQueue, this.cleanupTask, 0L, 2, null);
        return false;
    }

    public final void evictAll() {
        Iterator<RealConnection> iterator2 = this.connections.iterator();
        Intrinsics.checkNotNullExpressionValue(iterator2, "");
        Iterator<RealConnection> iterator3 = iterator2;
        while (iterator3.hasNext()) {
            RealConnection realConnection = iterator3.next();
            Intrinsics.checkNotNullExpressionValue(realConnection, "");
            RealConnection realConnection2 = realConnection;
            synchronized (realConnection2) {
                Socket socket;
                if (realConnection.getCalls().isEmpty()) {
                    iterator3.remove();
                    realConnection.setNoNewExchanges(true);
                    socket = realConnection.socket();
                } else {
                    socket = null;
                }
                realConnection = socket;
            }
            if (realConnection == null) continue;
            Util.closeQuietly((Socket)((Object)realConnection));
        }
        if (this.connections.isEmpty()) {
            this.cleanupQueue.cancelAll();
        }
    }

    public final long cleanup(long l2) {
        int n2 = 0;
        int n3 = 0;
        RealConnection realConnection = null;
        long l3 = Long.MIN_VALUE;
        for (RealConnection realConnection2 : this.connections) {
            Intrinsics.checkNotNullExpressionValue(realConnection2, "");
            RealConnection realConnection3 = realConnection2;
            synchronized (realConnection3) {
                if (this.pruneAndGetAllocationCount(realConnection2, l2) > 0) {
                    int n4 = n2;
                    n2 = n4 + 1;
                    Integer.valueOf(n4);
                } else {
                    ++n3;
                    long l4 = l2 - realConnection2.getIdleAtNs$okhttp();
                    if (l4 > l3) {
                        l3 = l4;
                        realConnection = realConnection2;
                    }
                }
            }
        }
        if (l3 >= this.keepAliveDurationNs || n3 > this.maxIdleConnections) {
            RealConnection realConnection4 = realConnection;
            Intrinsics.checkNotNull(realConnection4);
            Iterator<RealConnection> iterator2 = realConnection4;
            RealConnection realConnection5 = realConnection4;
            synchronized (realConnection4) {
                block17: {
                    block16: {
                        if (!(!((Collection)((RealConnection)((Object)iterator2)).getCalls()).isEmpty())) break block16;
                        // ** MonitorExit[var7_11] (shouldn't be in output)
                        return 0L;
                    }
                    if (((RealConnection)((Object)iterator2)).getIdleAtNs$okhttp() + l3 == l2) break block17;
                    // ** MonitorExit[var7_11] (shouldn't be in output)
                    return 0L;
                }
                ((RealConnection)((Object)iterator2)).setNoNewExchanges(true);
                this.connections.remove(realConnection);
                // ** MonitorExit[var7_11] (shouldn't be in output)
                Util.closeQuietly(((RealConnection)((Object)iterator2)).socket());
                if (this.connections.isEmpty()) {
                    this.cleanupQueue.cancelAll();
                }
                return 0L;
            }
        }
        if (n3 > 0) {
            return this.keepAliveDurationNs - l3;
        }
        if (n2 > 0) {
            return this.keepAliveDurationNs;
        }
        return -1L;
    }

    private final int pruneAndGetAllocationCount(RealConnection realConnection, long l2) {
        Object object = realConnection;
        if (Util.assertionsEnabled && !Thread.holdsLock(object)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + object));
        }
        object = realConnection.getCalls();
        int n2 = 0;
        while (n2 < object.size()) {
            Reference reference = (Reference)object.get(n2);
            if (reference.get() != null) {
                ++n2;
                continue;
            }
            Intrinsics.checkNotNull(reference);
            reference = (RealCall.CallReference)reference;
            String string = "A connection to " + realConnection.route().address().url() + " was leaked. Did you forget to close a response body?";
            Platform.Companion.get().logCloseableLeak(string, ((RealCall.CallReference)reference).getCallStackTrace());
            object.remove(n2);
            realConnection.setNoNewExchanges(true);
            if (!object.isEmpty()) continue;
            realConnection.setIdleAtNs$okhttp(l2 - this.keepAliveDurationNs);
            return 0;
        }
        return object.size();
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2={"Lokhttp3/internal/connection/RealConnectionPool$Companion;", "", "()V", "get", "Lokhttp3/internal/connection/RealConnectionPool;", "connectionPool", "Lokhttp3/ConnectionPool;", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final RealConnectionPool get(@NotNull ConnectionPool connectionPool) {
            Intrinsics.checkNotNullParameter(connectionPool, "");
            return connectionPool.getDelegate$okhttp();
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

