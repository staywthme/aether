/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.collections.ByteIterator;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000Z\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0000\u001a\u00020\u0005*\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0007\u001a\u00020\b*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\u000b\u001a\u00020\f*\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\r\u001a\u00020\u000e*\u00020\u000f2\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u001c\u0010\u0010\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\r\u0010\u0013\u001a\u00020\u000e*\u00020\u0014H\u0087\b\u001a\u001d\u0010\u0013\u001a\u00020\u000e*\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0004H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u0018*\u00020\u0001H\u0086\u0002\u001a\f\u0010\u0019\u001a\u00020\u0014*\u00020\u0002H\u0007\u001a\u0016\u0010\u0019\u001a\u00020\u0014*\u00020\u00022\b\b\u0002\u0010\u001a\u001a\u00020\u0004H\u0007\u001a\u0017\u0010\u001b\u001a\u00020\u001c*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\u001d\u001a\u00020\u001e*\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u00a8\u0006\u001f"}, d2={"buffered", "Ljava/io/BufferedInputStream;", "Ljava/io/InputStream;", "bufferSize", "", "Ljava/io/BufferedOutputStream;", "Ljava/io/OutputStream;", "bufferedReader", "Ljava/io/BufferedReader;", "charset", "Ljava/nio/charset/Charset;", "bufferedWriter", "Ljava/io/BufferedWriter;", "byteInputStream", "Ljava/io/ByteArrayInputStream;", "", "copyTo", "", "out", "inputStream", "", "offset", "length", "iterator", "Lkotlin/collections/ByteIterator;", "readBytes", "estimatedSize", "reader", "Ljava/io/InputStreamReader;", "writer", "Ljava/io/OutputStreamWriter;", "kotlin-stdlib"})
@JvmName(name="ByteStreamsKt")
public final class ByteStreamsKt {
    @NotNull
    public static final ByteIterator iterator(@NotNull BufferedInputStream bufferedInputStream) {
        Intrinsics.checkNotNullParameter(bufferedInputStream, "");
        return new ByteIterator(bufferedInputStream){
            private int nextByte;
            private boolean nextPrepared;
            private boolean finished;
            final /* synthetic */ BufferedInputStream $this_iterator;
            {
                this.$this_iterator = bufferedInputStream;
                this.nextByte = -1;
            }

            public final int getNextByte() {
                return this.nextByte;
            }

            public final void setNextByte(int n2) {
                this.nextByte = n2;
            }

            public final boolean getNextPrepared() {
                return this.nextPrepared;
            }

            public final void setNextPrepared(boolean bl) {
                this.nextPrepared = bl;
            }

            public final boolean getFinished() {
                return this.finished;
            }

            public final void setFinished(boolean bl) {
                this.finished = bl;
            }

            private final void prepareNext() {
                if (!this.nextPrepared && !this.finished) {
                    this.nextByte = this.$this_iterator.read();
                    this.nextPrepared = true;
                    this.finished = this.nextByte == -1;
                }
            }

            public final boolean hasNext() {
                this.prepareNext();
                return !this.finished;
            }

            public final byte nextByte() {
                this.prepareNext();
                if (this.finished) {
                    throw new NoSuchElementException("Input stream is over.");
                }
                byte by = (byte)this.nextByte;
                this.nextPrepared = false;
                return by;
            }
        };
    }

    @InlineOnly
    private static final ByteArrayInputStream byteInputStream(String string, Charset charset) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(charset, "");
        byte[] byArray = string.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(byArray, "");
        return new ByteArrayInputStream(byArray);
    }

    static /* synthetic */ ByteArrayInputStream byteInputStream$default(String string, Charset charset, int n2, Object object) {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(charset, "");
        byte[] byArray = string.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(byArray, "");
        return new ByteArrayInputStream(byArray);
    }

    @InlineOnly
    private static final ByteArrayInputStream inputStream(byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return new ByteArrayInputStream(byArray);
    }

    @InlineOnly
    private static final ByteArrayInputStream inputStream(byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return new ByteArrayInputStream(byArray, n2, n3);
    }

    @InlineOnly
    private static final BufferedInputStream buffered(InputStream inputStream2, int n2) {
        Intrinsics.checkNotNullParameter(inputStream2, "");
        if (inputStream2 instanceof BufferedInputStream) {
            return (BufferedInputStream)inputStream2;
        }
        return new BufferedInputStream(inputStream2, n2);
    }

    static /* synthetic */ BufferedInputStream buffered$default(InputStream inputStream2, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n2 = 8192;
        }
        Intrinsics.checkNotNullParameter(inputStream2, "");
        if (inputStream2 instanceof BufferedInputStream) {
            return (BufferedInputStream)inputStream2;
        }
        return new BufferedInputStream(inputStream2, n2);
    }

    @InlineOnly
    private static final InputStreamReader reader(InputStream inputStream2, Charset charset) {
        Intrinsics.checkNotNullParameter(inputStream2, "");
        Intrinsics.checkNotNullParameter(charset, "");
        return new InputStreamReader(inputStream2, charset);
    }

    static /* synthetic */ InputStreamReader reader$default(InputStream inputStream2, Charset charset, int n2, Object object) {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(inputStream2, "");
        Intrinsics.checkNotNullParameter(charset, "");
        return new InputStreamReader(inputStream2, charset);
    }

    @InlineOnly
    private static final BufferedReader bufferedReader(InputStream closeable, Charset charset) {
        Intrinsics.checkNotNullParameter(closeable, "");
        Intrinsics.checkNotNullParameter(charset, "");
        closeable = new InputStreamReader((InputStream)closeable, charset);
        return new BufferedReader((Reader)closeable, 8192);
    }

    static /* synthetic */ BufferedReader bufferedReader$default(InputStream inputStream2, Charset charset, int n2, Object object) {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(inputStream2, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Reader reader = new InputStreamReader(inputStream2, charset);
        return new BufferedReader(reader, 8192);
    }

    @InlineOnly
    private static final BufferedOutputStream buffered(OutputStream outputStream2, int n2) {
        Intrinsics.checkNotNullParameter(outputStream2, "");
        if (outputStream2 instanceof BufferedOutputStream) {
            return (BufferedOutputStream)outputStream2;
        }
        return new BufferedOutputStream(outputStream2, n2);
    }

    static /* synthetic */ BufferedOutputStream buffered$default(OutputStream outputStream2, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n2 = 8192;
        }
        Intrinsics.checkNotNullParameter(outputStream2, "");
        if (outputStream2 instanceof BufferedOutputStream) {
            return (BufferedOutputStream)outputStream2;
        }
        return new BufferedOutputStream(outputStream2, n2);
    }

    @InlineOnly
    private static final OutputStreamWriter writer(OutputStream outputStream2, Charset charset) {
        Intrinsics.checkNotNullParameter(outputStream2, "");
        Intrinsics.checkNotNullParameter(charset, "");
        return new OutputStreamWriter(outputStream2, charset);
    }

    static /* synthetic */ OutputStreamWriter writer$default(OutputStream outputStream2, Charset charset, int n2, Object object) {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(outputStream2, "");
        Intrinsics.checkNotNullParameter(charset, "");
        return new OutputStreamWriter(outputStream2, charset);
    }

    @InlineOnly
    private static final BufferedWriter bufferedWriter(OutputStream closeable, Charset charset) {
        Intrinsics.checkNotNullParameter(closeable, "");
        Intrinsics.checkNotNullParameter(charset, "");
        closeable = new OutputStreamWriter((OutputStream)closeable, charset);
        return new BufferedWriter((Writer)closeable, 8192);
    }

    static /* synthetic */ BufferedWriter bufferedWriter$default(OutputStream outputStream2, Charset charset, int n2, Object object) {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(outputStream2, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Writer writer = new OutputStreamWriter(outputStream2, charset);
        return new BufferedWriter(writer, 8192);
    }

    public static final long copyTo(@NotNull InputStream inputStream2, @NotNull OutputStream outputStream2, int n2) {
        Intrinsics.checkNotNullParameter(inputStream2, "");
        Intrinsics.checkNotNullParameter(outputStream2, "");
        long l2 = 0L;
        byte[] byArray = new byte[n2];
        int n3 = inputStream2.read(byArray);
        while (n3 >= 0) {
            outputStream2.write(byArray, 0, n3);
            l2 += (long)n3;
            n3 = inputStream2.read(byArray);
        }
        return l2;
    }

    public static /* synthetic */ long copyTo$default(InputStream inputStream2, OutputStream outputStream2, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = 8192;
        }
        return ByteStreamsKt.copyTo(inputStream2, outputStream2, n2);
    }

    @Deprecated(message="Use readBytes() overload without estimatedSize parameter", replaceWith=@ReplaceWith(expression="readBytes()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.5")
    @NotNull
    public static final byte[] readBytes(@NotNull InputStream inputStream2, int n2) {
        Intrinsics.checkNotNullParameter(inputStream2, "");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(Math.max(n2, inputStream2.available()));
        ByteStreamsKt.copyTo$default(inputStream2, byteArrayOutputStream, 0, 2, null);
        byte[] byArray = byteArrayOutputStream.toByteArray();
        Intrinsics.checkNotNullExpressionValue(byArray, "");
        return byArray;
    }

    public static /* synthetic */ byte[] readBytes$default(InputStream inputStream2, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n2 = 8192;
        }
        return ByteStreamsKt.readBytes(inputStream2, n2);
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final byte[] readBytes(@NotNull InputStream inputStream2) {
        Intrinsics.checkNotNullParameter(inputStream2, "");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(Math.max(8192, inputStream2.available()));
        ByteStreamsKt.copyTo$default(inputStream2, byteArrayOutputStream, 0, 2, null);
        byte[] byArray = byteArrayOutputStream.toByteArray();
        Intrinsics.checkNotNullExpressionValue(byArray, "");
        return byArray;
    }
}

