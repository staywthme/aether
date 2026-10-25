/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.internal.InlineOnly;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.io.ExposingBufferByteArrayOutputStream;
import kotlin.io.FilesKt;
import kotlin.io.FilesKt__FilePathComponentsKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000z\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010\u0005\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a!\u0010\n\u001a\u00020\u000b*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0087\b\u001a!\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0087\b\u001aB\u0010\u0010\u001a\u00020\u0001*\u00020\u000226\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004\u00a2\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r\u00a2\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001aJ\u0010\u0010\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\r26\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004\u00a2\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r\u00a2\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001a7\u0010\u0018\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2!\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u00010\u0019\u001a\r\u0010\u001b\u001a\u00020\u001c*\u00020\u0002H\u0087\b\u001a\r\u0010\u001d\u001a\u00020\u001e*\u00020\u0002H\u0087\b\u001a\u0017\u0010\u001f\u001a\u00020 *\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u001a\n\u0010!\u001a\u00020\u0004*\u00020\u0002\u001a\u001a\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00070#*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0014\u0010$\u001a\u00020\u0007*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010%\u001a\u00020&*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u001a?\u0010'\u001a\u0002H(\"\u0004\b\u0000\u0010(*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\u0018\u0010)\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070*\u0012\u0004\u0012\u0002H(0\u0019H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010+\u001a\u0012\u0010,\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010-\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010.\u001a\u00020/*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u00060"}, d2={"appendBytes", "", "Ljava/io/File;", "array", "", "appendText", "text", "", "charset", "Ljava/nio/charset/Charset;", "bufferedReader", "Ljava/io/BufferedReader;", "bufferSize", "", "bufferedWriter", "Ljava/io/BufferedWriter;", "forEachBlock", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "buffer", "bytesRead", "blockSize", "forEachLine", "Lkotlin/Function1;", "line", "inputStream", "Ljava/io/FileInputStream;", "outputStream", "Ljava/io/FileOutputStream;", "printWriter", "Ljava/io/PrintWriter;", "readBytes", "readLines", "", "readText", "reader", "Ljava/io/InputStreamReader;", "useLines", "T", "block", "Lkotlin/sequences/Sequence;", "(Ljava/io/File;Ljava/nio/charset/Charset;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "writeBytes", "writeText", "writer", "Ljava/io/OutputStreamWriter;", "kotlin-stdlib"}, xs="kotlin/io/FilesKt")
@SourceDebugExtension(value={"SMAP\nFileReadWrite.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FileReadWrite.kt\nkotlin/io/FilesKt__FileReadWriteKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,232:1\n231#1:234\n1#2:233\n1#2:235\n*S KotlinDebug\n*F\n+ 1 FileReadWrite.kt\nkotlin/io/FilesKt__FileReadWriteKt\n*L\n230#1:234\n230#1:235\n*E\n"})
class FilesKt__FileReadWriteKt
extends FilesKt__FilePathComponentsKt {
    @InlineOnly
    private static final InputStreamReader reader(File file, Charset charset) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(charset, "");
        return new InputStreamReader((InputStream)new FileInputStream(file), charset);
    }

    static /* synthetic */ InputStreamReader reader$default(File file, Charset charset, int n2, Object object) {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(charset, "");
        return new InputStreamReader((InputStream)new FileInputStream(file), charset);
    }

    @InlineOnly
    private static final BufferedReader bufferedReader(File object, Charset charset, int n2) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(charset, "");
        object = new InputStreamReader((InputStream)new FileInputStream((File)object), charset);
        return new BufferedReader((Reader)object, n2);
    }

    static /* synthetic */ BufferedReader bufferedReader$default(File file, Charset charset, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if ((n3 & 2) != 0) {
            n2 = 8192;
        }
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Object object2 = file;
        object2 = new InputStreamReader((InputStream)new FileInputStream((File)object2), charset);
        return new BufferedReader((Reader)object2, n2);
    }

    @InlineOnly
    private static final OutputStreamWriter writer(File file, Charset charset) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(charset, "");
        return new OutputStreamWriter((OutputStream)new FileOutputStream(file), charset);
    }

    static /* synthetic */ OutputStreamWriter writer$default(File file, Charset charset, int n2, Object object) {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(charset, "");
        return new OutputStreamWriter((OutputStream)new FileOutputStream(file), charset);
    }

    @InlineOnly
    private static final BufferedWriter bufferedWriter(File object, Charset charset, int n2) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(charset, "");
        object = new OutputStreamWriter((OutputStream)new FileOutputStream((File)object), charset);
        return new BufferedWriter((Writer)object, n2);
    }

    static /* synthetic */ BufferedWriter bufferedWriter$default(File file, Charset charset, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if ((n3 & 2) != 0) {
            n2 = 8192;
        }
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Object object2 = file;
        object2 = new OutputStreamWriter((OutputStream)new FileOutputStream((File)object2), charset);
        return new BufferedWriter((Writer)object2, n2);
    }

    @InlineOnly
    private static final PrintWriter printWriter(File object, Charset charset) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(charset, "");
        object = new OutputStreamWriter((OutputStream)new FileOutputStream((File)object), charset);
        return new PrintWriter(new BufferedWriter((Writer)object, 8192));
    }

    static /* synthetic */ PrintWriter printWriter$default(File object, Charset charset, int n2, Object object2) {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(charset, "");
        File file = object;
        object = file;
        object = new OutputStreamWriter((OutputStream)new FileOutputStream((File)object), charset);
        return new PrintWriter(new BufferedWriter((Writer)object, 8192));
    }

    @NotNull
    public static final byte[] readBytes(@NotNull File file) {
        Object object;
        Intrinsics.checkNotNullParameter(file, "");
        Closeable closeable = new FileInputStream(file);
        Throwable throwable = null;
        try {
            byte[] byArray;
            int n2;
            object = (FileInputStream)closeable;
            int n3 = 0;
            long l2 = file.length();
            if (l2 > Integer.MAX_VALUE) {
                throw new OutOfMemoryError("File " + file + " is too big (" + l2 + " bytes) to fit in memory.");
            }
            int n4 = (int)l2;
            byte[] byArray2 = new byte[n4];
            while (n4 > 0 && (n2 = ((FileInputStream)object).read(byArray2, n3, n4)) >= 0) {
                n4 -= n2;
                n3 += n2;
            }
            if (n4 > 0) {
                byte[] byArray3 = Arrays.copyOf(byArray2, n3);
                byArray = byArray3;
                Intrinsics.checkNotNullExpressionValue(byArray3, "");
            } else {
                n2 = ((FileInputStream)object).read();
                if (n2 == -1) {
                    byArray = byArray2;
                } else {
                    ExposingBufferByteArrayOutputStream exposingBufferByteArrayOutputStream = new ExposingBufferByteArrayOutputStream(8193);
                    exposingBufferByteArrayOutputStream.write(n2);
                    ByteStreamsKt.copyTo$default((InputStream)object, exposingBufferByteArrayOutputStream, 0, 2, null);
                    int n5 = byArray2.length + exposingBufferByteArrayOutputStream.size();
                    if (n5 < 0) {
                        throw new OutOfMemoryError("File " + file + " is too big to fit in memory.");
                    }
                    byte[] byArray4 = exposingBufferByteArrayOutputStream.getBuffer();
                    byte[] byArray5 = Arrays.copyOf(byArray2, n5);
                    Intrinsics.checkNotNullExpressionValue(byArray5, "");
                    byArray = ArraysKt.copyInto(byArray4, byArray5, byArray2.length, 0, exposingBufferByteArrayOutputStream.size());
                }
            }
            object = byArray;
        }
        catch (Throwable throwable2) {
            try {
                Throwable throwable3 = throwable2;
                throwable = throwable2;
                throw throwable3;
            }
            catch (Throwable throwable4) {
                CloseableKt.closeFinally(closeable, throwable);
                throw throwable4;
            }
        }
        CloseableKt.closeFinally(closeable, null);
        return object;
    }

    public static final void writeBytes(@NotNull File object, @NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        object = new FileOutputStream((File)object);
        Throwable throwable = null;
        try {
            FileOutputStream fileOutputStream = (FileOutputStream)object;
            fileOutputStream.write(byArray);
        }
        catch (Throwable throwable2) {
            try {
                Throwable throwable3 = throwable2;
                throwable = throwable2;
                throw throwable3;
            }
            catch (Throwable throwable4) {
                CloseableKt.closeFinally((Closeable)object, throwable);
                throw throwable4;
            }
        }
        CloseableKt.closeFinally((Closeable)object, null);
        return;
    }

    public static final void appendBytes(@NotNull File object, @NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        object = new FileOutputStream((File)object, true);
        Throwable throwable = null;
        try {
            FileOutputStream fileOutputStream = (FileOutputStream)object;
            fileOutputStream.write(byArray);
        }
        catch (Throwable throwable2) {
            try {
                Throwable throwable3 = throwable2;
                throwable = throwable2;
                throw throwable3;
            }
            catch (Throwable throwable4) {
                CloseableKt.closeFinally((Closeable)object, throwable);
                throw throwable4;
            }
        }
        CloseableKt.closeFinally((Closeable)object, null);
        return;
    }

    @NotNull
    public static final String readText(@NotNull File object, @NotNull Charset object2) {
        Object object3;
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(object2, "");
        object = new InputStreamReader((InputStream)new FileInputStream((File)object), (Charset)object2);
        object2 = null;
        try {
            object3 = (InputStreamReader)object;
            object3 = TextStreamsKt.readText((Reader)object3);
        }
        catch (Throwable throwable) {
            try {
                Throwable throwable2 = throwable;
                object2 = throwable;
                throw throwable2;
            }
            catch (Throwable throwable3) {
                CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
                throw throwable3;
            }
        }
        CloseableKt.closeFinally((Closeable)object, null);
        return object3;
    }

    public static /* synthetic */ String readText$default(File file, Charset charset, int n2, Object object) {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return FilesKt.readText(file, charset);
    }

    public static final void writeText(@NotNull File file, @NotNull String string, @NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(charset, "");
        byte[] byArray = string.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(byArray, "");
        FilesKt.writeBytes(file, byArray);
    }

    public static /* synthetic */ void writeText$default(File file, String string, Charset charset, int n2, Object object) {
        if ((n2 & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        FilesKt.writeText(file, string, charset);
    }

    public static final void appendText(@NotNull File file, @NotNull String string, @NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(charset, "");
        byte[] byArray = string.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(byArray, "");
        FilesKt.appendBytes(file, byArray);
    }

    public static /* synthetic */ void appendText$default(File file, String string, Charset charset, int n2, Object object) {
        if ((n2 & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        FilesKt.appendText(file, string, charset);
    }

    public static final void forEachBlock(@NotNull File file, @NotNull Function2<? super byte[], ? super Integer, Unit> function2) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(function2, "");
        FilesKt.forEachBlock(file, 4096, function2);
    }

    public static final void forEachBlock(@NotNull File object, int n2, @NotNull Function2<? super byte[], ? super Integer, Unit> function2) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function2, "");
        byte[] byArray = new byte[RangesKt.coerceAtLeast(n2, 512)];
        object = new FileInputStream((File)object);
        Throwable throwable = null;
        try {
            int n3;
            FileInputStream fileInputStream = (FileInputStream)object;
            while ((n3 = fileInputStream.read(byArray)) > 0) {
                function2.invoke((byte[])byArray, (Integer)n3);
            }
        }
        catch (Throwable throwable2) {
            try {
                Throwable throwable3 = throwable2;
                throwable = throwable2;
                throw throwable3;
            }
            catch (Throwable throwable4) {
                CloseableKt.closeFinally((Closeable)object, throwable);
                throw throwable4;
            }
        }
        CloseableKt.closeFinally((Closeable)object, null);
        return;
    }

    public static final void forEachLine(@NotNull File file, @NotNull Charset charset, @NotNull Function1<? super String, Unit> function1) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Intrinsics.checkNotNullParameter(function1, "");
        TextStreamsKt.forEachLine(new BufferedReader(new InputStreamReader((InputStream)new FileInputStream(file), charset)), function1);
    }

    public static /* synthetic */ void forEachLine$default(File file, Charset charset, Function1 function1, int n2, Object object) {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        FilesKt.forEachLine(file, charset, function1);
    }

    @InlineOnly
    private static final FileInputStream inputStream(File file) {
        Intrinsics.checkNotNullParameter(file, "");
        return new FileInputStream(file);
    }

    @InlineOnly
    private static final FileOutputStream outputStream(File file) {
        Intrinsics.checkNotNullParameter(file, "");
        return new FileOutputStream(file);
    }

    @NotNull
    public static final List<String> readLines(@NotNull File file, @NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(charset, "");
        ArrayList<String> arrayList = new ArrayList<String>();
        FilesKt.forEachLine(file, charset, (Function1<? super String, Unit>)new Function1<String, Unit>(arrayList){
            final /* synthetic */ ArrayList<String> $result;
            {
                this.$result = arrayList;
                super(1);
            }

            public final void invoke(@NotNull String string) {
                Intrinsics.checkNotNullParameter(string, "");
                this.$result.add(string);
            }
        });
        return arrayList;
    }

    public static /* synthetic */ List readLines$default(File file, Charset charset, int n2, Object object) {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return FilesKt.readLines(file, charset);
    }

    public static final <T> T useLines(@NotNull File object, @NotNull Charset object2, @NotNull Function1<? super Sequence<String>, ? extends T> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(object2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        Object object3 = object;
        object3 = new InputStreamReader((InputStream)new FileInputStream((File)object3), (Charset)object2);
        object = new BufferedReader((Reader)object3, 8192);
        object2 = null;
        try {
            object3 = (BufferedReader)object;
            object3 = function1.invoke(TextStreamsKt.lineSequence((BufferedReader)object3));
        }
        catch (Throwable throwable) {
            try {
                object3 = throwable;
                object2 = throwable;
                throw object3;
            }
            catch (Throwable throwable2) {
                InlineMarker.finallyStart(1);
                CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
                InlineMarker.finallyEnd(1);
                throw throwable2;
            }
        }
        InlineMarker.finallyStart(1);
        CloseableKt.closeFinally((Closeable)object, null);
        InlineMarker.finallyEnd(1);
        return (T)object3;
    }

    public static /* synthetic */ Object useLines$default(File object, Charset object2, Function1 function1, int n2, Object object3) {
        if ((n2 & 1) != 0) {
            object2 = Charsets.UTF_8;
        }
        Object object4 = object;
        object4 = new InputStreamReader((InputStream)new FileInputStream((File)object4), (Charset)object2);
        object = new BufferedReader((Reader)object4, 8192);
        object2 = null;
        try {
            object4 = (BufferedReader)object;
            object4 = function1.invoke(TextStreamsKt.lineSequence((BufferedReader)object4));
        }
        catch (Throwable throwable) {
            try {
                object4 = throwable;
                object2 = throwable;
                throw object4;
            }
            catch (Throwable throwable2) {
                InlineMarker.finallyStart(1);
                CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
                InlineMarker.finallyEnd(1);
                throw throwable2;
            }
        }
        InlineMarker.finallyStart(1);
        CloseableKt.closeFinally((Closeable)object, null);
        InlineMarker.finallyEnd(1);
        return object4;
    }
}

