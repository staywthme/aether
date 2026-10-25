/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.Closeable;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import okio.BlackholeSink;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.RealBufferedSink;
import okio.RealBufferedSource;
import okio.Sink;
import okio.Source;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=5, xi=48, d1={"\u0000,\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\r\u0010\u0000\u001a\u00020\u0001H\u0007\u00a2\u0006\u0002\b\u0002\u001a\n\u0010\u0003\u001a\u00020\u0004*\u00020\u0001\u001a\n\u0010\u0003\u001a\u00020\u0005*\u00020\u0006\u001aA\u0010\u0007\u001a\u0002H\b\"\u0010\b\u0000\u0010\t*\n\u0018\u00010\nj\u0004\u0018\u0001`\u000b\"\u0004\b\u0001\u0010\b*\u0002H\t2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u0002H\t\u0012\u0004\u0012\u0002H\b0\rH\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000e\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\u000f"}, d2={"blackholeSink", "Lokio/Sink;", "blackhole", "buffer", "Lokio/BufferedSink;", "Lokio/BufferedSource;", "Lokio/Source;", "use", "R", "T", "Ljava/io/Closeable;", "Lokio/Closeable;", "block", "Lkotlin/Function1;", "(Ljava/io/Closeable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "okio"}, xs="okio/Okio")
final class Okio__OkioKt {
    @NotNull
    public static final BufferedSource buffer(@NotNull Source source2) {
        Intrinsics.checkNotNullParameter(source2, "");
        return new RealBufferedSource(source2);
    }

    @NotNull
    public static final BufferedSink buffer(@NotNull Sink sink2) {
        Intrinsics.checkNotNullParameter(sink2, "");
        return new RealBufferedSink(sink2);
    }

    @JvmName(name="blackhole")
    @NotNull
    public static final Sink blackhole() {
        return new BlackholeSink();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final <T extends Closeable, R> R use(T t2, @NotNull Function1<? super T, ? extends R> object) {
        Throwable throwable;
        R r2;
        block13: {
            Intrinsics.checkNotNullParameter(object, "");
            r2 = null;
            throwable = null;
            try {
                r2 = object.invoke(t2);
            }
            catch (Throwable throwable2) {
                object = throwable2;
                throwable = throwable2;
                InlineMarker.finallyStart(1);
                try {
                    T t3 = t2;
                    if (t3 != null) {
                        t3.close();
                    }
                }
                catch (Throwable throwable4) {
                    ExceptionsKt.addSuppressed(throwable, throwable4);
                }
                InlineMarker.finallyEnd(1);
                break block13;
            }
            catch (Throwable throwable5) {
                InlineMarker.finallyStart(1);
                try {
                    T t4 = t2;
                    if (t4 != null) {
                        t4.close();
                    }
                }
                catch (Throwable throwable3) {}
                InlineMarker.finallyEnd(1);
                throw throwable5;
            }
            InlineMarker.finallyStart(1);
            try {
                T t5 = t2;
                if (t5 != null) {
                    t5.close();
                }
            }
            catch (Throwable throwable4) {
                object = throwable4;
                throwable = throwable4;
            }
            InlineMarker.finallyEnd(1);
        }
        Throwable throwable5 = throwable;
        if (throwable5 != null) {
            throw throwable5;
        }
        R r3 = r2;
        Intrinsics.checkNotNull(r3);
        return r3;
    }
}

