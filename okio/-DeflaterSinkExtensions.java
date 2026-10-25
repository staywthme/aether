/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.util.zip.Deflater;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import okio.DeflaterSink;
import okio.Sink;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=2, xi=48, d1={"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0086\b\u00a8\u0006\u0005"}, d2={"deflate", "Lokio/DeflaterSink;", "Lokio/Sink;", "deflater", "Ljava/util/zip/Deflater;", "okio"})
@JvmName(name="-DeflaterSinkExtensions")
public final class -DeflaterSinkExtensions {
    @NotNull
    public static final DeflaterSink deflate(@NotNull Sink sink2, @NotNull Deflater deflater) {
        Intrinsics.checkNotNullParameter(sink2, "");
        Intrinsics.checkNotNullParameter(deflater, "");
        return new DeflaterSink(sink2, deflater);
    }

    public static /* synthetic */ DeflaterSink deflate$default(Sink sink2, Deflater deflater, int n2, Object object) {
        if ((n2 & 1) != 0) {
            deflater = new Deflater();
        }
        Intrinsics.checkNotNullParameter(sink2, "");
        Intrinsics.checkNotNullParameter(deflater, "");
        return new DeflaterSink(sink2, deflater);
    }
}

