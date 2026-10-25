/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http2;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\u0013\b\u0086\u0001\u0018\u0000 \u00152\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0015B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014\u00a8\u0006\u0016"}, d2={"Lokhttp3/internal/http2/ErrorCode;", "", "httpCode", "", "(Ljava/lang/String;II)V", "getHttpCode", "()I", "NO_ERROR", "PROTOCOL_ERROR", "INTERNAL_ERROR", "FLOW_CONTROL_ERROR", "SETTINGS_TIMEOUT", "STREAM_CLOSED", "FRAME_SIZE_ERROR", "REFUSED_STREAM", "CANCEL", "COMPRESSION_ERROR", "CONNECT_ERROR", "ENHANCE_YOUR_CALM", "INADEQUATE_SECURITY", "HTTP_1_1_REQUIRED", "Companion", "okhttp"})
public final class ErrorCode
extends Enum<ErrorCode> {
    @NotNull
    public static final Companion Companion;
    private final int httpCode;
    public static final /* enum */ ErrorCode NO_ERROR;
    public static final /* enum */ ErrorCode PROTOCOL_ERROR;
    public static final /* enum */ ErrorCode INTERNAL_ERROR;
    public static final /* enum */ ErrorCode FLOW_CONTROL_ERROR;
    public static final /* enum */ ErrorCode SETTINGS_TIMEOUT;
    public static final /* enum */ ErrorCode STREAM_CLOSED;
    public static final /* enum */ ErrorCode FRAME_SIZE_ERROR;
    public static final /* enum */ ErrorCode REFUSED_STREAM;
    public static final /* enum */ ErrorCode CANCEL;
    public static final /* enum */ ErrorCode COMPRESSION_ERROR;
    public static final /* enum */ ErrorCode CONNECT_ERROR;
    public static final /* enum */ ErrorCode ENHANCE_YOUR_CALM;
    public static final /* enum */ ErrorCode INADEQUATE_SECURITY;
    public static final /* enum */ ErrorCode HTTP_1_1_REQUIRED;
    private static final /* synthetic */ ErrorCode[] $VALUES;

    private ErrorCode(int n3) {
        this.httpCode = n3;
    }

    public final int getHttpCode() {
        return this.httpCode;
    }

    public static ErrorCode[] values() {
        return (ErrorCode[])$VALUES.clone();
    }

    public static ErrorCode valueOf(String string) {
        return Enum.valueOf(ErrorCode.class, string);
    }

    private static final /* synthetic */ ErrorCode[] $values() {
        ErrorCode[] errorCodeArray = new ErrorCode[14];
        ErrorCode[] errorCodeArray2 = errorCodeArray;
        errorCodeArray[0] = NO_ERROR;
        errorCodeArray2[1] = PROTOCOL_ERROR;
        errorCodeArray2[2] = INTERNAL_ERROR;
        errorCodeArray2[3] = FLOW_CONTROL_ERROR;
        errorCodeArray2[4] = SETTINGS_TIMEOUT;
        errorCodeArray2[5] = STREAM_CLOSED;
        errorCodeArray2[6] = FRAME_SIZE_ERROR;
        errorCodeArray2[7] = REFUSED_STREAM;
        errorCodeArray2[8] = CANCEL;
        errorCodeArray2[9] = COMPRESSION_ERROR;
        errorCodeArray2[10] = CONNECT_ERROR;
        errorCodeArray2[11] = ENHANCE_YOUR_CALM;
        errorCodeArray2[12] = INADEQUATE_SECURITY;
        errorCodeArray2[13] = HTTP_1_1_REQUIRED;
        return errorCodeArray2;
    }

    static {
        NO_ERROR = new ErrorCode(0);
        PROTOCOL_ERROR = new ErrorCode(1);
        INTERNAL_ERROR = new ErrorCode(2);
        FLOW_CONTROL_ERROR = new ErrorCode(3);
        SETTINGS_TIMEOUT = new ErrorCode(4);
        STREAM_CLOSED = new ErrorCode(5);
        FRAME_SIZE_ERROR = new ErrorCode(6);
        REFUSED_STREAM = new ErrorCode(7);
        CANCEL = new ErrorCode(8);
        COMPRESSION_ERROR = new ErrorCode(9);
        CONNECT_ERROR = new ErrorCode(10);
        ENHANCE_YOUR_CALM = new ErrorCode(11);
        INADEQUATE_SECURITY = new ErrorCode(12);
        HTTP_1_1_REQUIRED = new ErrorCode(13);
        $VALUES = ErrorCode.$values();
        Companion = new Companion(null);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2={"Lokhttp3/internal/http2/ErrorCode$Companion;", "", "()V", "fromHttp2", "Lokhttp3/internal/http2/ErrorCode;", "code", "", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nErrorCode.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ErrorCode.kt\nokhttp3/internal/http2/ErrorCode$Companion\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,53:1\n1#2:54\n*E\n"})
    public static final class Companion {
        private Companion() {
        }

        @Nullable
        public final ErrorCode fromHttp2(int n2) {
            ErrorCode[] errorCodeArray = ErrorCode.values();
            int n3 = errorCodeArray.length;
            for (int i2 = 0; i2 < n3; ++i2) {
                ErrorCode errorCode;
                ErrorCode errorCode2 = errorCode = errorCodeArray[i2];
                if (!(errorCode.getHttpCode() == n2)) continue;
                return errorCode;
            }
            return null;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

