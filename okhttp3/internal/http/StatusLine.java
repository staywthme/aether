/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Protocol;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u0000 \n2\u00020\u0001:\u0001\nB\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\b\u0010\t\u001a\u00020\u0007H\u0016R\u0010\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2={"Lokhttp3/internal/http/StatusLine;", "", "protocol", "Lokhttp3/Protocol;", "code", "", "message", "", "(Lokhttp3/Protocol;ILjava/lang/String;)V", "toString", "Companion", "okhttp"})
public final class StatusLine {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @JvmField
    @NotNull
    public final Protocol protocol;
    @JvmField
    public final int code;
    @JvmField
    @NotNull
    public final String message;
    public static final int HTTP_TEMP_REDIRECT = 307;
    public static final int HTTP_PERM_REDIRECT = 308;
    public static final int HTTP_MISDIRECTED_REQUEST = 421;
    public static final int HTTP_CONTINUE = 100;

    public StatusLine(@NotNull Protocol protocol, int n2, @NotNull String string) {
        Intrinsics.checkNotNullParameter((Object)protocol, "");
        Intrinsics.checkNotNullParameter(string, "");
        this.protocol = protocol;
        this.code = n2;
        this.message = string;
    }

    @NotNull
    public final String toString() {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2 = stringBuilder = new StringBuilder();
        if (this.protocol == Protocol.HTTP_1_0) {
            stringBuilder2.append("HTTP/1.0");
        } else {
            stringBuilder2.append("HTTP/1.1");
        }
        stringBuilder2.append(' ').append(this.code);
        stringBuilder2.append(' ').append(this.message);
        String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2={"Lokhttp3/internal/http/StatusLine$Companion;", "", "()V", "HTTP_CONTINUE", "", "HTTP_MISDIRECTED_REQUEST", "HTTP_PERM_REDIRECT", "HTTP_TEMP_REDIRECT", "get", "Lokhttp3/internal/http/StatusLine;", "response", "Lokhttp3/Response;", "parse", "statusLine", "", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final StatusLine get(@NotNull Response response) {
            Intrinsics.checkNotNullParameter(response, "");
            return new StatusLine(response.protocol(), response.code(), response.message());
        }

        @NotNull
        public final StatusLine parse(@NotNull String string) throws IOException {
            int n2;
            Protocol protocol;
            int n3;
            int n4;
            Intrinsics.checkNotNullParameter(string, "");
            if (StringsKt.startsWith$default(string, "HTTP/1.", false, 2, null)) {
                Protocol protocol2;
                if (string.length() < 9 || string.charAt(8) != ' ') {
                    throw new ProtocolException("Unexpected status line: " + string);
                }
                n4 = string.charAt(7) - 48;
                n3 = 9;
                switch (n4) {
                    case 0: {
                        protocol2 = Protocol.HTTP_1_0;
                        break;
                    }
                    case 1: {
                        protocol2 = Protocol.HTTP_1_1;
                        break;
                    }
                    default: {
                        throw new ProtocolException("Unexpected status line: " + string);
                    }
                }
                protocol = protocol2;
            } else if (StringsKt.startsWith$default(string, "ICY ", false, 2, null)) {
                protocol = Protocol.HTTP_1_0;
                n3 = 4;
            } else {
                throw new ProtocolException("Unexpected status line: " + string);
            }
            if (string.length() < n3 + 3) {
                throw new ProtocolException("Unexpected status line: " + string);
            }
            try {
                int n5 = n3;
                String string2 = string.substring(n5, n5 + 3);
                Intrinsics.checkNotNullExpressionValue(string2, "");
                n2 = Integer.parseInt(string2);
            }
            catch (NumberFormatException numberFormatException) {
                throw new ProtocolException("Unexpected status line: " + string);
            }
            n4 = n2;
            String string3 = "";
            if (string.length() > n3 + 3) {
                if (string.charAt(n3 + 3) != ' ') {
                    throw new ProtocolException("Unexpected status line: " + string);
                }
                String string4 = string.substring(n3 + 4);
                Intrinsics.checkNotNullExpressionValue(string4, "");
                string3 = string4;
            }
            return new StatusLine(protocol, n4, string3);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

