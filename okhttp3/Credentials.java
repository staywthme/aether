/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\"\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\b\b\u0002\u0010\u0007\u001a\u00020\bH\u0007\u00a8\u0006\t"}, d2={"Lokhttp3/Credentials;", "", "()V", "basic", "", "username", "password", "charset", "Ljava/nio/charset/Charset;", "okhttp"})
public final class Credentials {
    @NotNull
    public static final Credentials INSTANCE = new Credentials();

    private Credentials() {
    }

    @JvmStatic
    @JvmOverloads
    @NotNull
    public static final String basic(@NotNull String string, @NotNull String string2, @NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Intrinsics.checkNotNullParameter(charset, "");
        string = string + ':' + string2;
        string = ByteString.Companion.encodeString(string, charset).base64();
        return "Basic " + string;
    }

    public static /* synthetic */ String basic$default(String string, String string2, Charset charset, int n2, Object object) {
        if ((n2 & 4) != 0) {
            Charset charset2 = StandardCharsets.ISO_8859_1;
            Intrinsics.checkNotNullExpressionValue(charset2, "");
            charset = charset2;
        }
        return Credentials.basic(string, string2, charset);
    }

    @JvmStatic
    @JvmOverloads
    @NotNull
    public static final String basic(@NotNull String string, @NotNull String string2) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        return Credentials.basic$default(string, string2, null, 4, null);
    }
}

