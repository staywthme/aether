/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io.encoding;

import java.io.IOException;
import java.io.InputStream;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.io.encoding.Base64;
import kotlin.io.encoding.Base64Kt;
import kotlin.io.encoding.ExperimentalEncodingApi;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u000f\b\u0003\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0013\u001a\u00020\u0014H\u0016J \u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0018\u001a\u00020\tH\u0002J(\u0010\u0019\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\t2\u0006\u0010\u001b\u001a\u00020\tH\u0002J\u0010\u0010\u001c\u001a\u00020\t2\u0006\u0010\u001b\u001a\u00020\tH\u0002J\b\u0010\u001d\u001a\u00020\tH\u0016J \u0010\u001d\u001a\u00020\t2\u0006\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\t2\u0006\u0010\u0018\u001a\u00020\tH\u0016J\b\u0010 \u001a\u00020\tH\u0002J\b\u0010!\u001a\u00020\u0014H\u0002J\b\u0010\"\u001a\u00020\u0014H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\t8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2={"Lkotlin/io/encoding/DecodeInputStream;", "Ljava/io/InputStream;", "input", "base64", "Lkotlin/io/encoding/Base64;", "(Ljava/io/InputStream;Lkotlin/io/encoding/Base64;)V", "byteBuffer", "", "byteBufferEndIndex", "", "byteBufferLength", "getByteBufferLength", "()I", "byteBufferStartIndex", "isClosed", "", "isEOF", "singleByteBuffer", "symbolBuffer", "close", "", "copyByteBufferInto", "dst", "dstOffset", "length", "decodeSymbolBufferInto", "dstEndIndex", "symbolBufferLength", "handlePaddingSymbol", "read", "destination", "offset", "readNextSymbol", "resetByteBufferIfEmpty", "shiftByteBufferToStartIfNeeded", "kotlin-stdlib"})
@ExperimentalEncodingApi
final class DecodeInputStream
extends InputStream {
    @NotNull
    private final InputStream input;
    @NotNull
    private final Base64 base64;
    private boolean isClosed;
    private boolean isEOF;
    @NotNull
    private final byte[] singleByteBuffer;
    @NotNull
    private final byte[] symbolBuffer;
    @NotNull
    private final byte[] byteBuffer;
    private int byteBufferStartIndex;
    private int byteBufferEndIndex;

    public DecodeInputStream(@NotNull InputStream inputStream2, @NotNull Base64 base64) {
        Intrinsics.checkNotNullParameter(inputStream2, "");
        Intrinsics.checkNotNullParameter(base64, "");
        this.input = inputStream2;
        this.base64 = base64;
        this.singleByteBuffer = new byte[1];
        this.symbolBuffer = new byte[1024];
        this.byteBuffer = new byte[1024];
    }

    private final int getByteBufferLength() {
        return this.byteBufferEndIndex - this.byteBufferStartIndex;
    }

    @Override
    public final int read() {
        if (this.byteBufferStartIndex < this.byteBufferEndIndex) {
            int n2 = this.byteBuffer[this.byteBufferStartIndex] & 0xFF;
            ++this.byteBufferStartIndex;
            this.resetByteBufferIfEmpty();
            return n2;
        }
        DecodeInputStream decodeInputStream = this;
        switch (decodeInputStream.read(decodeInputStream.singleByteBuffer, 0, 1)) {
            case -1: {
                return -1;
            }
            case 1: {
                return this.singleByteBuffer[0] & 0xFF;
            }
        }
        throw new IllegalStateException("Unreachable".toString());
    }

    @Override
    public final int read(@NotNull byte[] object, int n2, int n3) {
        Intrinsics.checkNotNullParameter(object, "");
        if (n2 < 0 || n3 < 0 || n2 + n3 > ((byte[])object).length) {
            throw new IndexOutOfBoundsException("offset: " + n2 + ", length: " + n3 + ", buffer size: " + ((byte[])object).length);
        }
        if (this.isClosed) {
            throw new IOException("The input stream is closed.");
        }
        if (this.isEOF) {
            return -1;
        }
        if (n3 == 0) {
            return 0;
        }
        if (this.getByteBufferLength() >= n3) {
            this.copyByteBufferInto((byte[])object, n2, n3);
            return n3;
        }
        int n4 = n3 - this.getByteBufferLength();
        n4 = (n4 + 3 - 1) / 3;
        n4 <<= 2;
        int n5 = n2;
        while (!this.isEOF && n4 > 0) {
            int n6;
            int n7 = 0;
            int n8 = Math.min(this.symbolBuffer.length, n4);
            block5: while (!this.isEOF && n7 < n8) {
                n6 = this.readNextSymbol();
                switch (n6) {
                    case -1: {
                        this.isEOF = true;
                        continue block5;
                    }
                    case 61: {
                        n7 = this.handlePaddingSymbol(n7);
                        this.isEOF = true;
                        continue block5;
                    }
                }
                this.symbolBuffer[n7] = (byte)n6;
                ++n7;
            }
            n6 = this.isEOF || n7 == n8 ? 1 : 0;
            if (n6 == 0) {
                object = "Check failed.";
                throw new IllegalStateException(object.toString());
            }
            n4 -= n7;
            n5 += this.decodeSymbolBufferInto((byte[])object, n5, n3 + n2, n7);
        }
        if (n5 == n2 && this.isEOF) {
            return -1;
        }
        return n5 - n2;
    }

    @Override
    public final void close() {
        if (!this.isClosed) {
            this.isClosed = true;
            this.input.close();
        }
    }

    private final int decodeSymbolBufferInto(byte[] byArray, int n2, int n3, int n4) {
        this.byteBufferEndIndex += this.base64.decodeIntoByteArray(this.symbolBuffer, this.byteBuffer, this.byteBufferEndIndex, 0, n4);
        n3 = Math.min(this.getByteBufferLength(), n3 - n2);
        this.copyByteBufferInto(byArray, n2, n3);
        this.shiftByteBufferToStartIfNeeded();
        return n3;
    }

    private final void copyByteBufferInto(byte[] byArray, int n2, int n3) {
        ArraysKt.copyInto(this.byteBuffer, byArray, n2, this.byteBufferStartIndex, this.byteBufferStartIndex + n3);
        this.byteBufferStartIndex += n3;
        this.resetByteBufferIfEmpty();
    }

    private final void resetByteBufferIfEmpty() {
        if (this.byteBufferStartIndex == this.byteBufferEndIndex) {
            this.byteBufferStartIndex = 0;
            this.byteBufferEndIndex = 0;
        }
    }

    private final void shiftByteBufferToStartIfNeeded() {
        int n2 = this.symbolBuffer.length / 4 * 3;
        int n3 = this.byteBuffer.length - this.byteBufferEndIndex;
        if (n2 > n3) {
            ArraysKt.copyInto(this.byteBuffer, this.byteBuffer, 0, this.byteBufferStartIndex, this.byteBufferEndIndex);
            this.byteBufferEndIndex -= this.byteBufferStartIndex;
            this.byteBufferStartIndex = 0;
        }
    }

    private final int handlePaddingSymbol(int n2) {
        this.symbolBuffer[n2] = 61;
        if ((n2 & 3) == 2) {
            int n3 = this.readNextSymbol();
            if (n3 >= 0) {
                this.symbolBuffer[n2 + 1] = (byte)n3;
            }
            return n2 + 2;
        }
        return n2 + 1;
    }

    private final int readNextSymbol() {
        int n2;
        if (!this.base64.isMimeScheme$kotlin_stdlib()) {
            return this.input.read();
        }
        while ((n2 = this.input.read()) != -1 && !Base64Kt.isInMimeAlphabet(n2)) {
        }
        return n2;
    }
}

