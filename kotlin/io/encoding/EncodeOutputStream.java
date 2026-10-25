/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io.encoding;

import java.io.IOException;
import java.io.OutputStream;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.io.encoding.Base64;
import kotlin.io.encoding.ExperimentalEncodingApi;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\r\b\u0003\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u000fH\u0016J \u0010\u0011\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\tH\u0002J\b\u0010\u0015\u001a\u00020\u000fH\u0002J \u0010\u0016\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\tH\u0002J\b\u0010\u0017\u001a\u00020\u000fH\u0016J \u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\tH\u0016J\u0010\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\tH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2={"Lkotlin/io/encoding/EncodeOutputStream;", "Ljava/io/OutputStream;", "output", "base64", "Lkotlin/io/encoding/Base64;", "(Ljava/io/OutputStream;Lkotlin/io/encoding/Base64;)V", "byteBuffer", "", "byteBufferLength", "", "isClosed", "", "lineLength", "symbolBuffer", "checkOpen", "", "close", "copyIntoByteBuffer", "source", "startIndex", "endIndex", "encodeByteBufferIntoOutput", "encodeIntoOutput", "flush", "write", "offset", "length", "b", "kotlin-stdlib"})
@ExperimentalEncodingApi
final class EncodeOutputStream
extends OutputStream {
    @NotNull
    private final OutputStream output;
    @NotNull
    private final Base64 base64;
    private boolean isClosed;
    private int lineLength;
    @NotNull
    private final byte[] symbolBuffer;
    @NotNull
    private final byte[] byteBuffer;
    private int byteBufferLength;

    public EncodeOutputStream(@NotNull OutputStream outputStream2, @NotNull Base64 base64) {
        Intrinsics.checkNotNullParameter(outputStream2, "");
        Intrinsics.checkNotNullParameter(base64, "");
        this.output = outputStream2;
        this.base64 = base64;
        this.lineLength = this.base64.isMimeScheme$kotlin_stdlib() ? 76 : -1;
        this.symbolBuffer = new byte[1024];
        this.byteBuffer = new byte[3];
    }

    @Override
    public final void write(int n2) {
        this.checkOpen();
        int n3 = this.byteBufferLength;
        this.byteBufferLength = n3 + 1;
        this.byteBuffer[n3] = (byte)n2;
        if (this.byteBufferLength == 3) {
            this.encodeByteBufferIntoOutput();
        }
    }

    @Override
    public final void write(@NotNull byte[] object, int n2, int n3) {
        Intrinsics.checkNotNullParameter(object, "");
        this.checkOpen();
        if (n2 < 0 || n3 < 0 || n2 + n3 > ((byte[])object).length) {
            throw new IndexOutOfBoundsException("offset: " + n2 + ", length: " + n3 + ", source size: " + ((byte[])object).length);
        }
        if (n3 == 0) {
            return;
        }
        int n4 = this.byteBufferLength < 3 ? 1 : 0;
        if (n4 == 0) {
            String string = "Check failed.";
            throw new IllegalStateException(string.toString());
        }
        n4 = n2;
        n3 = n2 + n3;
        if (this.byteBufferLength != 0) {
            n4 = n2 + this.copyIntoByteBuffer((byte[])object, n2, n3);
            if (this.byteBufferLength != 0) {
                return;
            }
        }
        while (n4 + 3 <= n3) {
            n2 = (this.base64.isMimeScheme$kotlin_stdlib() ? this.lineLength : this.symbolBuffer.length) / 4;
            n2 = Math.min(n2, (n3 - n4) / 3);
            int n5 = n2 * 3;
            int n6 = n4;
            int n7 = this.encodeIntoOutput((byte[])object, n6, n6 + n5);
            n2 = n7 == n2 << 2 ? 1 : 0;
            if (n2 == 0) {
                object = "Check failed.";
                throw new IllegalStateException(object.toString());
            }
            n4 += n5;
        }
        ArraysKt.copyInto((byte[])object, this.byteBuffer, 0, n4, n3);
        this.byteBufferLength = n3 - n4;
    }

    @Override
    public final void flush() {
        this.checkOpen();
        this.output.flush();
    }

    @Override
    public final void close() {
        if (!this.isClosed) {
            this.isClosed = true;
            if (this.byteBufferLength != 0) {
                this.encodeByteBufferIntoOutput();
            }
            this.output.close();
        }
    }

    private final int copyIntoByteBuffer(byte[] byArray, int n2, int n3) {
        n3 = Math.min(3 - this.byteBufferLength, n3 - n2);
        int n4 = n2;
        ArraysKt.copyInto(byArray, this.byteBuffer, this.byteBufferLength, n4, n4 + n3);
        this.byteBufferLength += n3;
        if (this.byteBufferLength == 3) {
            this.encodeByteBufferIntoOutput();
        }
        return n3;
    }

    private final void encodeByteBufferIntoOutput() {
        EncodeOutputStream encodeOutputStream = this;
        int n2 = encodeOutputStream.encodeIntoOutput(encodeOutputStream.byteBuffer, 0, this.byteBufferLength);
        n2 = n2 == 4 ? 1 : 0;
        if (n2 == 0) {
            String string = "Check failed.";
            throw new IllegalStateException(string.toString());
        }
        this.byteBufferLength = 0;
    }

    private final int encodeIntoOutput(byte[] byArray, int n2, int n3) {
        int n4 = this.base64.encodeIntoByteArray(byArray, this.symbolBuffer, 0, n2, n3);
        if (this.lineLength == 0) {
            this.output.write(Base64.Default.getMimeLineSeparatorSymbols$kotlin_stdlib());
            this.lineLength = 76;
            n2 = n4 <= 76 ? 1 : 0;
            if (n2 == 0) {
                String string = "Check failed.";
                throw new IllegalStateException(string.toString());
            }
        }
        this.output.write(this.symbolBuffer, 0, n4);
        this.lineLength -= n4;
        return n4;
    }

    private final void checkOpen() {
        if (this.isClosed) {
            throw new IOException("The output stream is closed.");
        }
    }
}

