/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.io.IOException;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\b\u0086\u0001\u0018\u0000 \f2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\fB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0003H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b\u00a8\u0006\r"}, d2={"Lokhttp3/Protocol;", "", "protocol", "", "(Ljava/lang/String;ILjava/lang/String;)V", "toString", "HTTP_1_0", "HTTP_1_1", "SPDY_3", "HTTP_2", "H2_PRIOR_KNOWLEDGE", "QUIC", "Companion", "okhttp"})
public final class Protocol
extends Enum<Protocol> {
    @NotNull
    public static final Companion Companion;
    @NotNull
    private final String protocol;
    public static final /* enum */ Protocol HTTP_1_0;
    public static final /* enum */ Protocol HTTP_1_1;
    @Deprecated(message="OkHttp has dropped support for SPDY. Prefer {@link #HTTP_2}.")
    public static final /* enum */ Protocol SPDY_3;
    public static final /* enum */ Protocol HTTP_2;
    public static final /* enum */ Protocol H2_PRIOR_KNOWLEDGE;
    public static final /* enum */ Protocol QUIC;
    private static final /* synthetic */ Protocol[] $VALUES;

    private Protocol(String string2) {
        this.protocol = string2;
    }

    @NotNull
    public final String toString() {
        return this.protocol;
    }

    public static Protocol[] values() {
        return (Protocol[])$VALUES.clone();
    }

    public static Protocol valueOf(String string) {
        return Enum.valueOf(Protocol.class, string);
    }

    private static final /* synthetic */ Protocol[] $values() {
        Protocol[] protocolArray = new Protocol[6];
        Protocol[] protocolArray2 = protocolArray;
        protocolArray[0] = HTTP_1_0;
        protocolArray2[1] = HTTP_1_1;
        protocolArray2[2] = SPDY_3;
        protocolArray2[3] = HTTP_2;
        protocolArray2[4] = H2_PRIOR_KNOWLEDGE;
        protocolArray2[5] = QUIC;
        return protocolArray2;
    }

    @JvmStatic
    @NotNull
    public static final Protocol get(@NotNull String string) throws IOException {
        return Companion.get(string);
    }

    static {
        HTTP_1_0 = new Protocol("http/1.0");
        HTTP_1_1 = new Protocol("http/1.1");
        SPDY_3 = new Protocol("spdy/3.1");
        HTTP_2 = new Protocol("h2");
        H2_PRIOR_KNOWLEDGE = new Protocol("h2_prior_knowledge");
        QUIC = new Protocol("quic");
        $VALUES = Protocol.$values();
        Companion = new Companion(null);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lokhttp3/Protocol$Companion;", "", "()V", "get", "Lokhttp3/Protocol;", "protocol", "", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final Protocol get(@NotNull String string) throws IOException {
            Intrinsics.checkNotNullParameter(string, "");
            String string2 = string;
            if (Intrinsics.areEqual(string2, HTTP_1_0.protocol)) {
                return HTTP_1_0;
            }
            if (Intrinsics.areEqual(string2, HTTP_1_1.protocol)) {
                return HTTP_1_1;
            }
            if (Intrinsics.areEqual(string2, H2_PRIOR_KNOWLEDGE.protocol)) {
                return H2_PRIOR_KNOWLEDGE;
            }
            if (Intrinsics.areEqual(string2, HTTP_2.protocol)) {
                return HTTP_2;
            }
            if (Intrinsics.areEqual(string2, SPDY_3.protocol)) {
                return SPDY_3;
            }
            if (Intrinsics.areEqual(string2, QUIC.protocol)) {
                return QUIC;
            }
            throw new IOException("Unexpected protocol: " + string);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

