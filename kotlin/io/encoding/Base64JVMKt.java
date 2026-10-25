/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io.encoding;

import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.io.encoding.Base64;
import kotlin.io.encoding.ExperimentalEncodingApi;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000 \n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\u001a%\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0081\b\u001a5\u0010\b\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0081\b\u001a%\u0010\u000b\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0081\b\u001a%\u0010\f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0081\b\u00a8\u0006\u000e"}, d2={"platformCharsToBytes", "", "Lkotlin/io/encoding/Base64;", "source", "", "startIndex", "", "endIndex", "platformEncodeIntoByteArray", "destination", "destinationOffset", "platformEncodeToByteArray", "platformEncodeToString", "", "kotlin-stdlib"})
public final class Base64JVMKt {
    @SinceKotlin(version="1.8")
    @ExperimentalEncodingApi
    @InlineOnly
    private static final byte[] platformCharsToBytes(Base64 object, CharSequence object2, int n2, int n3) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(object2, "");
        if (object2 instanceof String) {
            ((Base64)object).checkSourceBounds$kotlin_stdlib(object2.length(), n2, n3);
            String string = ((String)object2).substring(n2, n3);
            Intrinsics.checkNotNullExpressionValue(string, "");
            object = string;
            object2 = Charsets.ISO_8859_1;
            Intrinsics.checkNotNull(object);
            byte[] byArray = ((String)object).getBytes((Charset)object2);
            Intrinsics.checkNotNullExpressionValue(byArray, "");
            return byArray;
        }
        return ((Base64)object).charsToBytesImpl$kotlin_stdlib((CharSequence)object2, n2, n3);
    }

    @SinceKotlin(version="1.8")
    @ExperimentalEncodingApi
    @InlineOnly
    private static final String platformEncodeToString(Base64 object, byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        object = ((Base64)object).encodeToByteArrayImpl$kotlin_stdlib(byArray, n2, n3);
        return new String((byte[])object, Charsets.ISO_8859_1);
    }

    @SinceKotlin(version="1.8")
    @ExperimentalEncodingApi
    @InlineOnly
    private static final int platformEncodeIntoByteArray(Base64 base64, byte[] byArray, byte[] byArray2, int n2, int n3, int n4) {
        Intrinsics.checkNotNullParameter(base64, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(byArray2, "");
        return base64.encodeIntoByteArrayImpl$kotlin_stdlib(byArray, byArray2, n2, n3, n4);
    }

    @SinceKotlin(version="1.8")
    @ExperimentalEncodingApi
    @InlineOnly
    private static final byte[] platformEncodeToByteArray(Base64 base64, byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(base64, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        return base64.encodeToByteArrayImpl$kotlin_stdlib(byArray, n2, n3);
    }
}

