/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http2;

import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.internal.Util;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u001a\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u001f\u001a\u00020\u00052\u0006\u0010 \u001a\u00020\u000b2\u0006\u0010!\u001a\u00020\u000bJ\u0015\u0010\"\u001a\u00020\u00052\u0006\u0010 \u001a\u00020\u000bH\u0000\u00a2\u0006\u0002\b#J.\u0010$\u001a\u00020\u00052\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u000b2\u0006\u0010(\u001a\u00020\u000b2\u0006\u0010 \u001a\u00020\u000b2\u0006\u0010!\u001a\u00020\u000bR\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006R\u0010\u0010\u0007\u001a\u00020\b8\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0018\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006R\u000e\u0010\n\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006R\u000e\u0010\u0014\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2={"Lokhttp3/internal/http2/Http2;", "", "()V", "BINARY", "", "", "[Ljava/lang/String;", "CONNECTION_PREFACE", "Lokio/ByteString;", "FLAGS", "FLAG_ACK", "", "FLAG_COMPRESSED", "FLAG_END_HEADERS", "FLAG_END_PUSH_PROMISE", "FLAG_END_STREAM", "FLAG_NONE", "FLAG_PADDED", "FLAG_PRIORITY", "FRAME_NAMES", "INITIAL_MAX_FRAME_SIZE", "TYPE_CONTINUATION", "TYPE_DATA", "TYPE_GOAWAY", "TYPE_HEADERS", "TYPE_PING", "TYPE_PRIORITY", "TYPE_PUSH_PROMISE", "TYPE_RST_STREAM", "TYPE_SETTINGS", "TYPE_WINDOW_UPDATE", "formatFlags", "type", "flags", "formattedType", "formattedType$okhttp", "frameLog", "inbound", "", "streamId", "length", "okhttp"})
public final class Http2 {
    @NotNull
    public static final Http2 INSTANCE;
    @JvmField
    @NotNull
    public static final ByteString CONNECTION_PREFACE;
    public static final int INITIAL_MAX_FRAME_SIZE = 16384;
    public static final int TYPE_DATA = 0;
    public static final int TYPE_HEADERS = 1;
    public static final int TYPE_PRIORITY = 2;
    public static final int TYPE_RST_STREAM = 3;
    public static final int TYPE_SETTINGS = 4;
    public static final int TYPE_PUSH_PROMISE = 5;
    public static final int TYPE_PING = 6;
    public static final int TYPE_GOAWAY = 7;
    public static final int TYPE_WINDOW_UPDATE = 8;
    public static final int TYPE_CONTINUATION = 9;
    public static final int FLAG_NONE = 0;
    public static final int FLAG_ACK = 1;
    public static final int FLAG_END_STREAM = 1;
    public static final int FLAG_END_HEADERS = 4;
    public static final int FLAG_END_PUSH_PROMISE = 4;
    public static final int FLAG_PADDED = 8;
    public static final int FLAG_PRIORITY = 32;
    public static final int FLAG_COMPRESSED = 32;
    @NotNull
    private static final String[] FRAME_NAMES;
    @NotNull
    private static final String[] FLAGS;
    @NotNull
    private static final String[] BINARY;

    private Http2() {
    }

    @NotNull
    public final String frameLog(boolean bl, int n2, int n3, int n4, int n5) {
        String string = this.formattedType$okhttp(n4);
        String string2 = this.formatFlags(n4, n5);
        String string3 = bl ? "<<" : ">>";
        Object[] objectArray = new Object[5];
        Object[] objectArray2 = objectArray;
        objectArray[0] = string3;
        objectArray2[1] = n2;
        objectArray2[2] = n3;
        objectArray2[3] = string;
        objectArray2[4] = string2;
        return Util.format("%s 0x%08x %5d %-13s %s", objectArray2);
    }

    @NotNull
    public final String formattedType$okhttp(int n2) {
        if (n2 < FRAME_NAMES.length) {
            return FRAME_NAMES[n2];
        }
        Object[] objectArray = new Object[1];
        Object[] objectArray2 = objectArray;
        objectArray[0] = n2;
        return Util.format("0x%02x", objectArray2);
    }

    @NotNull
    public final String formatFlags(int n2, int n3) {
        String string;
        String string2;
        if (n3 == 0) {
            return "";
        }
        switch (n2) {
            case 4: 
            case 6: {
                if (n3 == 1) {
                    return "ACK";
                }
                return BINARY[n3];
            }
            case 2: 
            case 3: 
            case 7: 
            case 8: {
                return BINARY[n3];
            }
        }
        if (n3 < FLAGS.length) {
            String string3 = FLAGS[n3];
            string2 = string3;
            Intrinsics.checkNotNull(string3);
        } else {
            string2 = string = BINARY[n3];
        }
        if (n2 == 5 && (n3 & 4) != 0) {
            return StringsKt.replace$default(string, "HEADERS", "PUSH_PROMISE", false, 4, null);
        }
        if (n2 == 0 && (n3 & 0x20) != 0) {
            return StringsKt.replace$default(string, "PRIORITY", "COMPRESSED", false, 4, null);
        }
        return string;
    }

    static {
        int n2;
        INSTANCE = new Http2();
        CONNECTION_PREFACE = ByteString.Companion.encodeUtf8("PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n");
        String[] stringArray = new String[10];
        String[] stringArray2 = stringArray;
        stringArray[0] = "DATA";
        stringArray2[1] = "HEADERS";
        stringArray2[2] = "PRIORITY";
        stringArray2[3] = "RST_STREAM";
        stringArray2[4] = "SETTINGS";
        stringArray2[5] = "PUSH_PROMISE";
        stringArray2[6] = "PING";
        stringArray2[7] = "GOAWAY";
        stringArray2[8] = "WINDOW_UPDATE";
        stringArray2[9] = "CONTINUATION";
        FRAME_NAMES = stringArray2;
        FLAGS = new String[64];
        int n3 = 0;
        Object[] objectArray = new String[256];
        while (n3 < 256) {
            int n4 = n3++;
            Object[] objectArray2 = new Object[1];
            Object[] objectArray3 = objectArray2;
            String string = Integer.toBinaryString(n4);
            Intrinsics.checkNotNullExpressionValue(string, "");
            objectArray2[0] = string;
            objectArray[n4] = StringsKt.replace$default(Util.format("%8s", objectArray3), ' ', '0', false, 4, null);
        }
        BINARY = objectArray;
        Http2.FLAGS[0] = "";
        Http2.FLAGS[1] = "END_STREAM";
        int[] nArray = new int[1];
        objectArray = nArray;
        nArray[0] = 1;
        Object[] objectArray4 = objectArray;
        Http2.FLAGS[8] = "PADDED";
        int cfr_ignored_0 = objectArray4.length;
        for (int i2 = 0; i2 <= 0; ++i2) {
            String string = objectArray4[0];
            Http2.FLAGS[string | 8] = FLAGS[string] + "|PADDED";
        }
        Http2.FLAGS[4] = "END_HEADERS";
        Http2.FLAGS[32] = "PRIORITY";
        Http2.FLAGS[36] = "END_HEADERS|PRIORITY";
        int[] nArray2 = new int[3];
        int[] nArray3 = nArray2;
        nArray2[0] = 4;
        nArray3[1] = 32;
        nArray3[2] = 36;
        int[] nArray4 = nArray3;
        int cfr_ignored_1 = nArray4.length;
        for (n2 = 0; n2 < 3; ++n2) {
            int n5 = nArray4[n2];
            int cfr_ignored_2 = objectArray4.length;
            for (int i3 = 0; i3 <= 0; ++i3) {
                String string = objectArray4[i3];
                Http2.FLAGS[string | n5] = FLAGS[string] + '|' + FLAGS[n5];
                Http2.FLAGS[string | n5 | 8] = FLAGS[string] + '|' + FLAGS[n5] + "|PADDED";
            }
        }
        int n6 = FLAGS.length;
        for (n2 = 0; n2 < n6; ++n2) {
            if (FLAGS[n2] != null) continue;
            Http2.FLAGS[n2] = BINARY[n2];
        }
    }
}

