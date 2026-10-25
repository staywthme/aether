/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.ws;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.Buffer;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0011\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u001d\u001a\u00020\u0004J\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u001f\u001a\u00020\u0006J\u0016\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#2\u0006\u0010\u001d\u001a\u00020$J\u000e\u0010%\u001a\u00020!2\u0006\u0010\u001f\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u000fX\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0006X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u000fX\u0080T\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2={"Lokhttp3/internal/ws/WebSocketProtocol;", "", "()V", "ACCEPT_MAGIC", "", "B0_FLAG_FIN", "", "B0_FLAG_RSV1", "B0_FLAG_RSV2", "B0_FLAG_RSV3", "B0_MASK_OPCODE", "B1_FLAG_MASK", "B1_MASK_LENGTH", "CLOSE_CLIENT_GOING_AWAY", "CLOSE_MESSAGE_MAX", "", "CLOSE_NO_STATUS_CODE", "OPCODE_BINARY", "OPCODE_CONTINUATION", "OPCODE_CONTROL_CLOSE", "OPCODE_CONTROL_PING", "OPCODE_CONTROL_PONG", "OPCODE_FLAG_CONTROL", "OPCODE_TEXT", "PAYLOAD_BYTE_MAX", "PAYLOAD_LONG", "PAYLOAD_SHORT", "PAYLOAD_SHORT_MAX", "acceptHeader", "key", "closeCodeExceptionMessage", "code", "toggleMask", "", "cursor", "Lokio/Buffer$UnsafeCursor;", "", "validateCloseCode", "okhttp"})
@SourceDebugExtension(value={"SMAP\nWebSocketProtocol.kt\nKotlin\n*S Kotlin\n*F\n+ 1 WebSocketProtocol.kt\nokhttp3/internal/ws/WebSocketProtocol\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,141:1\n1#2:142\n*E\n"})
public final class WebSocketProtocol {
    @NotNull
    public static final WebSocketProtocol INSTANCE = new WebSocketProtocol();
    @NotNull
    public static final String ACCEPT_MAGIC = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    public static final int B0_FLAG_FIN = 128;
    public static final int B0_FLAG_RSV1 = 64;
    public static final int B0_FLAG_RSV2 = 32;
    public static final int B0_FLAG_RSV3 = 16;
    public static final int B0_MASK_OPCODE = 15;
    public static final int OPCODE_FLAG_CONTROL = 8;
    public static final int B1_FLAG_MASK = 128;
    public static final int B1_MASK_LENGTH = 127;
    public static final int OPCODE_CONTINUATION = 0;
    public static final int OPCODE_TEXT = 1;
    public static final int OPCODE_BINARY = 2;
    public static final int OPCODE_CONTROL_CLOSE = 8;
    public static final int OPCODE_CONTROL_PING = 9;
    public static final int OPCODE_CONTROL_PONG = 10;
    public static final long PAYLOAD_BYTE_MAX = 125L;
    public static final long CLOSE_MESSAGE_MAX = 123L;
    public static final int PAYLOAD_SHORT = 126;
    public static final long PAYLOAD_SHORT_MAX = 65535L;
    public static final int PAYLOAD_LONG = 127;
    public static final int CLOSE_CLIENT_GOING_AWAY = 1001;
    public static final int CLOSE_NO_STATUS_CODE = 1005;

    private WebSocketProtocol() {
    }

    public final void toggleMask(@NotNull Buffer.UnsafeCursor unsafeCursor, @NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(unsafeCursor, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        int n2 = 0;
        int n3 = byArray.length;
        do {
            byte[] byArray2 = unsafeCursor.data;
            int n4 = unsafeCursor.start;
            int n5 = unsafeCursor.end;
            if (byArray2 == null) continue;
            while (n4 < n5) {
                byte by = byArray2[n4];
                byte by2 = byArray[n2 %= n3];
                byArray2[n4] = (byte)(by ^ by2);
                ++n4;
                ++n2;
            }
        } while (unsafeCursor.next() != -1);
    }

    @Nullable
    public final String closeCodeExceptionMessage(int n2) {
        block5: {
            block4: {
                if (n2 < 1000 || n2 >= 5000) {
                    return "Code must be in range [1000,5000): " + n2;
                }
                if (1004 <= n2 ? n2 < 1007 : false) break block4;
                boolean bl = 1015 <= n2 ? n2 < 3000 : false;
                if (!bl) break block5;
            }
            return "Code " + n2 + " is reserved and may not be used.";
        }
        return null;
    }

    public final void validateCloseCode(int n2) {
        String string = this.closeCodeExceptionMessage(n2);
        if (!(string == null)) {
            String string2 = string;
            Intrinsics.checkNotNull(string2);
            string = string2;
            throw new IllegalArgumentException(string.toString());
        }
    }

    @NotNull
    public final String acceptHeader(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return ByteString.Companion.encodeUtf8(string + ACCEPT_MAGIC).sha1().base64();
    }
}

